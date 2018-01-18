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

package com.highmobility.autoapi.vehiclestatus;

import com.highmobility.autoapi.Command;
import com.highmobility.autoapi.CommandParseException;

import com.highmobility.autoapi.Property;
import com.highmobility.autoapi.incoming.DiagnosticsState;
import com.highmobility.utils.Bytes;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by ttiganik on 21/12/2016.
 */
public class Diagnostics extends FeatureState {
    int mileage;
    int oilTemperature;
    int speed;
    int rpm;
    float fuelLevel;
    DiagnosticsState.WasherFluidLevel washerFluidLevel;

    /**
     *
     * @return The car mileage (odometer) in km
     */
    public int getMileage() {
        return mileage;
    }

    /**
     *
     * @return Engine oil temperature in Celsius, whereas can be negative
     */
    public int getOilTemperature() {
        return oilTemperature;
    }

    /**
     *
     * @return The car speed in km/h, whereas can be negative
     */
    public int getSpeed() {
        return speed;
    }

    /**
     *
     * @return RPM of the Engine
     */
    public int getRpm() {
        return rpm;
    }

    /**
     *
     * @return Fuel level percentage between 0-100
     */
    public float getFuelLevel() {
        return fuelLevel;
    }

    /**
     *
     * @return Washer fluid level
     */
    public DiagnosticsState.WasherFluidLevel getWasherFluidLevel() {
        return washerFluidLevel;
    }

    public Diagnostics(int mileage,
                       int oilTemperature,
                       int speed,
                       int rpm,
                       float fuelLevel,
                       DiagnosticsState.WasherFluidLevel washerFluidLevel) {
        super(Command.Identifier.DIAGNOSTICS);
        this.mileage = mileage;
        this.oilTemperature = oilTemperature;
        this.speed = speed;
        this.rpm = rpm;
        this.fuelLevel = fuelLevel;
        this.washerFluidLevel = washerFluidLevel;

        byte[] mileageBytes = Property.intToBytes(mileage, 3);
        byte[] oilTemperatureBytes = Property.intToBytes(oilTemperature, 2);
        byte[] speedBytes = Property.intToBytes(speed, 2);
        byte[] engineRpmBytes = Property.intToBytes(rpm, 2);

        bytes = getBytesWithMoreThanOneByteLongFields(6, 5);

        Bytes.setBytes(bytes, mileageBytes, 3);
        Bytes.setBytes(bytes, oilTemperatureBytes, 6);
        Bytes.setBytes(bytes, speedBytes, 8);
        Bytes.setBytes(bytes, engineRpmBytes, 10);

        bytes[12] = (byte)(int)(fuelLevel * 100);
        bytes[13] = washerFluidLevel.getByte();
    }

    Diagnostics(byte[] bytes) throws CommandParseException {
        super(Command.Identifier.DIAGNOSTICS);

        if (bytes.length < 14) throw new CommandParseException();

        mileage = Property.getUnsignedInt(Arrays.copyOfRange(bytes, 3, 3 + 3));
        oilTemperature = Property.getUnsignedInt(Arrays.copyOfRange(bytes, 6, 6 + 2));
        speed = Property.getUnsignedInt(Arrays.copyOfRange(bytes, 8, 8 + 2));
        rpm = Property.getUnsignedInt(Arrays.copyOfRange(bytes, 10, 10 + 2));
        fuelLevel = (int)bytes[12] / 100f;
        if (bytes[13] == 0x00) washerFluidLevel = DiagnosticsState.WasherFluidLevel.LOW;
        else washerFluidLevel = DiagnosticsState.WasherFluidLevel.FULL;

        this.bytes = bytes;
    }
}
