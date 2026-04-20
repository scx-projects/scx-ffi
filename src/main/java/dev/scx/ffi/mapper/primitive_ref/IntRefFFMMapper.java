package dev.scx.ffi.mapper.primitive_ref;

import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.type.IntRef;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_INT;

/// IntRefFFMMapper
///
/// 不建议直接使用, 推荐直接使用 [IntRef]
///
/// @author scx567888
/// @version 0.0.1
public record IntRefFFMMapper(IntRef intRef) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_INT, intRef.value());
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        intRef.value(memorySegment.get(JAVA_INT, 0));
    }

}
