package com.example.rqchallenge.employees.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Employee {

    @Id
    @JsonProperty("id")
    private final String id;
    @JsonProperty("employee_name")
    private final String name;
    @JsonProperty("employee_salary")
    private final long salary;
    @JsonProperty("employee_age")
    private final int age;
    @JsonProperty("profile_image")
    private final String profileImage;

    // Private no-argument constructor for JPA
    public Employee() {
        this.id = "";
        this.name = "";
        this.salary = 0;
        this.age = 0;
        this.profileImage = "";
    }

    public Employee(String id, String name, long salary, int age, String profileImage) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.profileImage = profileImage;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getSalary() {
        return salary;
    }

    public int getAge() {
        return age;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private Long salary;
        private Integer age;
        private String profileImage;

        private Builder() {
        }

        public Builder setId(String id) {
            Objects.requireNonNull(id, "Employee Id cannot be null");
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            Objects.requireNonNull(name, "Employee Name cannot be null");
            this.name = name;
            return this;
        }

        public Builder setSalary(Long salary) {
            Objects.requireNonNull(salary, "Employee Salary cannot be null");
            this.salary = salary;
            return this;
        }

        public Builder setAge(Integer age) {
            Objects.requireNonNull(age, "Employee Age cannot be null");
            this.age = age;
            return this;
        }

        public Builder setProfileImage(String profileImage) {
            this.profileImage = profileImage == null ? "" : profileImage;
            return this;
        }

        public Employee build() {
            return new Employee(id, name, salary, age, profileImage);
        }
    }

}
