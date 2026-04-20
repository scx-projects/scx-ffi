package dev.scx.ffi.type;

/// IntRef
///
/// @author scx567888
/// @version 0.0.1
public final class IntRef {

    private int value;

    public IntRef() {
        this.value = 0;
    }

    public IntRef(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public void value(int value) {
        this.value = value;
    }

}
