package pl.wolski.bank.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import pl.wolski.bank.repositories.RoleRepository;
import pl.wolski.bank.services.*;
import org.springframework.test.web.servlet.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserController userController;

    @MockBean
    private Model model;

    @Autowired
    private MockMvc mockMvc;

    public void setup() {
        this.mockMvc = standaloneSetup(new UserController()).build();
    }

    @Test
    void home() {
    }

    public <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @Test
    void greeting() throws Exception {
        String someString = "dsadasda";
        String uri = "/confirm?id=" + someString;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(view().name("actionMessage"))
                .andExpect(model().attribute("message", "Nie udało się aktywować konta")).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void newPassword() throws Exception {
        String someString = "dsadasda";
        String uri = "/newPassword?confirmationId=" + someString;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(view().name("actionMessage"))
                .andExpect(model().attribute("message", "Nie udało się nadać nowego hasła")).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    void testNewPassword() {
    }

    @Test
    void passwordReset() {
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