package com.github.joshuatcasey.LogIntoOktaSaml;

import org.opensaml.saml.saml2.core.Assertion;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.saml2.provider.service.authentication.DefaultSaml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.OpenSamlAuthenticationProvider.ResponseToken;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.joshuatcasey.LogIntoOktaSaml.XmlUtilsKt.getAssertionAttributes;

public class ConvertResponseToAuthentication implements
        Converter<ResponseToken, Saml2Authentication> {

    private final Saml2AuthorityAttributeLookup saml2AuthorityAttributeLookup;

    public ConvertResponseToAuthentication(final Saml2AuthorityAttributeLookup saml2AuthorityAttributeLookup) {
        this.saml2AuthorityAttributeLookup = saml2AuthorityAttributeLookup;
    }

    @Override
    public Saml2Authentication convert(ResponseToken responseToken) {
        final Assertion assertion = CollectionUtils.firstElement(responseToken.getResponse().getAssertions());
        final Map<String, List<Object>> attributes = getAssertionAttributes(assertion);
        final String registrationId = responseToken.getToken().getRelyingPartyRegistration().getRegistrationId();
        final ScimSaml2AuthenticatedPrincipal principal = new ScimSaml2AuthenticatedPrincipal(
                assertion,
                attributes,
                saml2AuthorityAttributeLookup.getIdentityMappings(registrationId));
        final Collection<? extends GrantedAuthority> assertionAuthorities =
                getAssertionAuthorities(attributes,
                        saml2AuthorityAttributeLookup.getAuthorityAttribute(registrationId));
        return new Saml2Authentication(
                principal,
                responseToken.getToken().getSaml2Response(),
                assertionAuthorities);
    }

    private static Collection<? extends GrantedAuthority> getAssertionAuthorities(
            final Map<String, List<Object>> attributes,
            final String authoritiesAttributeName) {
        if (attributes == null) {
            return Collections.emptySet();
        }

        final List<Object> groups = new ArrayList<>(attributes.get(authoritiesAttributeName));
        return groups.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(String::toLowerCase)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
