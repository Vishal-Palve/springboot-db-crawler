package com.grafyn.assignment;

import java.util.Locale;

public class JdbcToJavaTypeMapper {
    public static Class<?> map(String jdbcTypeName, int columnSize) {
        String t = jdbcTypeName.toUpperCase(Locale.ROOT);
        if (t.contains("CHAR") || t.contains("TEXT") || t.contains("VARCHAR")) return String.class;
        if (t.contains("INT")) return Integer.class;
        if (t.equals("BIGINT")) return Long.class;
        if (t.contains("DECIMAL") || t.contains("NUMERIC")) return java.math.BigDecimal.class;
        if (t.contains("DATE") && !t.contains("TIME")) return java.sql.Date.class;
        if (t.contains("TIME") && !t.contains("ZONE")) return java.sql.Time.class;
        if (t.contains("TIMESTAMP")) return java.sql.Timestamp.class;
        if (t.contains("BLOB") || t.contains("BINARY")) return byte[].class;
        if (t.contains("BOOLEAN") || t.equals("TINYINT(1)")) return Boolean.class;
        // fallback
        return Object.class;
    }
}

