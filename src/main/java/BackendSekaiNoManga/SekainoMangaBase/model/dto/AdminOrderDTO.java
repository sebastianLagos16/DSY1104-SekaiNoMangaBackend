// src/main/java/.../dto/AdminOrderDTO.java
package BackendSekaiNoManga.SekainoMangaBase.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AdminOrderDTO(
        Long id,
        LocalDateTime createdAt,
        String status,
        String customerName,
        String customerEmail,
        BigDecimal total,
        List<AdminOrderItemDTO> items) {
}