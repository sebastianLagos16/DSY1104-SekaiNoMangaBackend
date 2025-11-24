package BackendSekaiNoManga.SekainoMangaBase.repository;

import BackendSekaiNoManga.SekainoMangaBase.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  // para login
  Optional<User> findByEmail(String email);

  // para registro
  boolean existsByEmail(String email);
}
