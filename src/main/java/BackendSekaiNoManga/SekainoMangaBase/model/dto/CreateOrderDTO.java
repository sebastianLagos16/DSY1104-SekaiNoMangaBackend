package BackendSekaiNoManga.SekainoMangaBase.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderDTO(
        @NotEmpty List<Item> items) {
    public record Item(
            @NotNull Long mangaId,
            @NotNull @Min(1) Integer quantity) {
    }
}
