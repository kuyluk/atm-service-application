package com.solutions.atm.atmservice.model;

public enum Banknote {

    FIFTY("FIFTY", 50),
    TWENTY("TWENTY", 20),
    TEN("TEN", 10),
    FIVE("FIVE", 5);

    private final String  faceValue;
    private Integer noteValue;

    Banknote(String faceValue, Integer noteValue) {
        this.faceValue = faceValue;
        this.noteValue = noteValue;
    }

    public String faceValue() {
        return faceValue;
    }

    public Integer noteValue() {
        return noteValue;
    }
}
