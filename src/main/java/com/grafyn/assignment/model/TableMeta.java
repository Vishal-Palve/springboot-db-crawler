package com.grafyn.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableMeta {
    private String tableName;
    private Map<String, ColumnMeta> columns = new LinkedHashMap<>();
    private List<String> primaryKeys = new ArrayList<>();
    private List<ForeignKeyMeta> foreignKeys = new ArrayList<>();
    private List<IndexMeta> indexes = new ArrayList<>();
    // getters/setters
}
