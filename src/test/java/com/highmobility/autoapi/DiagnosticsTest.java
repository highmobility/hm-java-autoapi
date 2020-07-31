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
import com.highmobility.autoapi.value.*;
import com.highmobility.autoapi.value.measurement.AngularVelocity;
import com.highmobility.autoapi.value.measurement.Duration;
import com.highmobility.autoapi.value.measurement.Pressure;
import com.highmobility.autoapi.value.measurement.Temperature;
import com.highmobility.utils.ByteUtils;
import com.highmobility.value.Bytes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by ttiganik on 15/09/16.
 */
public class DiagnosticsTest extends BaseTest {
    Bytes bytes = new Bytes(
            COMMAND_HEADER + "003301" +
                    "010007010004000249F0" +
                    "0200050100020063" +
                    "030005010002003C" +
                    "04000501000209C4" +
                    "05000B0100083FECCCCCCCCCCCCD" +
                    "0600050100020109" +
                    "09000401000101" +
                    "0B000701000441400000" +
                    "0C00070100043F000000" +
                    "0D000501000205DC" +
                    "0E0005010002000A" +
                    "0F0007010004420E0000" +
                    "10000401000101" +
                    "1100050100020014" +
                    "12000701000444BB94CD" +
                    "13000701000446D78600" +
                    "14000401000100" +
                    "15000B0100083FC999999999999A" +
                    "16000B0100083FB999999999999A" +
                    "1700050100020041" +
                    "18000B0100083FE1EB851EB851EC" +
                    "19001E01001B000100019C78000C436865636B20656E67696E650005416C657274" +
                    "19001F01001C000100019C78000C436865636B20656E67696E650006416C65727474" +
                    "1A0008010005004013D70A" +
                    "1A0008010005014013D70A" +
                    "1A0008010005024013D70A" +
                    "1A0008010005034013D70A" +
                    "1B00080100050042200000" +
                    "1B00080100050142200000" +
                    "1B00080100050242200000" +
                    "1B00080100050342200000" +
                    "1C00060100030002EA" +
                    "1C00060100030102EA" +
                    "1C00060100030202EA" +
                    "1C00060100030302EA" +
                    "1D002101001E0200074331313136464100095244555F3231324652000750454E44494E47" +
                    "1D001E01001B020007433136334146410006445452323132000750454E44494E47" +
                    "1E0007010004000249F0"
    );

    @Test
    public void state() {
        setRuntime(CommandResolver.RunTime.ANDROID);
        Diagnostics.State state = (Diagnostics.State) CommandResolver.resolve(bytes);
        testState(state);
    }

    private void testState(Diagnostics.State state) {
        assertTrue(state.getNonce() == null);
        assertTrue(state.getSignature() == null);

        assertTrue(state.getMileage().getValue().getValue() == 150000);
        assertTrue(state.getEngineOilTemperature().getValue().getValue() == 99);
        assertTrue(state.getSpeed().getValue().getValue() == 60);
        assertTrue(state.getEngineRPM().getValue().getValue() == 2500);
        assertTrue(state.getEstimatedRange().getValue().getValue() == 265);
        assertTrue(state.getFuelLevel().getValue() == .9d);
        assertTrue(state.getWasherFluidLevel().getValue() == FluidLevel.FILLED);
        assertTrue(state.getFuelVolume().getValue().getValue() == 35.5f);

        assertTrue(state.getBatteryVoltage().getValue().getValue() == 12f);
        assertTrue(state.getAdBlueLevel().getValue().getValue() == .5f);
        assertTrue(state.getDistanceSinceReset().getValue().getValue() == 1500);
        assertTrue(state.getDistanceSinceStart().getValue().getValue() == 10);

        assertTrue(state.getAntiLockBraking().getValue() == ActiveState.ACTIVE);
        assertTrue(state.getEngineCoolantTemperature().getValue().getValue() == 20);
        assertTrue(state.getEngineTotalOperatingHours().getValue().getValue() == 1500.65f);
        assertTrue(state.getEngineTotalFuelConsumption().getValue().getValue() == 27587.0f);
        assertTrue(state.getBrakeFluidLevel().getValue() == FluidLevel.LOW);
        assertTrue(state.getEngineTorque().getValue() == .2d);
        assertTrue(state.getEngineLoad().getValue() == .1d);
        assertTrue(state.getWheelBasedSpeed().getValue().getValue() == 65d);

        // level 8
        assertTrue(state.getBatteryLevel().getValue() == .56d);

        int propertyCount = 0;

        for (Property<CheckControlMessage> checkControlMessage : state.getCheckControlMessages()) {
            if (checkControlMessage.getValue().getID() == 1 &&
                    checkControlMessage.getValue().getText().equals("Check engine") &&
                    checkControlMessage.getValue().getStatus().equals("Alert") &&
                    checkControlMessage.getValue().getRemainingTime().getValue() == 105592d) {
                propertyCount++;
            }

            if (checkControlMessage.getValue().getID() == 1 &&
                    checkControlMessage.getValue().getText().equals("Check engine") &&
                    checkControlMessage.getValue().getStatus().equals("Alertt") &&
                    checkControlMessage.getValue().getRemainingTime().getValue() == 105592d) {
                propertyCount++;
            }
        }

        assertTrue(propertyCount == 2);

        propertyCount = 0;
        for (Property<TirePressure> pressure : state.getTirePressures()) {
            if (pressure.getValue().getPressure().getValue() != 2.31f) fail();

            if (pressure.getValue().getLocation() == LocationWheel.FRONT_LEFT) propertyCount++;
            if (pressure.getValue().getLocation() == LocationWheel.FRONT_RIGHT) propertyCount++;
            if (pressure.getValue().getLocation() == LocationWheel.REAR_RIGHT) propertyCount++;
            if (pressure.getValue().getLocation() == LocationWheel.REAR_LEFT) propertyCount++;
        }

        assertTrue(propertyCount == 4);
        propertyCount = 0;

        for (Property<TireTemperature> tireTemperature : state.getTireTemperatures()) {
            if (tireTemperature.getValue().getTemperature().getValue() != 40f) fail();

            if (tireTemperature.getValue().getLocation() == LocationWheel.FRONT_LEFT)
                propertyCount++;
            if (tireTemperature.getValue().getLocation() == LocationWheel.FRONT_RIGHT)
                propertyCount++;
            if (tireTemperature.getValue().getLocation() == LocationWheel.REAR_RIGHT)
                propertyCount++;
            if (tireTemperature.getValue().getLocation() == LocationWheel.REAR_LEFT)
                propertyCount++;
        }

        assertTrue(propertyCount == 4);
        propertyCount = 0;

        for (Property<WheelRpm> wheelRpm : state.getWheelRPMs()) {
            if (wheelRpm.getValue().getRPM().getValue() != 746d) fail();

            if (wheelRpm.getValue().getLocation() == LocationWheel.FRONT_LEFT) propertyCount++;
            if (wheelRpm.getValue().getLocation() == LocationWheel.FRONT_RIGHT) propertyCount++;
            if (wheelRpm.getValue().getLocation() == LocationWheel.REAR_RIGHT) propertyCount++;
            if (wheelRpm.getValue().getLocation() == LocationWheel.REAR_LEFT) propertyCount++;
        }

        assertTrue(propertyCount == 4);
        propertyCount = 0;

        for (Property<TroubleCode> diagnosticsTroubleCode :
                state.getTroubleCodes()) {
            if (diagnosticsTroubleCode.getValue().getID().equals("C1116FA") &&
                    diagnosticsTroubleCode.getValue().getEcuID().equals("RDU_212FR") &&
                    diagnosticsTroubleCode.getValue().getStatus().equals("PENDING") &&
                    diagnosticsTroubleCode.getValue().getOccurrences() == 2) {
                propertyCount++;
            } else if (diagnosticsTroubleCode.getValue().getID().equals("C163AFA") &&
                    diagnosticsTroubleCode.getValue().getEcuID().equals("DTR212") &&
                    diagnosticsTroubleCode.getValue().getStatus().equals("PENDING") &&
                    diagnosticsTroubleCode.getValue().getOccurrences() == 2) {
                propertyCount++;
            }
        }

        assertTrue(propertyCount == 2);
        assertTrue(state.getMileageMeters().getValue().getValue() == 150000d);

        assertTrue(TestUtils.bytesTheSame(bytes, state));
    }

    @Test
    public void get() {
        Bytes waitingForBytes = new Bytes(COMMAND_HEADER + "003300");
        String commandBytes = ByteUtils.hexFromBytes(new Diagnostics.GetState().getByteArray());

        assertTrue(waitingForBytes.equals(commandBytes));

        setRuntime(CommandResolver.RunTime.JAVA);
        Command command = CommandResolver.resolve(waitingForBytes);
        assertTrue(command instanceof Diagnostics.GetState);
    }

    @Test
    public void troubleCodeWithZeroValues() throws CommandParseException {
        Property prop =
                new Property(new Bytes("1D001301000700000000000000020006016A7CF2AEAD").getByteArray());
        Property<TroubleCode> troubleCode =
                new Property(TroubleCode.class, prop);
        assertTrue(troubleCode.getValue().getStatus().equals(""));
    }

    @Test
    public void build() {
        Diagnostics.State.Builder builder = new Diagnostics.State.Builder();

        builder.setMileage(new Property(150000));
        builder.setEngineOilTemperature(new Property(99));
        builder.setSpeed(new Property(60));
        builder.setEngineRPM(new Property(2500));
        builder.setFuelLevel(new Property(.9d));
        builder.setEstimatedRange(new Property(265));
        builder.setWasherFluidLevel(new Property(FluidLevel.FILLED));

        builder.setBatteryVoltage(new Property(12f));
        builder.setAdBlueLevel(new Property(.5f));
        builder.setDistanceSinceReset(new Property(1500));
        builder.setDistanceSinceStart(new Property(10));
        builder.setFuelVolume(new Property(35.5f));

        builder.setAntiLockBraking(new Property(ActiveState.ACTIVE));
        builder.setEngineCoolantTemperature(new Property(20));
        builder.setEngineTotalOperatingHours(new Property(1500.65f));
        builder.setEngineTotalFuelConsumption(new Property(27587.0f));
        builder.setBrakeFluidLevel(new Property(FluidLevel.LOW));
        builder.setEngineTorque(new Property(.2d));
        builder.setEngineLoad(new Property(.1d));
        builder.setWheelBasedSpeed(new Property(65));

        // level8
        builder.setBatteryLevel(new Property(.56d));

        Property msg1 = new Property(new CheckControlMessage(1, new Duration(105592d, Duration.Unit.MINUTES), "Check " +
                "engine", "Alert"));
        Property msg2 = new Property(new CheckControlMessage(1, new Duration(105592d, Duration.Unit.MINUTES), "Check " +
                "engine", "Alertt"));
        builder.addCheckControlMessage(msg1);
        builder.addCheckControlMessage(msg2);

        builder.addTirePressure(new Property(new TirePressure(LocationWheel.FRONT_LEFT, new Pressure(2.31d, Pressure.Unit.BARS))));
        ;
        builder.addTirePressure(new Property(new TirePressure(LocationWheel.FRONT_RIGHT,
                new Pressure(2.31d, Pressure.Unit.BARS))));
        builder.addTirePressure(new Property(new TirePressure(LocationWheel.REAR_RIGHT,
                new Pressure(2.31d, Pressure.Unit.BARS))));
        builder.addTirePressure(new Property(new TirePressure(LocationWheel.REAR_LEFT,
                new Pressure(2.31d, Pressure.Unit.BARS))));

        builder.addTireTemperature(new Property(new TireTemperature(LocationWheel.FRONT_LEFT, new Temperature(40d, Temperature.Unit.CELSIUS))));
        builder.addTireTemperature(new Property(new TireTemperature(LocationWheel.FRONT_RIGHT,
                new Temperature(40d, Temperature.Unit.CELSIUS))));
        builder.addTireTemperature(new Property(new TireTemperature(LocationWheel.REAR_RIGHT, new Temperature(40d, Temperature.Unit.CELSIUS))));
        builder.addTireTemperature(new Property(new TireTemperature(LocationWheel.REAR_LEFT, new Temperature(40d, Temperature.Unit.CELSIUS))));

        builder.addWheelRpm(new Property(new WheelRpm(LocationWheel.FRONT_LEFT, new AngularVelocity(746d, AngularVelocity.Unit.REVOLUTIONS_PER_MINUTE))));
        builder.addWheelRpm(new Property(new WheelRpm(LocationWheel.FRONT_RIGHT, new AngularVelocity(746d, AngularVelocity.Unit.REVOLUTIONS_PER_MINUTE))));
        builder.addWheelRpm(new Property(new WheelRpm(LocationWheel.REAR_RIGHT, new AngularVelocity(746d, AngularVelocity.Unit.REVOLUTIONS_PER_MINUTE))));
        builder.addWheelRpm(new Property(new WheelRpm(LocationWheel.REAR_LEFT, new AngularVelocity(746d, AngularVelocity.Unit.REVOLUTIONS_PER_MINUTE))));

        Property code1 = new Property(new TroubleCode(2,
                "C1116FA",
                "RDU_212FR",
                "PENDING",
                TroubleCode.System.UNKNOWN));
        Property code2 = new Property(new TroubleCode(2,
                "C163AFA",
                "DTR212",
                "PENDING",
                TroubleCode.System.BODY));
        builder.addTroubleCode(code1);
        builder.addTroubleCode(code2);
        builder.setMileageMeters(new Property(150000));

        Diagnostics.State state = builder.build();
        assertTrue(TestUtils.bytesTheSame(state, bytes));
        testState(state);
    }
}
