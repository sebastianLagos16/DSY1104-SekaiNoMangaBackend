package BackendSekaiNoManga.SekainoMangaBase.repository;

import BackendSekaiNoManga.SekainoMangaBase.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Lista las ordenes de un usuario
    List<Order> findByUserEmailOrderByCreatedAtDesc(String email);
}
