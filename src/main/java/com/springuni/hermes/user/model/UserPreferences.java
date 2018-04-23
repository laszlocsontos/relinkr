package com.springuni.hermes.user.model;

import static com.springuni.hermes.core.model.TimeZone.UTC;
import static java.util.Locale.ENGLISH;
import static lombok.AccessLevel.PACKAGE;

import com.springuni.hermes.core.model.TimeZone;
import java.util.Locale;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Embeddable
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
@ToString(of = {"timeZone", "locale"})
public class UserPreferences {

    @NotNull
    private TimeZone timeZone = UTC;

    @NotNull
    private Locale locale = ENGLISH;

}
