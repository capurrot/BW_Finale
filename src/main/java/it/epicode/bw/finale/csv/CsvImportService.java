package it.epicode.bw.finale.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import it.epicode.bw.finale.indirizzi.Comune;
import it.epicode.bw.finale.indirizzi.ComuneRepository;
import it.epicode.bw.finale.indirizzi.Provincia;
import it.epicode.bw.finale.indirizzi.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CsvImportService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private ComuneRepository comuneRepository;

    @Transactional
    public void importComuniAndProvince(String comuniFile, String provinceFile) throws IOException {
        Map<String, Provincia> provinceMap = new HashMap<>();

        // 1. Leggi e importa le Province
        try (CSVReader reader = new CSVReader(new FileReader(provinceFile))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                String sigla = line[0].trim();
                String nomeProvincia = line[1].trim();
                String regione = line[2].trim();

                Provincia provincia = new Provincia();
                provincia.setSigla(sigla);
                provincia.setNome(nomeProvincia);

                // Salva la provincia nel DB
                provinciaRepository.save(provincia);

                // Aggiungi la provincia alla mappa
                provinceMap.put(sigla, provincia);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        // 2. Leggi e importa i Comuni
        try (CSVReader reader = new CSVReader(new FileReader(comuniFile))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                String provinciaSigla = line[0].trim();
                String nomeComune = line[2].trim();

                Provincia provincia = provinceMap.get(provinciaSigla);
                if (provincia != null) {
                    Comune comune = new Comune();
                    comune.setNome(nomeComune);
                    comune.setProvincia(provincia);
                    // Salva il comune nel DB
                    comuneRepository.save(comune);
                }
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
