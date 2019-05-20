package io.relinkr.user.model;

import org.junit.Test;

public class EmailAddressTest {

    @Test
    public void isValid_withValid_newTLD() {
        new EmailAddress("fabio@disapproved.solutions");
    }

    @Test
    public void isValid_withValid_oldTLD() {
        new EmailAddress("fabio@disapproved-solutions.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void isValid_withInValid() {
        new EmailAddress("zoé@disapproved-solutions.com");
    }

}
