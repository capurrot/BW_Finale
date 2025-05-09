package it.epicode.bw.finale;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.epicode.bw.finale.auth.*;
import it.epicode.bw.finale.indirizzi.ProvinciaRequest;
import it.epicode.bw.finale.indirizzi.ProvinciaResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProvinciaControllerTest {
    @LocalServerPort
    public int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppUserRepository appUserRepository;
    private static String tokenAdmin;
    private static Long provinciaCreataId;

    @Test
    @Order(1)
    @DisplayName("Test di registrazione admin")
    public void testRegisterAdmin() {
        AppUser appUser = new AppUser();
        appUser.setUsername("admin.test");
        appUser.setPassword(passwordEncoder.encode("adminpwd"));
        appUser.setRoles(Set.of(Role.ROLE_ADMIN));
        appUserRepository.save(appUser);

        LoginRequest loginRequest = new LoginRequest("admin.test", "adminpwd");
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/login", loginRequest, AuthResponse.class);
        tokenAdmin = response.getBody().getToken();

        System.out.println("Token ottenuto: " + tokenAdmin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAdmin);
        RegisterRequest registerRequest = new RegisterRequest("admin.test2", "password", "admin@example.com", "Admin", "Test");
        HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest, headers);
        ResponseEntity<String> response2 = restTemplate.postForEntity("http://localhost:" + port + "/register-admin", entity, String.class);

        System.out.println("Risposta registrazione admin.test2: " + response2.getBody());
        assertThat(response2.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @Order(2)
    @DisplayName("Test creazione provincia")
    public void testCreateProvincia() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAdmin);

        ProvinciaRequest provinciaRequest = new ProvinciaRequest(100L, "Provincia Test", "PT");
        HttpEntity<ProvinciaRequest> entity = new HttpEntity<>(provinciaRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/province", entity, String.class);

        System.out.println("Risposta creazione provincia: " + response.getBody());

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();

        ProvinciaResponse provinciaResponse = objectMapper.readValue(response.getBody(), ProvinciaResponse.class);

        provinciaCreataId = provinciaResponse.getId();

        System.out.println("Provincia creata con ID: " + provinciaCreataId);
        System.out.println("Provincia creata con nome: " + provinciaResponse.getNome());
    }

    @Test
    @Order(3)
    @DisplayName("Test recupero provincia per ID")
    public void testGetProvinciaById() {
        String url = "http://localhost:" + port + "/province/" + provinciaCreataId;
        ResponseEntity<ProvinciaResponse> response = restTemplate.getForEntity(url, ProvinciaResponse.class);

        System.out.println("Provincia trovata per ID " + provinciaCreataId + ": " + response.getBody());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(provinciaCreataId);
        assertThat(response.getBody().getNome()).isEqualTo("Provincia Test");
    }

    @Test
    @Order(4)
    @DisplayName("Test recupero tutte le province con paginazione")
    public void testGetAllProvince() throws Exception {
        String url = "http://localhost:" + port + "/province?page=0&size=10";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        System.out.println("Risposta da getAllProvince(): " + response.getBody());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @Order(5)
    @DisplayName("Test aggiornamento provincia")
    public void testUpdateProvincia() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAdmin);

        ProvinciaRequest updatedRequest = new ProvinciaRequest(provinciaCreataId, "Provincia Aggiornata", "PA");

        HttpEntity<ProvinciaRequest> entity = new HttpEntity<>(updatedRequest, headers);
        String url = "http://localhost:" + port + "/province/" + provinciaCreataId;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        System.out.println("Risposta aggiornamento provincia: " + response.getBody());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        ProvinciaResponse updated = objectMapper.readValue(response.getBody(), ProvinciaResponse.class);
        assertThat(updated.getNome()).isEqualTo("Provincia Aggiornata");
        assertThat(updated.getSigla()).isEqualTo("PA");
    }

    @Test
    @Order(6)
    @DisplayName("Test cancellazione provincia")
    public void testDeleteProvincia() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/province/" + provinciaCreataId;
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

        System.out.println("Provincia con ID " + provinciaCreataId + " cancellata.");

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }
}
