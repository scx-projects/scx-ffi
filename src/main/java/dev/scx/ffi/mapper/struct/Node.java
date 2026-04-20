package dev.scx.ffi.mapper.struct;

import dev.scx.reflect.FieldInfo;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

/// 内部构建辅助类
///
/// @author scx567888
/// @version 0.0.1
sealed interface Node permits ValueNode, StructNode {

    /// fieldInfo
    FieldInfo fieldInfo();

    /// 父 node
    Node patentNode();

    /// 设置 父 node
    void patentNode(Node patentNode);

    /// 创建内存布局
    MemoryLayout createMemoryLayout();

    /// 初始化 VarHandle
    void initVarHandle(MemoryLayout memoryLayout);

    /// 将 targetObject 写入到 memorySegment 中, 注意 不是直接读取 targetObject 本体, 而是读取 targetObject 的字段.
    void writeToMemorySegment(MemorySegment memorySegment, Object targetObject) throws IllegalAccessException;

    /// 将 memorySegment 读取到 targetObject 中, 注意 不是直接写入 targetObject 本体, 而是写入 targetObject 的字段.
    void readFromMemorySegment(MemorySegment memorySegment, Object targetObject) throws IllegalAccessException;

}
