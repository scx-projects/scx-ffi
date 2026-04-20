package dev.scx.ffi.test;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.ArrayList;

import static dev.scx.ffi.test.platform.win32.User32.USER32;
import static java.lang.foreign.ValueLayout.JAVA_LONG;

public class WindowTest {

    public static void main(String[] args) throws Throwable {
        test1();
    }

    public static void test1() throws Throwable {
        var list = new ArrayList<WindowInfo>();
        USER32.EnumWindows((hWnd, _) -> {
            //只要顶级窗口
            if (USER32.GetParent(hWnd).address() == 0) {
                var title = getWindowTitle(hWnd);
                var className = getWindowClassName(hWnd);
                var isVisible = USER32.IsWindowVisible(hWnd);
                var threadProcessId = USER32.GetWindowThreadProcessId(hWnd, Arena.global().allocate(JAVA_LONG));
                list.add(new WindowInfo(hWnd, className, title, isVisible, threadProcessId));
            }
            return 1;
        }, 0);

        for (WindowInfo windowInfo : list) {
            System.out.println(windowInfo.toString());
        }
    }

    public static String getWindowTitle(MemorySegment hWnd) {
        // 这里测试 A 函数
        int l = USER32.GetWindowTextLengthA(hWnd);
        var chars = new byte[l + 1];
        int l1 = USER32.GetWindowTextA(hWnd, chars, chars.length);
        return new String(chars, 0, l1);
    }

    public static String getWindowClassName(MemorySegment hWnd) {
        // 这里测试 W 函数
        var chars = new char[512];
        int l1 = USER32.GetClassNameW(hWnd, chars, 512);
        return new String(chars, 0, l1);
    }

    public record WindowInfo(MemorySegment hWnd,
                             String className,
                             String title,
                             int isVisible,
                             MemorySegment threadProcessId) {
        @Override
        public String toString() {
            return "WindowInfo{" +
                "className='" + className + '\'' +
                ", title='" + title + '\'' +
                ", isVisible=" + isVisible +
                '}';
        }
    }

}
