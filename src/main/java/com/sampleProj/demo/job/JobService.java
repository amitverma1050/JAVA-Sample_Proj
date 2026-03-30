package com.sampleProj.demo.job;

import java.util.List;

public interface JobService {
    List<Job> findAll();

    void AddJob(Job job);

    Job GetJobById(Long id);

    boolean DeleteJobById(Long id);

    boolean updateJob(long id, Job updatedJob);
}
