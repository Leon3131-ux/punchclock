package ch.zli.m223.punchclock.validator;

import ch.zli.m223.punchclock.domain.Company;
import ch.zli.m223.punchclock.dto.CompanyDto;
import ch.zli.m223.punchclock.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyValidator implements Validator {

    private final CompanyRepository companyRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CompanyDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CompanyDto dto = (CompanyDto) target;

        if(dto.getId() != null && dto.getId() != 0){
            Optional<Company> oldCompany = companyRepository.findById(dto.getId());
            if(oldCompany.isEmpty()){
                errors.rejectValue("id", "errors.company.id.invalid");
            }
        }

        if(dto.getName() == null || dto.getName().isBlank()){
            errors.rejectValue("name", "errors.company.name.empty");
        }
    }
}
