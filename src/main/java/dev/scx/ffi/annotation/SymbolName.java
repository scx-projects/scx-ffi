package dev.scx.ffi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/// SymbolName
///
/// 如果外部函数的方法名, 不属于 java 中的合法方法名 可以使用这个注解覆盖.
///
/// @author scx567888
/// @version 0.0.1
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SymbolName {

    String value();

}
