package hexlet.code;

import hexlet.code.config.security.Permission;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static hexlet.code.config.security.Permission.ADMIN_READ;
import static hexlet.code.config.security.Permission.ADMIN_UPDATE;
import static hexlet.code.config.security.Permission.ADMIN_CREATE;
import static hexlet.code.config.security.Permission.ADMIN_DELETE;

@RequiredArgsConstructor
public enum UserRole {
    ADMIN(
        Set.of(
            ADMIN_READ,
            ADMIN_UPDATE,
            ADMIN_DELETE,
            ADMIN_CREATE
        )
    ),
    USER(Collections.EMPTY_SET);

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
