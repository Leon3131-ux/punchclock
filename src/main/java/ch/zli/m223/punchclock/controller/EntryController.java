package ch.zli.m223.punchclock.controller;

import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.service.EntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class EntryController {
    private EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @GetMapping("/entries")
    @ResponseStatus(HttpStatus.OK)
    public List<Entry> getAllEntries() {
        return entryService.findAll();
    }

    @PostMapping("/entry")
    @ResponseStatus(HttpStatus.CREATED)
    public Entry createEntry(@Valid @RequestBody Entry entry) {
        return entryService.createEntry(entry);
    }

    @RequestMapping(value = "/entry", method = RequestMethod.PUT)
    public ResponseEntity<?> updateEntry(@Valid @RequestBody Entry entry){
        Optional<Entry> oldEntry = entryService.findById(entry.getId());
        if(oldEntry.isPresent()){
            return new ResponseEntity<>(entryService.updateEntry(oldEntry.get(), entry), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
