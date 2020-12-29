package com.github.joshuatcasey.LogIntoOktaSaml;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.saml2.provider.service.authentication.OpenSamlAuthenticationProvider;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Saml2AuthorityAttributeLookup saml2AuthorityAttributeLookup;

    public SecurityConfiguration(final Saml2AuthorityAttributeLookup saml2AuthorityAttributeLookup) {
        this.saml2AuthorityAttributeLookup = saml2AuthorityAttributeLookup;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        OpenSamlAuthenticationProvider authenticationProvider = new OpenSamlAuthenticationProvider();
        authenticationProvider
                .setResponseAuthenticationConverter(
                        new ConvertResponseToAuthentication(saml2AuthorityAttributeLookup));

        // @formatter:off
        http.authorizeRequests()
                .antMatchers("/").authenticated()
                .antMatchers("/protected").authenticated()
                .antMatchers("/whoami").authenticated()
                .antMatchers("/public").permitAll()
                .antMatchers("/admins").hasAuthority("admins")
                .anyRequest().denyAll()
                .and()
                    .saml2Login(saml2 ->
                            saml2.authenticationManager(new ProviderManager(authenticationProvider))
                    );
        // @formatter:on
    }
}
