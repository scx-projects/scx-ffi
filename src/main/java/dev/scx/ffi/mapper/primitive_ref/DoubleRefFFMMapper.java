package dev.scx.ffi.mapper.primitive_ref;

import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.type.DoubleRef;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_DOUBLE;

/// DoubleRefFFMMapper
///
/// 不建议直接使用, 推荐直接使用 [DoubleRef]
///
/// @author scx567888
/// @version 0.0.1
public record DoubleRefFFMMapper(DoubleRef doubleRef) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_DOUBLE, doubleRef.value());
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        doubleRef.value(memorySegment.get(JAVA_DOUBLE, 0));
    }

}
