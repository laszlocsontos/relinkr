package com.springuni.hermes.click.model;

import static com.springuni.hermes.test.Mocks.LINK_ID;
import static com.springuni.hermes.test.Mocks.TIMESTAMP;
import static com.springuni.hermes.test.Mocks.USER_ID;
import static com.springuni.hermes.test.Mocks.VISITOR_ID;
import static com.springuni.hermes.test.Mocks.VISITOR_IP;
import static com.springuni.hermes.core.model.Country.US;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class ClickTest {

    private Click click;

    @Before
    public void setUp() throws Exception {
        click = Click.of(LINK_ID, VISITOR_ID, USER_ID, VISITOR_IP, TIMESTAMP);
    }

    @Test
    public void shouldHaveVisitDayOfMonth() {
        assertEquals(TIMESTAMP.getDayOfMonth(), click.getVisitDayOfMonth());
    }

    @Test
    public void shouldHaveVisitDayOfWeek() {
        assertEquals(TIMESTAMP.getDayOfWeek().getValue(), click.getVisitDayOfWeek());
    }

    @Test
    public void shouldHaveVisitHour() {
        assertEquals(TIMESTAMP.getHour(), click.getVisitHour());
    }

    @Test
    public void getVisitMonth() {
        assertEquals(TIMESTAMP.getMonth().getValue(), click.getVisitMonth());
    }

    @Test
    public void shouldHaveVisitTimestamp() {
        assertEquals(TIMESTAMP, click.getVisitTimestamp());
    }

    @Test
    public void shouldHaveCountry() {
        assertEquals(US, click.with(US).getCountry().get());
    }

}
