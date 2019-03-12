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

package com.highmobility.autoapi.value.windscreen;

import com.highmobility.autoapi.CommandParseException;
import com.highmobility.autoapi.property.PropertyValueSingleByte;

public enum WindscreenDamage implements PropertyValueSingleByte {
    NO_IMPACT((byte)0x00),
    IMPACT_NO_DAMAGE((byte)0x01),
    DAMAGE_SMALLER_THAN_1((byte)0x02),
    DAMAGE_LARGER_THAN_1((byte)0x03);

    public static WindscreenDamage fromByte(byte byteValue) throws CommandParseException {
        WindscreenDamage[] values = WindscreenDamage.values();

        for (int i = 0; i < values.length; i++) {
            WindscreenDamage state = values[i];
            if (state.getByte() == byteValue) {
                return state;
            }
        }

        throw new CommandParseException();
    }

    private byte value;

    WindscreenDamage(byte value) {
        this.value = value;
    }

    public byte getByte() {
        return value;
    }
}