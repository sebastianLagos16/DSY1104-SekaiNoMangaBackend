package BackendSekaiNoManga.SekainoMangaBase.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MangaCreateDTO {

  @NotBlank(message = "mangaName es requerido")
  private String mangaName;

  @NotNull(message = "price es requerido")
  @DecimalMin(value = "0.0", inclusive = true, message = "price debe ser ≥ 0")
  private BigDecimal price;

  @NotBlank(message = "publisher es requerido")
  private String publisher;

  @NotNull(message = "stock es requerido")
  @Min(value = 0, message = "stock debe ser ≥ 0")
  private Integer stock;
  private String portadaUrl;
  private String author;
  private String description;
  private String genre;
  private Boolean onSale;
  private Boolean topSelling;
}
