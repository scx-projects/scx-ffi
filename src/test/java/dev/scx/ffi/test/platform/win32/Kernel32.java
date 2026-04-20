package dev.scx.ffi.test.platform.win32;

import dev.scx.ffi.ScxFFI;
import dev.scx.ffi.type.IntRef;

import java.lang.foreign.MemorySegment;

/// 提供一些 Kernel32 标准的接口
///
/// @author scx567888
/// @version 0.0.1
public interface Kernel32 {

    Kernel32 KERNEL32 = ScxFFI.createFFI(Kernel32.class, "kernel32");

    // https://learn.microsoft.com/zh-cn/windows/console/getstdhandle
    MemorySegment GetStdHandle(int nStdHandle);

    // https://learn.microsoft.com/zh-cn/windows/console/getconsolemode
    int GetConsoleMode(MemorySegment hConsoleHandle, IntRef lpMode);

    // https://learn.microsoft.com/zh-cn/windows/console/setconsolemode
    int SetConsoleMode(MemorySegment hConsoleHandle, long dwMode);

}
