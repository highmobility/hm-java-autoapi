/*
 * The MIT License
 * 
 * Copyright (c) 2014- High-Mobility GmbH (https://high-mobility.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.highmobility.autoapi.value;

import com.highmobility.autoapi.CommandParseException;
import com.highmobility.autoapi.property.Property;
import com.highmobility.autoapi.property.PropertyValueObject;
import com.highmobility.value.Bytes;

public class Light extends PropertyValueObject {
    public static final int SIZE = 2;

    LocationLongitudinal locationLongitudinal;
    ActiveState activeState;

    /**
     * @return The location longitudinal.
     */
    public LocationLongitudinal getLocationLongitudinal() {
        return locationLongitudinal;
    }

    /**
     * @return The active state.
     */
    public ActiveState getActiveState() {
        return activeState;
    }

    public Light(LocationLongitudinal locationLongitudinal, ActiveState activeState) {
        super(2);
        update(locationLongitudinal, activeState);
    }

    public Light(Property property) throws CommandParseException {
        super();
        if (property.getValueComponent() == null) throw new CommandParseException();
        update(property.getValueComponent().getValueBytes());
    }

    public Light() {
        super();
    } // needed for generic ctor

    @Override public void update(Bytes value) throws CommandParseException {
        super.update(value);
        if (bytes.length < 2) throw new CommandParseException();

        int bytePosition = 0;
        locationLongitudinal = LocationLongitudinal.fromByte(get(bytePosition));
        bytePosition += 1;

        activeState = ActiveState.fromByte(get(bytePosition));
    }

    public void update(LocationLongitudinal locationLongitudinal, ActiveState activeState) {
        this.locationLongitudinal = locationLongitudinal;
        this.activeState = activeState;

        bytes = new byte[getLength()];

        int bytePosition = 0;
        set(bytePosition, locationLongitudinal.getByte());
        bytePosition += 1;

        set(bytePosition, activeState.getByte());
    }

    public void update(Light value) {
        update(value.locationLongitudinal, value.activeState);
    }

    @Override public int getLength() {
        return 1 + 1;
    }
}