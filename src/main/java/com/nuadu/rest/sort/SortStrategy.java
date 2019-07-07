package com.nuadu.rest.sort;

public enum SortStrategy {
    CREATION_TIMESTAMP("creationTimestamp"),

    NAME("name");

    private final String label;

    SortStrategy(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
