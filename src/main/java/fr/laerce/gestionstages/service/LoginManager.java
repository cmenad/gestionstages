package fr.laerce.gestionstages.service;

import fr.laerce.gestionstages.dao.IndividuRepository;
import fr.laerce.gestionstages.domain.Individu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

/**
 * Projet gestages
 * Pour LAERCE SAS
 * <p>
 * Créé le  26/02/2018.
 *
 * @author fred
 */

@Service
public class LoginManager {

    @Autowired
    IndividuRepository repoIndividu;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void createLoginForIndividu(Long id) {
        Optional<Individu> val = repoIndividu.findById(id);
        if(val.isPresent()){
            Individu individu = val.get();
            if(individu.getLogin() == null || individu.getLogin().equals("")){
                calculLogin(individu);
            }
            individu.setMdpOrigine(this.createPassword());
            individu.setMdp(this.passwordEncoder.encode(individu.getMdpOrigine()));
            individu.setRoles("USER"); // rôle par défaut....
            repoIndividu.save(individu);
        }
    }

    private void calculLogin(Individu individu) {
        StringBuffer nom = new StringBuffer(individu.getNom().toLowerCase());
        strip(nom);
        String prenom = individu.getPrenom().toLowerCase().substring(0,1);
        String loginBase = prenom+nom.toString().substring(0,Integer.min(nom.length(),7));
        individu.setLogin(loginBase);
        boolean loginOk = false;
        int count = 1;
        do {
            Individu ind = repoIndividu.findByLogin(individu.getLogin());
            if(ind != null) {
                individu.setLogin(loginBase+count);
                count++;
            } else {
                loginOk = true;
            }
        } while (!loginOk);
    }

    private void strip(StringBuffer nom) {
        for(int i = 0; i < nom.length(); ){
            switch (nom.charAt(i)) {
                case ' ':
                case '\'':
                case '-':
                case '_':
                    nom.deleteCharAt(i);
                    break;
                default:
                    i++;
                    break;
            }
        }
    }

    private String createPassword(){
        StringBuffer password = new StringBuffer();
        String consonnes = "bcdfghjklmnpqrstvwxz";
        String voyelles = "aeiouy";
        Random alea = new Random();
        for(int i = 0; i <6; i++){
            if(i%2 == 0){
                password.append(consonnes.charAt(alea.nextInt(consonnes.length())));
            } else {
                password.append(voyelles.charAt(alea.nextInt(voyelles.length())));
            }
        }
        for(int i = 0; i < 4; i++){
            password.append(alea.nextInt(9));
        }

        return password.toString();
    }
}