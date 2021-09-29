package ch.zli.m223.punchclock.repository;

import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    List<Entry> findAllByUserOrderById(User user);

    void deleteAllByUser(User user);
}
