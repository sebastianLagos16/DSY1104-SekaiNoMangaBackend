package BackendSekaiNoManga.SekainoMangaBase.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
  String message() default
      "La contraseña debe tener 8–64 caracteres, incluir mayúscula, minúscula, número y símbolo, y no contener espacios.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
