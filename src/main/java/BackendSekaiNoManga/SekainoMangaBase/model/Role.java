package BackendSekaiNoManga.SekainoMangaBase.model;

import jakarta.persistence.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name="roles")
public class Role {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique=true, nullable=false, length=32)
  private String name; // "ROLE_ADMIN", "ROLE_USER"
}