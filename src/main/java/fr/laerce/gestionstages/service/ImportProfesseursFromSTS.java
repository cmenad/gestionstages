package fr.laerce.gestionstages.service;

import fr.laerce.gestionstages.dao.*;
import fr.laerce.gestionstages.domain.Discipline;
import fr.laerce.gestionstages.domain.Division;
import fr.laerce.gestionstages.domain.Niveau;
import fr.laerce.gestionstages.domain.Professeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImportProfesseursFromSTS {

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

    private String fileName;
    private DocumentBuilder documentBuilder;
    private Document document;
    private XPath xPath;

    private Map<String, Niveau> dicoNiveaux;
    private Map<String, Division> dicoDivisions;
    private Map<String, Professeur> dicoIndividus;
    private Map<String, Discipline> dicoDisciplines;

    public ImportProfesseursFromSTS() {
        dicoNiveaux = new HashMap<>();
        dicoDivisions = new HashMap<>();
        dicoIndividus = new HashMap<>();
        dicoDisciplines = new HashMap<>();
    }

    /**
     * Permet d'importer (mettre à jour ou création) les professeurs et les disciplines, divisions, niveaux et
     *   auxquels ils sont attachés.
     *   http://cria.ac-amiens.fr/cria/cria/cartable/sconet/pagesHtmFormateur/glossaire.htm
     *   http://cria.ac-amiens.fr/cria/cria/cartable/sconet/aideEnLigne/aideSTS_V2/00-glossaire.htm
     *
     * @param fileName document XML au format STS (URL  vers la description
     *                 de la structure XML STSWEB du SIECLE ?)
     *
     * @throws  ImportSTSException si le parse XML échoue
     */
    public void parseAndCreateUpdateIntoDatabase(String fileName) throws  ImportSTSException {
        this.parse(fileName);
        this.importIntoDataBase();
    }

    public void parse(String fileName) {
        try {
            this.fileName = fileName;

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            XPathFactory xPathFactory = XPathFactory.newInstance();
            xPath = xPathFactory.newXPath();
            document = documentBuilder.parse(new FileInputStream(fileName));

            // Initialisation des dictionnaires
            this.dicoNiveaux = buildNiveaux();
            this.dicoDivisions = buildDivision();
            this.dicoDisciplines = buildDiscipline();
            this.dicoIndividus = buildIndividu();

        } catch (Exception e) {
            e.printStackTrace();
            String message = (e.getMessage() == null) ? e.getClass().getName() : e.getMessage();
            throw new ImportSTSException(message);
        }
    }


    private void importIntoDataBase() {

        List<Discipline> updatedDisciplines = new ArrayList<>();

        for (String code : getDicoDisciplines().keySet()) {
            Discipline disciplineDBManaged = repoDiscipline.findByCode(code);
            Discipline disciplineImported = getDicoDisciplines().get(code);

            if (disciplineDBManaged != null) {
                if (!disciplineDBManaged.equals(disciplineImported)) {
                    updatedDisciplines.add(disciplineDBManaged);
                    // TODO "log" updated objects by import
                }
                // mise à jour des attributs d'après l'objet nouvellement importé
                disciplineDBManaged.setLibelle(disciplineImported.getLibelle());
                repoDiscipline.save(disciplineDBManaged);
            } else {
                repoDiscipline.save(disciplineImported);
            }
        }

        for (String code : getDicoNiveaux().keySet()) {
            Niveau niveauManaged = repoNiveau.findByCode(code);
            Niveau niveauImported = getDicoNiveaux().get(code);

            if (niveauManaged != null) {
                if (!niveauManaged.equals(niveauImported)) {

                }
                niveauManaged.setLibelleCourt(niveauImported.getLibelleCourt());
                niveauManaged.setLibelleLong(niveauImported.getLibelleLong());
                repoNiveau.save(niveauManaged);
            } else {
                repoNiveau.save(niveauImported);
            }
        }

        for (String code : getDicoDivisions().keySet()) {
            Division divisionManaged = repoDivision.findByCode(code);
            Division divisionImported = getDicoDivisions().get(code);

            if (divisionManaged != null) {
                divisionManaged.setLibelle(divisionImported.getLibelle());
                repoDivision.save(divisionManaged);
            } else {
                Niveau niveau = repoNiveau.findByCode(divisionImported.getNiveau().getCode());
                divisionImported.setNiveau(niveau);
                repoDivision.save(divisionImported);
            }
        }

        for (String code : getDicoIndividus().keySet()) {
            Professeur individuManaged = repoProfesseur.findByCodeSynchro(code);
            Professeur individuImported = getDicoIndividus().get(code);

            if (individuManaged != null) {
              // update ? pb de classes wrappers... il faut demander
              // explicitement les instances aux dao (afin d'éviter une second insertion
              // des objets de la collection ... voir si pas une autre solution ?)
              for (Division division : individuImported.getDivisions()){
                division = repoDivision.findByCode(division.getCode());
                individuManaged.addDivision(division);
              }
              for (Discipline discipline : individuImported.getDisciplines()) {
                discipline = repoDiscipline.findByCode(discipline.getCode());
                individuManaged.addDiscipline(discipline);
              }
              // et plus...
              repoProfesseur.save(individuManaged);
            } else {
              repoProfesseur.save(individuImported);
            }
        }
    }


    public Map<String, Niveau> getDicoNiveaux() {
        return dicoNiveaux;
    }

    public Map<String, Division> getDicoDivisions() {
        return dicoDivisions;
    }

    public Map<String, Professeur> getDicoIndividus() {
        return dicoIndividus;
    }

    public Map<String, Discipline> getDicoDisciplines() {
        return dicoDisciplines;
    }

    public String getFileName() {
        return fileName;
    }

    private Map<String, Niveau> buildNiveaux() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

        final String xPathNiveaux = "/STS_EDT/NOMENCLATURES/MEFSTATS4/*";

        Map<String, Niveau> niveaux = new HashMap<>();

        XPathExpression expression = xPath.compile(xPathNiveaux);
        NodeList nl1 = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nl1.getLength(); i++) {
            Node n1 = nl1.item(i);
            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                NamedNodeMap nnm = n1.getAttributes();
                String code = nnm.getNamedItem("CODE").getTextContent();
                if (!niveaux.containsKey(code)) {
                    Niveau niveau = new Niveau();
                    niveau.setCode(code);
                    NodeList nl2 = n1.getChildNodes();
                    for (int j = 0; j < nl2.getLength(); j++) {
                        Node n2 = nl2.item(j);
                        if (n2.getNodeType() == Node.ELEMENT_NODE) {
                            switch (n2.getNodeName()) {
                                case "LIBELLE_COURT":
                                    niveau.setLibelleCourt(n2.getTextContent());
                                    break;
                                case "LIBELLE_LONG":
                                    niveau.setLibelleLong(n2.getTextContent());
                                    break;
                            }
                        }
                    }
                    niveaux.put(code, niveau);
                }
            }
        }
        //System.out.println(niveaux.get("2213"));
        return niveaux;
    }

    private Map<String, Division> buildDivision() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        final String xPathDivisions = "/STS_EDT/DONNEES/STRUCTURE/DIVISIONS/*";

        Map<String, Division> divisions = new HashMap<>();

        XPathExpression expression = xPath.compile(xPathDivisions);
        NodeList nl1 = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nl1.getLength(); i++) {
            //List<Individu> lstProfs = new ArrayList<>();
            Node n1 = nl1.item(i);
            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                NamedNodeMap nnm = n1.getAttributes();
                String code = nnm.getNamedItem("CODE").getTextContent();
                if (!divisions.containsKey(code)) {
                    Division division = new Division();
                    division.setCode(code);
                    NodeList nl2 = n1.getChildNodes();
                    for (int j = 0; j < nl2.getLength(); j++) {
                        Node n2 = nl2.item(j);
                        if (n2.getNodeType() == Node.ELEMENT_NODE) {
                            switch (n2.getNodeName()) {
                                case "LIBELLE_LONG":
                                    division.setLibelle(n2.getTextContent());
                                    break;
                                //// LIAISON VERS NIVEAU ////
                                case "MEFS_APPARTENANCE":

                                    NodeList nl3 = n2.getChildNodes();
                                    for (int k = 0; k < nl3.getLength(); k++) {
                                        Node n3 = nl3.item(k);
                                        if (n3.getNodeType() == Node.ELEMENT_NODE) {
                                            NamedNodeMap nnm2 = n3.getAttributes();
                                            String codeMEF = nnm2.getNamedItem("CODE").getTextContent();
                                            //System.out.println(codeMEF);
                                            XPathExpression expr2 = xPath.compile("/STS_EDT/NOMENCLATURES/MEFS/MEF[@CODE='" + codeMEF + "']/*");
                                            NodeList nl4 = (NodeList) expr2.evaluate(document, XPathConstants.NODESET);
                                            for (int l = 0; l < nl4.getLength(); l++) {
                                                Node n4 = nl4.item(l);
                                                if (n4.getNodeType() == Node.ELEMENT_NODE) {
                                                    String nodeName = n4.getNodeName();
                                                    if (nodeName.equals("MEFSTAT4")) {
                                                        // On en prend 1 car tous les items ont le même niveau
                                                        String codeMEFSTAT4 = n4.getTextContent();
                                                        division.setNiveau(dicoNiveaux.get(codeMEFSTAT4));
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                    divisions.put(code, division);
                }
            }
        }
        return divisions;
    }

    private Map<String, Professeur> buildIndividu() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        final String xPathIndividus = "/STS_EDT/DONNEES/INDIVIDUS/*";

        Map<String, Professeur> individus = new HashMap<>();

        XPathExpression expression = xPath.compile(xPathIndividus);
        NodeList nl1 = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nl1.getLength(); i++) {
            //List<Discipline> lstDis = new ArrayList<>();
            //List<Division> lstDiv = new ArrayList<>();
            Node n1 = nl1.item(i);
            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                NamedNodeMap nnm = n1.getAttributes();
                String id = nnm.getNamedItem("ID").getTextContent();
                //individu.setId(Long.parseLong(id));
                String type = nnm.getNamedItem("TYPE").getTextContent();
                if (!individus.containsKey(id)) {
                    Professeur individu = new Professeur();
                    // TODO à vérifier le sens de ID == CodeSynchro ??
                    individu.setCodeSynchro(id);
                    NodeList nl2 = n1.getChildNodes();
                    for (int j = 0; j < nl2.getLength(); j++) {
                        Node n2 = nl2.item(j);
                        if (n2.getNodeType() == Node.ELEMENT_NODE) {
                            switch (n2.getNodeName()) {
                                case "NOM_USAGE":
                                    individu.setNom(n2.getTextContent());
                                    break;
                                case "PRENOM":
                                    individu.setPrenom(n2.getTextContent());
                                    break;
                                case "CIVILITE":
                                    individu.setCivilite(n2.getTextContent());
                                    break;
                                case "DATE_NAISSANCE":
                                    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
                                    String date = n2.getTextContent();
                                    LocalDate localDate = LocalDate.parse(date);
                                    //individu.setNaissance(localDate);
                                    break;
                                case "DISCIPLINES":
                                    //// LIASON VERS DISCIPLINE ////

                                    //if (type.equals("epp")) {
                                    NodeList nl3 = n2.getChildNodes();
                                    for (int k = 0; k < nl3.getLength(); k++) {
                                        Node n3 = nl3.item(k);
                                        if (n3.getNodeType() == Node.ELEMENT_NODE) {
                                            NamedNodeMap nnm2 = n3.getAttributes();
                                            String codeDis = nnm2.getNamedItem("CODE").getTextContent();
                                            individu.addDiscipline(dicoDisciplines.get(codeDis));

                                        }
                                    }
                                    break;
                            }
                            //// FIN LIAISON VERS DISCIPLINE ////
                            //// LIAISON VERS DIVISION ////
                            XPathExpression expr2 = xPath.compile("/STS_EDT/DONNEES/STRUCTURE/DIVISIONS/*/SERVICES/*/ENSEIGNANTS/*");
                            NodeList nl3 = (NodeList) expr2.evaluate(document, XPathConstants.NODESET);
                            for (int k = 0; k < nl3.getLength(); k++) {
                                Node n3 = nl3.item(k);
                                if (n3.getNodeType() == Node.ELEMENT_NODE) {
                                    NamedNodeMap nnm2 = n3.getAttributes();
                                    String idEns = nnm2.getNamedItem("ID").getTextContent();
                                    if (idEns.equals(id)) {
                                        // On remonte pour récupérer le code de la division
                                        Node n4 = n3.getParentNode().getParentNode().getParentNode().getParentNode();
                                        NamedNodeMap nnm3 = n4.getAttributes();
                                        String codeDiv = nnm3.getNamedItem("CODE").getTextContent();
                                        //System.out.println(id+" => "+codeDiv);
                                        //lstDiv.add(dicoDivisions.get(codeDiv));
                                        individu.addDivision(dicoDivisions.get(codeDiv));
                                    }
                                }
                            }
                        }
                    }
                    individus.put(id, individu);
                }
            }
        }
        return individus;
    }

    private Map<String, Discipline> buildDiscipline() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        final String xPathDisciplines = "/STS_EDT/DONNEES/INDIVIDUS/*/DISCIPLINES/*";

        Map<String, Discipline> disciplines = new HashMap<>();

        XPathExpression expression = xPath.compile(xPathDisciplines);
        NodeList nl1 = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
        for (int i = 0; i < nl1.getLength(); i++) {
            //List<Individu> lstProfs = new ArrayList<>();
            Node n1 = nl1.item(i);
            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                NamedNodeMap nnm = n1.getAttributes();
                String code = nnm.getNamedItem("CODE").getTextContent();
                if (!disciplines.containsKey(code)) {
                    Discipline discipline = new Discipline();
                    discipline.setCode(code);
                    NodeList nl2 = n1.getChildNodes();
                    for (int j = 0; j < nl2.getLength(); j++) {
                        Node n2 = nl2.item(j);
                        if (n2.getNodeType() == Node.ELEMENT_NODE) {
                            switch (n2.getNodeName()) {
                                case "LIBELLE_COURT":
                                    discipline.setLibelle(n2.getTextContent());
                                    break;
                            }
                        }
                    }
                    disciplines.put(discipline.getCode(), discipline);
                }
            }
        }
        return disciplines;
    }
}