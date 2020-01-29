package pl.wolski.bank.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import pl.wolski.bank.BankApplication;
import pl.wolski.bank.config.beans.WebMvcConfigurerImpl;
import pl.wolski.bank.models.Address;
import pl.wolski.bank.models.Role;
import pl.wolski.bank.models.User;
import pl.wolski.bank.repositories.AddressRepository;
import pl.wolski.bank.repositories.RoleRepository;
import pl.wolski.bank.services.*;
import org.springframework.test.web.servlet.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@TestPropertySource(locations="classpath:application-test.properties")
class UserControllerTest {
    @Autowired
    private EmailService emailService;

    @MockBean
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserController userController;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private Model model;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private static MockMvc mockMvc;


    @Before
    public void setup() {


    }

    @Test
    void home() {
    }

    @Test
    void greeting() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(this.userService)).build();

        String someString = "dsadasda";
        String uri = "/confirm?id=" + someString;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(view().name("actionMessage"))
                .andExpect(model().attribute("message", "Nie udało się aktywować konta"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void newPassword() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(this.userService)).build();

        String someString = "dsadasda";
        String uri = "/newPassword?confirmationId=" + someString;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(view().name("actionMessage"))
                .andExpect(model().attribute("message", "Nie udało się nadać nowego hasła"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void testNewPassword() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(this.userService)).build();

        String uri = "/newPassword";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .param("password", "password")
                .param("passwordConfirm", "password")
                .param("confirmationId", "")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(model().attribute("message", "Nie udało się ustawić hasła"))
                .andExpect(view().name("actionMessage"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void passwordReset() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(this.userService)).build();

        String uri = "/passwordReset";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .param("email", "email1@wp.pl")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(model().attribute("message", "Nie znaleziono użytkownika"))
                .andExpect(view().name("actionMessage"))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void testPasswordReset() {
    }

    @Test
    void getSimpleSearch() {
    }

    @Test
    void user() {
    }

    @Test
    void resetUserList() {
    }

    @Test
    void showUserList() {
    }
}