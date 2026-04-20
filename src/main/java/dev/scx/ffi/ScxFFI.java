package dev.scx.ffi;

import dev.scx.ffi.annotation.SymbolName;
import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.type.*;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.reflect.Proxy;
import java.nio.file.Path;

import static java.lang.foreign.Arena.global;
import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.foreign.SymbolLookup.libraryLookup;

/// ScxFFI
///
/// ### 支持的参数类型
/// - 1. 基本类型: `byte`, `short`, `int`, `long`, `float`, `double`, `char`.
/// - 2. 内存段: [MemorySegment].
/// - 3. 基本类型引用: [ByteRef], [ShortRef], [IntRef], [LongRef], [FloatRef], [DoubleRef], [CharRef].
/// - 4. 字符串: [String], [EncodedString].
/// - 5. 基本类型数组: `byte[]`, `short[]`, `int[]`, `long[]`, `float[]`, `double[]`, `char[]`.
/// - 6. 结构体: [FFIStruct].
/// - 7. 回调: [FFICallback].
/// - 8. 自定义映射: [FFMMapper].
///
/// ### 支持的返回值类型
/// - 1. 基本类型: `byte`, `short`, `int`, `long`, `float`, `double`, `char`.
/// - 2. 内存段: [MemorySegment].
///
/// ### 关于方法名映射
/// - 默认直接使用 接口中定义的方法名进行映射, 如果需要自定义名称 请使用 [SymbolName] 注解.
///
/// @author scx567888
/// @version 0.0.1
@SuppressWarnings("unchecked")
public final class ScxFFI {

    public static <T> T createFFI(Class<T> clazz) {
        return createFFI(clazz, nativeLinker().defaultLookup());
    }

    public static <T> T createFFI(Class<T> clazz, String libraryName) {
        return createFFI(clazz, libraryLookup(libraryName, global()));
    }

    public static <T> T createFFI(Class<T> clazz, Path libraryPath) {
        return createFFI(clazz, libraryLookup(libraryPath, global()));
    }

    public static <T> T createFFI(Class<T> clazz, SymbolLookup symbolLookup) {
        // 这里 newProxyInstance 会验证 clazz 是否是接口, 所以我们在 FFMProxy 中无需校验
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new FFMProxy(clazz, symbolLookup));
    }

}
