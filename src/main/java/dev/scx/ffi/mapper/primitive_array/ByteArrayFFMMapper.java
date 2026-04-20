package dev.scx.ffi.mapper.primitive_array;

import dev.scx.ffi.mapper.FFMMapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;

/// ByteArrayFFMMapper
///
/// 不建议直接使用, 推荐直接使用 `byte[]`
///
/// @author scx567888
/// @version 0.0.1
public record ByteArrayFFMMapper(byte[] value) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_BYTE, value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        var temp = memorySegment.toArray(JAVA_BYTE);
        // 原因参考 IntArrayFFMMapper
        System.arraycopy(temp, 0, value, 0, temp.length);
    }

}
