package com.example.rqchallenge.employees.model;

public class EmployeeRequest {

    private final String id;
    private final String name;
    private final String salary;
    private final String age;

    private EmployeeRequest(String id, String name, String salary, String age) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSalary() {
        return salary;
    }

    public String getAge() {
        return age;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String salary;
        private String age;

        private Builder() {
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSalary(String salary) {
            this.salary = salary;
            return this;
        }

        public Builder setAge(String age) {
            this.age = age;
            return this;
        }

        public EmployeeRequest build() {
            return new EmployeeRequest(id, name, salary, age);
        }
    }
}