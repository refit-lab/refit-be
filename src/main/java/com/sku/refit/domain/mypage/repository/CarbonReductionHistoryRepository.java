/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.mypage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sku.refit.domain.mypage.entity.CarbonReductionHistory;

public interface CarbonReductionHistoryRepository
    extends JpaRepository<CarbonReductionHistory, Long> {
  List<CarbonReductionHistory> findByUser_IdOrderByChangedAtDesc(Long userId);
}
