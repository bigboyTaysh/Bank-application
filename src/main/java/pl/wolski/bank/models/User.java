package pl.wolski.bank.models;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import pl.wolski.bank.annotations.UniqueEmail;
import pl.wolski.bank.annotations.UniquePersonalIdentificationNumber;
import pl.wolski.bank.annotations.UniqueUsername;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 36)
    @UniqueUsername
    private String username;

    @NotBlank
    @Size(min = 4, max = 36)
    private String password;

    @NotBlank
    @Size(min = 4, max = 36)
    private String firstName;

    @NotBlank
    @Size(min = 4, max = 36)
    private String lastName;

    @NumberFormat
    @DecimalMin(value = "10000000000")
    @DecimalMax(value = "99999999999")
    @Digits(integer=11, fraction=0)
    @UniquePersonalIdentificationNumber
    private BigDecimal personalIdentificationNumber;

    @NotBlank
    @Email
    @UniqueEmail
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 4, max = 36)
    private String phone;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date birthDate;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date joinDate;

    @Transient//pole nie będzie odwzorowane w db
    private String passwordConfirm;

    private boolean enabled = false;
    private String confirmationId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    private Set<Role> roles;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="address_id", nullable = true)
    private Address address;

    @OneToMany(mappedBy = "user")
    private Set<BankAccount> bankAccounts;

    @OneToMany(mappedBy = "user")
    private Set<CreditApplication> creditApplications;

    @OneToMany(mappedBy = "user")
    private Set<Credit> credits;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private Set<RecurringPayment> recurringPayments;


    @AssertTrue
    private boolean isPasswordsEquals(){
        return password == null || passwordConfirm == null || password.equals(passwordConfirm);
    }

    public User(String username){
        this(username, false);
    }

    public User(String username, boolean enabled){
        this.username = username;
        this.enabled = enabled;
    }

    public String createConfirmationID() {
        return java.util.UUID.randomUUID().toString();
    }
}