package com.example.demo.model.enums;

public enum Unit {

    GRAM(1, UnitType.WEIGHT),
    KILOGRAM(1000, UnitType.WEIGHT),

    MILLILITER(1, UnitType.VOLUME),
    LITER(1000, UnitType.VOLUME);

    private final int baseMultiplier;
    private final UnitType type;

    Unit(int baseMultiplier, UnitType type) {
        this.baseMultiplier = baseMultiplier;
        this.type = type;
    }

    public int toBase(int value) {
        return value * baseMultiplier;
    }

    public Unit getBaseUnit() {
        return this.type == UnitType.WEIGHT ? GRAM : MILLILITER;
    }
}