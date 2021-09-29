package ch.zli.m223.punchclock.converter;

import ch.zli.m223.punchclock.domain.Company;
import ch.zli.m223.punchclock.dto.CompanyDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CompanyDtoConverter {

    public CompanyDto toDto(Company company){
        if(company != null){
            return new CompanyDto(
                    company.getId(),
                    company.getName()
            );
        }
        return null;

    }

    public Company toEntity(CompanyDto companyDto){
        return new Company(
                companyDto.getId(),
                companyDto.getName(),
                new ArrayList<>()
        );
    }

    public List<CompanyDto> convertAll(List<Company> companies){
        return companies.stream().map(this::toDto).collect(Collectors.toList());
    }

}
