package com.highmobility.autoapitest;

import com.highmobility.autoapi.Command;
import com.highmobility.autoapi.CommandResolver;
import com.highmobility.autoapi.GetMaintenanceState;
import com.highmobility.autoapi.MaintenanceState;
import com.highmobility.utils.ByteUtils;
import com.highmobility.value.Bytes;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by ttiganik on 15/09/16.
 */
public class MaintenanceTest {
    @Test
    public void state() {
        Bytes bytes = new Bytes("003401" +
                "01000201F5" +
                "020003000E61");

        Command command = null;
        try {
            command = CommandResolver.resolve(bytes);
        } catch (Exception e) {
            fail();
        }

        assertTrue(command.getClass() == MaintenanceState.class);
        MaintenanceState state = (MaintenanceState) command;
        assertTrue(state.getDaysToNextService() == 501);
        assertTrue(state.getKilometersToNextService() == 3681);
    }

    @Test public void get() {
        String waitingForBytes = "003400";
        String commandBytes = ByteUtils.hexFromBytes(new GetMaintenanceState().getByteArray());
        assertTrue(waitingForBytes.equals(commandBytes));
    }

    @Test public void state0Properties() {
        Bytes bytes = new Bytes("003401");
        Command state = CommandResolver.resolve(bytes);
        assertTrue(((MaintenanceState) state).getKilometersToNextService() == null);
    }

    @Test public void build() {
        byte[] bytes = ByteUtils.bytesFromHex("00340101000201F5020003000E61");

        MaintenanceState.Builder builder = new MaintenanceState.Builder();
        builder.setDaysToNextService(501);
        builder.setKilometersToNextService(3681);
        MaintenanceState state = builder.build();
        byte[] builtBytes = state.getByteArray();
        assertTrue(Arrays.equals(builtBytes, bytes));
        assertTrue(state.getDaysToNextService() == 501);
        assertTrue(state.getKilometersToNextService() == 3681);
        assertTrue(state.getType() == MaintenanceState.TYPE);
    }
}