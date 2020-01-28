package pl.wolski.bank.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import pl.wolski.bank.features.PdfGenerator;
import pl.wolski.bank.models.*;
import pl.wolski.bank.services.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@SessionAttributes(names = {"user"})
@Log4j2
public class ConfirmationController {
    @Autowired(required = false)
    private UserService userService;

    @Autowired(required = false)
    private CreditApplicationService creditApplicationService;

    @Autowired(required = false)
    private CreditService creditService;

    @Autowired(required = false)
    private CreditTypeService creditTypeService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private TemplateEngine templateEngine;

    @PostMapping("/confirmation")
    public String getConfirmation(Model model,
                                @RequestParam(value="id", required=true) Long id,
                                @RequestParam(value="userAccount", required=true) BigDecimal userAccount) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        User user = userService.findByUsername(((UserDetails) principal).getUsername());

        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        Transaction transaction = transactionService.findById(id);

        model.addAttribute("transaction", transaction);
        model.addAttribute("date", date);
        model.addAttribute("user", user);
        model.addAttribute("userAccount", bankAccountService.findByBankAccountNumber(userAccount));

        Map<String, Object> data = new HashMap<>();

        data.put("transaction", transaction);
        data.put("date", date);
        data.put("user", user);
        data.put("userAccount", bankAccountService.findByBankAccountNumber(userAccount));

        PdfGenerator pdfGenerator =new PdfGenerator(templateEngine);

        pdfGenerator.createPdf("confirmation", data);

        return "redirect:/download?file=" + transaction.getId();
    }

    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@RequestParam("file") String id) throws IOException {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String SERVER_LOCATION = s + "\\src\\main\\resources\\static\\confirmation\\";
        File file = new File(SERVER_LOCATION + id + ".pdf");

        String fileName = UUID.randomUUID().toString();

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transaction_"+ fileName +".pdf");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);

    }
}
