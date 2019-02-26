package com.highmobility.autoapitest;

import com.highmobility.autoapi.Command;
import com.highmobility.autoapi.CommandResolver;
import com.highmobility.autoapi.EndParking;
import com.highmobility.autoapi.GetParkingTicket;
import com.highmobility.autoapi.ParkingTicket;
import com.highmobility.autoapi.StartParking;
import com.highmobility.autoapi.property.ParkingTicketState;
import com.highmobility.utils.ByteUtils;
import com.highmobility.value.Bytes;

import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ParkingTicketTest {
    Bytes bytes = new Bytes(
            "004701" +
                    "01000401000101" +
                    "02001101000E4265726C696E205061726B696E67" +
                    "03000B0100083634383934323333" +
                    "04000B01000800000160E0EA1388" +
                    "05000B01000800000160E1560840"
    );

    @Test
    public void state() throws ParseException {

        Command command = null;
        try {
            command = CommandResolver.resolve(bytes);
        } catch (Exception e) {
            fail();
        }

        assertTrue(command.is(ParkingTicket.TYPE));
        ParkingTicket state = (ParkingTicket) command;

        assertTrue(((ParkingTicket) command).getState() == ParkingTicketState.STARTED);
        assertTrue(state.getOperatorName().equals("Berlin Parking"));
        assertTrue(state.getOperatorTicketId().equals("64894233"));

        assertTrue(TestUtils.dateIsSame(state.getTicketStartDate(), "2018-01-10T16:32:05"));
        assertTrue(TestUtils.dateIsSame(state.getTicketEndDate(), "2018-01-10T18:30:00"));
    }

    @Test public void build() throws ParseException {
        ParkingTicket.Builder builder = new ParkingTicket.Builder();

        builder.setState(ParkingTicketState.STARTED);
        builder.setOperatorName("Berlin Parking");
        builder.setOperatorTicketId("64894233");
        builder.setTicketStart(TestUtils.getCalendar("2018-01-10T16:32:05"));
        builder.setTicketEnd(TestUtils.getCalendar("2018-01-10T18:30:00"));

        ParkingTicket command = builder.build();
        assertTrue(TestUtils.bytesTheSame(command, bytes));
    }

    @Test public void get() {
        byte[] waitingForBytes = ByteUtils.bytesFromHex("004700");
        byte[] bytes = new GetParkingTicket().getByteArray();
        assertTrue(Arrays.equals(waitingForBytes, bytes));
    }

    @Test public void startParking() throws ParseException {
        Bytes waitingForBytes = new Bytes(
                "004702" +
                        "01001101000E4265726c696e205061726b696e67" +
                        "02000B0100083634383934323333" +
                        "03000B01000800000160E1560840");

        Calendar expected = TestUtils.getCalendar("2018-01-10T18:30:00");
        StartParking command = new StartParking("Berlin Parking", "64894233", expected, null);
        assertTrue(TestUtils.bytesTheSame(command, waitingForBytes));

        command = (StartParking) CommandResolver.resolve(waitingForBytes);

        assertTrue(TestUtils.dateIsSame(command.getStartDate(), "2018-01-10T18:30:00"));
        assertTrue(command.getEndDate() == null);
        assertTrue(command.getOperatorName().equals("Berlin Parking"));
        assertTrue(command.getOperatorTicketId().equals("64894233"));
    }

    @Test public void endParking() {
        byte[] waitingForBytes = ByteUtils.bytesFromHex("004703");

        byte[] bytes = new EndParking().getByteArray();
        assertTrue(Arrays.equals(waitingForBytes, bytes));
    }

    @Test public void state0Properties() {
        Bytes bytes = new Bytes("004701");
        Command state = CommandResolver.resolve(bytes);
        assertTrue(((ParkingTicket) state).getState() == null);
    }
}
