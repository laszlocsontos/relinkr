package com.springuni.hermes.domain.link;

import static java.util.Collections.unmodifiableSet;

import com.springuni.hermes.domain.user.UserId;
import java.util.LinkedHashSet;
import java.util.Set;

class Mocks {

    static final String LONG_URL_BASE_S
            = "https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html";

    static final String LONG_URL_FRAGMENT_S = "#append-java.lang.String-";
    static final String LONG_URL_WITHOUT_UTM_S = LONG_URL_BASE_S + LONG_URL_FRAGMENT_S;
    static final String LONG_URL_VALID_UTM_S =
            LONG_URL_BASE_S + "?utm_source=source&utm_medium=medium&utm_campaign=campaign" +
                    LONG_URL_FRAGMENT_S;

    static final String LONG_URL_INVALID_UTM_S =
            LONG_URL_BASE_S + "?utm_source=source" + LONG_URL_FRAGMENT_S;

    static final LongUrl LONG_URL_BASE;
    static final LongUrl LONG_URL_WITHOUT_UTM;
    static final LongUrl LONG_URL_VALID_UTM;
    static final LongUrl LONG_URL_INVALID_UTM;

    static final String UTM_SOURCE_V = "source";
    static final String UTM_MEDIUM_V = "medium";
    static final String UTM_CAMPAIGN_V = "campaign";
    static final String UTM_TERM_V = "term";
    static final String UTM_CONTENT_V = "content";

    static final UtmParameters UTM_PARAMETERS_MINIMAL;
    static final UtmParameters UTM_PARAMETERS_FULL;
    static final Set<UtmParameters> UTM_PARAMETERS_SET;

    static final UserId OWNER = new UserId(1);
    static final String UTM_TEMPLATE_NAME = "template";

    static final Tag TAG_A = new Tag("A");
    static final Tag TAG_B = new Tag("B");

    static {
        try {
            LONG_URL_BASE = new LongUrl(LONG_URL_BASE_S);
            LONG_URL_WITHOUT_UTM = new LongUrl(LONG_URL_WITHOUT_UTM_S);
            LONG_URL_VALID_UTM = new LongUrl(LONG_URL_VALID_UTM_S);
            LONG_URL_INVALID_UTM = new LongUrl(LONG_URL_INVALID_UTM_S);

            UTM_PARAMETERS_FULL = new UtmParameters(
                    UTM_SOURCE_V,
                    UTM_MEDIUM_V,
                    UTM_CAMPAIGN_V,
                    UTM_TERM_V,
                    UTM_CONTENT_V
            );

            UTM_PARAMETERS_MINIMAL = new UtmParameters(
                    UTM_SOURCE_V,
                    UTM_MEDIUM_V,
                    UTM_CAMPAIGN_V
            );

            Set<UtmParameters> utmParametersSet = new LinkedHashSet<>(2);
            utmParametersSet.add(UTM_PARAMETERS_FULL);
            utmParametersSet.add(UTM_PARAMETERS_MINIMAL);
            UTM_PARAMETERS_SET = unmodifiableSet(utmParametersSet);

        } catch (Exception e) {
            // This shouldn't happen, if it does, make test cases fail.
            throw new AssertionError(e);
        }
    }

}
