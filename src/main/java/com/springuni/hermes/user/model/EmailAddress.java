package com.springuni.hermes.user.model;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Embeddable
@EqualsAndHashCode(of = "value")
@ToString(of = "value")
public class EmailAddress {

    private static final Pattern EMAIL_ADDRESS_PATTERN =
            Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\\b", CASE_INSENSITIVE);

    private String value;

    public EmailAddress(String value) {
        validate(value);
        this.value = value;
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    EmailAddress() {
    }

    public static EmailAddress of(String value) {
        return new EmailAddress(value);
    }

    private void validate(String value) {
        if (!EMAIL_ADDRESS_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email address: " + value);
        }
    }

}
