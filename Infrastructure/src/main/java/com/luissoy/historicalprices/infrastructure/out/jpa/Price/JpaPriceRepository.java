package com.luissoy.historicalprices.infrastructure.out.jpa.Price;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {
    List<PriceEntity> findByProductId(Long productId);

    @Query("""
    SELECT p FROM PriceEntity p
    WHERE p.productId = :productId
      AND :applicationDate BETWEEN p.initDate AND p.endDate
""")
    Optional<PriceEntity> findByProductIdAndDate(
            @Param("productId") Long productId,
            @Param("applicationDate") LocalDateTime applicationDate
    );
}
