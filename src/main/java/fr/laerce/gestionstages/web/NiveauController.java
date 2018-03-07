package fr.laerce.gestionstages.web;

import fr.laerce.gestionstages.dao.DivisionRepository;
import fr.laerce.gestionstages.dao.NiveauReposiroty;
import fr.laerce.gestionstages.domain.Division;
import fr.laerce.gestionstages.domain.Niveau;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
public class NiveauController {

    @Autowired
    NiveauReposiroty repoNiveau;

    @GetMapping("/niveau/liste")
    public String liste(Model model){
        model.addAttribute("niveaux", repoNiveau.findAllByOrderByIdAsc());
        return "admin/niveauliste";
    }

    @GetMapping("/niveau/form")
    public String formGet(Model model){
        model.addAttribute("niveau", new Niveau());
        return "admin/niveauform";
    }

    @PostMapping("/niveau/update")
    public String formPost(@ModelAttribute Niveau niveau){
        // System.out.println(niveau);
        repoNiveau.save(niveau);
        return "redirect:/niveau/liste";
    }

    @GetMapping("/niveau/delete/{id}")
    public String delete(@PathVariable("id")Long id){
        //repoNiveau.deleteById(id);
        Optional<Niveau> n = repoNiveau.findById(id);
        if (n.isPresent()) {
            repoNiveau.delete(n.get());
        }
        return "redirect:/niveau/liste";
    }

    @GetMapping("/niveau/modif/{id}")
    public String modif(@PathVariable("id")Long id, ModelMap model){
        Optional<Niveau> niveau = repoNiveau.findById(id);
        System.out.println("-----> MODIF   "+niveau.get());
        model.addAttribute("niveau",niveau.get());
        return "admin/niveauform";
        //return new ModelAndView("admin/niveauform", model);
    }

}
