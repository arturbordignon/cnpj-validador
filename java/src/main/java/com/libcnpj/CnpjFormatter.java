package com.libcnpj;

import java.util.regex.Pattern;

public final class CnpjFormatter {

    private static final Pattern MASK_CHARACTERS = Pattern.compile("[./-]");
    private static final Pattern FORMATTED_PATTERN = Pattern.compile("^[A-Z0-9]{2}\\.[A-Z0-9]{3}\\.[A-Z0-9]{3}/[A-Z0-9]{4}-[0-9]{2}$");
    private static final Pattern ALLOWED_CHARACTERS_PATTERN = Pattern.compile("^[A-Z0-9./-]+$");

    private CnpjFormatter() {
    }

    public static String strip(String value) {
        return MASK_CHARACTERS.matcher(value).replaceAll("");
    }

    public static String format(String value) {
        String clean = strip(value);

        if (clean.length() != 14) {
            return value;
        }

        return clean.substring(0, 2)
            + "."
            + clean.substring(2, 5)
            + "."
            + clean.substring(5, 8)
            + "/"
            + clean.substring(8, 12)
            + "-"
            + clean.substring(12, 14);
    }

    public static boolean isFormatted(String value) {
        return FORMATTED_PATTERN.matcher(value).matches();
    }

    public static boolean containsOnlyAllowedCharacters(String value) {
        return ALLOWED_CHARACTERS_PATTERN.matcher(value).matches();
    }
}
