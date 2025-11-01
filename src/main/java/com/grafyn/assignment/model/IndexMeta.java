package com.grafyn.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexMeta {
    private String indexName;
    private boolean nonUnique;
    private String columnName;
    private Integer ordinalPosition;
}

