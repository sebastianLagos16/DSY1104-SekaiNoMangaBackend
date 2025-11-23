package BackendSekaiNoManga.SekainoMangaBase.model.dto;

public record AdminOrderItemDTO(
        Long mangaId,
        String titulo,
        int quantity,
        int unitPrice) {
}