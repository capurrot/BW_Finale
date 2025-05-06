package it.epicode.bw.finale.fatture;

import it.epicode.bw.finale.auth.AppUser;
import it.epicode.bw.finale.auth.Role;
import it.epicode.bw.finale.clienti.ClienteRepository;
import it.epicode.bw.finale.clienti.ClienteService;
import it.epicode.bw.finale.fatture.stati.StatoFatturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class FatturaService {

    @Autowired
    private FatturaRepository fatturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private StatoFatturaService statoFatturaService;

    //inserire anche clienteRepository una volta fatta


    private Fattura toEntity(FatturaRequest request){
        Fattura fattura = new Fattura();
        fattura.setData(request.getData());
        fattura.setNumero(request.getNumero());
        fattura.setImporto(request.getImporto());
        fattura.setCliente(clienteRepository.findById(request.getClienteId()).orElseThrow());

        //impostare stato fattura se null in non pagata passata in stringa
        if(request.getStato()==null){
            fattura.setStato(statoFatturaService.findStatoFatturaByNome("non pagata"));
        }else{
            fattura.setStato(statoFatturaService.findStatoFatturaByNome(request.getStato()));
        }

        return fattura;
    }

    private FatturaResponse toDTO(Fattura fattura) {
        return new FatturaResponse(
                fattura.getId(),
                fattura.getData(),
                fattura.getImporto(),
                fattura.getNumero(),
                fattura.getCliente().getId(),
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
        fattura.setStato(statoFatturaService.findStatoFatturaByNome(request.getStato()));
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

    public Page<FatturaResponse> filterFatture(FatturaFilterDto filter, Pageable pageable) {
        Specification<Fattura> spec = FatturaSpecification.filterBy(filter);
        Page<Fattura> page = fatturaRepository.findAll(spec, pageable);
        return page.map(this::toDTO);
    }

}
