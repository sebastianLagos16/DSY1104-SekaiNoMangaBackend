package BackendSekaiNoManga.SekainoMangaBase.model.dto;

import java.util.Set;

public record UserSummaryDTO(
        Long id,
        String email,
        String nombre,
        Set<String> roles
) {}
