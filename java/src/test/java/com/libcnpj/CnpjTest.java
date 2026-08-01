package com.libcnpj;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class CnpjTest {

    private static final Path FIXTURE_PATH = Paths.get("../fixtures/test-vectors.json");

    @ParameterizedTest
    @CsvSource({
        "11.222.333/0001-81, true",
        "11222333000181, true",
        "12.ABC.345/01DE-35, true",
        "12ABC34501DE35, true"
    })
    void acceptsValidCnpj(String value, boolean expected) {
        assertEquals(expected, Cnpj.isValid(value));
    }

    @ParameterizedTest
    @CsvSource({
        "11.222.333/0001-82, false",
        "12.ABC.345/01DE-36, false",
        "00.000.000/0000-00, false",
        "00000000000000, false",
        "11.222.333/0001-8, false",
        "112233, false",
        "11.222.333/0001-8A, false",
        "12.ABC.345/01DE-3I, false",
        "11.222.333/0001_81, false",
        "11.222.333/0001 81, false",
        "12.abc.345/01de-35, false"
    })
    void rejectsInvalidCnpj(String value, boolean expected) {
        assertEquals(expected, Cnpj.isValid(value));
    }

    @Test
    void rejectsEmptyString() {
        assertFalse(Cnpj.isValid(""));
    }

    @ParameterizedTest
    @CsvSource({
        "11222333000181, 11.222.333/0001-81",
        "12ABC34501DE35, 12.ABC.345/01DE-35"
    })
    void formatsRawCnpj(String raw, String expected) {
        assertEquals(expected, Cnpj.format(raw));
    }

    @Test
    void formatReturnsOriginalWhenLengthIsWrong() {
        assertEquals("1122233", Cnpj.format("1122233"));
    }

    @Test
    void stripsMaskCharacters() {
        assertEquals("11222333000181", Cnpj.strip("11.222.333/0001-81"));
        assertEquals("12ABC34501DE35", Cnpj.strip("12.ABC.345/01DE-35"));
    }

    @Test
    void detectsFormattedValues() {
        assertTrue(Cnpj.isFormatted("11.222.333/0001-81"));
        assertTrue(Cnpj.isFormatted("12.ABC.345/01DE-35"));
        assertFalse(Cnpj.isFormatted("11222333000181"));
        assertFalse(Cnpj.isFormatted("11.222.333/0001-8"));
    }

    @ParameterizedTest
    @CsvSource({
        "112223330001, 81",
        "12ABC34501DE, 35"
    })
    void calculatesCheckDigits(String base, String expected) {
        assertEquals(expected, Cnpj.calculateCheckDigits(base));
    }

    @Test
    void calculateCheckDigitsReturnsNullForInvalidBase() {
        assertNull(Cnpj.calculateCheckDigits("11222333000"));
        assertNull(Cnpj.calculateCheckDigits("11222333000a"));
        assertNull(Cnpj.calculateCheckDigits(""));
    }

    @Test
    void sharedFixturesAreConsistent() throws IOException {
        JSONObject fixture = loadFixture();

        JSONArray valid = fixture.getJSONArray("valid");
        for (int index = 0; index < valid.length(); index = index + 1) {
            JSONObject item = valid.getJSONObject(index);
            assertTrue(Cnpj.isValid(item.getString("value")), item.getString("description"));
        }

        JSONArray invalid = fixture.getJSONArray("invalid");
        for (int index = 0; index < invalid.length(); index = index + 1) {
            JSONObject item = invalid.getJSONObject(index);
            assertFalse(Cnpj.isValid(item.getString("value")), item.getString("description"));
        }

        JSONArray checkDigits = fixture.getJSONArray("checkDigits");
        for (int index = 0; index < checkDigits.length(); index = index + 1) {
            JSONObject item = checkDigits.getJSONObject(index);
            assertEquals(item.getString("digits"), Cnpj.calculateCheckDigits(item.getString("base")));
        }

        JSONArray formatCases = fixture.getJSONArray("formatCases");
        for (int index = 0; index < formatCases.length(); index = index + 1) {
            JSONObject item = formatCases.getJSONObject(index);
            assertEquals(item.getString("formatted"), Cnpj.format(item.getString("raw")));
        }
    }

    private JSONObject loadFixture() throws IOException {
        String json = new String(Files.readAllBytes(FIXTURE_PATH));
        return new JSONObject(json);
    }
}
