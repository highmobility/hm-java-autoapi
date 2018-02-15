package com.highmobility.autoapitest;

import com.highmobility.autoapi.Command;
import com.highmobility.autoapi.CommandResolver;
import com.highmobility.autoapi.GetTheftAlarmState;
import com.highmobility.autoapi.SetTheftAlarm;
import com.highmobility.autoapi.TheftAlarmState;
import com.highmobility.utils.Bytes;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TheftAlarmTest {
    @Test
    public void state() {
        byte[] bytes = Bytes.bytesFromHex(
                "004601" +
                        "01000101");

        Command command = CommandResolver.resolve(bytes);
        if (command == null) fail();

        assertTrue(command.is(TheftAlarmState.TYPE));
        TheftAlarmState state = (TheftAlarmState) command;
        assertTrue(state.getState() == TheftAlarmState.State.ARMED);
    }

    @Test public void get() {
        String waitingForBytes = "004600";
        String commandBytes = Bytes.hexFromBytes(new GetTheftAlarmState().getBytes());
        assertTrue(waitingForBytes.equals(commandBytes));
    }

    @Test public void setAlarm() {
        String waitingForBytes = "00460202";
        String commandBytes = Bytes.hexFromBytes(new SetTheftAlarm(TheftAlarmState.State
                .TRIGGERED).getBytes());
        assertTrue(waitingForBytes.equals(commandBytes));
    }
}
