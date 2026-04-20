package dev.scx.ffi.type;

/// LongRef
///
/// @author scx567888
/// @version 0.0.1
public final class LongRef {

    private long value;

    public LongRef() {
        this.value = 0;
    }

    public LongRef(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    public void value(long value) {
        this.value = value;
    }

}
