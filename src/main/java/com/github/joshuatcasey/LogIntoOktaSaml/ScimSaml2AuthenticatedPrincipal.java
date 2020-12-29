package com.github.joshuatcasey.LogIntoOktaSaml;

import com.unboundid.scim2.common.types.Email;
import com.unboundid.scim2.common.types.Name;
import com.unboundid.scim2.common.types.UserResource;
import org.opensaml.saml.saml2.core.Assertion;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScimSaml2AuthenticatedPrincipal implements AuthenticatedPrincipal {
    private final UserResource userResource;

    private final Map<String, List<Object>> attributes;

    public ScimSaml2AuthenticatedPrincipal(
            final Assertion assertion,
            final Map<String, List<Object>> attributes,
            final Map<String, String> attributeMappings) {
        Assert.notNull(assertion, "assertion cannot be null");
        Assert.notNull(assertion.getSubject(), "assertion subject cannot be null");
        Assert.notNull(assertion.getSubject().getNameID(), "assertion subject NameID cannot be null");
        Assert.notNull(attributes, "attributes cannot be null");
        Assert.notNull(attributeMappings, "attributeMappings cannot be null");
        this.attributes = attributes;

        final Name name = new Name()
                .setFamilyName(getAttribute(attributes, attributeMappings, "familyName"))
                .setGivenName(getAttribute(attributes, attributeMappings, "givenName"));

        final List<Email> emails = new ArrayList<>(1);
        emails.add(new Email()
                .setValue(getAttribute(attributes, attributeMappings, "email"))
                .setPrimary(true));

        userResource = new UserResource()
                .setUserName(assertion.getSubject().getNameID().getValue())
                .setName(name)
                .setEmails(emails);
    }

    private static String getAttribute(
            final Map<String, List<Object>> attributes,
            final Map<String, String> attributeMappings,
            final String attributeName) {

        final String key = attributeMappings.get(attributeName);
        final List<Object> objects = attributes.get(key != null ? key : attributeName);
        if (objects == null || objects.size() < 1) {
            return null;
        }

        if (objects.get(0) instanceof String) {
            return (String) objects.get(0);
        }

        return null;
    }

    @Override
    public String getName() {
        return this.userResource.getUserName();
    }

    public UserResource getUserResource() {
        return this.userResource;
    }
}
