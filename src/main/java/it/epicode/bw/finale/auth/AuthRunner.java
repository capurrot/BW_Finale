package it.epicode.bw.finale.auth;

import com.cloudinary.provisioning.Account;
import com.github.javafaker.Faker;
import it.epicode.bw.finale.utenti.Utente;
import it.epicode.bw.finale.utenti.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UtenteService utenteService;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    Faker faker;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Optional<AppUser> admin = appUserService.findByUsername("admin");
        if(!admin.isPresent()){
            AppUser appUser = new AppUser();
            appUser.setUsername("admin");
            appUser.setPassword(passwordEncoder.encode("adminpwd"));
            appUser.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER));
            appUserRepository.save(appUser);
            Utente utente = new Utente();
            utente.setAppUser(appUser);
            utente.setAvatar("https://ui-avatars.com/api/?name=Admin");
            utente.setId(appUser.getId());
            utente.setNome("Mauro");
            utente.setCognome("Larese");
            utente.setEmail("maurettothebest@gmail.com");
            utenteService.save(utente);
        }
        if (appUserRepository.count() <= 1) {

            for (int i = 0; i < 10; i++) {
                AppUser appUser = new AppUser();
                appUser.setUsername(faker.name().username());
                appUser.setPassword(passwordEncoder.encode("userpwd"));
                appUser.setRoles(Set.of(Role.ROLE_USER));
                appUserRepository.save(appUser);
                Utente utente = new Utente();
                utente.setAppUser(appUser);
                utente.setId(appUser.getId());
                utente.setNome(faker.name().firstName());
                utente.setCognome(faker.name().lastName());
                utente.setEmail(faker.internet().emailAddress());
                utente.setAvatar("https://ui-avatars.com/api/?name=" + utente.getNome() + "+" + utente.getCognome());
                utenteService.save(utente);
            }
        }


    }
}
