package dev.scx.ffi.test.platform.win32;

import dev.scx.ffi.type.FFIStruct;

public final class WinDef {

    /// GetWindowRect 结构
    public static class RECT implements FFIStruct {

        public int left;
        public int top;
        public int right;
        public int bottom;

    }

}
