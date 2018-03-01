package com.springuni.hermes.link;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Embeddable
@EqualsAndHashCode
@ToString
public class UtmParameters {

    static final String UTM_SOURCE = "utm_source";
    static final String UTM_MEDIUM = "utm_medium";
    static final String UTM_CAMPAIGN = "utm_campaign";
    static final String UTM_TERM = "utm_term";
    static final String UTM_CONTENT = "utm_content";

    private String utmSource;
    private String utmMedium;
    private String utmCampaign;
    private String utmTerm;
    private String utmContent;

    public UtmParameters(String utmSource, String utmMedium, String utmCampaign)
            throws MissingUtmParameterException {

        this(utmSource, utmMedium, utmCampaign, null, null);
    }

    public UtmParameters(
            String utmSource, String utmMedium, String utmCampaign, String utmTerm,
            String utmContent) throws MissingUtmParameterException {

        if (!StringUtils.hasText(utmSource)) {
            throw MissingUtmParameterException.forUtmParameter(UTM_SOURCE);
        }

        if (!StringUtils.hasText(utmMedium)) {
            throw MissingUtmParameterException.forUtmParameter(UTM_MEDIUM);
        }

        if (!StringUtils.hasText(utmCampaign)) {
            throw MissingUtmParameterException.forUtmParameter(UTM_CAMPAIGN);
        }

        this.utmSource = utmSource;
        this.utmMedium = utmMedium;
        this.utmCampaign = utmCampaign;
        this.utmTerm = utmTerm;
        this.utmContent = utmContent;
    }

    /*
     * http://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html_single/#persistent-classes-pojo-constructor
     */
    UtmParameters() {
    }

    public Map<String, String> asMap() {
        Map<String, String> utmParameterMap = new LinkedHashMap<>();

        // Mandatory UTM parameters
        utmParameterMap.put(UTM_SOURCE, utmSource);
        utmParameterMap.put(UTM_MEDIUM, utmMedium);
        utmParameterMap.put(UTM_CAMPAIGN, utmCampaign);

        // Optional UTM parameters
        getUtmTerm().ifPresent(utmTerm -> utmParameterMap.put(UTM_TERM, utmTerm));
        getUtmContent().ifPresent(utmContent -> utmParameterMap.put(UTM_CONTENT, utmContent));

        return Collections.unmodifiableMap(utmParameterMap);
    }

    public String getUtmSource() {
        return utmSource;
    }

    public String getUtmMedium() {
        return utmMedium;
    }

    public String getUtmCampaign() {
        return utmCampaign;
    }

    public Optional<String> getUtmTerm() {
        return Optional.ofNullable(utmTerm);
    }

    public Optional<String> getUtmContent() {
        return Optional.ofNullable(utmContent);
    }

    public static UtmParameters of(Map<String, String> utmParameterMap)
            throws MissingUtmParameterException {

        Assert.notNull(utmParameterMap, "utmParameterMap cannot be null");

        return new UtmParameters(
                utmParameterMap.get(UTM_SOURCE),
                utmParameterMap.get(UTM_MEDIUM),
                utmParameterMap.get(UTM_CAMPAIGN),
                utmParameterMap.get(UTM_TERM),
                utmParameterMap.get(UTM_CONTENT)
        );
    }

}
