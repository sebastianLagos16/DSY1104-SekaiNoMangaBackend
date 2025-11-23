package BackendSekaiNoManga.SekainoMangaBase.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
  // Reglas fijas: 8-64, 1 mayúscula, 1 minúscula, 1 dígito, 1 símbolo. Sin espacios.
  private static final String REGEX =
      "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9])[\\S]{8,64}$";

  @Override public boolean isValid(String value, ConstraintValidatorContext ctx) {
    if (value == null) return false;
    return value.matches(REGEX);
  }
}
