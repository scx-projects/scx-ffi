package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.FieldInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemoryLayout.PathElement;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;

import static java.lang.foreign.MemoryLayout.PathElement.groupElement;

/// 内部构建辅助类
///
/// @author scx567888
/// @version 0.0.1
final class ValueNode implements Node {

    private final FieldInfo fieldInfo;
    private final ValueLayout baseLayout;
    private Node parentNode;
    private VarHandle varHandle;

    /// fieldInfo 必不为 null
    ValueNode(FieldInfo fieldInfo, ValueLayout baseLayout) {
        this.fieldInfo = fieldInfo;
        this.baseLayout = baseLayout;
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
        return baseLayout.withName(fieldInfo.name());
    }

    @Override
    public void initVarHandle(MemoryLayout memoryLayout) {
        var list = new ArrayList<PathElement>();
        Node n = this;
        while (n != null && n.fieldInfo() != null) {
            list.addFirst(groupElement(n.fieldInfo().name()));
            n = n.patentNode();
        }
        this.varHandle = memoryLayout.varHandle(list.toArray(PathElement[]::new));
        this.varHandle = MethodHandles.insertCoordinates(varHandle, 1, 0L);
    }

    @Override
    public void writeToMemorySegment(MemorySegment memorySegment, Object targetObject) throws IllegalAccessException {
        var value = fieldInfo.get(targetObject);
        varHandle.set(memorySegment, value);
    }

    @Override
    public void readFromMemorySegment(MemorySegment memorySegment, Object targetObject) throws IllegalAccessException {
        var value = varHandle.get(memorySegment);
        fieldInfo.set(targetObject, value);
    }

}
