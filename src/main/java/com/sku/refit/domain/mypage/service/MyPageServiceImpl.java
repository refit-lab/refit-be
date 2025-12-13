/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sku.refit.domain.event.entity.Event;
import com.sku.refit.domain.event.repository.EventRepository;
import com.sku.refit.domain.mypage.dto.response.MyPageResponse.*;
import com.sku.refit.domain.mypage.exception.MyPageErrorCode;
import com.sku.refit.domain.mypage.mapper.MyPageMapper;
import com.sku.refit.domain.post.dto.response.PostDetailResponse;
import com.sku.refit.domain.post.entity.Post;
import com.sku.refit.domain.post.mapper.PostMapper;
import com.sku.refit.domain.post.repository.PostLikeRepository;
import com.sku.refit.domain.post.repository.PostRepository;
import com.sku.refit.domain.ticket.entity.Ticket;
import com.sku.refit.domain.ticket.entity.TicketType;
import com.sku.refit.domain.ticket.repository.TicketRepository;
import com.sku.refit.domain.user.entity.User;
import com.sku.refit.domain.user.service.UserService;
import com.sku.refit.global.exception.CustomException;
import com.sku.refit.global.page.mapper.InfiniteMapper;
import com.sku.refit.global.page.response.InfiniteResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageServiceImpl implements MyPageService {

  private final UserService userService;
  private final TicketRepository ticketRepository;
  private final EventRepository eventRepository;
  private final MyPageMapper myPageMapper;

  private final PostRepository postRepository;
  private final PostLikeRepository postLikeRepository;
  private final PostMapper postMapper;
  private final InfiniteMapper infiniteMapper;

  @Override
  public MyTicketsResponse getMyTickets(int page, int size) {

    Long userId = userService.getCurrentUser().getId();
    LocalDate today = LocalDate.now();

    try {
      Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
      Page<Ticket> ticketPage = ticketRepository.findAllByUserId(userId, pageable);

      Map<Long, Event> eventMap = loadEventMap(ticketPage.getContent());

      log.info(
          "[MYPAGE] getMyTickets userId={}, page={}, size={}, totalElements={}",
          userId,
          page,
          size,
          ticketPage.getTotalElements());

      return myPageMapper.toMyTicketsResponse(ticketPage, today, eventMap);

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error("[MYPAGE] getMyTickets failed userId={}, page={}, size={}", userId, page, size, e);
      throw new CustomException(MyPageErrorCode.TICKETS_FETCH_FAILED);
    }
  }

  @Override
  public JoinedEventsResponse getJoinedEvents() {

    Long userId = userService.getCurrentUser().getId();

    try {
      List<Ticket> usedEventTickets =
          ticketRepository.findAllByUserIdAndTypeAndUsedAtIsNotNullOrderByUsedAtDesc(
              userId, TicketType.EVENT);

      if (usedEventTickets.isEmpty()) {
        log.info("[MYPAGE] getJoinedEvents userId={}, resultCount=0", userId);
        return JoinedEventsResponse.builder().items(List.of()).build();
      }

      LinkedHashSet<Long> orderedEventIds = new LinkedHashSet<>();
      for (Ticket t : usedEventTickets) {
        orderedEventIds.add(t.getTargetId());
      }

      Map<Long, Event> eventMap =
          eventRepository.findAllById(orderedEventIds).stream()
              .collect(Collectors.toMap(Event::getId, Function.identity()));

      List<Event> orderedEvents =
          orderedEventIds.stream().map(eventMap::get).filter(Objects::nonNull).toList();

      log.info(
          "[MYPAGE] getJoinedEvents userId={}, tickets={}, uniqueEvents={}",
          userId,
          usedEventTickets.size(),
          orderedEvents.size());

      return myPageMapper.toJoinedEventsResponse(orderedEvents);

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error("[MYPAGE] getJoinedEvents failed userId={}", userId, e);
      throw new CustomException(MyPageErrorCode.JOINED_EVENTS_FETCH_FAILED);
    }
  }

  @Override
  public InfiniteResponse<PostDetailResponse> getMyPosts(Long lastPostId, Integer size) {

    User user = userService.getCurrentUser();

    try {
      Pageable pageable = PageRequest.of(0, size + 1, Sort.by(Sort.Direction.DESC, "id"));

      List<Post> posts;
      if (lastPostId == null) {
        posts = postRepository.findAllByUser_Id(user.getId(), pageable).getContent();
      } else {
        posts =
            postRepository
                .findAllByUser_IdAndIdLessThan(user.getId(), lastPostId, pageable)
                .getContent();
      }

      boolean hasNext = posts.size() > size;
      if (hasNext) {
        posts = posts.subList(0, size);
      }

      List<Long> postIds = posts.stream().map(Post::getId).toList();

      Map<Long, Long> likeCountMap = new HashMap<>();
      if (!postIds.isEmpty()) {
        List<Object[]> likeCounts = postLikeRepository.countByPostIds(postIds);
        for (Object[] row : likeCounts) {
          likeCountMap.put((Long) row[0], (Long) row[1]);
        }
      }

      Set<Long> likedPostIds =
          postIds.isEmpty()
              ? Set.of()
              : new HashSet<>(postLikeRepository.findLikedPostIds(postIds, user.getId()));

      List<PostDetailResponse> responseList =
          posts.stream()
              .map(
                  post ->
                      postMapper.toDetailResponse(
                          post,
                          likeCountMap.getOrDefault(post.getId(), 0L),
                          likedPostIds.contains(post.getId()),
                          user))
              .toList();

      Long newLastCursor = posts.isEmpty() ? null : posts.getLast().getId();

      log.info(
          "[MYPAGE] getMyPosts userId={}, lastPostId={}, size={}, resultCount={}, hasNext={}",
          user.getId(),
          lastPostId,
          size,
          responseList.size(),
          hasNext);

      return infiniteMapper.toInfiniteResponse(responseList, newLastCursor, hasNext, size);

    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error(
          "[MYPAGE] getMyPosts failed userId={}, lastPostId={}, size={}",
          user.getId(),
          lastPostId,
          size,
          e);
      throw new CustomException(MyPageErrorCode.MY_POSTS_FETCH_FAILED);
    }
  }

  /* =========================
   * Private
   * ========================= */

  private Map<Long, Event> loadEventMap(List<Ticket> tickets) {

    List<Long> eventIds =
        tickets.stream()
            .filter(t -> t.getType() == TicketType.EVENT)
            .map(Ticket::getTargetId)
            .distinct()
            .toList();

    if (eventIds.isEmpty()) {
      return Map.of();
    }

    return eventRepository.findAllById(eventIds).stream()
        .collect(Collectors.toMap(Event::getId, Function.identity()));
  }
}
