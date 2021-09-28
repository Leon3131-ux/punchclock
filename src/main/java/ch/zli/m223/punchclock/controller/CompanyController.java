package ch.zli.m223.punchclock.controller;

import ch.zli.m223.punchclock.converter.CompanyDtoConverter;
import ch.zli.m223.punchclock.dto.CompanyDto;
import ch.zli.m223.punchclock.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyDtoConverter companyDtoConverter;

    @PreAuthorize("hasAnyAuthority('ADMINISTRATE', 'SUPER_ADMINISTRATE')")
    @RequestMapping(value = "/api/companies", method = RequestMethod.GET)
    public List<CompanyDto> getCompanies(){
        return companyDtoConverter.convertAll(companyService.getCompanies());
    }

}
