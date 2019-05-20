package io.relinkr.user.model;

import static java.util.Locale.ENGLISH;
import static lombok.AccessLevel.PACKAGE;

import io.relinkr.core.model.TimeZone;
import java.util.Locale;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString(of = {"timeZone", "locale"})
public class UserPreferences {

    @NonNull
    private TimeZone timeZone = TimeZone.UTC;

    @NonNull
    private Locale locale = ENGLISH;

}
