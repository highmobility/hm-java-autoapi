package com.highmobility.autoapitest;

import com.highmobility.autoapi.Command;
import com.highmobility.autoapi.CommandResolver;
import com.highmobility.autoapi.ControlCommand;
import com.highmobility.autoapi.ControlMode;
import com.highmobility.autoapi.GetControlMode;
import com.highmobility.autoapi.StartControlMode;
import com.highmobility.value.Bytes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RemoteControlTest {
    Bytes bytes = new Bytes(
            "002701" +
                    "01000401000102" +
                    "0200050100020032");

    @Test
    public void controlMode() {
        Command command = CommandResolver.resolve(bytes);
        assertTrue(command.is(ControlMode.TYPE));
        ControlMode state = (ControlMode) command;
        assertTrue(state.getAngle().getValue() == 50);
        assertTrue(state.getMode().getValue() == ControlMode.Value.STARTED);
    }

    @Test public void get() {
        String waitingForBytes = "002700";

        assertTrue(new GetControlMode().equals(waitingForBytes));
        Command command = CommandResolver.resolveHex(waitingForBytes);
        assertTrue(command instanceof GetControlMode);
    }

    @Test public void startControlMode() {
        String waitingForBytes = "002712" +
                "01000401000101";
        StartControlMode command = new StartControlMode(true);
        assertTrue(command.equals(waitingForBytes));
        command = (StartControlMode) CommandResolver.resolveHex(waitingForBytes);
        assertTrue(command.getStart().getValue() == true);
    }

    @Test public void stopControlMode() {
        String waitingForBytes = "002712" +
                "01000401000100";
        assertTrue(new StartControlMode(false).equals(waitingForBytes));
        Command command = CommandResolver.resolveHex(waitingForBytes);
        assertTrue(((StartControlMode) command).getStart().getValue() == false);
    }

    @Test public void controlCommand() {
        String waitingForBytes = "002704" +
                "01000401000103" +
                "0200050100020032";

        assertTrue(new ControlCommand(3, 50).equals(waitingForBytes));

        Command command = CommandResolver.resolveHex(waitingForBytes);
        assertTrue(command instanceof ControlCommand);
        ControlCommand state = (ControlCommand) command;

        assertTrue(state.getAngle().getValue() == 50);
        assertTrue(state.getSpeed().getValue() == 3);
    }

    @Test public void failsWherePropertiesMandatory() {
        TestUtils.errorLogExpected(() -> {
            assertTrue(CommandResolver.resolve(StartControlMode.TYPE.getIdentifierAndType()).getClass() == Command.class);
        });
    }
}
