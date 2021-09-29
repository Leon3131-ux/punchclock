package ch.zli.m223.punchclock.converter;

import ch.zli.m223.punchclock.domain.Permission;
import ch.zli.m223.punchclock.dto.PermissionDto;
import ch.zli.m223.punchclock.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionDtoConverter {

    private final PermissionRepository permissionRepository;

    public PermissionDto toDto(Permission permission){
        return new PermissionDto(
                permission.getId(),
                permission.getName().toString()
        );
    }

    public List<PermissionDto> convertAll(List<Permission> permissions){
        return permissions.stream().map(this::toDto).collect(Collectors.toList());
    }

}
