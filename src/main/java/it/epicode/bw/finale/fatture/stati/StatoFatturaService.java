package it.epicode.bw.finale.fatture.stati;

import it.epicode.bw.finale.auth.AppUser;
import it.epicode.bw.finale.auth.Role;
import it.epicode.bw.finale.fatture.Fattura;
import it.epicode.bw.finale.fatture.FatturaRequest;
import it.epicode.bw.finale.fatture.FatturaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class StatoFatturaService {

    @Autowired
    private StatoFatturaRepository statoFatturaRepository;

    public StatoFattura createStatoFattura(StatoFatturaRequest statoFattura, AppUser adminLoggato) {
        boolean isAdmin=adminLoggato.getRoles().contains(Role.ROLE_ADMIN);
        if(!isAdmin) {
            throw new RuntimeException("Non hai i permessi per creare un nuovo stato fattura");
        }else{


        return statoFatturaRepository.save(new StatoFattura(statoFattura.getNome()));
        }
    }

    public StatoFattura findById(Long id) {
        return statoFatturaRepository.findById(id).orElse(null);
    }

    public List<StatoFattura> findAll() {
        return statoFatturaRepository.findAll();
    }

    public StatoFattura updateStatoFattura(StatoFattura stato, Long id, AppUser adminLoggato) {
        StatoFattura statoFattura =  statoFatturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stato fattura non trovato"));

        boolean isAdmin=adminLoggato.getRoles().contains(Role.ROLE_ADMIN);
        if(!isAdmin) {
            throw new RuntimeException("Non hai i permessi per modificare questo stato fattura");
        }else{
            statoFattura.setNome(stato.getNome());
        }
        return statoFatturaRepository.save(statoFattura);
    }

    public void deleteStatoFattura(Long id, AppUser adminLoggato) {
        StatoFattura statoFattura = statoFatturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stato fattura non trovato"));

        boolean isAdmin=adminLoggato.getRoles().contains(Role.ROLE_ADMIN);
        if(!isAdmin) {
            throw new RuntimeException("Non hai i permessi per eliminare questo stato fattura");
        }else{
        statoFatturaRepository.deleteById(id);
        }
    }

    public StatoFattura findStatoFatturaByNome(String nome) {
        return statoFatturaRepository.findByNome(nome).orElseThrow(() -> new RuntimeException("Stato fattura non trovato"));
    }
}
