package dev.scx.ffi.mapper.string;

import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.type.EncodedString;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/// EncodedStringFFMMapper
///
/// @author scx567888
/// @version 0.0.1
public record EncodedStringFFMMapper(EncodedString encodedString) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(encodedString.value(), encodedString.charset());
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        // 只读的 这里忽略
    }

}
