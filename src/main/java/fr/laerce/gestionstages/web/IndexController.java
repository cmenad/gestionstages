package fr.laerce.gestionstages.web;


import fr.laerce.gestionstages.dao.*;
import fr.laerce.gestionstages.domain.*;
import fr.laerce.gestionstages.service.ImportProfesseursFromSTS;
import fr.laerce.gestionstages.service.ImportSTSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class IndexController {

    // TODO placer vers ProfesseursControler ?
    @Autowired
    private ProfesseurRepository profRepo;
    // FIN TODO

    @GetMapping("/")
    public String index(){
        return "index";
    }

    // TODO login
    @GetMapping("/login")
    public String login() {
        return "login";
    }



    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }
        return "redirect:/";
    }
    // FIN TODO

    // TODO placer vers ProfesseursController ?
    @GetMapping("/professeurs/{codeSynchro}/divisions")
    @ResponseBody
    public String divisionsForProf(@PathVariable("codeSynchro") String codeSynchro) {
        List<Division> divisions = profRepo.findDivisionsForProfesseur(codeSynchro);

        String divisionsStr = "";
        for (Division div : divisions) {
            divisionsStr += div;
        }
        if (divisionsStr.equals("")){
            divisionsStr = "Pas de divisions pour ce professeur";
        }
        return divisionsStr;
    }
    // FIN TODO

    // TODO placer vers ProfesseursController ?
    @GetMapping("/professeurs/{codeSynchro}/niveaux")
    @ResponseBody
    public String niveauxForProf(@PathVariable("codeSynchro") String codeSynchro) {
        List<Niveau> niveaux = profRepo.findNiveauxForProfesseur(codeSynchro);

        String niveauxStr = "";
        for (Niveau niv : niveaux) {
            niveauxStr += niv;
        }
        if (niveauxStr.equals("")){
            niveauxStr = "Pas de niveau pour ce professeur";
        }
        return niveauxStr;
    }
    // FIN TODO


    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

    /** un dropdown qui fonctionne avec BT-beta et CDN qui vont bien
     *
     */
    @GetMapping("/dropdown")
    public String dropdown(){
        return "testDropDown";
    }

    @GetMapping("/login-error")
    public String loginError() {
        return "login";
    }

}
