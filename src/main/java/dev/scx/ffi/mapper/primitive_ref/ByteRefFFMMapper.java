package dev.scx.ffi.mapper.primitive_ref;

import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.type.ByteRef;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_BYTE;

/// ByteRefFFMMapper
///
/// 不建议直接使用, 推荐直接使用 [ByteRef]
///
/// @author scx567888
/// @version 0.0.1
public record ByteRefFFMMapper(ByteRef byteRef) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_BYTE, byteRef.value());
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        byteRef.value(memorySegment.get(JAVA_BYTE, 0));
    }

}
