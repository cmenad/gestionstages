package fr.laerce.gestionstages.configuration;


import fr.laerce.gestionstages.service.UserDetailsServiceJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceJPA userDetailsServiceJPA;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/webjars/**","/index").permitAll()
                    //.antMatchers("/discipline/**","/division/**","/individu/**","/niveau/**").hasRole("USER") user all access
                    .antMatchers("/discipline/liste","/division/liste","/individu/liste","/niveau/liste").hasAuthority("USER") // on met le plus restreint en premier
                    .antMatchers("/admin/**","/upload","/discipline/**","/division/**","/individu/**","/niveau/**").hasAuthority("ADMIN") // on met le moins restreint en dernier



                .and()

                .formLogin()
                    .loginPage("/login").failureUrl("/login-error");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
       /* auth
                .inMemoryAuthentication()
                    .withUser("user").password("{noop}password").roles("USER")
                .and()
                    .withUser("admin").password("{noop}mdp").roles("ADMIN","USER");*/
        auth.userDetailsService(userDetailsServiceJPA);
    }


   @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

}


