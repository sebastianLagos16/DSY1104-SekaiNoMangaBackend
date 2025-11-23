
package BackendSekaiNoManga.SekainoMangaBase.service;

import BackendSekaiNoManga.SekainoMangaBase.model.Role;
import BackendSekaiNoManga.SekainoMangaBase.model.User;
import BackendSekaiNoManga.SekainoMangaBase.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = users.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        Set<Role> roles = Optional.ofNullable(u.getRoles()).orElse(Collections.emptySet());
        List<GrantedAuthority> auths = roles.stream()
                // fuerza a String, con prefijo ROLE_ ya guardado en BD
                .map(r -> new SimpleGrantedAuthority(r.getName().toString()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(),
                u.getPasswordHash(),
                true,  // enabled (si no tienes boolean en User)
                true,
                true,
                true,
                auths
        );
    }
}
