package BackendSekaiNoManga.SekainoMangaBase.repository;

import BackendSekaiNoManga.SekainoMangaBase.model.Manga;
import BackendSekaiNoManga.SekainoMangaBase.model.Manga.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MangaRepository extends JpaRepository<Manga, Long> {

  // Catálogo público
  List<Manga> findByEliminadoFalseAndEstadoAndStockGreaterThan(Estado estado, int minStock);

  // ↓↓↓ ATÓMICOS PARA CARRITO ↓↓↓
  @Modifying
  @Transactional
  @Query("""
         UPDATE Manga m
            SET m.stock = m.stock - :qty
          WHERE m.id = :id AND m.stock >= :qty AND m.eliminado = false
         """)
  int tryDecrementStock(@Param("id") Long id, @Param("qty") int qty);

  @Modifying
  @Transactional
  @Query("""
         UPDATE Manga m
            SET m.stock = m.stock + :qty
          WHERE m.id = :id AND m.eliminado = false
         """)
  int incrementStock(@Param("id") Long id, @Param("qty") int qty);
}
