package dev.scx.ffi.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/// 内存段映射器
///
/// @author scx567888
/// @version 0.0.1
public interface FFMMapper {

    /// 将内部数据转换为 MemorySegment (内存段)
    ///
    /// @param arena 作用域
    /// @return MemorySegment
    MemorySegment toMemorySegment(Arena arena) throws Exception;

    /// 从 MemorySegment (内存段) 设置值
    ///
    /// @param memorySegment a
    void fromMemorySegment(MemorySegment memorySegment) throws Exception;

}
