package dev.scx.ffi.type;

/// CharRef
///
/// @author scx567888
/// @version 0.0.1
public final class CharRef {

    private char value;

    public CharRef() {
        this.value = 0;
    }

    public CharRef(char value) {
        this.value = value;
    }

    public char value() {
        return value;
    }

    public void value(char value) {
        this.value = value;
    }

}
