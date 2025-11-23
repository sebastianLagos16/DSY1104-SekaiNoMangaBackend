package BackendSekaiNoManga.SekainoMangaBase.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MangaStockUpdateDTO {
  @NotNull
  @Min(0)
  private Integer stock;
}
