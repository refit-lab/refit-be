/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.ticket.util;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class TicketTokenGenerator {

  private static final SecureRandom RANDOM = new SecureRandom();

  public String generate() {
    byte[] bytes = new byte[32];
    RANDOM.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
