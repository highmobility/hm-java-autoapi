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

package com.highmobility.autoapi.property.doors;

import com.highmobility.autoapi.CommandParseException;
import com.highmobility.autoapi.property.Property;
import com.highmobility.autoapi.property.value.Location;
import com.highmobility.autoapi.property.value.Lock;

public class DoorLockState extends Property {
    Location location;
    Lock doorLock;

    /**
     * @return The door location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return The door lock state.
     */
    public Lock getLock() {
        return doorLock;
    }

    public DoorLockState(byte[] bytes) throws CommandParseException {
        this(Location.fromByte(bytes[3]), Lock.fromByte(bytes[4]));
    }

    public DoorLockState(Location location, Lock doorLock) {
        super((byte) 0x01, 2);
        this.location = location;
        this.doorLock = doorLock;

        bytes[3] = location.getByte();
        bytes[4] = doorLock.getByte();
    }
}