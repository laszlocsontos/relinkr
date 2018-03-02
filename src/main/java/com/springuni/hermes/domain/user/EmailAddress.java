package com.springuni.hermes.domain.user;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class EmailAddress {

    private static final Pattern EMAIL_ADDRESS_PATTERN =
            Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\\b", CASE_INSENSITIVE);

    private final String value;

    public EmailAddress(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (!EMAIL_ADDRESS_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email address: " + value);
        }
    }

}
