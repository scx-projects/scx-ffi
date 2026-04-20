package dev.scx.ffi.type;

/// ByteRef
///
/// @author scx567888
/// @version 0.0.1
public final class ByteRef {

    private byte value;

    public ByteRef() {
        this.value = 0;
    }

    public ByteRef(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }

    public void value(byte value) {
        this.value = value;
    }

}
