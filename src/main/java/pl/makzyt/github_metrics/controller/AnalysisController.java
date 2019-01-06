package pl.makzyt.github_metrics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.makzyt.github_metrics.util.sonarqube.SQProject;
import pl.makzyt.github_metrics.util.sonarqube.SonarQube;

import java.io.IOException;
import java.util.HashMap;

@Controller
public class AnalysisController {
    @Autowired
    private SonarQube sonarQube;

    @RequestMapping(value = "/analysis/{key}", method = RequestMethod.GET)
    public String analysisGetMapping(@PathVariable("key") String key,
                                     Model model) {
        try {
            HashMap<String, SQProject> projects = sonarQube.getProjects();

            if (projects == null) {
                return "redirect:/";
            }

            SQProject project = projects.get(key);

            if (project == null) {
                return "redirect:/";
            }

            model.addAttribute("project", project);
            return "analysis";
        } catch (IOException e) {
            return "redirect:/";
        }
    }
}
