// src/main/java/BackendSekaiNoManga/SekainoMangaBase/controller/OrderAdminController.java
package BackendSekaiNoManga.SekainoMangaBase.controller;

import BackendSekaiNoManga.SekainoMangaBase.model.Order;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.AdminOrderDTO;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.AdminOrderItemDTO;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.UpdateOrderStatusDTO;
import BackendSekaiNoManga.SekainoMangaBase.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class OrderAdminController {

        private final OrderRepository orderRepository;

        // Lista todos los pedidos ordenados por fecha de creacion (admin)
        @GetMapping
        public List<AdminOrderDTO> findAll() {
                return orderRepository.findAll().stream()
                                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                                .map(this::toAdminDTO)
                                .toList();
        }

        // cambia el estado de un pedido Desde el Dashboard del Admin no usado aun hasta
        // implementar api de logistica
        @PatchMapping("/{id}/status")
        public AdminOrderDTO updateStatus(
                        @PathVariable Long id,
                        @RequestBody @Valid UpdateOrderStatusDTO body) {

                Order order = orderRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Order not found"));

                Order.Status newStatus;
                try {
                        newStatus = Order.Status.valueOf(body.getStatus().toUpperCase());
                } catch (IllegalArgumentException ex) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Invalid status: " + body.getStatus());
                }

                order.setStatus(newStatus);
                Order saved = orderRepository.save(order);

                return toAdminDTO(saved);
        }

        private AdminOrderDTO toAdminDTO(Order order) {
                var user = order.getUser();

                List<AdminOrderItemDTO> items = order.getItems().stream()
                                .map(oi -> new AdminOrderItemDTO(
                                                oi.getManga().getId(),
                                                oi.getManga().getMangaName(),
                                                oi.getQuantity(),
                                                oi.getUnitPrice().intValue() // unitPrice en DTO es int
                                ))
                                .toList();

                String customerName = user != null ? user.getNombre() : null;
                String customerEmail = user != null ? user.getEmail() : null;

                return new AdminOrderDTO(
                                order.getId(),
                                order.getCreatedAt(),
                                order.getStatus().name(),
                                customerName,
                                customerEmail,
                                order.getTotal(),
                                items);
        }
}
