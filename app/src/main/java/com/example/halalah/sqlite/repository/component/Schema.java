package com.example.halalah.sqlite.repository.component;

import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;
import com.example.halalah.sqlite.repository.annotation.SqliteUnique;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

public class Schema {

    private String tableName;
    private Map<String, String> columns;
    private String columnId;

    private Schema() {
        columns = new TreeMap<String, String>();
    }

    private static Schema create() {
        return new Schema();
    }

    private Schema tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    private Schema column(String columnName, String columnType, boolean notNull, boolean primaryKey, boolean autoIncrement, boolean unique) {
        if (notNull) {
            columnType += " NOT NULL";
        } else {
            return this;
        }
        if (primaryKey) {
            columnType += " PRIMARY KEY";
            this.columnId = columnName;
        }
        if (primaryKey && autoIncrement) {
            columnType += " AUTOINCREMENT";
        }
        if (unique) {
            columnType += " UNIQUE";
        }

        this.columns.put(columnName, columnType);
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnId() {
        return columnId;
    }

    public String getCreateTableCommand() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(tableName).append("(");
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            builder.append(entry.getKey())
                    .append(" ")
                    .append(entry.getValue())
                    .append(",");
        }

        int lastComma = builder.lastIndexOf(",");
        builder.replace(lastComma, lastComma + 1, ");");

        return builder.toString();
    }

    public String getDropTableCommand() {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    public static <T extends SqliteGenericObject> Schema generateSchema(Class<T> clazz) {
        String tableName = clazz.getAnnotation(SqliteTableName.class).value();

        Schema schema = Schema.create().tableName(tableName);

        for (Field field : clazz.getDeclaredFields()) {
            boolean primaryKey = false, notNull = false, autoIncrement = false, unique = false;
            String columnName, columnType = DataType.TEXT;
            columnName = field.getName();

            String simpleName = field.getType().getSimpleName();
            if ("String".equals(simpleName)) {
                columnType = DataType.TEXT;
            } else if ("int".equals(simpleName)) {
                columnType = DataType.INTEGER;
            } else if ("double".equals(simpleName)) {
                columnType = DataType.REAL;
            } else if ("float".equals(simpleName)) {
                columnType = DataType.REAL;
            } else if ("long".equals(simpleName)) {
                columnType = DataType.INTEGER;
            } else if ("boolean".equals(simpleName)) {
                columnType = DataType.NUMERIC;
            }

            if (field.isAnnotationPresent(SqliteNotNull.class)) {
                notNull = true;
            }

            if (field.isAnnotationPresent(SqlitePrimaryKey.class)) {
                primaryKey = true;
                autoIncrement = field.getAnnotation(SqlitePrimaryKey.class).autoIncrement();
            }

            if (field.isAnnotationPresent(SqliteUnique.class)) {
                unique = true;
            }

            schema.column(columnName, columnType, notNull, primaryKey, autoIncrement, unique);
        }

        return schema;
    }
}
