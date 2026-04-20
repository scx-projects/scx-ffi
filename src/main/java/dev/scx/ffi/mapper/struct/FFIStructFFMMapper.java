package dev.scx.ffi.mapper.struct;

import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.type.FFIStruct;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

import static dev.scx.ffi.mapper.struct.FFIStructSupport.createNode;

/// FFIStructFFMMapper
///
/// 不建议直接使用, 推荐直接使用 [FFIStruct]
///
/// @author scx567888
/// @version 0.0.1
public final class FFIStructFFMMapper implements FFMMapper {

    private final FFIStruct ffiStruct;
    private final Node node;
    private final MemoryLayout memoryLayout;

    public FFIStructFFMMapper(FFIStruct ffiStruct) throws IllegalAccessException {
        this.ffiStruct = ffiStruct;
        // 1, 创建 当前 ffiStruct 的树形结构表达
        this.node = createNode(ffiStruct, null, new NodeCreateContext());
        // 2, 创建 内存布局
        this.memoryLayout = this.node.createMemoryLayout();
        // 3, 初始化 VarHandle
        this.node.initVarHandle(memoryLayout);
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) throws IllegalAccessException {
        var memorySegment = arena.allocate(memoryLayout);
        node.writeToMemorySegment(memorySegment, ffiStruct);
        return memorySegment;
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) throws IllegalAccessException {
        node.readFromMemorySegment(memorySegment, ffiStruct);
    }

}
