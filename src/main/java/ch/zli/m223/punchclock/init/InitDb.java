package ch.zli.m223.punchclock.init;

import ch.zli.m223.punchclock.domain.Permission;
import ch.zli.m223.punchclock.domain.PermissionName;
import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.repository.PermissionRepository;
import ch.zli.m223.punchclock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    @PostConstruct
    public void initDb(){
        List<Permission> permissions = initPermissions();
        initAdmin(permissions);
    }

    private void initAdmin(List<Permission> permissions){
        Optional<Permission> adminPermission = permissions.stream()
                .filter(
                        permission ->
                                permission
                                        .getName()
                                        .equals(PermissionName.SUPER_ADMINISTRATE)
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

    private List<Permission> initPermissions(){
        List<Permission> permissions = new ArrayList<>();
        for(PermissionName permissionName : PermissionName.values()){
            permissions.add(permissionRepository.save(new Permission(0L, permissionName, new ArrayList<>())));
        }
        return permissions;
    }

}
