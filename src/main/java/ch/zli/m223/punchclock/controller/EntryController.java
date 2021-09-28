package ch.zli.m223.punchclock.controller;

import ch.zli.m223.punchclock.converter.EntryDtoConverter;
import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.dto.EntryDto;
import ch.zli.m223.punchclock.service.EntryService;
import ch.zli.m223.punchclock.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/api/entries")
    public List<EntryDto> getAllEntries() {
        return entryDtoConverter.convertAll(entryService.findAll());
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

    @RequestMapping(value = "/entry/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEntry(@PathVariable("id") Long id){
        Optional<Entry> oldEntry = entryService.findById(id);
        if(oldEntry.isPresent()){
            entryService.deleteEntry(oldEntry.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
