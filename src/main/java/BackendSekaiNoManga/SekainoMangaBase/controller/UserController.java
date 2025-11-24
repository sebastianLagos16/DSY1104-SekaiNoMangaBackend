package BackendSekaiNoManga.SekainoMangaBase.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import BackendSekaiNoManga.SekainoMangaBase.model.User;
import BackendSekaiNoManga.SekainoMangaBase.model.dto.UserProfileDTO;
import BackendSekaiNoManga.SekainoMangaBase.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository users;

    @GetMapping("/me")
    public UserProfileDTO me(Authentication auth) {
        String email = auth.getName();
        User u = users.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        UserProfileDTO dto = new UserProfileDTO();
        dto.setEmail(u.getEmail());
        dto.setNombre(u.getNombre());
        dto.setComuna(u.getComuna());
        dto.setDireccion(u.getDireccion());
        dto.setRegion(u.getRegion());
        dto.setTelefono(u.getTelefono());
        dto.setCodigoPostal(u.getCodigoPostal());
        return dto;
    }

    @PutMapping("/me")
    public UserProfileDTO updateMe(Authentication auth,
            @Valid @RequestBody UserProfileDTO body) {
        String email = auth.getName();
        User u = users.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // El correo no se cambia aquí
        u.setNombre(body.getNombre());
        u.setComuna(body.getComuna());
        u.setDireccion(body.getDireccion());
        u.setRegion(body.getRegion());
        u.setTelefono(body.getTelefono());
        u.setCodigoPostal(body.getCodigoPostal());

        users.save(u);

        return me(auth);
    }
}
