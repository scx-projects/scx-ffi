package dev.scx.ffi.mapper.struct;

import java.util.ArrayList;
import java.util.List;

/// 内部构建辅助类
///
/// @author scx567888
/// @version 0.0.1
final class NodeCreateContext {

    private final List<Class<?>> chain;

    public NodeCreateContext() {
        this.chain = new ArrayList<>();
    }

    public void startRecursionCheck(Class<?> type) {
        if (this.chain.contains(type)) {
            throw new IllegalArgumentException("不支持递归嵌套结构体, type: " + type);
        }
        this.chain.addLast(type);
    }

    public void endRecursionCheck() {
        chain.removeLast();
    }

}
