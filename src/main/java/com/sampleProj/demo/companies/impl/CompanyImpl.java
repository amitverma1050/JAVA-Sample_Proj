package com.sampleProj.demo.companies.impl;

import com.sampleProj.demo.companies.Company;
import com.sampleProj.demo.companies.CompanyRepository;
import com.sampleProj.demo.companies.CompanyService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public boolean addCompany(Company company) {
        try{
            companyRepository.save(company);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public Company findById(Long id){
        return companyRepository.findById(id).orElse(null);
    }

    public boolean deleteCompany(Long id){
//        if(companyRepository.existsById(id)){
//            companyRepository.deleteById(id);
//            return true;
//        }
//        return false;
        try{
            companyRepository.deleteById(id);  // Deletes if exists
            return true;
        }
        catch(EmptyResultDataAccessException e){  // Throws if not found
            return false;
        }
    }

    public boolean updateCompany(Long id, Company company){
        Optional<Company> companyOptional = companyRepository.findById(id);
        if(companyOptional.isPresent()){
            Company existingCompany = companyOptional.get();
            existingCompany.setName(company.getName());
            existingCompany.setAddress(company.getAddress());
            existingCompany.setEmail(company.getEmail());
            existingCompany.setPhone(company.getPhone());
            companyRepository.save(existingCompany);
            return true;
        }
        return false;
    }
}
