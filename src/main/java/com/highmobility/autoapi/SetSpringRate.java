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

package com.highmobility.autoapi;

import com.highmobility.autoapi.property.value.Axle;
import com.highmobility.autoapi.property.Property;
import com.highmobility.autoapi.property.SpringRateProperty;

/**
 * Set the spring rate. The result is sent through the Chassis Settings message.
 */
public class SetSpringRate extends CommandWithProperties {
    public static final Type TYPE = new Type(Identifier.CHASSIS_SETTINGS, 0x14);
    private static final byte PROPERTY_IDENTIFIER = 0x01;

    Axle axle;
    Integer springRate;

    /**
     * @return The axle.
     */
    public Axle getAxle() {
        return axle;
    }

    /**
     * @return The suspension spring rate in N/mm.
     */
    public Integer getSpringRate() {
        return springRate;
    }

    public SetSpringRate(Axle axle, int springRate) {
        super(getValues(axle, springRate));
        this.axle = axle;
        this.springRate = springRate;
    }

    static byte[] getValues(Axle axle, int springRate) {
        Property prop = new SpringRateProperty(axle, springRate);
        prop.setIdentifier(PROPERTY_IDENTIFIER);
        return TYPE.addProperty(prop);
    }

    SetSpringRate(byte[] bytes) throws CommandParseException {
        super(bytes);
        Property prop = getProperty(PROPERTY_IDENTIFIER);
        if (prop != null) {
            axle = Axle.fromByte(prop.getValueBytes()[0]);
            springRate = Property.getUnsignedInt(prop.getValueBytes()[1]);

        }
    }
}
