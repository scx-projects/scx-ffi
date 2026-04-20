package dev.scx.ffi.type;

import java.lang.foreign.MemorySegment;

/// 结构数据需要继承此接口 以便在处理时按照结构数据的方式来处理
///
/// ### 支持的 字段类型
/// - 1. 基本类型: `byte`, `short`, `int`, `long`, `float`, `double`, `char`.
/// - 2. 内存段: [MemorySegment].
/// - 3. 结构体: [FFIStruct].
///
/// ### 关于 fieldOrder 方法
/// - 默认使用 字段声明顺序.
/// - 如果需要自定义字段顺序, 可重写此方法, 返回字段名称数组.
/// - 返回 null 表示使用默认顺序.
///
/// @author scx567888
/// @version 0.0.1
public interface FFIStruct {

    default String[] fieldOrder() {
        return null;
    }

}
