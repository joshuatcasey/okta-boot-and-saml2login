package com.github.joshuatcasey.LogIntoOktaSaml;

public interface Saml2AuthorityAttributeLookup {
    String getAuthorityAttribute(String registrationId);
}
