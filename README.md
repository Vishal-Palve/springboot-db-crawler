# MySQL Database Crawler & Model Generator (Java + Spring Boot)

## Overview
This project is a **Database Crawler** built with **Java and Spring Boot** that connects to a MySQL database, extracts its schema metadata (tables, columns, keys, relationships), and **automatically generates Java model classes** dynamically at runtime using **ByteBuddy**.

It provides **REST APIs** to:
- Extract database schema metadata  
- Generate Java model classes programmatically  

---

## Features

- Connects to any MySQL database via JDBC  
- Crawls database schema (tables, columns, keys, indexes, relationships)  
- Dynamically generates Java model classes at runtime  
- Provides REST APIs for metadata extraction and model generation  
- Supports easy configuration using a JSON file  
- Modular architecture (Controller, Service, Model layers)  

---

## Technologies Used

| Component | Technology |
|-----------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Database | MySQL |
| ORM / Runtime Generation | ByteBuddy |
| API Tool | Postman |
| Build Tool | Maven |

---

## Project Structure

```
src/
├── main/
│   ├── java/com/grafyn/assignment/
│   │   ├── controller/          # REST Controllers
│   │   ├── service/              # Crawler & Model generation logic
│   │   ├── model/                # Schema representation models
│   │   └── Application.java      # Spring Boot main class
│   └── resources/
│       ├── application.properties
│       └── crawler-config.json   # JSON configuration for DB connection
└── test/                         # (Optional) Unit tests
```

---

## Configuration

Edit the file: `src/main/resources/crawler-config.json`

```json
{
  "host": "localhost",
  "port": 3306,
  "username": "root",
  "password": "yourpassword",
  "schema": "mydb"
}
```

This file tells the crawler which database to connect to. You can switch databases simply by changing these values — no code changes needed.

---

## REST API Endpoints

### 1. Extract Schema Metadata

**Endpoint:**
```
GET /api/schema/
```

**Description:**  
Fetches all database metadata such as tables, columns, primary keys, foreign keys, and indexes.

**Sample Response:**
```json
{
  "schemaName": "first",
  "tables": {
    "stud": {
      "columns": {
        "id": {"jdbcType": "INT", "nullable": false},
        "name": {"jdbcType": "VARCHAR", "nullable": true},
        "age": {"jdbcType": "INT", "nullable": false}
      },
      "primaryKeys": ["id"],
      "foreignKeys": [],
      "indexes": [{"indexName": "PRIMARY", "columnName": "id"}]
    }
  }
}
```

### 2. Generate Java Models

**Endpoint:**
```
POST /api/schema/generate
```

**Description:**  
Takes the JSON output from `/api/schema/` and generates corresponding Java classes dynamically at runtime.

**Sample Response:**
```json
{
  "generatedCount": 1,
  "classNames": ["com.example.models.Stud"]
}
```

---

## How It Works (Architecture)

1. The crawler connects to the MySQL database using JDBC.
2. It extracts metadata via DatabaseMetaData API (tables, columns, keys, etc.).
3. The metadata is stored in an internal schema object.
4. On calling `/api/schema/generate`, the metadata is passed to ByteBuddy, which generates corresponding Java model classes dynamically.
5. The generated classes represent tables and relationships, ready to be used by any application layer.

---

## Run the Project

### Prerequisites
- Java 17+
- Maven 3+
- MySQL server running
- Database configured in `crawler-config.json`

### Steps

```bash
# Clone this repository
git clone https://github.com/<your-username>/mysql-schema-crawler.git

# Navigate into the project
cd mysql-schema-crawler

# Build and run the project
mvn spring-boot:run
```

---

## Testing the APIs

You can test using Postman or curl:

```bash
# Extract schema
GET http://localhost:8080/api/schema/

# Generate models
POST http://localhost:8080/api/schema/generate
```

---

## Sample Output

```
✅ Loaded configuration from: src/main/resources/crawler-config.json
✅ Connected to database: first
✅ Extracted 1 tables
✅ Generated 1 model class: com.example.models.Stud
```

---

## Documentation

The documentation explains:
- Architecture and workflow
- Schema extraction process
- Model generation flow
- Sample API outputs

---

## Author

**Vishal Uttam Palve**  
📧 vishalxpalve@gmail.com

---

## License

This project is developed as part of an assignment for Vistora.  
All rights reserved © 2025 Vishal Palve.
