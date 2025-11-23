package BackendSekaiNoManga.SekainoMangaBase.repository;

import BackendSekaiNoManga.SekainoMangaBase.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  // para login / lookup
  Optional<User> findByEmail(String email);

  // el que te falta en AuthService.register(...)
  boolean existsByEmail(String email);
}
