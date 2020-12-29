package com.github.joshuatcasey.LogIntoOktaSaml;

import java.util.Map;

public interface Saml2AuthorityAttributeLookup {
    String getAuthorityAttribute(String registrationId);
    Map<String, String> getIdentityMappings(String registrationId);
}
