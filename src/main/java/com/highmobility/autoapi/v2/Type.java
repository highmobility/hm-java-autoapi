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

package com.highmobility.autoapi.v2;

import com.highmobility.autoapi.property.ByteEnum;

public enum Type implements ByteEnum {
    GET((byte) 0x00),
    SET((byte) 0x01);

    public static Type fromByte(byte value)  {
        Type[] values = Type.values();

        for (int i = 0; i < values.length; i++) {
            Type value1 = values[i];
            if (value1.getByte() == value) {
                return value1;
            }
        }

        throw new IllegalArgumentException();
    }

    private byte value;

    Type(byte value) {
        this.value = value;
    }

    public byte getByte() {
        return value;
    }
}

//public class Type {
//    static HashMap<Type, Type> stateCommands;
//
//    static {
//        stateCommands = new HashMap<>();
//        stateCommands.put(GetChargeState.TYPE, ChargeState.TYPE);
//        stateCommands.put(GetChassisSettings.TYPE, ChassisSettings.TYPE);
//        stateCommands.put(GetClimateState.TYPE, ClimateState.TYPE);
//        stateCommands.put(GetControlMode.TYPE, ControlMode.TYPE);
//        stateCommands.put(GetCruiseControlState.TYPE, CruiseControlState.TYPE);
//        stateCommands.put(GetDashboardLights.TYPE, DashboardLights.TYPE);
//        stateCommands.put(GetDiagnosticsState.TYPE, DiagnosticsState.TYPE);
//        stateCommands.put(GetFirmwareVersion.TYPE, FirmwareVersion.TYPE);
//        stateCommands.put(GetFlashersState.TYPE, FlashersState.TYPE);
//        stateCommands.put(GetGasFlapState.TYPE, GasFlapState.TYPE);
//        stateCommands.put(GetHistoricalStates.TYPE, HistoricalStates.TYPE);
//        stateCommands.put(GetHomeChargerState.TYPE, HomeChargerState.TYPE);
//        stateCommands.put(GetIgnitionState.TYPE, IgnitionState.TYPE);
//        stateCommands.put(GetKeyFobPosition.TYPE, KeyFobPosition.TYPE);
//        stateCommands.put(GetLightConditions.TYPE, LightConditions.TYPE);
//        stateCommands.put(GetLightsState.TYPE, LightsState.TYPE);
//        stateCommands.put(GetLockState.TYPE, LockState.TYPE);
//        stateCommands.put(GetMaintenanceState.TYPE, MaintenanceState.TYPE);
//        stateCommands.put(GetMobileState.TYPE, MobileState.TYPE);
//        stateCommands.put(GetNaviDestination.TYPE, NaviDestination.TYPE);
//        stateCommands.put(GetOffroadState.TYPE, OffroadState.TYPE);
//        stateCommands.put(GetParkingBrakeState.TYPE, ParkingBrakeState.TYPE);
//        stateCommands.put(GetParkingTicket.TYPE, ParkingTicket.TYPE);
//        stateCommands.put(GetPowerTakeOffState.TYPE, PowerTakeOffState.TYPE);
//        stateCommands.put(GetRaceState.TYPE, RaceState.TYPE);
//        stateCommands.put(GetRooftopState.TYPE, RooftopState.TYPE);
//        stateCommands.put(GetSeatsState.TYPE, SeatsState.TYPE);
//        stateCommands.put(GetStartStopState.TYPE, StartStopState.TYPE);
//        stateCommands.put(GetTachographState.TYPE, TachographState.TYPE);
//        stateCommands.put(GetTheftAlarmState.TYPE, TheftAlarmState.TYPE);
//        stateCommands.put(GetTrunkState.TYPE, TrunkState.TYPE);
//        stateCommands.put(GetUsage.TYPE, Usage.TYPE);
//        stateCommands.put(GetValetMode.TYPE, ValetMode.TYPE);
//        stateCommands.put(GetVehicleLocation.TYPE, VehicleLocation.TYPE);
//        stateCommands.put(GetVehicleStatus.TYPE, VehicleStatus.TYPE);
//        stateCommands.put(GetVehicleTime.TYPE, VehicleTime.TYPE);
//        stateCommands.put(GetWeatherConditions.TYPE, WeatherConditions.TYPE);
//        stateCommands.put(GetWifiState.TYPE, WifiState.TYPE);
//        stateCommands.put(GetWindowsState.TYPE, WindowsState.TYPE);
//        stateCommands.put(GetHoodState.TYPE, HoodState.TYPE);
//        stateCommands.put(GetWindscreenState.TYPE, WindscreenState.TYPE);
//    }
//
//    byte[] identifierAndType;
//
//    Identifier identifier;
//
//    public Identifier getIdentifier() {
//        return identifier;
//    }
//
//    public byte[] getIdentifierAndType() {
//        return identifierAndType;
//    }
//
//    public byte[] getIdentifierBytes() {
//        return new byte[]{identifierAndType[0], identifierAndType[1]};
//    }
//
//    public byte getType() {
//        return identifierAndType[2];
//    }
//
//    @Nullable public Type getStateCommand() {
//        return stateCommands.get(this);
//    }
//
//    /**
//     * @param identifierAndType 2 identifier bytes and 1 type byte
//     */
//    public Type(byte[] identifierAndType) {
//        this.identifierAndType = identifierAndType;
//        identifier = Identifier.fromBytes(identifierAndType[0], identifierAndType[1]);
//    }
//
//    public Type(byte[] identifier, int type) {
//        this(ByteUtils.concatBytes(identifier, (byte) type));
//    }
//
//    Type(Identifier identifier, int type) {
//        this(identifier.getBytesWithType((byte) type));
//    }
//
//    Type(int identifierFirstByte, int identifierSecondByte, int type) {
//        this(new byte[]{(byte) identifierFirstByte, (byte) identifierSecondByte, (byte) type});
//    }
//
//    @Override public boolean equals(Object obj) {
//        return obj.getClass() == Type.class
//                && Arrays.equals(((Type) obj).getIdentifierAndType(), getIdentifierAndType());
//    }
//
//    @Override public String toString() {
//        return ByteUtils.hexFromBytes(getIdentifierAndType());
//    }
//}