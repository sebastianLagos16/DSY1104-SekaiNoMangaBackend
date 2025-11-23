package BackendSekaiNoManga.SekainoMangaBase.model.dto;

import BackendSekaiNoManga.SekainoMangaBase.model.Manga.Estado;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MangaUpdateDTO {

  private String mangaName;

  @DecimalMin(value = "0.0", inclusive = true, message = "price debe ser ≥ 0")
  private BigDecimal price;

  private String publisher;

  @Min(value = 0, message = "stock debe ser ≥ 0")
  private Integer stock;

  private String portadaUrl;

  private Estado estado;
  private String author;
  private String description;
  private String genre;
  private Boolean onSale;
  private Boolean topSelling;
}
