package fr.laerce.gestionstages.web;

import fr.laerce.gestionstages.dao.DivisionRepository;
import fr.laerce.gestionstages.dao.IndividuRepository;
import fr.laerce.gestionstages.domain.Division;
import fr.laerce.gestionstages.domain.Individu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

/**
 * Projet gestionstages
 * Pour LAERCE SAS
 * <p>
 * Créé le  12/02/2018.
 *
 * @author fred
 */

@Controller
public class IndividuController {

    @Autowired
    IndividuRepository repoIndividu;

    @GetMapping("/individu/liste")
    public String liste(Model model){
        model.addAttribute("individus", repoIndividu.findAllByOrderByIdAsc());
        return "admin/individuliste";
    }

    @GetMapping("/individu/form")
    public String formGet(Model model){
        model.addAttribute("individu", new Individu());
        return "admin/individuform";
    }

    @PostMapping("/individu/update")
    public String formPost(@ModelAttribute Individu individu){
        // System.out.println(individu);
        repoIndividu.save(individu);
        return "redirect:/individu/liste";
    }

    @GetMapping("/individu/delete/{id}")
    public String delete(@PathVariable("id")Long id){
        //repoIndividu.deleteById(id);
        Optional<Individu> i = repoIndividu.findById(id);
        if (i.isPresent()) {
            repoIndividu.delete(i.get());
        }
        return "redirect:/individu/liste";
    }

    @GetMapping("/individu/modif/{id}")
    public String modif(@PathVariable("id")Long id, ModelMap model){
        Optional<Individu> individu = repoIndividu.findById(id);
        System.out.println("-----> MODIF   "+individu.get());
        model.addAttribute("individu",individu.get());
        return "admin/individuform";
        //return new ModelAndView("admin/individuform", model);
    }

    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("ADMIN,USER").build());
        return manager;
    }

}
