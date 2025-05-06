package it.epicode.bw.finale.indirizzi;

import it.epicode.bw.finale.auth.AppUser;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/province")
@RequiredArgsConstructor
public class ProvinciaController {

    @Autowired
    private ProvinciaService provinciaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProvinciaResponse create(@RequestBody @Valid ProvinciaRequest request, @AuthenticationPrincipal AppUser adminLoggato) {
        return provinciaService.create(request, adminLoggato);
    }

    @GetMapping("/{id}")

    public ProvinciaResponse getById(@PathVariable Long id) {
        return provinciaService.getById(id);
    }

    @GetMapping
    public Page<ProvinciaResponse> getAll(@ParameterObject Pageable pageable) {
        return provinciaService.getAll(pageable);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProvinciaResponse update(@PathVariable Long id, @RequestBody @Valid ProvinciaRequest request, @AuthenticationPrincipal AppUser adminLoggato) {
        return provinciaService.update(id, request, adminLoggato);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal AppUser adminLoggato) {
        provinciaService.delete(id, adminLoggato);
    }
}
