package ch.zli.m223.punchclock.controller;

import ch.zli.m223.punchclock.converter.CompanyDtoConverter;
import ch.zli.m223.punchclock.domain.Company;
import ch.zli.m223.punchclock.dto.CompanyDto;
import ch.zli.m223.punchclock.service.CompanyService;
import ch.zli.m223.punchclock.validator.CompanyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyDtoConverter companyDtoConverter;
    private final CompanyValidator companyValidator;

    @InitBinder("companyDto")
    public void setCompanyDtoBinder(WebDataBinder binder){binder.setValidator(companyValidator);}

    @PreAuthorize("hasAnyAuthority('SUPER_ADMINISTRATE')")
    @RequestMapping(value = "/api/companies", method = RequestMethod.GET)
    public List<CompanyDto> getCompanies(){
        return companyDtoConverter.convertAll(companyService.getCompanies());
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMINISTRATE')")
    @RequestMapping(value = "/api/company", method = RequestMethod.POST)
    public ResponseEntity<?> createCompany(@Valid @RequestBody CompanyDto companyDto){
        if(companyDto.getId() == null || companyDto.getId() == 0){
            Company company = companyDtoConverter.toEntity(companyDto);
            return new ResponseEntity<>(companyDtoConverter.toDto(companyService.createCompany(company)), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMINISTRATE')")
    @RequestMapping(value = "/api/company", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCompany(@Valid @RequestBody CompanyDto companyDto){
        Optional<Company> oldCompany = companyService.getById(companyDto.getId());
        if(oldCompany.isPresent()){
            Company company = companyDtoConverter.toEntity(companyDto);
            return new ResponseEntity<>(companyDtoConverter.toDto(companyService.updateCompany(oldCompany.get(), company)), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMINISTRATE')")
    @RequestMapping(value = "/api/company/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCompany(@PathVariable("id") Long id){
        Optional<Company> oldCompany = companyService.getById(id);
        if(oldCompany.isPresent()){
            companyService.deleteCompany(oldCompany.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
