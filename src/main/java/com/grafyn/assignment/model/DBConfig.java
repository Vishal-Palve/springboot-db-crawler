package com.grafyn.assignment.model;


import java.util.List;

public class DBConfig {
    private String jdbcUrl;
    private String username;
    private String password;
    private String schema;
    private List<String> includeTables;
    private List<String> excludeTables;
    private boolean generateModels;
    private String targetPackage;
    private String outputDir;
    private boolean generateSourceFiles;
    private boolean includeIndexes;


    public String getJdbcUrl() { return jdbcUrl; }
    public void setJdbcUrl(String jdbcUrl) { this.jdbcUrl = jdbcUrl; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSchema() { return schema; }
    public void setSchema(String schema) { this.schema = schema; }

    public List<String> getIncludeTables() { return includeTables; }
    public void setIncludeTables(List<String> includeTables) { this.includeTables = includeTables; }

    public List<String> getExcludeTables() { return excludeTables; }
    public void setExcludeTables(List<String> excludeTables) { this.excludeTables = excludeTables; }

    public boolean isGenerateModels() { return generateModels; }
    public void setGenerateModels(boolean generateModels) { this.generateModels = generateModels; }

    public String getTargetPackage() { return targetPackage; }
    public void setTargetPackage(String targetPackage) { this.targetPackage = targetPackage; }

    public String getOutputDir() { return outputDir; }
    public void setOutputDir(String outputDir) { this.outputDir = outputDir; }

    public boolean isGenerateSourceFiles() { return generateSourceFiles; }
    public void setGenerateSourceFiles(boolean generateSourceFiles) { this.generateSourceFiles = generateSourceFiles; }

    public boolean isIncludeIndexes() { return includeIndexes; }
    public void setIncludeIndexes(boolean includeIndexes) { this.includeIndexes = includeIndexes; }
}

