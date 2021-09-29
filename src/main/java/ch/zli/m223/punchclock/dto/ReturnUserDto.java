package ch.zli.m223.punchclock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReturnUserDto {

    private Long id;
    private String username;
    private List<PermissionDto> permissions;
    private CompanyDto company;

}
