package ch.zli.m223.punchclock.controller;

import ch.zli.m223.punchclock.converter.PermissionDtoConverter;
import ch.zli.m223.punchclock.dto.PermissionDto;
import ch.zli.m223.punchclock.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
    private final PermissionDtoConverter permissionDtoConverter;

    /* This endpoint allows users with the ADMINISTRATE or SUPER_ADMINISTRATE
    permission, to get all available permissions.
     */
    @PreAuthorize("hasAnyAuthority('ADMINISTRATE', 'SUPER_ADMINISTRATE')")
    @RequestMapping(value = "/api/permissions", method = RequestMethod.GET)
    public List<PermissionDto> getPermissions(){
        return permissionDtoConverter.convertAll(permissionService.getAll());
    }

}
