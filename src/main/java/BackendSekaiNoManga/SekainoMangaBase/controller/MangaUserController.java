package BackendSekaiNoManga.SekainoMangaBase.controller;

import BackendSekaiNoManga.SekainoMangaBase.model.Manga;
import BackendSekaiNoManga.SekainoMangaBase.service.MangaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mangas")
@RequiredArgsConstructor
public class MangaUserController {
  private final MangaService mangaService;

  @GetMapping
  public List<Manga> list(@RequestParam(required = false) String q) {
    return mangaService.findAllPublic(q);
  }

  @GetMapping("/{id}")
  public Manga get(@PathVariable Long id) {
    return mangaService.findPublicById(id);
  }
}