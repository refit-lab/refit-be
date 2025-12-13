/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.util;

import org.springframework.stereotype.Component;

@Component
public class TicketQrPayloadFactory {

  private static final String QR_BASE_URL = "https://api.refitlab.site/ticket";
  private static final String QR_VERSION = "1";

  /**
   * QR에 인코딩될 payload URL을 생성합니다.
   *
   * <p>예시: https://api.refitlab.site/ticket?v=1&token=xxxx
   *
   * @param token 티켓 고유 토큰
   * @return QR payload URL
   */
  public String create(String token) {
    return QR_BASE_URL + "?v=" + QR_VERSION + "&token=" + token;
  }
}
