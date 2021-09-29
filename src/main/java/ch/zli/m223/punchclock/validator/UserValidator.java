package ch.zli.m223.punchclock.validator;

import ch.zli.m223.punchclock.domain.User;
import ch.zli.m223.punchclock.dto.SaveUserDto;
import ch.zli.m223.punchclock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SaveUserDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SaveUserDto dto = (SaveUserDto) target;

        if(dto.getId() != null && dto.getId() != 0){
            Optional<User> oldUser = userRepository.findById(dto.getId());
            if(oldUser.isEmpty()){
                errors.rejectValue("id", "errors.user.id.invalid");
            }
        }else{
            if (dto.getSavePassword() == null || dto.getSavePassword().isBlank()){
                errors.rejectValue("password", "errors.user.password.blank");
            }
        }

        if(dto.getUsername() != null && !dto.getUsername().isBlank()){
            Optional<User> userWithSameName = userRepository.findByUsername(dto.getUsername());
            if(userWithSameName.isPresent() && !Objects.equals(userWithSameName.get().getId(), dto.getId())){
                errors.rejectValue("username", "errors.user.username.invalid");
            }
        }else if(dto.getUsername() == null || dto.getUsername().isBlank()){
            errors.rejectValue("username", "errors.user.username.blank");
        }

    }
}
