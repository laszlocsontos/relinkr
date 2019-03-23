package com.springuni.hermes.click.model;

import static com.springuni.hermes.Mocks.LINK_ID;
import static com.springuni.hermes.Mocks.TIMESTAMP;
import static com.springuni.hermes.Mocks.VISITOR_ID;
import static com.springuni.hermes.Mocks.VISITOR_IP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.springuni.hermes.click.model.Click;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

public class ClickTest {

    private Click click;

    @Before
    public void setUp() throws Exception {
        click = new Click(VISITOR_ID, LINK_ID, VISITOR_IP, TIMESTAMP);
    }

    @Test
    public void getVisitDayOfMonth() {
        assertEquals(TIMESTAMP.getDayOfMonth(), click.getVisitDayOfMonth());
    }

    @Test
    public void getVisitDayOfWeek() {
        assertEquals(TIMESTAMP.getDayOfWeek().getValue(), click.getVisitDayOfWeek());
    }

    @Test
    public void getVisitHour() {
        assertEquals(TIMESTAMP.getHour(), click.getVisitHour());
    }

    @Test
    public void getVisitMonth() {
        assertEquals(TIMESTAMP.getMonth().getValue(), click.getVisitMonth());
    }

    @Test
    public void getVisitTimestamp() {
        assertEquals(TIMESTAMP, click.getVisitTimestamp());
    }

    @Test
    public void setVisitTimestamp() {
        click.setVisitTimestamp(LocalDateTime.MAX);
        assertNotEquals(TIMESTAMP.getDayOfMonth(), click.getVisitDayOfMonth());
        assertNotEquals(TIMESTAMP.getDayOfWeek().getValue(), click.getVisitDayOfWeek());
        assertNotEquals(TIMESTAMP.getHour(), click.getVisitHour());
        assertNotEquals(TIMESTAMP.getMonth(), click.getVisitMonth());
        assertNotEquals(TIMESTAMP, click.getVisitTimestamp());
    }

}
