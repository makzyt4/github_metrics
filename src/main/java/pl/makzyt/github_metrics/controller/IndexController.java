package pl.makzyt.github_metrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.makzyt.github_metrics.model.LoginFormModel;
import pl.makzyt.github_metrics.util.sonarqube.SonarQube;

import java.io.IOException;

@Controller
public class IndexController {
    @Autowired
    private SonarQube sonarQube;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexGetMapping(Model model) {
        boolean loggedIn;

        try {
            loggedIn = sonarQube.privileged();
        } catch (IOException e) {
            loggedIn = false;
        }

        model.addAttribute("form", new LoginFormModel());

        if (loggedIn) {
            return "project_list";
        }

        return "login";
    }
}
