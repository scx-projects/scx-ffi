package dev.scx.ffi.type;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

/// 回调函数需要继承此接口, 同时需要创建一个名为 callbackMethodName 返回值 的方法 (默认叫做 "callback")
///
/// ### 回调函数支持的参数类型.
/// - 1. 基本类型: `byte`, `short`, `int`, `long`, `float`, `double`, `char`.
/// - 2. 内存段: [MemorySegment].
///
/// ### 回调函数支持的返回值类型.
/// - 1. 基本类型: `byte`, `short`, `int`, `long`, `float`, `double`, `char`.
/// - 2. 内存段: [MemorySegment].
///
/// ### 关于 parameterTargetLayouts 方法.
/// - 功能: 为类型为 [MemorySegment] 的回调参数提供实际的内存布局信息.
/// - 用途: 当回调参数是 [MemorySegment] 类型且指向具体数据时, 需要通过 [MemoryLayout] 指定其真实类型, 使 FFI 层能够正确地从 [MemorySegment] 中读取或写入数据
/// - 返回值: 对应每个 MemorySegment 参数的 [MemoryLayout] 数组. 可以返回 null (表示不提供额外的 layout 信息).
///
/// @author scx567888
/// @version 0.0.1
public interface FFICallback {

    default String callbackMethodName() {
        return "callback";
    }

    default MemoryLayout[] parameterTargetLayouts() {
        return null;
    }

}
