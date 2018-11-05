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

package com.highmobility.autoapi.property.diagnostics;

import com.highmobility.autoapi.CommandParseException;
import com.highmobility.autoapi.property.Property;
import com.highmobility.autoapi.property.value.TireLocation;
import com.highmobility.utils.ByteUtils;

public class TireTemperature extends Property {
    TireLocation tireLocation;
    float temperature;

    /**
     * @return The tire location.
     */
    public TireLocation getTireLocation() {
        return tireLocation;
    }

    /**
     * @return The tire pressure.
     */
    public float getTemperature() {
        return temperature;
    }

    public TireTemperature(TireLocation tireLocation, float temperature) {
        super((byte) 0x00, 5);
        this.tireLocation = tireLocation;
        this.temperature = temperature;
        bytes[3] = tireLocation.getByte();
        ByteUtils.setBytes(bytes, Property.floatToBytes(temperature), 4);
    }

    public TireTemperature(byte[] bytes) throws CommandParseException {
        super(bytes);
        if (bytes.length < 8) throw new CommandParseException();

        this.tireLocation = TireLocation.fromByte(bytes[3]);
        this.temperature = Property.getFloat(bytes, 4);
    }
}