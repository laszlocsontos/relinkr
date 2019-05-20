package io.relinkr.link.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LinkViewController {

    @GetMapping("/pages/dashboard")
    public String dashboard() {
        return "pages/dashboard";
    }

    @GetMapping("/pages/links")
    public String links() {
        return "pages/links";
    }

    @GetMapping("/pages/stats")
    public String stats() {
        return "pages/stats";
    }

}
