package BackendSekaiNoManga.SekainoMangaBase.service;

import BackendSekaiNoManga.SekainoMangaBase.model.Manga;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.MangaCreateDTO;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.MangaUpdateDTO;
import BackendSekaiNoManga.SekainoMangaBase.repository.MangaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MangaService {

  private final MangaRepository repo;

  /* ===== PÚBLICO ===== */
  @Transactional(readOnly = true)
  public List<Manga> findAllPublic(String q) {
    var all = repo.findByEliminadoFalseAndEstadoAndStockGreaterThan(Manga.Estado.ACTIVO, 0);
    if (q == null || q.isBlank())
      return all;
    String k = q.toLowerCase();
    return all.stream()
        .filter(m -> m.getMangaName().toLowerCase().contains(k) || m.getPublisher().toLowerCase().contains(k))
        .toList();
  }

  @Transactional(readOnly = true)
  public List<Manga> findAllAdmin(String q) {
    var all = repo.findAll(); // Admin ve todos
    if (q == null || q.isBlank())
      return all;

    String k = q.toLowerCase();
    return all.stream()
        .filter(m -> (m.getMangaName() != null && m.getMangaName().toLowerCase().contains(k)) ||
            (m.getPublisher() != null && m.getPublisher().toLowerCase().contains(k)))
        .toList();
  }

  @Transactional(readOnly = true)
  public Manga findPublicById(Long id) {
    return repo.findById(id)
        .filter(m -> !m.isEliminado() && m.getEstado() == Manga.Estado.ACTIVO && m.getStock() > 0)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manga no existe o no disponible: " + id));
  }

  /* ===== CRUD ADMIN ===== */
  @Transactional
  public Manga create(MangaCreateDTO dto) {
    Manga m = new Manga();
    m.setMangaName(dto.getMangaName().trim());
    m.setPrice(dto.getPrice());
    m.setPublisher(dto.getPublisher().trim());
    m.setStock(dto.getStock());
    m.setPortadaUrl(dto.getPortadaUrl());

    m.setAuthor(dto.getAuthor() != null ? dto.getAuthor().trim() : "Autor desconocido");
    m.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : "");
    m.setGenre(dto.getGenre() != null && !dto.getGenre().isBlank()
        ? dto.getGenre().trim()
        : "Sin género");

    if (dto.getOnSale() != null) {
      m.setOnSale(dto.getOnSale());
    }
    if (dto.getTopSelling() != null) {
      m.setTopSelling(dto.getTopSelling());
    }

    m.setEstado(Manga.Estado.ACTIVO);
    m.setEliminado(false);

    return repo.save(m);
  }

  @Transactional
  public Manga update(Long id, MangaUpdateDTO dto) {
    Manga m = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manga no encontrado"));

    if (dto.getMangaName() != null) {
      m.setMangaName(dto.getMangaName().trim());
    }
    if (dto.getPrice() != null) {
      m.setPrice(dto.getPrice());
    }
    if (dto.getPublisher() != null) {
      m.setPublisher(dto.getPublisher().trim());
    }
    if (dto.getStock() != null) {
      m.setStock(dto.getStock());
    }
    if (dto.getPortadaUrl() != null) {
      m.setPortadaUrl(dto.getPortadaUrl().trim());
    }
    if (dto.getAuthor() != null) {
      m.setAuthor(dto.getAuthor().trim());
    }
    if (dto.getDescription() != null) {
      m.setDescription(dto.getDescription().trim());
    }
    if (dto.getGenre() != null) {
      m.setGenre(dto.getGenre().trim());
    }
    if (dto.getOnSale() != null) {
      m.setOnSale(dto.getOnSale());
    }
    if (dto.getTopSelling() != null) {
      m.setTopSelling(dto.getTopSelling());
    }
    if (dto.getEstado() != null) {
      m.setEstado(dto.getEstado());
    }

    return repo.save(m);
  }

  @Transactional
  public Manga updateStock(Long id, int newStock) {
    Manga m = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manga no existe: " + id));
    if (newStock < 0)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "stock inválido");
    m.setStock(newStock);
    return repo.save(m);
  }

  @Transactional
  public void delete(Long id) {
    Manga m = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manga no existe: " + id));
    m.setEliminado(true);
    repo.save(m);
  }
}