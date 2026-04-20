package dev.scx.ffi.type;

/// ShortRef
///
/// @author scx567888
/// @version 0.0.1
public final class ShortRef {

    private short value;

    public ShortRef() {
        this.value = 0;
    }

    public ShortRef(short value) {
        this.value = value;
    }

    public short value() {
        return value;
    }

    public void value(short value) {
        this.value = value;
    }

}
