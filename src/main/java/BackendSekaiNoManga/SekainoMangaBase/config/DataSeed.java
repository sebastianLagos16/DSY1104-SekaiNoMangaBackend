package BackendSekaiNoManga.SekainoMangaBase.config;

import BackendSekaiNoManga.SekainoMangaBase.model.Role;
import BackendSekaiNoManga.SekainoMangaBase.model.User;
import BackendSekaiNoManga.SekainoMangaBase.repository.RoleRepository;
import BackendSekaiNoManga.SekainoMangaBase.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeed implements CommandLineRunner {

    private final RoleRepository roles;
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public DataSeed(RoleRepository roles, UserRepository users, PasswordEncoder encoder) {
        this.roles = roles;
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // 1) Roles base
        Role rAdmin = roles.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_ADMIN");
            return roles.save(r);
        });

        Role rUser = roles.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_USER");
            return roles.save(r);
        });

        // 2) Admin
        User admin = users.findByEmail("admin@sekai.cl").orElseGet(() -> {
            User u = new User();
            u.setEmail("admin@sekai.cl");
            u.setPasswordHash(encoder.encode("123456.."));
            u.setNombre("Administrador");
            u.setTelefono("987654321");
            u.setRegion("RM");
            u.setComuna("Santiago");
            u.setDireccion("Antonio varas 666");
            u.setCodigoPostal("8320000");
            return users.save(u);
        });
        // asegurar roles
        if (!admin.getRoles().contains(rAdmin))
            admin.getRoles().add(rAdmin);
        if (!admin.getRoles().contains(rUser))
            admin.getRoles().add(rUser);
        users.save(admin);

        // Usuario de prueba
        User demo = users.findByEmail("user@sekai.cl").orElseGet(() -> {
            User u = new User();
            u.setEmail("user@sekai.cl");
            u.setPasswordHash(encoder.encode("123456."));
            u.setNombre("Usuario de prueba");
            u.setTelefono("912345678");
            u.setRegion("RM");
            u.setComuna("Providencia");
            u.setDireccion("Calle Falsa 123");
            u.setCodigoPostal("7500000");
            return users.save(u);
        });
        if (!demo.getRoles().contains(rUser))
            demo.getRoles().add(rUser);
        users.save(demo);
    }
}
