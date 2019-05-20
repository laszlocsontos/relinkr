package com.springuni.hermes.core.model;

import static com.springuni.hermes.core.model.Country.US;
import static com.springuni.hermes.core.model.Country.ZZ;
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
        assertEquals(ZZ, Country.fromString("SU").get());
    }

    @Test
    public void givenLowercaseCountryCode_whenFromString_thenEmpty() {
        assertEquals(US, Country.fromString("us").get());
    }

    @Test
    public void givenUppercaseCountryCode_whenFromString_thenEmpty() {
        assertEquals(US, Country.fromString("US").get());
    }

}
