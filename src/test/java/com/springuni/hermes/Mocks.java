package com.springuni.hermes;

import static com.springuni.hermes.user.model.Role.ADMIN;
import static java.util.Collections.unmodifiableSet;

import com.springuni.hermes.click.IpAddress;
import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.link.model.EmbeddedLink;
import com.springuni.hermes.link.model.InvalidUrlException;
import com.springuni.hermes.link.model.LinkSet;
import com.springuni.hermes.link.model.LongUrl;
import com.springuni.hermes.link.model.StandaloneLink;
import com.springuni.hermes.link.model.Tag;
import com.springuni.hermes.user.model.EmailAddress;
import com.springuni.hermes.user.model.User;
import com.springuni.hermes.utm.UtmParameters;
import com.springuni.hermes.utm.UtmTemplate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Mocks {

    public static final EmailAddress EMAIL_ADDRESS = new EmailAddress("test@test.com");

    public static final String LONG_URL_BASE_S
            = "https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html";

    public static final String LONG_URL_FRAGMENT_S = "#append-java.lang.String-";
    public static final String LONG_URL_WITHOUT_UTM_S = LONG_URL_BASE_S + LONG_URL_FRAGMENT_S;
    public static final String LONG_URL_VALID_UTM_S =
            LONG_URL_BASE_S + "?utm_source=source&utm_medium=medium&utm_campaign=campaign" +
                    LONG_URL_FRAGMENT_S;

    public static final String LONG_URL_INVALID_UTM_S =
            LONG_URL_BASE_S + "?utm_source=source" + LONG_URL_FRAGMENT_S;

    public static final LongUrl LONG_URL_BASE;
    public static final LongUrl LONG_URL_WITHOUT_UTM;
    public static final LongUrl LONG_URL_VALID_UTM;
    public static final LongUrl LONG_URL_INVALID_UTM;

    public static final String UTM_SOURCE_V = "source";
    public static final String UTM_MEDIUM_V = "medium";
    public static final String UTM_CAMPAIGN_V = "campaign";
    public static final String UTM_TERM_V = "term";
    public static final String UTM_CONTENT_V = "content";

    public static final UtmParameters UTM_PARAMETERS_MINIMAL;
    public static final UtmParameters UTM_PARAMETERS_FULL;
    public static final Set<UtmParameters> UTM_PARAMETERS_SET;

    public static final Long USER_ID = 1L;
    public static final String UTM_TEMPLATE_NAME = "template";

    public static final Tag TAG_A = new Tag("A");
    public static final Tag TAG_B = new Tag("B");

    public static final String IPV4_ADDRESS = "184.52.70.179";
    public static final String IPV6_ADDRESS = "2001:db8:85a3:0:0:8a2e:370:7334";

    public static final Long LINK_ID = 1L;
    public static final Long LINK_SET_ID = 1L;
    public static final Long VISITOR_ID = 1L;
    public static final IpAddress VISITOR_IP;
    public static final LocalDateTime TIMESTAMP = LocalDateTime.of(2018, 02, 28, 19, 52);

    public static final Pageable PAGEABLE = PageRequest.of(0, 10);

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

            VISITOR_IP = new IpAddress(IPV4_ADDRESS);
        } catch (Exception e) {
            // This shouldn't happen, if it does, make test cases fail.
            throw new AssertionError(e);
        }
    }

    public static UtmTemplate createUtmTemplate() {
        UtmTemplate utmTemplate = new UtmTemplate(UTM_TEMPLATE_NAME, USER_ID);
        utmTemplate.addUtmParameters(UTM_PARAMETERS_FULL);
        return utmTemplate;
    }

    public static LinkSet createLinkSet() throws InvalidUrlException {
        UtmTemplate utmTemplate = createUtmTemplate();
        LinkSet linkSet = new LinkSet(LONG_URL_BASE_S, utmTemplate, USER_ID);
        linkSet.markActive();
        linkSet.regenerateLinks();
        return linkSet;
    }

    public static EmbeddedLink createEmbeddedLink() throws InvalidUrlException {
        LinkSet linkSet = createLinkSet();
        return linkSet.getEmbeddedLinks().get(0);
    }

    public static StandaloneLink createStandaloneLink() throws ApplicationException {
        StandaloneLink standaloneLink =
                new StandaloneLink(LONG_URL_WITHOUT_UTM_S, UTM_PARAMETERS_FULL, USER_ID);
        standaloneLink.setId(2L);
        standaloneLink.markActive();
        return standaloneLink;
    }

    public static User createUser() {
        User user =
                new User(EmailAddress.of("test@springuni.com"), "Test", "test");
        user.setId(2L);
        user.grantRole(ADMIN);
        return user;
    }

}
