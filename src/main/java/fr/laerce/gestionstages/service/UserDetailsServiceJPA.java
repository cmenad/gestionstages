package fr.laerce.gestionstages.service;

import fr.laerce.gestionstages.dao.IndividuRepository;
import fr.laerce.gestionstages.domain.Individu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.soap.SOAPBinding;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Projet gestages
 * Pour LAERCE SAS
 * <p>
 * Créé le  26/02/2018.
 *
 * @author fred
 */
@Service
public class UserDetailsServiceJPA implements UserDetailsService{
    @Autowired
    private IndividuRepository repoIndividu;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Individu individu = repoIndividu.findByLogin(s);
        User user = null;
        if(individu != null){
            String[] roles = individu.getRoles().split(",");
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            for(String role : roles){
                grantedAuthorities.add(new SimpleGrantedAuthority(role));
            }
            user = new User(individu.getLogin(),individu.getMdp(),grantedAuthorities);
        }
        return user;
    }
}
