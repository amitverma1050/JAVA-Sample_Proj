package com.sampleProj.demo.companies;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sampleProj.demo.job.Job;
import com.sampleProj.demo.review.Review;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    private String name;
    private String address;
    private String email;
    private String phone;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    private List<Job> jobs;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    public Company(Long id, List<Job> jobs, String phone, String email, String name, String address) {
        this.id = id;
        this.jobs = jobs;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.address = address;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public Company() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
