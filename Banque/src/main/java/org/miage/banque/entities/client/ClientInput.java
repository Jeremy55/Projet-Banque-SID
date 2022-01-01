package org.miage.banque.entities.client;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInput {

    @NotNull
    @NotBlank
    @Size(min = 2, max = 20)
    private String nom;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 20)
    private String prenom;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 20)
    private String pays;

    // Most countries have 9 digits, if it's less, complete with 0s.
    @NotBlank
    @NotNull
    @Size(min = 9, max = 9)
    private String no_passport;

    @NotBlank
    @NotNull
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^[0-9]*$")
    private String telephone;

    @NotBlank
    @NotNull
    private String secret;
}
