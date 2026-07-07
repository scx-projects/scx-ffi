package dev.scx.ffi.test.platform.win32;

import dev.scx.ffi.type.FFICallback;
import dev.scx.ffi.type.FFIStruct;

import java.lang.foreign.MemorySegment;

public final class WinUser {

    /// EnumWindows 回调接口
    public interface WNDENUMPROC extends FFICallback {

        int callback(MemorySegment hwnd, long lParam);

    }

    public static class POINT implements FFIStruct {
        public int x;
        public int y;
    }

}
