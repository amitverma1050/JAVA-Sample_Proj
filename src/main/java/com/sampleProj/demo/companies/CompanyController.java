package com.sampleProj.demo.companies;
import com.sampleProj.demo.job.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService, JobService jobService) {
        this.companyService = companyService;
    }

    @GetMapping
    public List<Company> findAllCompanies() {
        return companyService.findAllCompanies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> findCompany(@PathVariable Long id) {
        Company company = companyService.findById(id);
        if(company == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addCompany(@RequestBody Company company) {

        boolean isAdded = companyService.addCompany(company);
        if(isAdded) {
            return new ResponseEntity<>("Company added successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Company could not be added", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        boolean isDeleted = companyService.deleteCompany(id);
        if(isDeleted) {
            return new ResponseEntity<>("Company deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Company not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        boolean isUpdated = companyService.updateCompany(id, company);
        if(isUpdated) {
            return new ResponseEntity<>("Company updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Company could not be updated", HttpStatus.BAD_REQUEST);
    }
}
