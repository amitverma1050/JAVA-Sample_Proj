package com.sampleProj.demo.job;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;
    
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    @GetMapping
    public ResponseEntity<List<Job>> findAll(){
        return new ResponseEntity<>(jobService.findAll(),  HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> GetJobById(@PathVariable long id){
        Job job = jobService.GetJobById(id);
        if(job != null)
            return ResponseEntity.ok(job);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<String> AddJob(@RequestBody Job job){
        jobService.AddJob(job);
        return new ResponseEntity<>("Job Added Successfully",  HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> DeleteJobById(@PathVariable long id){
        boolean result = jobService.DeleteJobById(id);
        if(result)
            return new ResponseEntity<>("Job Deleted Successfully",  HttpStatus.OK);
        return new ResponseEntity<>("Job Not Found",  HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> UpdateJob(@RequestBody Job updatedJob, @PathVariable long id){
        boolean isUpdated = jobService.updateJob(id, updatedJob);
        if(isUpdated)
            return new ResponseEntity<>("Job Updated Successfully",  HttpStatus.OK);
        return new ResponseEntity<>("Job Not Found",  HttpStatus.NOT_FOUND);
    }
}
