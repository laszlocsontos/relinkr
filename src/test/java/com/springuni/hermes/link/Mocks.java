package com.springuni.hermes.link;

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

    static UtmParameters UTM_PARAMETERS_MINIMAL;
    static UtmParameters UTM_PARAMETERS_FULL;

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
        } catch (Exception e) {
            // This shouldn't happen, if it does, make test cases fail.
            throw new AssertionError(e);
        }
    }

}
