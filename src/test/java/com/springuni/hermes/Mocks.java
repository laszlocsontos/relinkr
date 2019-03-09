package com.springuni.hermes;

import static com.springuni.hermes.user.model.Gender.MALE;
import static com.springuni.hermes.user.model.Role.ADMIN;
import static com.springuni.hermes.user.model.UserProfileType.GOOGLE;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;

import com.springuni.hermes.click.ClickId;
import com.springuni.hermes.click.IpAddress;
import com.springuni.hermes.core.model.ApplicationException;
import com.springuni.hermes.core.security.authn.signin.SignInRequest;
import com.springuni.hermes.link.model.LinkId;
import com.springuni.hermes.link.model.LongUrl;
import com.springuni.hermes.link.model.StandaloneLink;
import com.springuni.hermes.link.model.Tag;
import com.springuni.hermes.user.model.EmailAddress;
import com.springuni.hermes.user.model.User;
import com.springuni.hermes.user.model.UserId;
import com.springuni.hermes.user.model.UserProfile;
import com.springuni.hermes.utm.model.UtmParameters;
import com.springuni.hermes.utm.model.UtmTemplate;
import com.springuni.hermes.utm.model.UtmTemplateId;
import com.springuni.hermes.visitor.model.VisitorId;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Mocks {

    public static final EmailAddress EMAIL_ADDRESS = new EmailAddress("test@test.com");

    public static final String LONG_URL_BASE_S
            = "https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/";

    public static final String LONG_URL_FRAGMENT_S = "#howto-spring-mvc";
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

    public static final UserId USER_ID = UserId.of(1L);
    public static final UserId USER_ID_ZERO = UserId.of(0L);

    public static final UtmTemplateId UTM_TEMPLATE_ID = UtmTemplateId.of(1L);
    public static final UtmTemplateId UTM_TEMPLATE_ID_ZERO = UtmTemplateId.of(0L);

    public static final String UTM_TEMPLATE_NAME = "template";

    public static final Tag TAG_A = new Tag("A");
    public static final Tag TAG_B = new Tag("B");

    public static final ClickId CLICK_ID = ClickId.of(1L);
    public static final ClickId CLICK_ID_ZERO = ClickId.of(0L);

    public static final String IPV4_ADDRESS = "184.52.70.179";
    public static final String IPV6_ADDRESS = "2001:db8:85a3:0:0:8a2e:370:7334";

    public static final LinkId LINK_ID = LinkId.of(1L);
    public static final LinkId LINK_ID_ZERO = LinkId.of(0L);

    public static final VisitorId VISITOR_ID = VisitorId.of(1L);
    public static final VisitorId VISITOR_ID_ZERO = VisitorId.of(0L);

    public static final IpAddress VISITOR_IP;
    public static final LocalDateTime TIMESTAMP = LocalDateTime.of(2018, 02, 28, 19, 52);

    public static final Pageable PAGEABLE = PageRequest.of(0, 10);

    public static final String JWT_TOKEN_VALID =
            "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIzNDU0MzUzMjQ1MzQ1MyIsInN1YiI6IjUzMjQ1"
                    + "MzQ1MzQ1MzQ1IiwiZXhwIjoyNTM0MDIyMTQ0MDAsImlhdCI6MTUxNjIzOTAyMiwiYXV0aG9yaXR"
                    + "pZXMiOiJVU0VSIn0.xTT81NHZdRdo-Enk5Dfl-v90wYcbF5sbHCKmda5yTB8n5kZ3Y-VhykVGXn"
                    + "VmfgsPXPgO6QmmpIky1vPQYRUHsw";

    public static final String JWT_TOKEN_INVALID =
            "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIzNDU0MzUzMjQ1MzQ1MyIsInN1YiI6IjUzMjQ1"
                    + "MzQ1MzQ1MzQ1IiwiZXhwIjoyNTM0MDIyMTQ0MDAsImlhdCI6MTUxNjIzOTAyMiwiYXV0aG9yaXR"
                    + "pZXMiOiJVU0VSIn0.B_m-1j9SqmjrcyHwyMMKsxdBi9qLe2akpfZXq4VPG73ppuJXCuB6GPvDvH"
                    + "GeMqLQkCA1Al7iBu1oGU7i5QHc5A";

    public static final String JWT_TOKEN_EXPIRED =
            "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIzNDU0MzUzMjQ1MzQ1MyIsInN1YiI6IjUzMjQ1"
                    + "MzQ1MzQ1MzQ1IiwiZXhwIjoxNTE0Njc4NDAwLCJpYXQiOjE1MTYyMzkwMjIsImF1dGhvcml0aWV"
                    + "zIjoiVVNFUiJ9.tJYq8hIbSc2gSKNU2F4c29Nn6X1L4HgEPnqS8MIEDEMN0nCnYLNqo_yWStemV"
                    + "vcOV0YVKWvZCey3KspAhEXQKA";

    public static final String JWT_SECRET_KEY =
            "cFZJY3VpV2RMZHZMQTdVNzRAMVUqc2RFWTJoSlNpJk5MNzE2TkghI1FqKnEmKjk2TjY4TnZ5MG9t";

    public static final String CLEARTEXT_PASSWORD = "password";

    public static final String ENCRYPTED_PASSWORD =
            "{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG";

    public static final Map<String, Object> GOOGLE_USER_ATTRIBUTES;

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

            Map<String, Object> googleUserAttributes = new HashMap<>();

            googleUserAttributes.put("sub", "12345789");
            googleUserAttributes.put("name", "László Csontos");
            googleUserAttributes.put("given_name", "László");
            googleUserAttributes.put("family_name", "Csontos");
            googleUserAttributes.put("link", "https://plus.google.com/104401221461109262503");
            googleUserAttributes.put("picture",
                    "https://lh3.googleusercontent.com/-7EVTpxqEgj8/AAAAAAAAAAI/AAAAAAAAAAA/Qo9wrOAoxPU/photo.jpg");

            GOOGLE_USER_ATTRIBUTES = unmodifiableMap(googleUserAttributes);
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

    public static StandaloneLink createStandaloneLink() throws ApplicationException {
        StandaloneLink standaloneLink =
                new StandaloneLink(LONG_URL_WITHOUT_UTM_S, UTM_PARAMETERS_FULL, USER_ID);
        standaloneLink.setId(LINK_ID);
        standaloneLink.markActive();
        return standaloneLink;
    }

    public static User createUser() {
        User user = new User(EMAIL_ADDRESS, ENCRYPTED_PASSWORD);
        user.setId(USER_ID);
        user.grantRole(ADMIN);
        return user;
    }

    public static UserProfile createUserProfile() {
        return UserProfile.of(
                GOOGLE, "123456789", "Laszlo Csontos", "Laszlo",
                null, "Csontos", null, null, MALE,
                null
        );
    }

    public static SignInRequest createSignInRequest() {
        return new SignInRequest(EMAIL_ADDRESS.getValue(), CLEARTEXT_PASSWORD);
    }

}
