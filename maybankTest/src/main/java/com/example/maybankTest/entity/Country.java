package com.example.maybankTest.entity;

import lombok.Data;

import java.util.List;

@Data
public class Country {
    private String name;
    private List<String> topLevelDomain;
    private String alpha2Code;
    private String alpha3Code;
    private List<String> callingCodes;
    private String capital;
    private List<String> altSpellings;
    private String subregion;
    private String region;
    private long population;
    private List<Float> latlng;
    private String demonym;
    private double area;
    private double gini;
    private List<String> timezones;
    private List<String> borders;
    private String nativeName;
    private String numericCode;
    private List<Currency> currencies;
    private String flag;

    @Data
    public static class Currency {
        private String code;
        private String name;
        private String symbol;
    }
}
