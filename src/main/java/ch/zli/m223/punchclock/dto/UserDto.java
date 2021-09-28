package ch.zli.m223.punchclock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String savePassword;
    private List<PermissionDto> permissions;

}
