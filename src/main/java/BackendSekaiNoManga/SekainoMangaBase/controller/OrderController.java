package BackendSekaiNoManga.SekainoMangaBase.controller;

import BackendSekaiNoManga.SekainoMangaBase.model.dto.CreateOrderDTO;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.OrderDTO;
import BackendSekaiNoManga.SekainoMangaBase.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Crear Order desde el carrito
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO create(@RequestBody CreateOrderDTO dto,
            Authentication auth) {
        String email = auth.getName(); // mismo patrón que changePassword
        return orderService.create(dto, email);
    }

    // "Mis compras"
    @GetMapping("/me")
    public List<OrderDTO> myOrders(Authentication auth) {
        String email = auth.getName();
        return orderService.findByUser(email);
    }

    // Confirmar -> descuenta stock
    @PatchMapping("/{id}/confirm")
    public OrderDTO confirm(@PathVariable Long id,
            Authentication auth) {
        String email = auth.getName();
        return orderService.confirm(id, email);
    }

    // Cancelar -> NO toca stock
    @PatchMapping("/{id}/cancel")
    public OrderDTO cancel(@PathVariable Long id,
            Authentication auth) {
        String email = auth.getName();
        return orderService.cancel(id, email);
    }
}
