package org.miage.conversion;

import java.util.HashMap;

public class Converter {

    HashMap<String, Double> currencies = new HashMap<String, Double>();

    public Converter() {
        this.currencies.put("EUR/USD", 1.14);
        this.currencies.put("USD/EUR", 0.88);
        this.currencies.put("EUR/GBP", 0.83);
        this.currencies.put("GBP/EUR", 1.20);
        this.currencies.put("EUR/CHF", 1.05);
        this.currencies.put("CHF/EUR", 0.95);
        this.currencies.put("EUR/JPY", 131.30);
        this.currencies.put("JPY/EUR", 0.0076);
        this.currencies.put("AUD/EUR", 0.64);
        this.currencies.put("EUR/AUD", 1.57);
        this.currencies.put("EUR/CAD",1.43);
        this.currencies.put("CAD/EUR",0.7);
        this.currencies.put("EUR/CNY",7.26);
        this.currencies.put("CNY/EUR",0.14);
    }

    public double getConversionRate(String from, String to) {
        return this.currencies.get(from + "/" + to);
    }

    public String getSupportedPairs(){
        return this.currencies.keySet().toString();
    }
}
