package io.relinkr.core.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class CountryTest {

    @Test
    public void givenNullCountryCode_whenFromString_thenEmpty() {
        assertFalse(Country.fromString(null).isPresent());
    }

    @Test
    public void givenEmptyCountryCode_whenFromString_thenEmpty() {
        assertFalse(Country.fromString("").isPresent());
    }

    @Test
    public void givenIllegalCountryCode_whenFromString_thenEmpty() {
        assertFalse(Country.fromString("bad").isPresent());
    }

    @Test
    public void givenUnknownCountryCode_whenFromString_thenEmpty() {
        // The old country code of the Soviet Union should be classified as unknown
        assertEquals(Country.ZZ, Country.fromString("SU").get());
    }

    @Test
    public void givenLowercaseCountryCode_whenFromString_thenEmpty() {
        assertEquals(Country.US, Country.fromString("us").get());
    }

    @Test
    public void givenUppercaseCountryCode_whenFromString_thenEmpty() {
        assertEquals(Country.US, Country.fromString("US").get());
    }

}
