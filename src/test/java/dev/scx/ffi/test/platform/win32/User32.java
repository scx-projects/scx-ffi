package dev.scx.ffi.test.platform.win32;

import dev.scx.ffi.ScxFFI;
import dev.scx.ffi.test.platform.win32.WinUser.POINT;
import dev.scx.ffi.test.platform.win32.WinUser.WNDENUMPROC;
import dev.scx.ffi.type.EncodedString;

import java.lang.foreign.MemorySegment;

/// 提供一些 User32 标准的接口
///
/// @author scx567888
/// @version 0.0.1
public interface User32 {

    User32 USER32 = ScxFFI.createFFI(User32.class, "user32");

    // https://learn.microsoft.com/zh-cn/windows/win32/api/winuser/nf-winuser-messageboxa
    int MessageBoxA(MemorySegment hWnd, String lpText, String lpCaption, int uType);

    int MessageBoxW(MemorySegment hWnd, EncodedString lpText, EncodedString lpCaption, int uType);

    // https://learn.microsoft.com/zh-cn/windows/win32/api/winuser/nf-winuser-iswindowvisible
    int IsWindowVisible(MemorySegment hWnd);

    MemorySegment GetParent(MemorySegment hWnd);

    int GetWindowTextLengthW(MemorySegment hWnd);

    int GetWindowTextLengthA(MemorySegment hWnd);

    int GetClassNameW(MemorySegment hWnd, char[] lpClassName, int nMaxCount);

    int GetClassNameA(MemorySegment hWnd, byte[] lpClassName, int nMaxCount);

    int GetWindowTextW(MemorySegment hWnd, char[] lpString, int nMaxCount);

    int GetWindowTextA(MemorySegment hWnd, byte[] lpString, int nMaxCount);

    MemorySegment GetWindowThreadProcessId(MemorySegment hWnd, MemorySegment lpdwProcessId);

    int PostMessageW(MemorySegment hWnd, int Msg, MemorySegment wParam, MemorySegment lParam);

    int SendMessageW(MemorySegment hWnd, int Msg, MemorySegment wParam, MemorySegment lParam);

    int EnumWindows(WNDENUMPROC lpEnumFunc, long lParam);

    MemorySegment FindWindowA(String lpClassName, String lpWindowName);

    int CloseWindow(MemorySegment hWnd);

    int GetWindowRect(MemorySegment hWnd, WinDef.RECT lpRect);

    int SetCursorPos(int x, int y);

    int GetCursorPos(POINT lpPoint);

}
