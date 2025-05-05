package it.epicode.bw.finale.utenti;

import it.epicode.bw.finale.auth.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PatchMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)

    public Utente uploadImage(@PathVariable Long id, @RequestPart MultipartFile file) {
        utenteService.uploadAvatar(id, file);
        return utenteService.getUtentebyId(id);
    }
}
