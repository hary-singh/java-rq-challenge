package com.example.rqchallenge.employees.model;

import com.example.rqchallenge.config.EmployeeDataDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class EmployeeResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    @JsonDeserialize(using = EmployeeDataDeserializer.class)
    private List<Employee> data;

    @JsonProperty("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Employee> getData() {
        return data;
    }

    public void setData(List<Employee> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
