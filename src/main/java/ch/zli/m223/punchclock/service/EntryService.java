package ch.zli.m223.punchclock.service;

import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.domain.PermissionName;
import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.repository.EntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntryService {
    private EntryRepository entryRepository;

    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public Entry createEntry(Entry entry) {
        return entryRepository.saveAndFlush(entry);
    }

    public Entry updateEntry(Entry oldEntry, Entry newEntry){
        oldEntry.setCheckIn(newEntry.getCheckIn());
        oldEntry.setCheckOut(newEntry.getCheckOut());
        return entryRepository.saveAndFlush(oldEntry);
    }

    public void deleteEntry(Entry entry){
        entryRepository.delete(entry);
    }

    public List<Entry> getAll() {
        return entryRepository.findAll();
    }

    public List<Entry> getAllByUser(User user){
        return entryRepository.findAllByUserOrderById(user);
    }

    public Optional<Entry> getById(Long id){
        return entryRepository.findById(id);
    }

    public boolean isAllowedToManage(Entry entry, User user){
        if(entry.getUser().equals(user)) return true;
        return user.hasPermission(PermissionName.ADMINISTRATE)
                || user.hasPermission(PermissionName.SUPER_ADMINISTRATE);
    }

}

