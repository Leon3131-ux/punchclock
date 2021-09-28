package ch.zli.m223.punchclock.converter;

import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoConverter {

    private final PermissionDtoConverter permissionDtoConverter;

    public UserDto toDto(User user){
        return new UserDto(
                user.getId(),
                user.getUsername(),
                "",
                user.getPermissions()
                        .stream()
                        .map(permissionDtoConverter::toDto)
                        .collect(Collectors.toList())
        );
    }

}
