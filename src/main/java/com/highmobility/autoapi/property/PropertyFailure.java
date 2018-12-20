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

package com.highmobility.autoapi.property;

import com.highmobility.autoapi.CommandParseException;

import javax.annotation.Nullable;

/**
 * This property holds a timestamp for a specific property of when it was recorded by the car.
 */
public class PropertyFailure extends Property {
    public static final byte IDENTIFIER = (byte) 0xA5;

    byte failedPropertyIdentifier;
    String description;
    Reason reason;

    /**
     * @return The identifier of the failed property.
     */
    public byte getFailedPropertyIdentifier() {
        return failedPropertyIdentifier;
    }

    /**
     * @return The failure reason.
     */
    public Reason getFailureReason() {
        return reason;
    }

    /**
     * @return The failure description.
     */
    @Nullable public String getFailureDescription() {
        return description;
    }

    public PropertyFailure(byte[] bytes) throws CommandParseException {
        super(bytes);
        if (bytes.length < 6) throw new CommandParseException();

        failedPropertyIdentifier = bytes[3];
        reason = Reason.fromByte(bytes[4]);
        int descriptionLength = Property.getUnsignedInt(bytes, 5, 1);
        if (descriptionLength > 0) {
            description = Property.getString(bytes, 6, descriptionLength);
        }
    }

    // TBODO:

    public enum Reason {
        RATE_LIMIT((byte) 0x00),        // Property rate limit has been exceeded
        EXECUTION_TIMEOUT((byte) 0x01), // Failed to retrieve property in time
        FORMAT_ERROR((byte) 0x02),      // Could not interpret property
        UNAUTHORISED((byte) 0x03),      // Insufficient permissions to get the property
        UNKNOWN((byte) 0x04),           // Property failed for unknown reason
        PENDING((byte) 0x05);           // Property is being refreshed

        public static Reason fromByte(byte value) throws CommandParseException {
            Reason[] values = Reason.values();

            for (int i = 0; i < values.length; i++) {
                Reason value1 = values[i];
                if (value1.getByte() == value) {
                    return value1;
                }
            }

            throw new CommandParseException();
        }

        private byte value;

        Reason(byte value) {
            this.value = value;
        }

        public byte getByte() {
            return value;
        }
    }
}