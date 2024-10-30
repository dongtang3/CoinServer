

import edu.wpi.controllers.UserController;
import edu.wpi.enties.ChangePasswordRequest;
import edu.wpi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangePassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();

        doNothing().when(service).changePassword(request, principal);

        ResponseEntity<?> result = controller.changePassword(request, principal);

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        verify(service, times(1)).changePassword(request, principal);
    }
}
