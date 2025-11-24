package BackendSekaiNoManga.SekainoMangaBase.service;

import BackendSekaiNoManga.SekainoMangaBase.model.Manga;
import BackendSekaiNoManga.SekainoMangaBase.model.Order;
import BackendSekaiNoManga.SekainoMangaBase.model.OrderItem;
import BackendSekaiNoManga.SekainoMangaBase.model.User;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.CreateOrderDTO;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.OrderDTO;
import BackendSekaiNoManga.SekainoMangaBase.repository.MangaRepository;
import BackendSekaiNoManga.SekainoMangaBase.repository.OrderRepository;
import BackendSekaiNoManga.SekainoMangaBase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MangaRepository mangaRepository;
    private final UserRepository userRepository;

    // Crea una orden
    @Transactional
    public OrderDTO create(CreateOrderDTO dto, String userEmail) {
        if (dto.items() == null || dto.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Order must contain at least one item.");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found."));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderDTO.Item itemDto : dto.items()) {
            Manga manga = mangaRepository.findById(itemDto.mangaId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Manga not found: " + itemDto.mangaId()));

            if (manga.isEliminado() || manga.getEstado() != Manga.Estado.ACTIVO) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Manga not available: " + manga.getMangaName());
            }

            Integer qty = itemDto.quantity();
            if (qty == null || qty < 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid quantity for manga: " + manga.getMangaName());
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setManga(manga);
            item.setQuantity(qty);
            item.setUnitPrice(manga.getPrice());

            items.add(item);

            BigDecimal lineTotal = manga.getPrice().multiply(BigDecimal.valueOf(qty));
            total = total.add(lineTotal);
        }

        order.setItems(items);
        order.setTotal(total);

        Order saved = orderRepository.save(order);
        return toDTO(saved);
    }

    // Lista las ordenes de un usuario
    @Transactional(readOnly = true)
    public List<OrderDTO> findByUser(String userEmail) {
        return orderRepository.findByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // confirma una compra y descuenta stock
    @Transactional
    public OrderDTO confirm(Long id, String userEmail) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found"));

        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You cannot modify this order.");
        }

        if (order.getStatus() != Order.Status.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Order already processed.");
        }

        // Verificar y descontar stock
        for (OrderItem item : order.getItems()) {
            Manga manga = item.getManga();

            if (manga.isEliminado() || manga.getEstado() != Manga.Estado.ACTIVO) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Manga not available: " + manga.getMangaName());
            }

            int currentStock = manga.getStock() != null ? manga.getStock() : 0;
            if (currentStock < item.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Not enough stock for: " + manga.getMangaName());
            }

            manga.setStock(currentStock - item.getQuantity());

            if (manga.getStock() == 0) {
                manga.setEstado(Manga.Estado.DESCONTINUADO);
            }

            mangaRepository.save(manga);
        }

        order.setStatus(Order.Status.CONFIRMED);
        Order saved = orderRepository.save(order);
        return toDTO(saved);
    }

    // Cancelar una Compra
    @Transactional
    public OrderDTO cancel(Long id, String userEmail) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found"));

        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You cannot modify this order.");
        }

        if (order.getStatus() != Order.Status.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Order already processed.");
        }

        order.setStatus(Order.Status.CANCELLED);
        Order saved = orderRepository.save(order);
        return toDTO(saved);
    }

    // Mapeo a DTO
    public OrderDTO toDTO(Order order) {
        List<OrderDTO.Item> items = order.getItems().stream()
                .map(oi -> new OrderDTO.Item(
                        oi.getId(),
                        oi.getManga().getId(),
                        oi.getManga().getMangaName(),
                        oi.getQuantity(),
                        oi.getUnitPrice()))
                .toList();

        return new OrderDTO(
                order.getId(),
                order.getStatus().name(),
                order.getTotal(),
                order.getCreatedAt(),
                items);
    }
}
