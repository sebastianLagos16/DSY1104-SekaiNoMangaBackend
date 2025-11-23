package BackendSekaiNoManga.SekainoMangaBase.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateOrderStatusDTO {

    @NotBlank
    private String status;
}
