package fr.laerce.gestionstages.web;

import fr.laerce.gestionstages.dao.DisciplineRepository;
import fr.laerce.gestionstages.dao.DivisionRepository;
import fr.laerce.gestionstages.dao.NiveauReposiroty;
import fr.laerce.gestionstages.domain.Discipline;
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

import java.util.List;
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
public class DivisionController {

    @Autowired
    DivisionRepository repoDivision;

    ////
    @Autowired
    NiveauReposiroty repoNiveau;
    ////

    @GetMapping("/division/liste")
    public String liste(Model model){
        model.addAttribute("divisions", repoDivision.findAllByOrderByIdAsc());
        return "admin/divisionliste";
    }

    @GetMapping("/division/form")
    public String formGet(Model model){
        List<Niveau> niveaux = repoNiveau.findAllByOrderByIdAsc();
        model.addAttribute("division", new Division());
        model.addAttribute("niveaux", niveaux);
        return "admin/divisionform";
    }

    @PostMapping("/division/update")
    public String formPost(@ModelAttribute Division division){
        System.out.println(division);
        System.out.println(division.getCode());
        System.out.println(division.getLibelle());
        System.out.println(division.getNiveau());
        repoDivision.save(division);
        return "redirect:/division/liste";
    }

    @GetMapping("/division/delete/{id}")
    public String delete(@PathVariable("id")Long id){
        //repoDivision.deleteById(id);
        Optional<Division> div = repoDivision.findById(id);
        if (div.isPresent()) {
            repoDivision.delete(div.get());
        }
        return "redirect:/division/liste";
    }

    @GetMapping("/division/modif/{id}")
    public String modif(@PathVariable("id")Long id, ModelMap model){
        Optional<Division> division = repoDivision.findById(id);
        ////
        //Optional<Niveau> niveau = repoNiveau.findById(division.get().getNiveau().getId());
        ////
        List<Niveau> niveaux = repoNiveau.findAllByOrderByIdAsc();
        ////
        System.out.println("-----> MODIF   "+division.get());
        model.addAttribute("division",division.get());
        System.out.println(division.get().getNiveau());
        /*if (niveau.isPresent()) {
            model.addAttribute("niveau", niveau.get());
        }*/
        model.addAttribute("niveaux", niveaux);
        model.addAttribute("action", "modif");
        return "admin/divisionform";
        //return new ModelAndView("admin/divisionform", model);
    }

}
