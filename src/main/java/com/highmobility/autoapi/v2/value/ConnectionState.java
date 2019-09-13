// TODO: license
package com.highmobility.autoapi.v2.value;

import com.highmobility.autoapi.v2.CommandParseException;

public enum ConnectionState {
    DISCONNECTED((byte)0x00),
    CONNECTED((byte)0x01);

    public static ConnectionState fromByte(byte byteValue) throws CommandParseException {
        ConnectionState[] values = ConnectionState.values();

        for (int i = 0; i < values.length; i++) {
            ConnectionState state = values[i];
            if (state.getByte() == byteValue) {
                return state;
            }
        }

        throw new CommandParseException();
    }

    private byte value;

    ConnectionState(byte value) {
        this.value = value;
    }

    public byte getByte() {
        return value;
    }
}