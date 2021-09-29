package ch.zli.m223.punchclock.converter;

import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.dto.ReturnUserDto;
import ch.zli.m223.punchclock.dto.SaveUserDto;
import ch.zli.m223.punchclock.repository.CompanyRepository;
import ch.zli.m223.punchclock.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoConverter {

    private final PermissionDtoConverter permissionDtoConverter;
    private final CompanyDtoConverter companyDtoConverter;
    private final CompanyRepository companyRepository;
    private final PermissionRepository permissionRepository;

    public ReturnUserDto toDto(User user){
        return new ReturnUserDto(
                user.getId(),
                user.getUsername(),
                user.getPermissions()
                        .stream()
                        .map(permissionDtoConverter::toDto)
                        .collect(Collectors.toList()),
                companyDtoConverter.toDto(user.getCompany())
        );
    }

    public User toEntity(SaveUserDto userDto){
        return new User(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getSavePassword(),
                (userDto.getCompanyId() == null) ? null : companyRepository.findById(userDto.getCompanyId()).orElse(null),
                permissionRepository.findAllById(userDto.getPermissionIds())
        );
    }

    public List<ReturnUserDto> convertAll(List<User> users){
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

}
