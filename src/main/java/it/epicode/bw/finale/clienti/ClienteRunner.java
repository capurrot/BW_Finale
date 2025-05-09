package it.epicode.bw.finale.clienti;

import com.github.javafaker.Faker;
import it.epicode.bw.finale.enums.TipoCliente;
import it.epicode.bw.finale.fatture.Fattura;
import it.epicode.bw.finale.fatture.FatturaRepository;
import it.epicode.bw.finale.fatture.stati.StatoFattura;
import it.epicode.bw.finale.fatture.stati.StatoFatturaRepository;
import it.epicode.bw.finale.indirizzi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Component
@Order(3)
public class ClienteRunner implements CommandLineRunner {

    @Autowired
    Faker faker;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ProvinciaRepository provinciaRepository;

    @Autowired
    ComuneRepository comuneRepository;

    @Autowired
    IndirizzoRepository indirizzoRepository;

    @Autowired
    FatturaRepository fatturaRepository;

    @Autowired
    StatoFatturaRepository statoFatturaRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        for (int i = 0; i < 10; i++) {
            String nome = faker.name().firstName();
            String cognome = faker.name().lastName();
            String compagnia = faker.company().name();

            // Crea il cliente
            Cliente cliente = new Cliente();
            cliente.setNomeContatto(nome);
            cliente.setCognomeContatto(cognome);
            cliente.setRagioneSociale(compagnia);
            cliente.setPartitaIva(faker.number().digits(11));
            cliente.setEmailContatto((nome + cognome + "@gmail.com").toLowerCase());
            cliente.setEmail((nome + cognome + "@gmail.com").toLowerCase());
            cliente.setTelefono(faker.phoneNumber().phoneNumber());
            cliente.setTelefonoContatto(faker.phoneNumber().phoneNumber());
            cliente.setPec((nome + cognome + "@gmail.com").toLowerCase());
            cliente.setTipoCliente(TipoCliente.valueOf(faker.options().option("SAS", "SRL", "SPA", "PA")));
            cliente.setFatturatoAnnuale(faker.number().randomDouble(2, 10000, 100000));
            cliente.setLogoAziendale("https://ui-avatars.com/api/?name=" + compagnia);
            cliente.setDataInserimento(LocalDate.now());
            cliente.setDataUltimoContatto(LocalDate.now());

            // Crea provincia
            Provincia provincia = new Provincia();
            provincia.setNome(faker.address().city());
            provincia.setSigla(faker.address().stateAbbr());
            provinciaRepository.save(provincia);  // Salva la provincia

            // Crea comune
            Comune comune = new Comune();
            comune.setNome(faker.address().cityName());
            comune.setProvincia(provincia);
            comuneRepository.save(comune);  // Salva il comune

            // Crea indirizzo
            Indirizzo indirizzo = new Indirizzo();
            indirizzo.setVia(faker.address().streetName());
            indirizzo.setCivico(faker.address().streetAddressNumber());
            indirizzo.setCap(faker.address().zipCode());
            indirizzo.setLocalita(faker.address().city());
            indirizzo.setComune(comune);
            indirizzo.setTipoSede(TipoSede.valueOf(faker.options().option("SEDE_LEGALE", "SEDE_OPERATIVA")));
            indirizzo.setCliente(cliente);
            indirizzoRepository.save(indirizzo);  // Salva l'indirizzo

            // Associa l'indirizzo al cliente
            cliente.setIndirizzo(Set.of(indirizzo));


            // Salva il cliente
            clienteRepository.save(cliente);

            // Creo 10 fatture per cliente
            for (int j = 0; j < 10; j++) {
                Fattura fattura = new Fattura();
                fattura.setData(LocalDate.now());
                fattura.setImporto(faker.number().randomDouble(2, 100, 1000));
                fattura.setStato(statoFatturaRepository.findByNome(faker.options().option("PAGATA", "NON PAGATA", "IN ATTESA")).orElseThrow());
                fattura.setNumero(faker.number().numberBetween(1, 1000));
                fattura.setCliente(cliente);
                fatturaRepository.save(fattura);
            }
        }

    }
}
