package com.highmobility.autoapitest;

import com.highmobility.autoapi.Command;
import com.highmobility.autoapi.CommandParseException;
import com.highmobility.autoapi.CommandResolver;
import com.highmobility.autoapi.CommandWithProperties;
import com.highmobility.autoapi.ParkingBrakeState;
import com.highmobility.autoapi.RooftopState;
import com.highmobility.autoapi.property.IntegerProperty;
import com.highmobility.autoapi.property.Property;
import com.highmobility.autoapi.property.StringProperty;
import com.highmobility.utils.Bytes;

import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

public class PropertyTest {
    String parkingBrakeCommand = "00580101000101";

    @Test public void propertyLength() {
        IntegerProperty property = new IntegerProperty((byte) 0x01, 2, 2);
        assertTrue(Arrays.equals(property.getPropertyBytes(), new byte[]{0x01, 0x00, 0x02, 0x00,
                0x02}));

        String longString =
                "longstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstring" +
                        "longstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstring" +
                        "longstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstringlongstring";

        StringProperty stringProperty = new StringProperty((byte) 0x02, longString);
        assertTrue(stringProperty.getPropertyBytes()[1] == 0x01);
        assertTrue(stringProperty.getPropertyBytes()[2] == 0x4A);
    }

    @Test public void nonce() {
        CommandWithProperties command = getCommandWithSignature();
        byte[] nonce = command.getNonce();
        assertTrue(Arrays.equals(nonce, Bytes.bytesFromHex("324244433743483436")));

        // builder with #signature()
    }

    @Test public void signature() {
        CommandWithProperties command = getCommandWithSignature();
        assertTrue(Arrays.equals(command.getSignature(), Bytes.bytesFromHex
                ("4D2C6ADCEF2DC5631E63A178BF5C9FDD8F5375FB6A5BC05432877D6A00A18F6C749B1D3C3C85B6524563AC3AB9D832AFF0DB20828C1C8AB8C7F7D79A322099E6")));

        ParkingBrakeState.Builder builder = new ParkingBrakeState.Builder();
        builder.setIsActive(true);
        builder.setNonce(Bytes.bytesFromHex("324244433743483436"));
        builder.setSignature(Bytes.bytesFromHex
                ("4D2C6ADCEF2DC5631E63A178BF5C9FDD8F5375FB6A5BC05432877D6A00A18F6C749B1D3C3C85B6524563AC3AB9D832AFF0DB20828C1C8AB8C7F7D79A322099E6"));
        ParkingBrakeState state = builder.build();
        assertTrue(Arrays.equals(state.getBytes(), command.getBytes()));
    }

    @Test public void timestamp() throws ParseException {
        byte[] bytes = Bytes.bytesFromHex(parkingBrakeCommand + "A2000811010A1122000000");
        String expectedDate = "2017-01-10T17:34:00";
        ParkingBrakeState command = (ParkingBrakeState) CommandResolver.resolve(bytes);
        assertTrue(TestUtils.dateIsSame(command.getTimestamp(), expectedDate));

        Calendar calendar = TestUtils.getCalendar(expectedDate);
        ParkingBrakeState.Builder builder = new ParkingBrakeState.Builder();
        builder.setIsActive(true);
        builder.setTimestamp(calendar);
        ParkingBrakeState state = builder.build();
        assertTrue(Arrays.equals(state.getBytes(), bytes));
    }

    @Test public void signedBytes() {
        CommandWithProperties command = getCommandWithSignature();
        byte[] signedBytes = command.getSignedBytes();
        assertTrue(Arrays.equals(signedBytes, Bytes.bytesFromHex
                (parkingBrakeCommand + "A00009324244433743483436")));
    }

    CommandWithProperties getCommandWithSignature() {
        byte[] bytes = Bytes.bytesFromHex
                (parkingBrakeCommand +
                        "A00009324244433743483436A100404D2C6ADCEF2DC5631E63A178BF5C9FDD8F5375FB6A5BC05432877D6A00A18F6C749B1D3C3C85B6524563AC3AB9D832AFF0DB20828C1C8AB8C7F7D79A322099E6");
        try {
            Command command = null;
            try {
                command = CommandResolver.resolve(bytes);
            } catch (Exception e) {
                fail();
            }

            if (command instanceof CommandWithProperties) {
                return (CommandWithProperties) command;
            }

            throw new CommandParseException();
        } catch (CommandParseException e) {
            fail();
            return null;
        }
    }

    @Test public void unknownProperty() throws CommandParseException {
        byte[] bytes = Bytes.bytesFromHex(
                "002501" +
                        "01000101" +
                        "1A000135");

        Command command = null;
        try {
            command = CommandResolver.resolve(bytes);
        } catch (Exception e) {
            fail();
        }

        assertTrue(command.is(RooftopState.TYPE));

        assertTrue(command.getClass() == RooftopState.class);
        RooftopState state = (RooftopState) command;
        assertTrue(state.getDimmingPercentage() == .01f);
        assertTrue(state.getOpenPercentage() == null);
        assertTrue(state.getProperties().length == 2);

        boolean foundUnknownProperty = false;
        boolean foundDimmingProperty = false;

        for (int i = 0; i < state.getProperties().length; i++) {
            Property property = state.getProperties()[i];
            if (property.getPropertyIdentifier() == 0x1A) {
                assertTrue(property.getPropertyLength() == 1);
                assertTrue(Arrays.equals(property.getValueBytes(), new byte[]{0x35}));
                assertTrue(Arrays.equals(property.getPropertyBytes(), Bytes.bytesFromHex
                        ("1A000135")));
                foundUnknownProperty = true;
            } else if (property.getPropertyIdentifier() == 0x01) {
                assertTrue(property.getPropertyLength() == 1);
                assertTrue(Arrays.equals(property.getValueBytes(), new byte[]{0x01}));
                assertTrue(Arrays.equals(property.getPropertyBytes(), Bytes.bytesFromHex
                        ("01000101")));
                foundDimmingProperty = true;
            }
        }

        assertTrue(foundDimmingProperty == true);
        assertTrue(foundUnknownProperty == true);
    }
}
