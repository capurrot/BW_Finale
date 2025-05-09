package it.epicode.bw.finale.indirizzi;

import it.epicode.bw.finale.auth.AppUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comuni")
@RequiredArgsConstructor
public class ComuneController {

    @Autowired
    private ComuneService comuneService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ComuneResponse create(@RequestBody @Valid ComuneRequest request, @AuthenticationPrincipal AppUser adminLoggato) {
        return comuneService.create(request, adminLoggato);  // Passiamo anche l'adminLoggato
    }

    @GetMapping("/{id}")
    public ComuneResponse getById(@PathVariable Long id) {
        return comuneService.getById(id);
    }

    @GetMapping
    public Page<ComuneResponse> getAll(@ParameterObject Pageable pageable) {
        return comuneService.getAll(pageable);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ComuneResponse update(@PathVariable Long id, @RequestBody @Valid ComuneRequest request, @AuthenticationPrincipal AppUser adminLoggato) {
        return comuneService.update(id, request, adminLoggato);  // Passiamo anche l'adminLoggato
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal AppUser adminLoggato) {
        comuneService.delete(id, adminLoggato);  // Passiamo anche l'adminLoggato
    }
}
