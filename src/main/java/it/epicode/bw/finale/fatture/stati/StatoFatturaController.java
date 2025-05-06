package it.epicode.bw.finale.fatture.stati;

import it.epicode.bw.finale.auth.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stati")
@RequiredArgsConstructor
public class StatoFatturaController {

    @Autowired
    private StatoFatturaService statoFatturaService;

    @GetMapping
    public List<StatoFattura> findAll(){
        return statoFatturaService.findAll();
    }

    @GetMapping("/{id}")
    public StatoFattura findById(Long id){
        return statoFatturaService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public StatoFattura create(@RequestBody StatoFatturaRequest statoFattura, @AuthenticationPrincipal AppUser adminLoggato){
        return statoFatturaService.createStatoFattura(statoFattura, adminLoggato);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public StatoFattura update(@PathVariable Long id, @RequestBody StatoFattura statoFattura, @AuthenticationPrincipal AppUser adminLoggato){
        return statoFatturaService.updateStatoFattura(statoFattura, id, adminLoggato);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal AppUser adminLoggato){
        statoFatturaService.deleteStatoFattura(id, adminLoggato);
    }
}
