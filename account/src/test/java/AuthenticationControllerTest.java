

import edu.wpi.auth.AuthenticationRequest;
import edu.wpi.auth.AuthenticationResponse;
import edu.wpi.auth.RegisterRequest;
import edu.wpi.controllers.AuthenticationController;
import edu.wpi.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService service;

    @InjectMocks
    private AuthenticationController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        AuthenticationResponse response = new AuthenticationResponse();

        when(service.register(any(RegisterRequest.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = controller.register(request);

        assertNotNull(result);
        assertEquals(response, result.getBody());
        verify(service, times(1)).register(request);
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest();
        AuthenticationResponse response = new AuthenticationResponse();

        when(service.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = controller.authenticate(request);

        assertNotNull(result);
        assertEquals(response, result.getBody());
        verify(service, times(1)).authenticate(request);
    }

    @Test
    void testRefreshToken() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        doNothing().when(service).refreshToken(request, response);

        controller.refreshToken(request, response);

        verify(service, times(1)).refreshToken(request, response);
    }
}
