package BackendSekaiNoManga.SekainoMangaBase.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {

    @Email
    @NotBlank
    private String email; // solo lectura en el update, pero lo devolvemos

    @NotBlank
    private String nombre;

    private String comuna;
    private String direccion;
    private String region;
    private String telefono;
    private String codigoPostal;
}
