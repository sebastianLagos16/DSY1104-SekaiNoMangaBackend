package BackendSekaiNoManga.SekainoMangaBase.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
        Long id,
        String status,
        BigDecimal total,
        LocalDateTime createdAt,
        List<Item> items) {
    public record Item(
            Long id,
            Long mangaId,
            String title,
            Integer quantity,
            BigDecimal unitPrice) {
    }
}
