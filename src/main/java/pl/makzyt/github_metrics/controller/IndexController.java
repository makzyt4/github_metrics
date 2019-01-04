package pl.makzyt.github_metrics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.makzyt.github_metrics.model.RepositoryForm;

import javax.validation.Valid;

@Controller
public class IndexController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexGet(Model model) {
        model.addAttribute("form", new RepositoryForm());

        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexPost(@Valid @ModelAttribute("form") RepositoryForm form,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "index";
        }

        return "results";
    }
}
