package ch.zli.m223.punchclock.service;

import ch.zli.m223.punchclock.domain.Company;
import ch.zli.m223.punchclock.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Company createCompany(Company company){
        return companyRepository.saveAndFlush(company);
    }

    public Company updateCompany(Company oldCompany, Company newCompany){
        oldCompany.setName(newCompany.getName());
        return companyRepository.saveAndFlush(oldCompany);
    }

    public void deleteCompany(Company company){
        companyRepository.delete(company);
    }

    public List<Company> getCompanies(){
        return companyRepository.findAll();
    }

    public Optional<Company> getById(Long id){
        return companyRepository.findById(id);
    }

}
