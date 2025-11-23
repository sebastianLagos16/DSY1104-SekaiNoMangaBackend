package BackendSekaiNoManga.SekainoMangaBase.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mangas")
public class Manga {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false, length = 80)
  private String mangaName;

  @NotNull
  @DecimalMin("0.0")
  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal price;

  @NotBlank
  @Column(nullable = false, length = 60)
  private String publisher;

  @Builder.Default
  @NotNull
  @Min(0)
  @Column(nullable = false)
  private Integer stock = 0;

  private String portadaUrl;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Estado estado = Estado.ACTIVO; // ACTIVO/OCULTO/DESCONTINUADO

  @Builder.Default
  @Column(nullable = false)
  private boolean eliminado = false;

  @Column(length = 150)
  private String author;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(length = 50)
  private String genre;

  @Builder.Default
  @Column(name = "on_sale", nullable = false)
  private boolean onSale = false;

  @Builder.Default
  @Column(name = "top_selling", nullable = false)
  private boolean topSelling = false;

  public enum Estado {
    ACTIVO, OCULTO, DESCONTINUADO
  }

  @PrePersist
  public void prePersist() {
    if (estado == null) {
      estado = Estado.ACTIVO;
    }
    if (stock == null) {
      stock = 0;
    }
  }
}
