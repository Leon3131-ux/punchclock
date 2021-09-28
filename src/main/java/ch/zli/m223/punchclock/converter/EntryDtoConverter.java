package ch.zli.m223.punchclock.converter;

import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.dto.EntryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EntryDtoConverter {

    private final UserDtoConverter userDtoConverter;

    public EntryDto toDto(Entry entry){
        return new EntryDto(
                entry.getId(),
                entry.getCheckIn(),
                entry.getCheckOut(),
                userDtoConverter.toDto(entry.getUser())
        );
    }

    public Entry toEntity(EntryDto entryDto, User user){
        return new Entry(
                entryDto.getId(),
                entryDto.getCheckIn(),
                entryDto.getCheckOut(),
                user
        );
    }

    public List<EntryDto> convertAll(List<Entry> entries){
        return entries.stream().map(this::toDto).collect(Collectors.toList());
    }

}
