package com.grafyn.assignment.service;
import com.grafyn.assignment.JdbcToJavaTypeMapper;
import com.grafyn.assignment.model.ColumnMeta;
import com.grafyn.assignment.model.DatabaseSchema;    // ✅ should match controller
import com.grafyn.assignment.model.ForeignKeyMeta;
import com.grafyn.assignment.model.TableMeta;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ModelGeneratorByteBuddy {

    private final ByteBuddy byteBuddy = new ByteBuddy();

    public Map<String, Class<?>> generate(DatabaseSchema schema, String targetPackage) {
        Map<String, Class<?>> generated = new HashMap<>();
        ClassLoader cl = this.getClass().getClassLoader();

        for (TableMeta table : schema.getTables().values()) {
            String className = targetPackage + "." + toCamelCase(table.getTableName(), true);
            DynamicType.Builder<Object> builder = (DynamicType.Builder<Object>) byteBuddy.subclass(Object.class)
                    .name(className);

            for (ColumnMeta cm : table.getColumns().values()) {
                Class<?> javaType = JdbcToJavaTypeMapper.map(cm.getJdbcType(), cm.getSize());
                builder = builder.defineField(cm.getName(), javaType, Visibility.PRIVATE)
                        .defineMethod("get" + cap(cm.getName()), javaType, Visibility.PUBLIC)
                        .intercept(FieldAccessor.ofBeanProperty())
                        .defineMethod("set" + cap(cm.getName()), void.class, Visibility.PUBLIC)
                        .withParameter(javaType)
                        .intercept(FieldAccessor.ofBeanProperty());
            }


            for (ForeignKeyMeta fk : table.getForeignKeys()) {
                String refClassName = targetPackage + "." + toCamelCase(fk.getPkTableName(), true);
                Class<?> refClass = generated.getOrDefault(fk.getPkTableName(), Object.class); // may be Object if not yet generated
                String fieldName = fk.getFkColumnName() + "Ref";
                builder = builder.defineField(fieldName, Object.class, Visibility.PRIVATE)
                        .defineMethod("get" + cap(fieldName), Object.class, Visibility.PUBLIC)
                        .intercept(FieldAccessor.ofBeanProperty())
                        .defineMethod("set" + cap(fieldName), void.class, Visibility.PUBLIC)
                        .withParameter(Object.class)
                        .intercept(FieldAccessor.ofBeanProperty());
            }

            Class<?> dynamicClass = builder.make()
                    .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)

                    .getLoaded();

            generated.put(table.getTableName(), dynamicClass);
        }

        return generated;
    }

    private String cap(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private String toCamelCase(String name, boolean upperFirst) {
        // convert snake_case to CamelCase
        String[] parts = name.split("[_\\s]+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String p = parts[i].toLowerCase();
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1));
        }
        if (!upperFirst && sb.length() > 0) {
            sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        }
        return sb.toString();
    }
}
