package dev.scx.ffi.mapper.primitive_ref;

import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.type.CharRef;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_CHAR;

/// CharRefFFMMapper
///
/// 不建议直接使用, 推荐直接使用 [CharRef]
///
/// @author scx567888
/// @version 0.0.1
public record CharRefFFMMapper(CharRef charRef) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_CHAR, charRef.value());
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        charRef.value(memorySegment.get(JAVA_CHAR, 0));
    }

}
