package it.epicode.bw.finale.utenti;

import it.epicode.bw.finale.auth.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
("/utenti")
public class UtenteController {
    @Autowired
    private UtenteService utenteService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public Utente getCurrentUserComplete(@AuthenticationPrincipal AppUser appUser) {
        return utenteService.findByUsername(appUser.getUsername());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public List<Utente> getAllUsers() {
        return utenteService.getAllUtenti();
    }
}
