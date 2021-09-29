package ch.zli.m223.punchclock.validator;

import ch.zli.m223.punchclock.domain.Entry;
import ch.zli.m223.punchclock.dto.EntryDto;
import ch.zli.m223.punchclock.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EntryValidator implements Validator {

    private final EntryRepository entryRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(EntryDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EntryDto dto = (EntryDto) target;

        if(dto.getId() != null && dto.getId() != 0){
            Optional<Entry> oldEntry = entryRepository.findById(dto.getId());
            if(oldEntry.isEmpty()){
                errors.rejectValue("id", "errors.entry.id.invalid");
            }
        }

        if(dto.getCheckIn() != null && dto.getCheckOut() != null){
            if(dto.getCheckIn().isAfter(dto.getCheckOut())){
                errors.rejectValue("checkIn", "errors.entry.checkIn.invalid");
            }else if(dto.getCheckOut().isBefore(dto.getCheckIn())){
                errors.rejectValue("checkOut", "errors.entry.checkOut.invalid");
            }
        }else if(dto.getCheckIn() == null && dto.getCheckOut() == null){
            errors.rejectValue("checkOut", "errors.entry.checkOut.invalid");
            errors.rejectValue("checkIn", "errors.entry.checkIn.invalid");
        }
    }
}
