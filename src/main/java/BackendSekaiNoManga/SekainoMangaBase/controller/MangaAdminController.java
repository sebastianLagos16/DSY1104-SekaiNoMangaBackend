package BackendSekaiNoManga.SekainoMangaBase.controller;

import BackendSekaiNoManga.SekainoMangaBase.model.Manga;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.MangaCreateDTO;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.MangaStockUpdateDTO;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.MangaUpdateDTO;
import BackendSekaiNoManga.SekainoMangaBase.service.MangaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/mangas")
@RequiredArgsConstructor
public class MangaAdminController {

  private final MangaService mangaService;

  @GetMapping
  public List<Manga> list(@RequestParam(required = false) String q) {
    return mangaService.findAllAdmin(q);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Manga create(@Valid @RequestBody MangaCreateDTO dto) {
    return mangaService.create(dto);
  }

  @PutMapping("/{id}")
  public Manga update(@PathVariable Long id,
      @Valid @RequestBody MangaUpdateDTO dto) {
    return mangaService.update(id, dto);
  }

  @PatchMapping("/{id}/stock")
  public Manga updateStock(@PathVariable Long id,
      @Valid @RequestBody MangaStockUpdateDTO dto) {
    return mangaService.updateStock(id, dto.getStock());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    mangaService.delete(id);
  }

  @PostMapping("/upload-json")
  @ResponseStatus(HttpStatus.CREATED)
  public List<Manga> uploadMassive(@RequestBody List<@Valid MangaCreateDTO> dtos) {
    return dtos.stream()
        .map(mangaService::create)
        .toList();
  }
}
