package it.epicode.bw.finale.clienti;


import it.epicode.bw.finale.exceptions.BadRequestException;
import it.epicode.bw.finale.exceptions.NotFoundException;
import it.epicode.bw.finale.fatture.Fattura;
import it.epicode.bw.finale.fatture.FatturaFilterDto;
import it.epicode.bw.finale.fatture.FatturaResponse;
import it.epicode.bw.finale.fatture.FatturaSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Service
@Validated
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    //RICERCA SINGOLO CLIENTE PER ID

    public Cliente findClienteById(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente con id: " + id + " non trovato"));
    }


    //RICERCA SINGOLO CLIENTE PER ID E LO ELIMINA

    public void findClienteByIdAndDelete(Long id) {
        Cliente cliente = findClienteById(id);
        clienteRepository.delete(cliente);
    }



    //RICERCA SINGOLO CLIENTE PER ID E LO AGGIORNA

    public Cliente findClienteByIdAndUpdate(Long id, ClienteRequest clienteRequest) {

       if(clienteRepository.existsByEmail(clienteRequest.getEmail()))
            throw new BadRequestException("Email già esistente");

       if(clienteRepository.existsByPartitaIva(clienteRequest.getPartitaIva()))
            throw new BadRequestException("Partita Iva già esistente");

       if(clienteRepository.existsByPec(clienteRequest.getPec()))
            throw new BadRequestException("Pec già esistente");

       if(clienteRepository.existsByTelefono(clienteRequest.getTelefono()))
            throw new BadRequestException("Telefono già esistente");

       if(clienteRepository.existsByRagioneSociale(clienteRequest.getRagioneSociale()))
            throw new BadRequestException("Ragione sociale già esistente");

        Cliente clienteUpdate = findClienteById(id);
        clienteUpdate.setRagioneSociale(clienteRequest.getRagioneSociale());
        clienteUpdate.setPartitaIva(clienteRequest.getPartitaIva());
        clienteUpdate.setPec(clienteRequest.getPec());
        clienteUpdate.setEmail(clienteRequest.getEmail());
        clienteUpdate.setTelefono(clienteRequest.getTelefono());
        clienteUpdate.setEmailContatto(clienteRequest.getEmailContatto());
        clienteUpdate.setTelefonoContatto(clienteRequest.getTelefonoContatto());
        clienteUpdate.setNomeContatto(clienteRequest.getNomeContatto());
        clienteUpdate.setCognomeContatto(clienteRequest.getCognomeContatto());
        clienteUpdate.setTipoCliente(clienteRequest.getTipoCliente());
        return clienteRepository.save(clienteUpdate);

    }


    //CREAZIONE CLIENTE

    public Cliente saveCliente(ClienteRequest clienteRequest) {

        if(clienteRepository.existsByEmail(clienteRequest.getEmail()))
            throw new BadRequestException("Email già esistente");

        if(clienteRepository.existsByPartitaIva(clienteRequest.getPartitaIva()))
            throw new BadRequestException("Partita Iva già esistente");

        if(clienteRepository.existsByPec(clienteRequest.getPec()))
            throw new BadRequestException("Pec già esistente");

        if(clienteRepository.existsByTelefono(clienteRequest.getTelefono()))
            throw new BadRequestException("Telefono già esistente");

        if(clienteRepository.existsByRagioneSociale(clienteRequest.getRagioneSociale()))
            throw new BadRequestException("Ragione sociale già esistente");

        Cliente cliente = new Cliente();
        cliente.setRagioneSociale(clienteRequest.getRagioneSociale());
        cliente.setPartitaIva(clienteRequest.getPartitaIva());
        cliente.setPec(clienteRequest.getPec());
        cliente.setEmail(clienteRequest.getEmail());
        cliente.setTelefono(clienteRequest.getTelefono());
        cliente.setEmailContatto(clienteRequest.getEmailContatto());
        cliente.setTelefonoContatto(clienteRequest.getTelefonoContatto());
        cliente.setNomeContatto(clienteRequest.getNomeContatto());
        cliente.setCognomeContatto(clienteRequest.getCognomeContatto());
        cliente.setTipoCliente(clienteRequest.getTipoCliente());
        return clienteRepository.save(cliente);
    }

    public Page<ClienteResponse> filterClienti(ClienteFilterDto filter, Pageable pageable) {
        Specification<Cliente> spec = ClienteSpecification.filterBy(filter);
        Page<Cliente> page = clienteRepository.findAll(spec, pageable);
        return page.map(this::toResponse);
    }

    public ClienteResponse toResponse(Cliente cliente) {
        ClienteResponse response = new ClienteResponse();
        response.setId(cliente.getId());
        response.setRagioneSociale(cliente.getRagioneSociale());
        response.setPartitaIva(cliente.getPartitaIva());
        response.setEmail(cliente.getEmail());
        response.setDataInserimento(cliente.getDataInserimento());
        response.setDataUltimoContatto(cliente.getDataUltimoContatto());
        response.setFatturatoAnnuale(cliente.getFatturatoAnnuale());
        response.setPec(cliente.getPec());
        response.setTelefono(cliente.getTelefono());
        response.setNomeContatto(cliente.getNomeContatto());
        response.setCognomeContatto(cliente.getCognomeContatto());
        response.setEmailContatto(cliente.getEmailContatto());
        response.setTelefonoContatto(cliente.getTelefonoContatto());
        response.setLogoAziendale(cliente.getLogoAziendale());
        response.setTipoCliente(cliente.getTipoCliente());
        return response;
    }

    public Page<ClienteResponse> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable).map(this::toResponse);
    }
}
