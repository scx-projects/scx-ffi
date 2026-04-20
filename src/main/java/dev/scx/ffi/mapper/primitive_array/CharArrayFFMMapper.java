package dev.scx.ffi.mapper.primitive_array;

import dev.scx.ffi.mapper.FFMMapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_CHAR;

/// CharArrayFFMMapper
///
/// 不建议直接使用, 推荐直接使用 `char[]`
///
/// @author scx567888
/// @version 0.0.1
public record CharArrayFFMMapper(char[] value) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_CHAR, value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        var temp = memorySegment.toArray(JAVA_CHAR);
        // 原因参考 IntArrayFFMMapper
        System.arraycopy(temp, 0, value, 0, temp.length);
    }

}
