package com.sampleProj.demo.companies;
import java.util.List;

public interface CompanyService {
    boolean addCompany(Company company);
    List<Company> findAllCompanies();
    Company findById(Long id);
    boolean deleteCompany(Long id);
    boolean updateCompany(Long id, Company company);
}
