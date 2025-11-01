package com.grafyn.assignment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grafyn.assignment.model.*;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class DBMetadataService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public DatabaseSchema crawlSchema(DBConfig config) throws SQLException {
        DatabaseSchema dbSchema = new DatabaseSchema();
        dbSchema.setSchemaName(config.getSchema());
        try (Connection conn = DriverManager.getConnection(config.getJdbcUrl(), config.getUsername(), config.getPassword())) {
            DatabaseMetaData md = conn.getMetaData();

            // 1. fetch tables
            try (ResultSet tablesRs = md.getTables(conn.getCatalog(), config.getSchema(), "%", new String[]{"TABLE"})) {
                while (tablesRs.next()) {
                    String tableName = tablesRs.getString("TABLE_NAME");
                    if (shouldSkip(tableName, config)) continue;
                    TableMeta t = new TableMeta();
                    t.setTableName(tableName);
                    dbSchema.getTables().put(tableName, t);
                }
            }

            // 2. fetch columns for each table
            for (TableMeta tableMeta : dbSchema.getTables().values()) {
                try (ResultSet cols = md.getColumns(conn.getCatalog(), config.getSchema(), tableMeta.getTableName(), "%")) {
                    while (cols.next()) {
                        ColumnMeta cm = new ColumnMeta();
                        cm.setName(cols.getString("COLUMN_NAME"));
                        cm.setJdbcType(cols.getString("TYPE_NAME"));
                        cm.setSize(cols.getInt("COLUMN_SIZE"));
                        cm.setNullable(DatabaseMetaData.columnNullable == cols.getInt("NULLABLE"));
                        cm.setAutoIncrement("YES".equalsIgnoreCase(cols.getString("IS_AUTOINCREMENT")));
                        cm.setDefaultValue(cols.getString("COLUMN_DEF"));
                        tableMeta.getColumns().put(cm.getName(), cm);
                    }
                }
            }

            // 3. primary keys
            for (TableMeta tableMeta : dbSchema.getTables().values()) {
                try (ResultSet pkRs = md.getPrimaryKeys(conn.getCatalog(), config.getSchema(), tableMeta.getTableName())) {
                    while (pkRs.next()) {
                        tableMeta.getPrimaryKeys().add(pkRs.getString("COLUMN_NAME"));
                    }
                }
            }

            // 4. foreign keys
            for (TableMeta tableMeta : dbSchema.getTables().values()) {
                try (ResultSet fkRs = md.getImportedKeys(conn.getCatalog(), config.getSchema(), tableMeta.getTableName())) {
                    while (fkRs.next()) {
                        ForeignKeyMeta fk = new ForeignKeyMeta();
                        fk.setFkName(fkRs.getString("FK_NAME"));
                        fk.setPkTableName(fkRs.getString("PKTABLE_NAME"));
                        fk.setPkColumnName(fkRs.getString("PKCOLUMN_NAME"));
                        fk.setFkTableName(fkRs.getString("FKTABLE_NAME"));
                        fk.setFkColumnName(fkRs.getString("FKCOLUMN_NAME"));
                        tableMeta.getForeignKeys().add(fk);
                    }
                }
            }

            // 5. indexes (optional)
            if (config.isIncludeIndexes()) {
                for (TableMeta tableMeta : dbSchema.getTables().values()) {
                    try (ResultSet idxRs = md.getIndexInfo(conn.getCatalog(), config.getSchema(), tableMeta.getTableName(), false, false)) {
                        while (idxRs.next()) {
                            IndexMeta im = new IndexMeta();
                            im.setIndexName(idxRs.getString("INDEX_NAME"));
                            im.setNonUnique(idxRs.getBoolean("NON_UNIQUE"));
                            im.setColumnName(idxRs.getString("COLUMN_NAME"));
                            im.setOrdinalPosition(idxRs.getInt("ORDINAL_POSITION"));
                            tableMeta.getIndexes().add(im);
                        }
                    }
                }
            }
        }
        return dbSchema;
    }

    private boolean shouldSkip(String tableName, DBConfig config) {
        return false;
    }
}

