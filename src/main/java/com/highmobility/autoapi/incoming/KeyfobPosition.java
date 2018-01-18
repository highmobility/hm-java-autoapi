/*
 * HMKit Auto API - Auto API Parser for Java
 * Copyright (C) 2018 High-Mobility <licensing@high-mobility.com>
 *
 * This file is part of HMKit Auto API.
 *
 * HMKit Auto API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HMKit Auto API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HMKit Auto API.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.highmobility.autoapi.incoming;
import com.highmobility.autoapi.CommandParseException;

/**
 * Created by ttiganik on 13/09/16.
 *
 * This is an evented message that is sent by the car every time the relative position of the
 * keyfob changes. This message is also sent when a Get Keyfob Position message is received by the
 * car.
 */
public class KeyfobPosition extends IncomingCommand {
    public enum Position {
        OUT_OF_RANGE((byte)0x00),
        OUTSIDE_DRIVER_SIDE((byte)0x01),
        OUTSIDE_IN_FRONT_OF_CAR((byte)0x02),
        OUTSIDE_PASSENGER_SIDE((byte)0x03),
        OUTSIDE_BEHIND_CAR((byte)0x04),
        INSIDE_CAR((byte)0x05);

        public static Position fromByte(byte value) throws CommandParseException {
            Position[] values = Position.values();

            for (int i = 0; i < values.length; i++) {
                Position possibleValue = values[i];
                if (possibleValue.getByte() == value) {
                    return possibleValue;
                }
            }

            throw new CommandParseException();
        }

        private byte value;

        Position(byte value) {
            this.value = value;
        }

        public byte getByte() {
            return value;
        }
    }

    Position position;

    /**
     *
     * @return Keyfob relative position to the car
     */
    public Position getPosition() {
        return position;
    }

    public KeyfobPosition(byte[] bytes) throws CommandParseException {
        super(bytes);

        if (bytes.length != 4) throw new CommandParseException();

        position = Position.fromByte(bytes[3]);
    }
}
