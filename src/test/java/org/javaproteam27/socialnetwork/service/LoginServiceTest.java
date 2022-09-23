package org.javaproteam27.socialnetwork.service;

import org.javaproteam27.socialnetwork.handler.exception.InvalidRequestException;
import org.javaproteam27.socialnetwork.model.dto.request.LoginRq;
import org.javaproteam27.socialnetwork.model.dto.response.PersonRs;
import org.javaproteam27.socialnetwork.model.dto.response.ResponseRs;
import org.javaproteam27.socialnetwork.model.entity.Person;
import org.javaproteam27.socialnetwork.repository.PersonRepository;
import org.javaproteam27.socialnetwork.security.jwt.JwtTokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LoginServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private LoginService loginService;

    @Before
    public void setUp() {
        loginService = new LoginService(jwtTokenProvider, personRepository);
    }

    @Test
    public void profileResponse() {
        String token = "token";

        Person person = new Person();
        person.setEmail("email");
        person.setPassword("pass");
        person.setIsBlocked(false);

        Integer expectedOffset = 0;
        Integer expectedPerPage = 20;

        when(jwtTokenProvider.getUsername(token)).thenReturn("email");
        when(personRepository.findByEmail("email")).thenReturn(person);

        var response = loginService.profileResponse(token);

        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals("string", response.getError());
        assertEquals(expectedOffset, response.getOffset());
        assertEquals(expectedPerPage, response.getPerPage());
    }

    @Test
    public void login() {
        LoginRq loginRq = new LoginRq();
        loginRq.setEmail("test@mail.ru");
        loginRq.setPassword("test");

        Person person = new Person();
        person.setEmail("test@mail.ru");
        person.setPassword("test");
        person.setIsBlocked(false);

        when(personRepository.findByEmail(loginRq.getEmail())).thenReturn(person);
        when(jwtTokenProvider.createToken(anyString())).thenReturn("");

        ResponseRs<PersonRs> response = loginService.login(loginRq);

        assertNotNull(response);
        assertNotNull(response.getData().getToken());
        assertNotNull(response.getData());
        assertEquals("", response.getError());
        assertTrue(response.getTimestamp() instanceof Long);

        verify(jwtTokenProvider, times(1)).createToken(loginRq.getEmail());
    }

    @Test
    public void loginFail() {
        LoginRq loginRq = new LoginRq();
        loginRq.setEmail("test@mail.ru");
        loginRq.setPassword("test");

        Person person = new Person();
        person.setEmail("test@mail.ru");
        person.setPassword("wrong");
        person.setIsBlocked(false);

        when(personRepository.findByEmail(any())).thenReturn(person);
        when(jwtTokenProvider.createToken(anyString())).thenReturn("");

        InvalidRequestException thrown = assertThrows(InvalidRequestException.class,
                () -> loginService.login(loginRq));

        assertEquals("Incorrect password", thrown.getMessage());

        verify(jwtTokenProvider, times(0)).createToken(loginRq.getEmail());
    }

    @Test
    public void logout() {
        ResponseRs<Object> response = loginService.logout();

        HashMap<String, String> expectedData = new HashMap<>();
        expectedData.put("message", "ok");

        assertNotNull(response);
        assertEquals("", response.getError());
        assertTrue(response.getTimestamp() instanceof Long);
        assertNotNull(response.getData());
        assertEquals(expectedData, response.getData());
    }
}