package com.luissoy.historicalprices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.luissoy.historicalprices")
public class HistoricalPricesApplication {

    public static void main(String[] args) {
        SpringApplication.run(HistoricalPricesApplication.class, args);
    }
}
