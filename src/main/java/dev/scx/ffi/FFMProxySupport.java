package dev.scx.ffi;

import dev.scx.ffi.annotation.SymbolName;
import dev.scx.ffi.mapper.FFMMapper;
import dev.scx.ffi.mapper.callback.FFICallbackFFMMapper;
import dev.scx.ffi.mapper.primitive_array.*;
import dev.scx.ffi.mapper.primitive_ref.*;
import dev.scx.ffi.mapper.string.EncodedStringFFMMapper;
import dev.scx.ffi.mapper.string.StringFFMMapper;
import dev.scx.ffi.mapper.struct.FFIStructFFMMapper;
import dev.scx.ffi.type.*;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ScxReflect;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static java.lang.foreign.Linker.nativeLinker;
import static java.lang.foreign.ValueLayout.*;

/// FFMProxySupport
///
/// @author scx567888
/// @version 0.0.1
final class FFMProxySupport {

    /// 获取 方法 返回值的 内存布局
    public static MemoryLayout getReturnMemoryLayout(Class<?> type) {
        // 1, 基本类型
        if (type == byte.class) {
            return JAVA_BYTE;
        }
        if (type == short.class) {
            return JAVA_SHORT;
        }
        if (type == int.class) {
            return JAVA_INT;
        }
        if (type == long.class) {
            return JAVA_LONG;
        }
        if (type == float.class) {
            return JAVA_FLOAT;
        }
        if (type == double.class) {
            return JAVA_DOUBLE;
        }
        if (type == char.class) {
            return JAVA_CHAR;
        }
        // 2, 内存段
        if (type == MemorySegment.class) {
            return ADDRESS;
        }
        // 其余全不支持 !!!
        throw new IllegalArgumentException("不支持的返回值类型 : " + type);
    }

    /// 获取 方法 参数的 内存布局
    public static MemoryLayout getParameterMemoryLayout(Class<?> type) {
        // 1, 基本类型
        if (type == byte.class) {
            return JAVA_BYTE;
        }
        if (type == short.class) {
            return JAVA_SHORT;
        }
        if (type == int.class) {
            return JAVA_INT;
        }
        if (type == long.class) {
            return JAVA_LONG;
        }
        if (type == float.class) {
            return JAVA_FLOAT;
        }
        if (type == double.class) {
            return JAVA_DOUBLE;
        }
        if (type == char.class) {
            return JAVA_CHAR;
        }
        // 2, 内存段
        if (type == MemorySegment.class) {
            return ADDRESS;
        }
        // 3, 基本类型 引用
        if (type == ByteRef.class ||
            type == ShortRef.class ||
            type == IntRef.class ||
            type == LongRef.class ||
            type == FloatRef.class ||
            type == DoubleRef.class ||
            type == CharRef.class) {
            return ADDRESS;
        }
        // 4, 字符串
        if (type == String.class || type == EncodedString.class) {
            return ADDRESS;
        }
        // 5, 基本类型数组 (这里我们使用 ADDRESS 而不是 MemoryLayout.sequenceLayout(), 因为只有在运行时才知道具体长度)
        if (type == byte[].class ||
            type == short[].class ||
            type == int[].class ||
            type == long[].class ||
            type == float[].class ||
            type == double[].class ||
            type == char[].class) {
            return ADDRESS;
        }
        // 6, 结构体 (这里我们使用 ADDRESS 而不是 MemoryLayout.structLayout(), 因为只有在运行时才知道具体结构)
        if (FFIStruct.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        // 7, 回调
        if (FFICallback.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        // 8, 自定义映射
        if (FFMMapper.class.isAssignableFrom(type)) {
            return ADDRESS;
        }
        // 其余全不支持 !!!
        throw new IllegalArgumentException("不支持的参数类型 : " + type);
    }

    /// 获取 方法 参数的 内存布局
    public static MemoryLayout[] getParameterMemoryLayouts(Class<?>... types) {
        var memoryLayouts = new MemoryLayout[types.length];
        for (var i = 0; i < types.length; i = i + 1) {
            memoryLayouts[i] = getParameterMemoryLayout(types[i]);
        }
        return memoryLayouts;
    }

    /// 转换成 (基本类型 | MemorySegment | FFMMapper) 三种
    public static Object wrapParameter(Object o) throws IllegalAccessException {
        return switch (o) {
            // 1, 基本类型 (FFM 能够直接处理, 无需包装)
            case Byte _,
                 Short _,
                 Integer _,
                 Long _,
                 Float _,
                 Double _,
                 Character _ -> o;
            // 2, 内存段 (FFM 能够直接处理, 无需包装)
            case MemorySegment _ -> o;
            // 3, 基本类型引用
            case ByteRef r -> new ByteRefFFMMapper(r);
            case ShortRef r -> new ShortRefFFMMapper(r);
            case IntRef r -> new IntRefFFMMapper(r);
            case LongRef r -> new LongRefFFMMapper(r);
            case FloatRef r -> new FloatRefFFMMapper(r);
            case DoubleRef r -> new DoubleRefFFMMapper(r);
            case CharRef r -> new CharRefFFMMapper(r);
            // 4, 字符串
            case String s -> new StringFFMMapper(s);
            case EncodedString r -> new EncodedStringFFMMapper(r);
            // 5, 基本类型数组
            case byte[] c -> new ByteArrayFFMMapper(c);
            case short[] c -> new ShortArrayFFMMapper(c);
            case int[] c -> new IntArrayFFMMapper(c);
            case long[] c -> new LongArrayFFMMapper(c);
            case float[] c -> new FloatArrayFFMMapper(c);
            case double[] c -> new DoubleArrayFFMMapper(c);
            case char[] c -> new CharArrayFFMMapper(c);
            // 6, 结构体
            case FFIStruct c -> new FFIStructFFMMapper(c);
            // 7, 回调
            case FFICallback c -> new FFICallbackFFMMapper(c);
            // 8, 自定义映射
            case FFMMapper m -> m;
            // 9, 空值
            case null -> MemorySegment.NULL;
            // 其余抛异常
            default -> throw new IllegalArgumentException("无法转换的类型 : " + o.getClass());
        };
    }

    /// 转换成 (基本类型 | MemorySegment | FFMMapper) 三种
    public static Object[] wrapParameters(Object[] objs) throws IllegalAccessException {
        // 针对 null 做防御处理
        if (objs == null) {
            return new Object[0];
        }
        var parameters = new Object[objs.length];
        for (var i = 0; i < objs.length; i = i + 1) {
            parameters[i] = wrapParameter(objs[i]);
        }
        return parameters;
    }

    /// 这里因为是内部调用 我们假定 wrappedParameters 一定是 wrapParameters 的返回值.
    /// 转换成 (基本类型 | MemorySegment) 两种
    public static Object[] prepareNativeParameters(Object[] wrappedParameters, Arena arena) throws Exception {
        var nativeParameters = new Object[wrappedParameters.length];
        for (var i = 0; i < wrappedParameters.length; i = i + 1) {
            var wrappedParameter = wrappedParameters[i];
            if (wrappedParameter instanceof FFMMapper ffmMapper) {
                nativeParameters[i] = ffmMapper.toMemorySegment(arena);
            } else {
                //这里只剩下 基本类型 | MemorySegment, 能够直接使用.
                nativeParameters[i] = wrappedParameter;
            }
        }
        return nativeParameters;
    }

    /// 回写参数
    public static void writeBackParameters(Object[] wrappedParameters, Object[] nativeParameters) throws Exception {
        for (int i = 0; i < wrappedParameters.length; i = i + 1) {
            var wrappedParameter = wrappedParameters[i];
            var nativeParameter = nativeParameters[i];
            // 只回写 FFMMapper
            if (wrappedParameter instanceof FFMMapper ffmMapper && nativeParameter instanceof MemorySegment memorySegment) {
                ffmMapper.fromMemorySegment(memorySegment);
            }
        }
    }

    /// 创建 FFMMethodHandle
    public static MethodHandle createFFMMethodHandle(Method method, SymbolLookup symbolLookup) {
        // 0, 获取方法名
        var symbolName = method.getAnnotation(SymbolName.class);
        var name = symbolName == null ? method.getName() : symbolName.value();

        // 1, 根据方法名查找对应的方法
        var fun = symbolLookup.find(name).orElse(null);
        if (fun == null) {
            throw new IllegalArgumentException("未找到对应外部方法 : " + method.getName());
        }

        // 2, 创建方法的描述, 包括 返回值类型 参数类型列表
        var functionDescriptor = createFunctionDescriptor(method);

        // 3, 根据方法和描述, 获取可以调用本机方法的方法句柄
        return nativeLinker().downcallHandle(fun, functionDescriptor);
    }

    /// 生成方法描述
    public static FunctionDescriptor createFunctionDescriptor(Method method) {
        if (method.getReturnType() == void.class) {
            var paramLayouts = getParameterMemoryLayouts(method.getParameterTypes());
            return FunctionDescriptor.ofVoid(paramLayouts);
        } else {
            var returnLayout = getReturnMemoryLayout(method.getReturnType());
            var paramLayouts = getParameterMemoryLayouts(method.getParameterTypes());
            return FunctionDescriptor.of(returnLayout, paramLayouts);
        }
    }

    public static Map<Method, MethodHandle> createFFMMethodHandles(Class<?> clazz, SymbolLookup symbolLookup) {
        var typeInfo = ScxReflect.typeOf(clazz);
        if (!(typeInfo instanceof ClassInfo classInfo)) {
            throw new IllegalArgumentException("该类型不能作为 FFI 代理接口: " + clazz.getName());
        }
        var result = new HashMap<Method, MethodHandle>();
        // 获取接口整个层级的所有方法
        var methodInfos = classInfo.allMethods();
        for (var methodInfo : methodInfos) {
            // 这里只 处理 abstract 方法.
            if (methodInfo.isAbstract()) {
                result.put(methodInfo.rawMethod(), createFFMMethodHandle(methodInfo.rawMethod(), symbolLookup));
            }
        }
        return result;
    }

}
