package dev.scx.ffi.mapper.primitive_ref;

import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.type.FloatRef;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

/// FloatRefFFMMapper
///
/// 不建议直接使用, 推荐直接使用 [FloatRef]
///
/// @author scx567888
/// @version 0.0.1
public record FloatRefFFMMapper(FloatRef floatRef) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_FLOAT, floatRef.value());
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        floatRef.value(memorySegment.get(JAVA_FLOAT, 0));
    }

}
