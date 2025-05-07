package it.epicode.bw.finale;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.epicode.bw.finale.auth.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class AuthControllerTest {
    @LocalServerPort
    public int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    private static String token;

    @Test
    @DisplayName("Test di registrazione utente")
    public void testRegisterUser() {
        RegisterRequest registerRequest = new RegisterRequest("giorgio.mastrota", "password", "mondialcasa@example.com", "Giorgio", "Mastrota");
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:"+ port +"/register", registerRequest, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
    /*@Test
    @DisplayName("Test di login utente")
    public void testLoginUser() {
        // Implementa il test per il login di un utente
    }
    @Test
    @DisplayName("Test di registrazione admin")
    public void testRegisterAdmin() {
        // Implementa il test per la registrazione di un admin
    }
    @Test
    @DisplayName("Test di recupero utente corrente")
    public void testGetCurrentUser() {
        // Implementa il test per ottenere l'utente corrente
    }
    @Test
    @DisplayName("test test")
    public void test() {
        assertThat(1).isEqualTo(1);
    }*/

}
