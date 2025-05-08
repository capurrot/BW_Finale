package it.epicode.bw.finale;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.epicode.bw.finale.auth.*;
import it.epicode.bw.finale.auth.AppUser;
import it.epicode.bw.finale.auth.AppUserRepository;
import it.epicode.bw.finale.auth.Role;
import it.epicode.bw.finale.fatture.*;
import it.epicode.bw.finale.fatture.stati.StatoFattura;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FatturaControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AppUserRepository appUserRepository;

    private static String tokenAdmin;
    private static Long idFattura;
    private static Long clienteId;

    @Test
    @Order(1)
    @DisplayName("Login e registrazione admin")
    public void testRegisterAdmin() {
        AppUser appUser = new AppUser();
        appUser.setUsername("admin.test");
        appUser.setPassword(passwordEncoder.encode("adminpwd"));
        appUser.setRoles(Set.of(Role.ROLE_ADMIN));
        appUserRepository.save(appUser);

        LoginRequest loginRequest = new LoginRequest("admin.test", "adminpwd");
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/login", loginRequest, AuthResponse.class);
        tokenAdmin = response.getBody().getToken();

        assertThat(tokenAdmin).isNotNull();
    }

    @Test
    @Order(2)
    @DisplayName("Crea utente cliente")
    public void createCliente() {
        AppUser cliente = new AppUser();
        cliente.setUsername("cliente.test");
        cliente.setPassword(passwordEncoder.encode("cliente123"));
        cliente.setRoles(Set.of(Role.ROLE_USER));
        appUserRepository.save(cliente);
        clienteId = cliente.getId();

        assertThat(clienteId).isNotNull();
    }

    @Test
    @Order(3)
    @DisplayName("Crea fattura")
    public void testCreateFattura() {
        FatturaRequest request = new FatturaRequest(LocalDate.now(), 200.000, 2, 1L, "PAGATA");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FatturaRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<FatturaResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/fatture",
                entity,
                FatturaResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        idFattura = response.getBody().id();
        assertThat(idFattura).isNotNull();
    }

    @Test
    @Order(4)
    @DisplayName("Filtra fatture per cliente")
    public void testFilterFatture() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/fatture?idCliente=" + clienteId,
                HttpMethod.GET,
                entity,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Recupera fattura per ID")
    public void testFindById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<FatturaResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/fatture/" + idFattura,
                HttpMethod.GET,
                entity,
                FatturaResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(idFattura, response.getBody().id());
    }

    @Test
    @Order(6)
    @DisplayName("Aggiorna fattura")
    public void testUpdateFattura() {
        FatturaRequest update = new FatturaRequest(LocalDate.now(), 500.000, 55, 1L, "NON PAGATA");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FatturaRequest> entity = new HttpEntity<>(update, headers);

        ResponseEntity<FatturaResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/fatture/" + idFattura,
                HttpMethod.PUT,
                entity,
                FatturaResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(500.0, response.getBody().importo());
    }

    @Test
    @Order(7)
    @DisplayName("Elimina fattura")
    public void testDeleteFattura() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/fatture/" + idFattura,
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
