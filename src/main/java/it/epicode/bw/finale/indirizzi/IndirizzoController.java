package it.epicode.bw.finale.indirizzi;

import it.epicode.bw.finale.auth.AppUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/indirizzi")
@RequiredArgsConstructor
public class IndirizzoController {

    @Autowired
    private IndirizzoService indirizzoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public IndirizzoResponse create(@RequestBody @Valid IndirizzoRequest request, @AuthenticationPrincipal AppUser adminLoggato) {
        return indirizzoService.create(request, adminLoggato);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public IndirizzoResponse getById(@PathVariable Long id, @AuthenticationPrincipal AppUser userLoggato) {
        return indirizzoService.getById(id, userLoggato);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public List<IndirizzoResponse> getAll(@AuthenticationPrincipal AppUser userLoggato) {
        return indirizzoService.getAll(userLoggato);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public IndirizzoResponse update(@PathVariable Long id, @RequestBody @Valid IndirizzoRequest request, @AuthenticationPrincipal AppUser adminLoggato) {
        return indirizzoService.update(id, request, adminLoggato);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal AppUser adminLoggato) {
        indirizzoService.delete(id, adminLoggato);
    }
}
