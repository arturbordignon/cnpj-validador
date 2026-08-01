package com.libcnpj;

public final class Cnpj {

    private Cnpj() {
    }

    public static boolean isValid(String value) {
        return CnpjValidator.isValid(value);
    }

    public static String format(String value) {
        return CnpjFormatter.format(value);
    }

    public static String strip(String value) {
        return CnpjFormatter.strip(value);
    }

    public static boolean isFormatted(String value) {
        return CnpjFormatter.isFormatted(value);
    }

    public static String calculateCheckDigits(String base) {
        return CnpjValidator.calculateCheckDigits(base);
    }
}
