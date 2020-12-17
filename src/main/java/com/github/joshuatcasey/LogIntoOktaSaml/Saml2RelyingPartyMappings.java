package com.github.joshuatcasey.LogIntoOktaSaml;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties("spring.security.saml2.relyingparty")
public class Saml2RelyingPartyMappings implements Saml2AuthorityAttributeLookup {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Map<String, String> authorityMapping = new LinkedHashMap<>();

    @SuppressWarnings("unused")
    Map<String, String> getAuthorityMapping() {
        return authorityMapping;
    }

    @Override
    public String getAuthorityAttribute(final String registrationId) {
        return this.authorityMapping.get(registrationId);
    }

}
