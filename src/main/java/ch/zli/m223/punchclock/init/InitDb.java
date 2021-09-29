package ch.zli.m223.punchclock.init;

import ch.zli.m223.punchclock.domain.Permission;
import ch.zli.m223.punchclock.domain.PermissionName;
import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.repository.PermissionRepository;
import ch.zli.m223.punchclock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class InitDb {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    @Value("${db.initialize}")
    private boolean initialize;

    @PostConstruct
    public void initDb(){
        if(initialize){
            List<Permission> permissions = initPermissions();
            initSuperAdmin(permissions);
            initAdmin(permissions);
            initUser();
        }
    }

    private void initSuperAdmin(List<Permission> permissions){
        Optional<Permission> superAdminPermission = permissions.stream()
                .filter(
                        permission ->
                                permission
                                        .getName()
                                        .equals(PermissionName.SUPER_ADMINISTRATE)
                ).findFirst();
        if(superAdminPermission.isPresent()){
            User user = new User(
                    0L,
                    "superadmin",
                    bCryptPasswordEncoder.encode("superadmin"),
                    null,
                    List.of(superAdminPermission.get())
            );
            userRepository.save(user);
        }
    }

    private void initAdmin(List<Permission> permissions){
        Optional<Permission> adminPermission = permissions.stream()
                .filter(
                        permission ->
                                permission
                                        .getName()
                                        .equals(PermissionName.ADMINISTRATE)
                ).findFirst();
        if(adminPermission.isPresent()){
            User user = new User(
                    0L,
                    "admin",
                    bCryptPasswordEncoder.encode("admin"),
                    null,
                    List.of(adminPermission.get())
            );
            userRepository.save(user);
        }
    }

    private void initUser(){
        User user = new User(
                0L,
                "user",
                bCryptPasswordEncoder.encode("user"),
                null,
                new ArrayList<>()
        );
        userRepository.save(user);
    }

    private List<Permission> initPermissions(){
        List<Permission> permissions = new ArrayList<>();
        for(PermissionName permissionName : PermissionName.values()){
            permissions.add(permissionRepository.save(new Permission(0L, permissionName)));
        }
        return permissions;
    }

}
