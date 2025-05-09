package it.epicode.bw.finale.fatture.stati;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Order(2)
public class StatiFatturaRunner implements CommandLineRunner {

    @Autowired
    private StatoFatturaRepository statoFatturaRepository;

    @Override
    public void run(String... args) throws Exception {
        StatoFattura  statoFattura = new StatoFattura();
        statoFattura.setNome("PAGATA");

        StatoFattura  statoFattura2 = new StatoFattura();
        statoFattura2.setNome("NON PAGATA");

        StatoFattura  statoFattura3 = new StatoFattura();
        statoFattura3.setNome("IN ATTESA");

        statoFatturaRepository.save(statoFattura);
        statoFatturaRepository.save(statoFattura2);
        statoFatturaRepository.save(statoFattura3);

    }
}
