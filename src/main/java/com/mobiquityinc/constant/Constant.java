package com.mobiquityinc.constant;

public class Constant {
    public static final String INDEX = "index";
    public static final String WEIGHT = "weight";
    public static final String COST = "cost";
    public static final String PACKAGE_REGEX = "\\((?<" + INDEX + ">\\d+)\\,(?<" + WEIGHT + ">\\d+(\\.\\d{1,2})?)\\,€(?<" + COST + ">\\d+(\\.\\d{1,2})?)\\)";
    public static final int MAX_ITEMS_IN_LINE = 15;
    public static final int MAX_WEIGHT = 100 * 100;
    public static final int MAX_COST = 100 * 100;
}
