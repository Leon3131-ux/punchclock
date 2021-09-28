package ch.zli.m223.punchclock.controller;

import ch.zli.m223.punchclock.converter.EntryDtoConverter;
import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.domain.PermissionName;
import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.dto.EntryDto;
import ch.zli.m223.punchclock.service.EntryService;
import ch.zli.m223.punchclock.service.UserService;
import ch.zli.m223.punchclock.validator.EntryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;
    private final EntryDtoConverter entryDtoConverter;
    private final UserService userService;
    private final EntryValidator entryValidator;

    @InitBinder("entryDto")
    public void setEntryDtoBinder(WebDataBinder binder){binder.setValidator(entryValidator);}


    @GetMapping("/api/entries")
    public List<EntryDto> getAllEntries(Principal principal) {
        User user = userService.getByUsernameOrElseThrow(principal.getName());
        if(user.hasPermission(PermissionName.ADMINISTRATE) || user.hasPermission(PermissionName.SUPER_ADMINISTRATE)){
            return entryDtoConverter.convertAll(entryService.getAll());
        }else {
            return entryDtoConverter.convertAll(entryService.getAllByUser(user));
        }

    }

    @RequestMapping(value = "/api/entry", method = RequestMethod.POST)
    public ResponseEntity<?> createEntry(@Valid @RequestBody EntryDto entryDto, Principal principal){
        User user = userService.getByUsernameOrElseThrow(principal.getName());
        if(entryDto.getId() == null || entryDto.getId() == 0){
            Entry entry = entryDtoConverter.toEntity(entryDto, user);
            return new ResponseEntity<>(entryDtoConverter.toDto(entryService.createEntry(entry)), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/api/entry", method = RequestMethod.PUT)
    public ResponseEntity<?> updateEntry(@Valid @RequestBody EntryDto entryDto, Principal principal){
        User user = userService.getByUsernameOrElseThrow(principal.getName());
        if(entryDto.getId() != null && entryDto.getId() != 0){
            Entry entry = entryDtoConverter.toEntity(entryDto, user);
            Optional<Entry> oldEntry = entryService.getById(entryDto.getId());
            if(oldEntry.isPresent() && entryService.isAllowedToManage(oldEntry.get(), user)){
                return new ResponseEntity<>(entryDtoConverter.toDto(entryService.updateEntry(oldEntry.get(), entry)), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/api/entry/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEntry(@PathVariable("id") Long id, Principal principal){
        User user = userService.getByUsernameOrElseThrow(principal.getName());
        Optional<Entry> oldEntry = entryService.getById(id);
        if(oldEntry.isPresent() && entryService.isAllowedToManage(oldEntry.get(), user)){
            entryService.deleteEntry(oldEntry.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
