package fr.laerce.gestionstages.web;


import fr.laerce.gestionstages.dao.*;
import fr.laerce.gestionstages.domain.*;
import fr.laerce.gestionstages.service.ImportProfesseursFromSTS;
import fr.laerce.gestionstages.service.ImportSTSException;
import fr.laerce.gestionstages.service.LoginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class AdminController {

    @Autowired
    private DisciplineRepository repoDiscipline;

    @Autowired
    private NiveauReposiroty repoNiveau;

    @Autowired
    private DivisionRepository repoDivision;

    @Autowired
    private ProfesseurRepository repoProfesseur;

    @Autowired
    private EleveRepository repoEleve;

    @Autowired
    private ImportProfesseursFromSTS importProfesseursFromSTS;

    @Value("${gstages.import}")
    private String stsFileName;

    @Value("${gstages.upload.dir}")
    private String uploadFolder;

    @Autowired
    private LoginManager loginmanager;

    @GetMapping("/populate")
    public String populate(Model model) {
        String errorServiceImport = "";

        try {
            // changer le lien avec le fichier dans le docs
            //this.importProfesseursFromSTS.parseAndCreateUpdateIntoDatabase("J:/CDI/JAVAprojets/gestionStages/src/main/docs/sts_emp_0940321S_2017(clean).xml");
            this.importProfesseursFromSTS.parseAndCreateUpdateIntoDatabase(stsFileName);
            //this.importProfesseursFromSTS.parseAndCreateUpdateIntoDatabase("F:/CDI/JAVAprojets/gestionStagesV2Layout/src/main/docs/sts_emp.xml");

        } catch (ImportSTSException e) {
            errorServiceImport = "Erreur traitement XML : " + e.getMessage();
        } catch (Exception e) {
            errorServiceImport = "Erreur synchronisation base de données : " + e.getMessage();
        }

        if (!errorServiceImport.isEmpty()) {
            model.addAttribute("message", errorServiceImport);
        }
        for(Professeur professeur:repoProfesseur.findAll()){
           loginmanager.createLoginForIndividu(professeur.getId());
        }

        model.addAttribute("niveaux", importProfesseursFromSTS.getDicoNiveaux().size());
        model.addAttribute("disciplines", importProfesseursFromSTS.getDicoDisciplines().size());
        model.addAttribute("divisions", importProfesseursFromSTS.getDicoDivisions().size());
        model.addAttribute("individus", importProfesseursFromSTS.getDicoIndividus().size());

        System.out.println("importSTS finished");
        System.out.println("Opening of the web page index");
        // rediriger vers l'index
        //return "populate";
        return "redirect:/";
    }

    // GESTION DE L'UPLOAD DU FICHIER
    //Save the uploaded file to this folder
    //private static String UPLOADED_FOLDER = "J:/CDI/JAVAprojets/gestionStagesV2Layout/src/main/docs/";
    //private static String UPLOADED_FOLDER = "F:/CDI/JAVAprojets/gestionStagesV2Layout/src/main/docs/";

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
            //System.out.println(file.getOriginalFilename());
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            //Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Path path = Paths.get(uploadFolder + "sts_emp.xml");
            //System.out.println(path);
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/uploadStatus";
    }

    @GetMapping("/upload")
    public String upload(){
        return "admin/upload";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "admin/uploadStatus";
    }
    // FIN GESTION UPLOAD



    // après avoir étudier
    //  https://spring.io/guides/gs/accessing-data-jpa/
    // puis
    //  https://spring.io/guides/gs/handling-form-submission/
    // implémenter une interaction de création, suppression et liste
    // des objets de type Discipline

    // voici un extrait de solution de suppression sans confirmation
    // de l'utilisateur.


    @GetMapping("/test")
    public String testCheck(Model model) {
        Professeur p1 = new Professeur();
        Eleve p2 = new Eleve();
        try {
            p1.setCivilite("M.");
            p1.setEmail("aze@rty");
            p1.setLogin("login5");
            repoProfesseur.save(p1);
            p2.setCivilite("Me");
            p2.setEmail("aze@error");
            p2.setLogin("login6");
            repoEleve.save(p2);


        } catch (Exception e) {
            model.addAttribute("message", "Non enregistré : " + e.getMessage());
        }
        model.addAttribute("individu", p1);
        return "testcheck";
    }
}



