/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.global.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

  @Override
  public boolean beforeHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Map<String, Object> attributes) {

    // SockJS 내부 요청은 인증 처리 안 함
    if (!(request instanceof ServletServerHttpRequest servletRequest)) {
      return true;
    }

    String token = servletRequest.getServletRequest().getHeader("Authorization");

    // ✅ 토큰이 있을 때만 attributes에 저장
    if (token != null && !token.isBlank()) {
      attributes.put("token", token);
    }

    return true; // ❗ 절대 false 반환하지 말 것
  }

  @Override
  public void afterHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Exception exception) {}
}
