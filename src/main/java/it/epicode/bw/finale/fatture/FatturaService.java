package it.epicode.bw.finale.fatture;

import it.epicode.bw.finale.auth.AppUser;
import it.epicode.bw.finale.auth.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Validated
@RequiredArgsConstructor
public class FatturaService {

    @Autowired
    private FatturaRepository fatturaRepository;

    //inserire anche clienteRepository una volta fatta

    private Fattura toEntity(FatturaRequest request){
        Fattura fattura = new Fattura();
        fattura.setData(request.getData());
        fattura.setNumero(request.getNumero());
        fattura.setImporto(request.getImporto());
        //da inserire la ricerca dell'id del cliente dalla repo cliente
        fattura.setStato(request.getStato());
        return fattura;
    }

    private FatturaResponse toDTO(Fattura fattura) {
        return new FatturaResponse(
                fattura.getId(),
                fattura.getData(),
                fattura.getImporto(),
                fattura.getNumero(),
                // per ora mettiamo null sul cliente:
                fattura.getCliente() != null ? fattura.getCliente().getId() : null,
                fattura.getStato()
        );
    }
    public FatturaResponse createFattura(FatturaRequest request) {
        Fattura fattura = toEntity(request);
        fattura = fatturaRepository.save(fattura);
        return toDTO(fattura);
    }

    public Page<FatturaResponse> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return fatturaRepository.findAll(pageable).map(this::toDTO);
    }
    public FatturaResponse findById(Long id) {
        Fattura fattura = fatturaRepository.findById(id).orElseThrow(() -> new RuntimeException("Fattura non trovata"));
        return toDTO(fattura);
    }

    public FatturaResponse updateFattura(Long id, FatturaRequest request, AppUser adminLoggato) {
        Fattura fattura = fatturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fattura non trovata"));

        boolean isAdmin = adminLoggato.getRoles().contains(Role.ROLE_ADMIN);
        if(!isAdmin) {
            throw new RuntimeException("Non hai i permessi per modificare questa fattura");
        }else{
        fattura.setData(request.getData());
        fattura.setImporto(request.getImporto());
        fattura.setNumero(request.getNumero());
        fattura.setStato(request.getStato());
        fattura = fatturaRepository.save(fattura);
        return toDTO(fattura);
        }
    }
    public void deleteFattura(Long id, AppUser adminLoggato) {
        Fattura fattura = fatturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fattura non trovata"));

        boolean isAdmin = adminLoggato.getRoles().contains(Role.ROLE_ADMIN);
        if(!isAdmin ) {
            throw new RuntimeException("Non hai i permessi per eliminare questa fattura");
        }else{
            fatturaRepository.delete(fattura);
        }

    }
}
