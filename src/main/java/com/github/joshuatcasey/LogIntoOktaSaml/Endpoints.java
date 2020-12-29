package com.github.joshuatcasey.LogIntoOktaSaml;

import com.unboundid.scim2.common.types.UserResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Endpoints {
    @GetMapping("/")
    public String index() {
        return "home page, try \"/public\", \"/protected\", or \"/admins\"";
    }

    @GetMapping("public")
    public String getPublic() {
        return "public";
    }

    @GetMapping("protected")
    public String getProtected() {
        return "protected";
    }

    @GetMapping("admins")
    public String getAdmins() {
        return "admins";
    }

    @GetMapping("whoami")
    public UserResource whoami() {
        if (SecurityContextHolder.getContext() == null
                || SecurityContextHolder.getContext().getAuthentication() == null) {
            throw new RuntimeException("whoami not able to find identity");
        }

        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof ScimSaml2AuthenticatedPrincipal) {
            return ((ScimSaml2AuthenticatedPrincipal) principal).getUserResource();
        }

        throw new RuntimeException("whoami not able to find identity");
    }
}
