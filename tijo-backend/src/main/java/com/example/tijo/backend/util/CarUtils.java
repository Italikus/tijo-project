package com.example.tijo.backend.util;

public class CarUtils {
    public static final String VIN_REGEX = "^[A-HJ-NPR-Z0-9]{17}$";

    public static final String VALIDATION_VIN_NOT_NULL_MESSAGE = "vin must not be null";
    public static final String VALIDATION_VIN_PATTERN_MESSAGE = "vin must contains 17 characters";
}
