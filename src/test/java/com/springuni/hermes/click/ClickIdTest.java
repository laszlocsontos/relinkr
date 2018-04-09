package com.springuni.hermes.click;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Dummy test case to ensure that Lombok is doing its magic properly.
 */
public class ClickIdTest {

    @Test
    public void testCreate_withNoArgsConstructor() {
        new ClickId();
    }

    @Test
    public void testCreate_withOf() {
        ClickId clickId = ClickId.of(1L);
        assertNotNull(clickId);
    }

    @Test
    public void equals() {
        ClickId cid1 = ClickId.of(1L);
        ClickId cid2 = ClickId.of(1L);
        assertEquals(cid1, cid2);
    }

    @Test
    public void testHashCode() {
        ClickId cid1 = ClickId.of(1L);
        ClickId cid2 = ClickId.of(1L);
        assertEquals(cid1.hashCode(), cid2.hashCode());
    }

}
