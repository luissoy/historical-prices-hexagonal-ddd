package com.luissoy.historicalprices.infrastructure.out.jpa.Price;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "PRICES")
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price_value")
    private BigDecimal value;
    private String currencyCode;
    private LocalDate initDate;
    private LocalDate endDate;
    private Long productId;

    public PriceEntity() {}

    public PriceEntity(
            Long id,
            BigDecimal value,
            String currencyCode,
            LocalDate initDate,
            LocalDate endDate,
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

    public LocalDate getInitDate() {
        return initDate;
    }

    public void setInitDate(LocalDate startDate) {
        this.initDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
