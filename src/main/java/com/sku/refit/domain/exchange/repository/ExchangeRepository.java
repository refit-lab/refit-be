/* 
 * Copyright (c) SKU 다시입을Lab 
 */
package com.sku.refit.domain.exchange.repository;

import com.sku.refit.domain.exchange.entity.ExchangeStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sku.refit.domain.exchange.entity.ExchangePost;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangePost, Long> {

  Optional<ExchangePost> findByIdAndExchangeStatus(
      Long id, ExchangeStatus exchangeStatus
  );

  @Query("""
    SELECT e
    FROM ExchangePost e
    WHERE e.exchangeStatus = :status
    ORDER BY
      function('ST_Distance_Sphere',
        point(e.spotLongitude, e.spotLatitude),
        point(:longitude, :latitude)
      )
  """)
  Page<ExchangePost> findByDistanceAndStatus(
      @Param("latitude") Double latitude,
      @Param("longitude") Double longitude,
      @Param("status") ExchangeStatus status,
      Pageable pageable
  );
}
