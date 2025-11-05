package com.luissoy.historicalprices.infrastructure.out.jpa.Price;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRICES")
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price_value")
    private BigDecimal value;
    private String currencyCode;
    private LocalDateTime initDate;
    private LocalDateTime endDate;
    private Long productId;

    public PriceEntity() {}

    public PriceEntity(
            Long id,
            BigDecimal value,
            String currencyCode,
            LocalDateTime initDate,
            LocalDateTime endDate,
            Long productId
    ) {
        this.id = id;
        this.value = value;
        this.currencyCode = currencyCode;
        this.initDate = initDate;
        this.endDate = endDate;
        this.productId = productId;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LocalDateTime getInitDate() {
        return initDate;
    }

    public void setInitDate(LocalDateTime startDate) {
        this.initDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
