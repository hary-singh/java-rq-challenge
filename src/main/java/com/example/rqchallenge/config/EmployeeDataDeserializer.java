package com.example.rqchallenge.config;

import com.example.rqchallenge.employees.model.Employee;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDataDeserializer extends JsonDeserializer<List<Employee>> {
    @Override
    public List<Employee> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = mapper.readTree(jsonParser);
        List<Employee> employees = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode jsonNode : node) {
                employees.add(mapper.treeToValue(jsonNode, Employee.class));
            }
        } else {
            employees.add(mapper.treeToValue(node, Employee.class));
        }

        return employees;
    }
}
