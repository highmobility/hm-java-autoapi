// TODO: license
package com.highmobility.autoapi.v2.value;

import com.highmobility.autoapi.v2.CommandParseException;

public enum Location {
    FRONT_LEFT((byte)0x00),
    FRONT_RIGHT((byte)0x01),
    REAR_RIGHT((byte)0x02),
    REAR_LEFT((byte)0x03);

    public static Location fromByte(byte byteValue) throws CommandParseException {
        Location[] values = Location.values();

        for (int i = 0; i < values.length; i++) {
            Location state = values[i];
            if (state.getByte() == byteValue) {
                return state;
            }
        }

        throw new CommandParseException();
    }

    private byte value;

    Location(byte value) {
        this.value = value;
    }

    public byte getByte() {
        return value;
    }
}