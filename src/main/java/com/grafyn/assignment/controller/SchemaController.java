package com.grafyn.assignment.controller;

import com.grafyn.assignment.model.DatabaseSchema;
import com.grafyn.assignment.model.TableMeta;
import com.grafyn.assignment.service.ConfigService;
import com.grafyn.assignment.service.DBMetadataService;
import com.grafyn.assignment.service.ModelGeneratorByteBuddy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schema")
public class SchemaController {

    private final DBMetadataService dbMetadataService;
    private final ModelGeneratorByteBuddy modelGenerator;
    private final ConfigService configService;

    private DatabaseSchema cachedSchema;
    private Map<String, Class<?>> generatedClasses = new HashMap<>();

    public SchemaController(DBMetadataService dbMetadataService, ModelGeneratorByteBuddy modelGenerator, ConfigService cs) {
        this.dbMetadataService = dbMetadataService;
        this.modelGenerator = modelGenerator;
        this.configService = cs;
    }

    @GetMapping("/")
    public ResponseEntity<DatabaseSchema> getSchema() throws SQLException {


        if (cachedSchema == null) {
            cachedSchema = dbMetadataService.crawlSchema(configService.getConfig());
        }
        return ResponseEntity.ok(cachedSchema);
    }

    @GetMapping("/tables")
    public ResponseEntity<List<String>> listTables() throws SQLException {
        DatabaseSchema schema = cachedSchema != null ? cachedSchema : dbMetadataService.crawlSchema(configService.getConfig());
        return ResponseEntity.ok(new ArrayList<>(schema.getTables().keySet()));
    }

    @GetMapping("/tables/{table}")
    public ResponseEntity<TableMeta> getTable(@PathVariable String table) throws SQLException {
        DatabaseSchema schema = cachedSchema != null ? cachedSchema : dbMetadataService.crawlSchema(configService.getConfig());
        TableMeta t = schema.getTables().get(table);
        return t == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(t);
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String,Object>> generateModels(@RequestParam(required = false) boolean force) throws SQLException {
        if (cachedSchema == null) cachedSchema = dbMetadataService.crawlSchema(configService.getConfig());
        if (generatedClasses.isEmpty() || force) {
            generatedClasses = modelGenerator.generate(cachedSchema, configService.getConfig().getTargetPackage());
        }
        Map<String,Object> result = new HashMap<>();
        result.put("generatedCount", generatedClasses.size());
        result.put("classNames", generatedClasses.values().stream().map(Class::getName).collect(Collectors.toList()));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/model/{table}")
    public ResponseEntity<Map<String,Object>> modelInfo(@PathVariable String table) {
        Class<?> cls = generatedClasses.get(table);
        if (cls == null) return ResponseEntity.notFound().build();
        Map<String,Object> map = new HashMap<>();
        map.put("className", cls.getName());
        map.put("fields", Arrays.stream(cls.getDeclaredFields())
                .map(f -> Map.of("name", f.getName(), "type", f.getType().getName()))
                .collect(Collectors.toList()));
        return ResponseEntity.ok(map);
    }

    @GetMapping("/models")
    public ResponseEntity<List<String>> listAllModels() {
        if (generatedClasses.isEmpty()) {
            return ResponseEntity.ok(List.of("No models generated yet"));
        }
        List<String> modelNames = generatedClasses.keySet().stream().toList();
        return ResponseEntity.ok(modelNames);
    }

}

