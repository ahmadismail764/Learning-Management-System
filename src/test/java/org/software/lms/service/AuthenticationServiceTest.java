import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.software.lms.dto.AuthenticationRequest;
import org.software.lms.dto.AuthenticationResponse;
import org.software.lms.model.*;
import org.software.lms.repository.*;
import org.software.lms.service.*;
import org.software.lms.security.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    private User mockUser;
    private AuthenticationRequest mockRequest;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encoded_password");
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");
        mockUser.setRole(Role.STUDENT);

        mockRequest = new AuthenticationRequest();
        mockRequest.setEmail("test@example.com");
        mockRequest.setPassword("password");
        mockRequest.setFirstName("Test");
        mockRequest.setLastName("User");
        mockRequest.setRole(Role.STUDENT);
    }

    @Test
    void testRegister_Success() {
        when(userRepository.findByEmail(mockRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(mockRequest.getPassword())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(jwtUtil.generateToken(any())).thenReturn("jwt_token");

        AuthenticationResponse response = authenticationService.register(mockRequest);

        assertNotNull(response);
        assertEquals(mockUser.getId(), response.getId());
        assertEquals(mockUser.getEmail(), response.getEmail());
        assertEquals("jwt_token", response.getToken());

        verify(userRepository, times(1)).findByEmail(mockRequest.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtUtil, times(1)).generateToken(any());
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        when(userRepository.findByEmail(mockRequest.getEmail())).thenReturn(Optional.of(mockUser));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authenticationService.register(mockRequest));

        assertEquals("Email already in use", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(mockRequest.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testAuthenticate_Success() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(mockRequest.getEmail())).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateToken(any())).thenReturn("jwt_token");

        AuthenticationResponse response = authenticationService.authenticate(mockRequest);

        assertNotNull(response);
        assertEquals(mockUser.getId(), response.getId());
        assertEquals(mockUser.getEmail(), response.getEmail());
        assertEquals("jwt_token", response.getToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(mockRequest.getEmail());
        verify(jwtUtil, times(1)).generateToken(any());
    }

    @Test
    void testAuthenticate_UserNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(mockRequest.getEmail())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authenticationService.authenticate(mockRequest));

        assertEquals("User not found", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail(mockRequest.getEmail());
    }
}
