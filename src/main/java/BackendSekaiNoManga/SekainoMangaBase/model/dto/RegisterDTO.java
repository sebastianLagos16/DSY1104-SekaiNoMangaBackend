package BackendSekaiNoManga.SekainoMangaBase.model.dto;

import BackendSekaiNoManga.SekainoMangaBase.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDTO {
  @Email @NotBlank
  private String email;

  @NotBlank
  private String nombre;

  @StrongPassword
  private String password;

  @NotBlank
  private String confirmPassword;

  public boolean passwordsMatch() {
    return password != null && password.equals(confirmPassword);
  }
}
