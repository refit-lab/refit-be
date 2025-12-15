/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.global.interceptor;

import java.util.Objects;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.sku.refit.global.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompJwtChannelInterceptor implements ChannelInterceptor {

  private final JwtProvider jwtProvider;
  private final UserDetailsService userDetailsService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {

    StompHeaderAccessor accessor =
        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (StompCommand.CONNECT.equals(Objects.requireNonNull(accessor).getCommand())) {

      String authHeader = accessor.getFirstNativeHeader("Authorization");

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);

        String username = jwtProvider.getUsernameFromToken(token);
        UserDetails user = userDetailsService.loadUserByUsername(username);

        Authentication authentication =
            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        accessor.setUser(authentication); // ★ 핵심
      }
    }
    return message;
  }
}
