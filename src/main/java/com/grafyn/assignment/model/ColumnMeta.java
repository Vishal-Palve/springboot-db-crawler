package com.grafyn.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnMeta {
    private String name;
    private String jdbcType;
    private Integer size;
    private boolean nullable;
    private boolean autoIncrement;
    private String defaultValue;
    // getters/setters/constructors
}

