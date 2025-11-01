package com.grafyn.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForeignKeyMeta {
    private String fkName;
    private String pkTableName;
    private String pkColumnName;
    private String fkTableName;
    private String fkColumnName;
    // getters/setters
}

