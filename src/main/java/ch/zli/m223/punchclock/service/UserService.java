package ch.zli.m223.punchclock.service;

import ch.zli.m223.punchclock.domain.Company;
import ch.zli.m223.punchclock.domain.PermissionName;
import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.repository.EntryRepository;
import ch.zli.m223.punchclock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EntryRepository entryRepository;

    public User getByUsernameOrElseThrow(String username){
        return userRepository.findByUsername(username).orElseThrow();
    }
    public List<User> getByCompany(Company company){
        return userRepository.findAllByCompany(company);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User createUser(User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    public User updateUser(User oldUser, User newUser){
        oldUser.setUsername(newUser.getUsername());
        if(newUser.getPassword() != null && !newUser.getPassword().isBlank()){
            oldUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        }
        oldUser.setCompany(newUser.getCompany());
        oldUser.setPermissions(newUser.getPermissions());
        return userRepository.saveAndFlush(oldUser);
    }

    @Transactional
    public void deleteUser(User user){
        entryRepository.deleteAllByUser(user);
        userRepository.delete(user);
    }

    public Optional<User> getById(Long id){
        return userRepository.findById(id);
    }

    public boolean isAllowedToCreate(User newUser, User user){
        if(user.hasPermission(PermissionName.ADMINISTRATE)){
            if(newUser.hasPermission(PermissionName.SUPER_ADMINISTRATE)) return false;
            if(newUser.getCompany() != user.getCompany()) return false;
        }
        return true;
    }

    public boolean isAllowedToDelete(User oldUser, User user){
        if(oldUser.equals(user)) return false;
        if(user.hasPermission(PermissionName.ADMINISTRATE)){
            if(oldUser.hasPermission(PermissionName.ADMINISTRATE) || oldUser.hasPermission(PermissionName.SUPER_ADMINISTRATE)){
                return false;
            }
        }
        if(user.hasPermission(PermissionName.SUPER_ADMINISTRATE)){
            if(oldUser.hasPermission(PermissionName.SUPER_ADMINISTRATE)){
                return false;
            }
        }
        return true;
    }

    public boolean isAllowedToUpdate(User oldUser, User newUser, User user){
        if(user.hasPermission(PermissionName.ADMINISTRATE)){
            if(!Objects.equals(oldUser.getCompany(), user.getCompany())) return false;
            if(!Objects.equals(oldUser.getCompany(), newUser.getCompany())) return false;
            if(!Objects.equals(oldUser.getId(), user.getId())){
                if(oldUser.hasPermission(PermissionName.ADMINISTRATE) || oldUser.hasPermission(PermissionName.SUPER_ADMINISTRATE)) return false;
                if(newUser.hasPermission(PermissionName.SUPER_ADMINISTRATE)) return false;
            }else{
                return oldUser.getPermissions().containsAll(newUser.getPermissions());
            }
        }
        if(user.hasPermission(PermissionName.SUPER_ADMINISTRATE)){
            if(!Objects.equals(oldUser.getId(), user.getId())){
                if(oldUser.hasPermission(PermissionName.SUPER_ADMINISTRATE)) return false;
            }else {
                return oldUser.getPermissions().containsAll(newUser.getPermissions());
            }
        }
        return true;
    }

}
