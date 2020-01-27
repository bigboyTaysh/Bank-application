package pl.wolski.bank.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 36)
    private String street;

    @NotBlank
    @Size(min = 1, max = 36)
    private String houseNumber;

    @Nullable
    private String apartmentNumber;

    @NotBlank
    @Size(min = 3, max = 36)
    private String city;

    @NotBlank
    @Size(min = 6, max = 6)
    private String zipCode;

    public Address(String street, String houseNumber, String apartmentNumber, String city, String zipCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
        this.city = city;
        this.zipCode = zipCode;
    }
}
