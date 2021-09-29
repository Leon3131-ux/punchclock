package ch.zli.m223.punchclock.service;

import ch.zli.m223.punchclock.domain.Permission;
import ch.zli.m223.punchclock.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public List<Permission> getAll(){
        return this.permissionRepository.findAll();
    }

}
