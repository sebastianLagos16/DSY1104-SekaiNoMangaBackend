package BackendSekaiNoManga.SekainoMangaBase.model.dto;

import BackendSekaiNoManga.SekainoMangaBase.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class ChangePasswordDTO {
  @NotBlank
  private String currentPassword;

  @StrongPassword
  private String newPassword;

  @NotBlank
  private String confirmNewPassword;

  public boolean newPasswordsMatch() {
    return newPassword != null && newPassword.equals(confirmNewPassword);
  }
}
