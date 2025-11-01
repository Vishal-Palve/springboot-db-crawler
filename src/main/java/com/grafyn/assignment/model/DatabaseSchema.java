package com.grafyn.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseSchema {
    private String schemaName;
    private Map<String, TableMeta> tables = new LinkedHashMap<>();
    // getters/setters
}
