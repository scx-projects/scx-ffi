package dev.scx.ffi.mapper.string;

import dev.scx.ffi.mapper.FFMMapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/// StringFFMMapper (因为 string 是不可变的, 所以不支持从内存段写回)
///
/// @author scx567888
/// @version 0.0.1
public record StringFFMMapper(String value) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        // 只读的 这里忽略
    }

}
