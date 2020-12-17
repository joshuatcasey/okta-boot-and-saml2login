package com.github.joshuatcasey.LogIntoOktaSaml;

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
}
