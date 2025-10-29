/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.global.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

/**
 * Redis 설정 클래스입니다.
 *
 * <p>Spring Data Redis를 사용하기 위한 Redis 연결 및 RedisTemplate 설정을 관리합니다.
 *
 * <p>구성 요소:
 *
 * <ul>
 *   <li>{@link RedisConnectionFactory}: Redis 서버와의 연결 관리
 *   <li>{@link RedisTemplate}: Redis 데이터 직렬화 및 조작을 담당
 * </ul>
 *
 * <p>본 설정은 애플리케이션의 {@code application.yml} 혹은 {@code application.properties} 에 정의된 Redis 설정 정보를
 * 기반으로 동작합니다.
 *
 * <p>예:
 *
 * <pre>
 * spring:
 *   data:
 *     redis:
 *       host: localhost
 *       port: 6379
 *       password: mypassword
 * </pre>
 *
 * @author SKU
 * @since 1.0.0
 */
@RequiredArgsConstructor
@EnableRedisRepositories
@Configuration
public class RedisConfig {

  private final RedisProperties redisProperties;

  /**
   * RedisConnectionFactory Bean을 생성합니다.
   *
   * <p>Lettuce 클라이언트를 기반으로 Redis Standalone 연결을 생성합니다. 비밀번호가 설정되어 있으면 인증이 포함됩니다.
   *
   * @return {@link LettuceConnectionFactory} Redis 연결 팩토리
   */
  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration redisConfig =
        new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
    if (redisProperties.getPassword() != null && !redisProperties.getPassword().isEmpty()) {
      redisConfig.setPassword(redisProperties.getPassword());
    }
    return new LettuceConnectionFactory(redisConfig);
  }

  /**
   * RedisTemplate Bean을 생성합니다.
   *
   * <p>Redis 데이터 접근을 위한 직렬화 설정을 구성합니다. 문자열 기반 직렬화를 사용하여 키, 값, 해시 키, 해시 값을 처리합니다.
   *
   * @return {@link RedisTemplate} Redis 데이터 접근용 템플릿
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new StringRedisSerializer());
    return redisTemplate;
  }
}
