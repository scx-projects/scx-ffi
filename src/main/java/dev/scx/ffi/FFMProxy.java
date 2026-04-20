package dev.scx.ffi;

import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import static dev.scx.ffi.FFMProxySupport.*;

/// FFMProxy
///
/// @author scx567888
/// @version 0.0.1
final class FFMProxy implements InvocationHandler {

    /// 这里只有读操作 所以 Map 即可保证线程安全.
    private final Map<Method, MethodHandle> ffmMethodHandles;

    public <T> FFMProxy(Class<T> clazz, SymbolLookup symbolLookup) {
        this.ffmMethodHandles = createFFMMethodHandles(clazz, symbolLookup);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // Object 方法 直接调用
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        // 默认方法 直接调用
        if (method.isDefault()) {
            return InvocationHandler.invokeDefault(proxy, method, args);
        }

        // 处理 FFM 的方法调用
        var methodHandle = this.ffmMethodHandles.get(method);

        try (var arena = Arena.ofConfined()) {

            // 1, 将 args 转换为只包含 (基本类型 | MemorySegment | FFMMapper) 三种类型的数组
            var wrappedParameters = wrapParameters(args);

            // 2, 将 wrappedParameters 转换为只包含 (基本类型 | MemorySegment) 两种类型的数组
            var nativeParameters = prepareNativeParameters(wrappedParameters, arena);

            // 3, 执行方法
            var result = methodHandle.invokeWithArguments(nativeParameters);

            // 4, FFMMapper 类型的参数 进行 内存段 数据回写.
            writeBackParameters(wrappedParameters, nativeParameters);

            // 5, 返回结果
            return result;
        }

    }

}
