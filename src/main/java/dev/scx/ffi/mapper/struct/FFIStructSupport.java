package dev.scx.ffi.mapper.struct;

import dev.scx.ffi.type.FFIStruct;
import dev.scx.reflect.AccessModifier;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.FieldInfo;
import dev.scx.reflect.ScxReflect;

import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static java.lang.foreign.ValueLayout.*;

/// 内部构建辅助类
///
/// @author scx567888
/// @version 0.0.1
final class FFIStructSupport {

    public static Node createNode(Object o, FieldInfo fieldInfo, NodeCreateContext context) throws IllegalAccessException {
        return switch (o) {
            case FFIStruct ffiStruct -> {
                // 检查递归嵌套
                context.startRecursionCheck(ffiStruct.getClass());
                try {
                    // 获取 属于 结构体的字段
                    var structFields = getStructFields(ffiStruct);
                    var fieldNodes = new Node[structFields.length];

                    for (int i = 0; i < structFields.length; i = i + 1) {
                        var structField = structFields[i];
                        var fo = structField.get(ffiStruct);
                        fieldNodes[i] = createNode(fo, structField, context);
                    }

                    yield new StructNode(fieldInfo, fieldNodes);
                } finally {
                    context.endRecursionCheck();
                }
            }
            case Byte _ -> new ValueNode(fieldInfo, JAVA_BYTE);
            case Short _ -> new ValueNode(fieldInfo, JAVA_SHORT);
            case Integer _ -> new ValueNode(fieldInfo, JAVA_INT);
            case Long _ -> new ValueNode(fieldInfo, JAVA_LONG);
            case Float _ -> new ValueNode(fieldInfo, JAVA_FLOAT);
            case Double _ -> new ValueNode(fieldInfo, JAVA_DOUBLE);
            case Character _ -> new ValueNode(fieldInfo, JAVA_CHAR);
            case MemorySegment _ -> new ValueNode(fieldInfo, ADDRESS);
            case null -> throw new NullPointerException("结构体字段 不允许为 null !!!");
            default -> throw new IllegalArgumentException("不支持的 结构体字段 类型 !!! " + o.getClass());
        };
    }

    /// 需要 过滤 + 排序
    public static FieldInfo[] getStructFields(FFIStruct ffiStruct) {
        // 这里强转是安全的 FFIStruct 必然是 ClassInfo
        var classInfo = (ClassInfo) ScxReflect.typeOf(ffiStruct.getClass());
        // 初步过滤 字段
        var result = new ArrayList<FieldInfo>();
        for (var f : classInfo.allFields()) {
            // 只处理 非静态的 public 字段
            if (f.accessModifier() == AccessModifier.PUBLIC && !f.isStatic()) {
                result.add(f);
            }
        }
        // 尝试排序
        var fieldOrder = ffiStruct.fieldOrder();

        // 尝试排序
        if (fieldOrder != null) {
            result.sort(new FieldOrderComparator(fieldOrder));
        }

        return result.toArray(FieldInfo[]::new);
    }

    public static class FieldOrderComparator implements Comparator<FieldInfo> {

        private final Map<String, Integer> orderMap;

        public FieldOrderComparator(String[] fieldOrder) {
            this.orderMap = new HashMap<>();
            if (fieldOrder != null) {
                for (int i = 0; i < fieldOrder.length; i = i + 1) {
                    orderMap.put(fieldOrder[i], i);
                }
            }
        }

        @Override
        public int compare(FieldInfo f1, FieldInfo f2) {
            int i1 = orderMap.getOrDefault(f1.name(), Integer.MAX_VALUE);
            int i2 = orderMap.getOrDefault(f2.name(), Integer.MAX_VALUE);
            return Integer.compare(i1, i2);
        }

    }

}
