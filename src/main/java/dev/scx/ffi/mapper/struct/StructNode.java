package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.FieldInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.util.ArrayList;

/// 内部构建辅助类
///
/// @author scx567888
/// @version 0.0.1
final class StructNode implements Node {

    private final FieldInfo fieldInfo;
    private final Node[] fieldNodes;
    private Node parentNode;

    /// fieldInfo 可能为 null
    StructNode(FieldInfo fieldInfo, Node[] fieldNodes) {
        this.fieldInfo = fieldInfo;
        this.fieldNodes = fieldNodes;
        for (var fieldNode : this.fieldNodes) {
            fieldNode.patentNode(this);
        }
    }

    @Override
    public FieldInfo fieldInfo() {
        return fieldInfo;
    }

    @Override
    public Node patentNode() {
        return parentNode;
    }

    @Override
    public void patentNode(Node patentNode) {
        this.parentNode = patentNode;
    }

    @Override
    public MemoryLayout createMemoryLayout() {

        // 1, 计算最大的 对齐单位.
        long maxAlignment = 1;
        var tempLayout = new ArrayList<MemoryLayout>();
        for (var fieldNode : fieldNodes) {
            var childLayout = fieldNode.createMemoryLayout();
            // 记录最大 alignment
            maxAlignment = Math.max(maxAlignment, childLayout.byteAlignment());
            tempLayout.add(childLayout);
        }

        // 2, 处理字段间 padding
        long offset = 0;
        var layouts = new ArrayList<MemoryLayout>(); // 多留空间给 padding
        for (var layout : tempLayout) {
            long alignment = layout.byteAlignment();
            long size = layout.byteSize();
            // 计算字段前 padding
            long padding = (alignment - (offset % alignment)) % alignment;
            if (padding > 0) {
                layouts.add(MemoryLayout.paddingLayout(padding));
                offset += padding;
            }

            layouts.add(layout);
            offset += size;
        }

        // 3, 计算结构体尾部 padding
        long structPadding = (maxAlignment - (offset % maxAlignment)) % maxAlignment;
        if (structPadding > 0) {
            layouts.add(MemoryLayout.paddingLayout(structPadding));
        }

        // 4, 生成 structLayout
        var finalLayouts = layouts.toArray(MemoryLayout[]::new);

        var structLayout = MemoryLayout.structLayout(finalLayouts);
        if (fieldInfo != null) {
            structLayout = structLayout.withName(fieldInfo.name());
        }

        return structLayout;
    }

    @Override
    public void initVarHandle(MemoryLayout memoryLayout) {
        // 递归初始化子节点
        for (var child : fieldNodes) {
            child.initVarHandle(memoryLayout);
        }
    }

    @Override
    public void writeToMemorySegment(MemorySegment memorySegment, Object targetObject) throws IllegalAccessException {
        for (var fieldNode : fieldNodes) {
            var subObject = targetObject;
            // 如果是 StructNode 我们 使用子对象.
            if (fieldNode instanceof StructNode) {
                // 这里不需要对 fieldInfo 判空, 因为 必定有值.
                subObject = fieldNode.fieldInfo().get(targetObject);
            }
            fieldNode.writeToMemorySegment(memorySegment, subObject);
        }
    }

    @Override
    public void readFromMemorySegment(MemorySegment memorySegment, Object targetObject) throws IllegalAccessException {
        for (var fieldNode : fieldNodes) {
            var subObject = targetObject;
            // 如果是 StructNode 我们 使用子对象.
            if (fieldNode instanceof StructNode) {
                // 这里不需要对 fieldInfo 判空, 因为 必定有值.
                subObject = fieldNode.fieldInfo().get(targetObject);
            }
            fieldNode.readFromMemorySegment(memorySegment, subObject);
        }
    }

}
