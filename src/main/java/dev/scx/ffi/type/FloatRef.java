package dev.scx.ffi.type;

/// FloatRef
///
/// @author scx567888
public final class FloatRef {

    private float value;

    public FloatRef() {
        this.value = 0;
    }

    public FloatRef(float value) {
        this.value = value;
    }

    public float value() {
        return value;
    }

    public void value(float value) {
        this.value = value;
    }

}
