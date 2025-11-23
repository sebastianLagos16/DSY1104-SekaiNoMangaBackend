package BackendSekaiNoManga.SekainoMangaBase.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class CartRequest {
  @NotEmpty
  private List<CartItemDTO> items;
}
