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
package com.highmobility.autoapi;

import com.highmobility.autoapi.property.Property;
import com.highmobility.autoapi.value.Acceleration;
import com.highmobility.autoapi.value.ActiveState;
import com.highmobility.autoapi.value.Axle;
import com.highmobility.autoapi.value.BrakeTorqueVectoring;
import com.highmobility.autoapi.value.measurement.AccelerationUnit;
import com.highmobility.utils.ByteUtils;
import com.highmobility.value.Bytes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by ttiganik on 15/09/16.
 */
public class RaceTest extends BaseTest {
    Bytes bytes = new Bytes(
            COMMAND_HEADER + "005701" +
                    "010008010005003F5D2F1B" +
                    "01000801000501BF40C49C" +
                    "02000B0100083FC851EB851EB852" +
                    "03000B0100080000000000000000" +
                    "04000B0100083FEF5C28F5C28F5C" +
                    "0500040100010A" +
                    "06000701000441A00000" +
                    "07000701000440D51EB8" +
                    "08000401000103" +
                    "09000401000101" +
                    "0A00050100020101" +
                    "0A00050100020000" +
                    "0B000401000104" +
                    "0C000401000104" +
                    "0D000B0100080000000000000000" +
                    "0E000401000101" +
                    "0F000401000101" +
                    "10000401000101" +
                    "11000401000101" +
                    "12000401000101"
            /* level8 */
    );

    @Test
    public void state() {
        Command command = CommandResolver.resolve(bytes);

        assertTrue(command.getClass() == Race.State.class);
        Race.State state = (Race.State) command;
        testState(state);
    }

    private void testState(Race.State state) {
        assertTrue(state.getAcceleration(Acceleration.Direction.LONGITUDINAL).getValue().
                getAcceleration().getValue() == .864d);
        assertTrue(state.getAcceleration(Acceleration.Direction.LATERAL).getValue().
                getAcceleration().getValue() == -0.753d);

        assertTrue(state.getUndersteering().getValue() == .19d);
        assertTrue(state.getOversteering().getValue() == 0f);
        assertTrue(state.getGasPedalPosition().getValue() == .98d);
        assertTrue(state.getSteeringAngle().getValue().getValue() == 10d);
        assertTrue(state.getBrakePressure().getValue().getValue() == 20d);
        assertTrue(state.getYawRate().getValue().getValue() == 6.66d);
        assertTrue(state.getRearSuspensionSteering().getValue().getValue() == 3d);
        assertTrue(state.getElectronicStabilityProgram().getValue() == ActiveState.ACTIVE);

        assertTrue(state.getBrakeTorqueVectoring(Axle.REAR).getValue().getState() == ActiveState.ACTIVE);
        assertTrue(state.getBrakeTorqueVectoring(Axle.FRONT).getValue().getState() == ActiveState.INACTIVE);
        assertTrue(state.getGearMode().getValue() == Race.GearMode.DRIVE);

        assertTrue(state.getSelectedGear().getValue() == 4);
        assertTrue(state.getBrakePedalPosition().getValue() == 0d);

        assertTrue(state.getBrakePedalSwitch().getValue() == ActiveState.ACTIVE);
        assertTrue(state.getClutchPedalSwitch().getValue() == ActiveState.ACTIVE);
        assertTrue(state.getAcceleratorPedalIdleSwitch().getValue() == ActiveState.ACTIVE);
        assertTrue(state.getAcceleratorPedalKickdownSwitch().getValue() == ActiveState.ACTIVE);

        assertTrue(state.getVehicleMoving().getValue() == Race.VehicleMoving.MOVING);
        assertTrue(TestUtils.bytesTheSame(state, bytes));
    }

    @Test
    public void build() {
        Race.State.Builder builder = new Race.State.Builder();

        builder.addAcceleration(new Property(new Acceleration(Acceleration.Direction.LONGITUDINAL,
                new AccelerationUnit(.864d, AccelerationUnit.Unit.METERS_PER_SECOND_SQUARED))));

        builder.addAcceleration(new Property(new Acceleration(Acceleration.Direction.LATERAL,
                new AccelerationUnit(-.753d, AccelerationUnit.Unit.METERS_PER_SECOND_SQUARED))));

        builder.setUndersteering(new Property(.19d));
        builder.setOversteering(new Property(0d));
        builder.setGasPedalPosition(new Property(.98d));
        builder.setSteeringAngle(new Property(10));
        builder.setBrakePressure(new Property(20f));
        builder.setYawRate(new Property(6.66f));
        builder.setRearSuspensionSteering(new Property(3));
        builder.setElectronicStabilityProgram(new Property(ActiveState.ACTIVE));
        builder.addBrakeTorqueVectoring(new Property(new BrakeTorqueVectoring(Axle.REAR, ActiveState.ACTIVE)));
        builder.addBrakeTorqueVectoring(new Property(new BrakeTorqueVectoring(Axle.FRONT, ActiveState.INACTIVE)));
        builder.setGearMode(new Property(Race.GearMode.DRIVE));
        builder.setSelectedGear(new Property(4));
        builder.setBrakePedalPosition(new Property(0d));

        builder.setBrakePedalSwitch(new Property(ActiveState.ACTIVE));
        builder.setClutchPedalSwitch(new Property(ActiveState.ACTIVE));
        builder.setAcceleratorPedalIdleSwitch(new Property(ActiveState.ACTIVE));
        builder.setAcceleratorPedalKickdownSwitch(new Property(ActiveState.ACTIVE));
        builder.setVehicleMoving(new Property(Race.VehicleMoving.MOVING));

        Race.State state = builder.build();
        testState(state);
    }

    @Test
    public void get() {
        String waitingForBytes = COMMAND_HEADER + "005700";
        String commandBytes = ByteUtils.hexFromBytes(new Race.GetState().getByteArray());
        assertTrue(waitingForBytes.equals(commandBytes));
    }
}