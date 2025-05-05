package it.epicode.bw.finale.clienti;

import it.epicode.bw.finale.auth.AppUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/clienti")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    // ✅ Crea un nuovo cliente
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente createCliente(@RequestBody @Valid ClienteRequest request) {
        return clienteService.saveCliente(request);
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<ClienteResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        return clienteService.findAll(pageable);
    }

    // ✅ Filtro dinamico dei clienti
    @GetMapping("/filter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<ClienteResponse> filterClienti(
            @RequestParam(required = false) Double fatturatoAnnuale,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInserimento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataUltimoContatto,
            @RequestParam(required = false) String nomeParziale,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        ClienteFilterDto filtro = new ClienteFilterDto();
        filtro.setFatturatoAnnuale(fatturatoAnnuale);
        filtro.setDataInserimento(dataInserimento);
        filtro.setDataUltimoContatto(dataUltimoContatto);
        filtro.setNomeParziale(nomeParziale);

        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        return clienteService.filterClienti(filtro, pageable);
    }

    @GetMapping("/{id}")
    public ClienteResponse findById(@PathVariable Long id) {
        return clienteService.toResponse(clienteService.findClienteById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ClienteResponse updateCliente(@PathVariable Long id,
                                         @RequestBody @Valid ClienteRequest request,
                                         @AuthenticationPrincipal AppUser adminLoggato) {
        return clienteService.toResponse(clienteService.findClienteByIdAndUpdate(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCliente(@PathVariable Long id, @AuthenticationPrincipal AppUser adminLoggato) {
        clienteService.findClienteByIdAndDelete(id);
    }
}
