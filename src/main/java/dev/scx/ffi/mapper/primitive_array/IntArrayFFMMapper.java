package dev.scx.ffi.mapper.primitive_array;

import dev.scx.ffi.mapper.FFMMapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

import static java.lang.foreign.ValueLayout.JAVA_INT;

/// IntArrayFFMMapper
///
/// 不建议直接使用, 推荐直接使用 `int[]`
///
/// @author scx567888
/// @version 0.0.1
public record IntArrayFFMMapper(int[] value) implements FFMMapper {

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(JAVA_INT, value);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {
        var temp = memorySegment.toArray(JAVA_INT);
        // 这里假设 temp 的长度与原始 value 数组完全一致.
        // 我们不直接替换 value 引用, 是为了保留外部传入的数组对象不变,
        // 这样外部对原数组的引用依然有效, 相当于模拟 C 中通过指针修改数组的效果.
        // 因此这里使用 System.arraycopy, 将数据写回原数组.
        System.arraycopy(temp, 0, value, 0, temp.length);
    }

}
