package ch.zli.m223.punchclock.controller;

import ch.zli.m223.punchclock.converter.UserDtoConverter;
import ch.zli.m223.punchclock.domain.PermissionName;
import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.dto.ReturnUserDto;
import ch.zli.m223.punchclock.dto.SaveUserDto;
import ch.zli.m223.punchclock.service.UserService;
import ch.zli.m223.punchclock.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserDtoConverter userDtoConverter;
    private final UserValidator userValidator;

    @InitBinder("saveUserDto")
    public void setSaveUserDtoBinder(WebDataBinder binder){binder.setValidator(userValidator);}

    /* This endpoint allows user with the ADMINISTRATE or SUPER_ADMINISTRATE
    permission to get all users which they are allowed to access */
    @PreAuthorize("hasAnyAuthority('ADMINISTRATE', 'SUPER_ADMINISTRATE')")
    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public List<ReturnUserDto> getUsers(Principal principal){
        User user = userService.getByUsernameOrElseThrow(principal.getName());
        if(user.hasPermission(PermissionName.ADMINISTRATE)){
            if(user.getCompany() != null){
                return userDtoConverter.convertAll(userService.getByCompany(user.getCompany()));
            }
            return List.of(userDtoConverter.toDto(user));
        }else if(user.hasPermission(PermissionName.SUPER_ADMINISTRATE)){
            return userDtoConverter.convertAll(userService.getAll());
        }
        return new ArrayList<>();
    }

    /* This endpoint allows user with the ADMINISTRATE or SUPER_ADMINISTRATE
    permission to create users which don't have higher permissions than themselves.
     Administrators can also not create users outside their own company */
    @PreAuthorize("hasAnyAuthority('ADMINISTRATE', 'SUPER_ADMINISTRATE')")
    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@Valid @RequestBody SaveUserDto saveUserDto, Principal principal){
        User user = userService.getByUsernameOrElseThrow(principal.getName());
        User newUser = userDtoConverter.toEntity(saveUserDto);
        if((saveUserDto.getId() == null || saveUserDto.getId() == 0) && userService.isAllowedToCreate(newUser, user)){
            return new ResponseEntity<>(userDtoConverter.toDto(userService.createUser(newUser)), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /* This endpoint allows user with the ADMINISTRATE or SUPER_ADMINISTRATE
    permission to edit users which don't have higher or the same permissions as themselves.
     Administrators can also not edit users outside their own company */
    @PreAuthorize("hasAnyAuthority('ADMINISTRATE', 'SUPER_ADMINISTRATE')")
    @RequestMapping(value = "/api/user", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@Valid @RequestBody SaveUserDto saveUserDto, Principal principal){
        User user = userService.getByUsernameOrElseThrow(principal.getName());
        User newUser = userDtoConverter.toEntity(saveUserDto);
        Optional<User> oldUser = userService.getById(saveUserDto.getId());
        if(oldUser.isPresent() && userService.isAllowedToUpdate(oldUser.get(), newUser, user)){
            return new ResponseEntity<>(userDtoConverter.toDto(userService.updateUser(oldUser.get(), newUser)), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /* This endpoint allows user with the ADMINISTRATE or SUPER_ADMINISTRATE
    permission to delete other users which don't have higher or the same permissions as themselves.
     Administrators can also not delete users outside their own company */
    @PreAuthorize("hasAnyAuthority('ADMINISTRATE', 'SUPER_ADMINISTRATE')")
    @RequestMapping(value = "/api/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id, Principal principal){
        User user = userService.getByUsernameOrElseThrow(principal.getName());
        Optional<User> oldUser = userService.getById(id);
        if(oldUser.isPresent() && userService.isAllowedToDelete(oldUser.get(), user)){
            userService.deleteUser(oldUser.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
