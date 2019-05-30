package com.highmobility.autoapitest;

import com.highmobility.autoapi.Command;
import com.highmobility.autoapi.CommandResolver;
import com.highmobility.value.Bytes;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandResolverTest {
    @Test public void testTooLittleBytesReturnsCommand() {
        Bytes bytes = new Bytes("0049");
        Command command = CommandResolver.resolve(bytes);
        assertTrue(command != null);
        assertTrue(Arrays.equals(command.getType().getIdentifierAndType(), new byte[]{0x00, 0x49,
                0x00}));
    }

    @Test public void testNullReturnsEmpty() {
        Command command = CommandResolver.resolve((byte[]) null);
        assertTrue(command != null);
        assertTrue(Arrays.equals(command.getType().getIdentifierAndType(), new byte[]{0x00, 0x00,
                0x00}));

        command = CommandResolver.resolve(new Bytes());
        assertTrue(command != null);
        assertTrue(Arrays.equals(command.getType().getIdentifierAndType(), new byte[]{0x00, 0x00,
                0x00}));
    }

    @Test public void testReturnsBaseClassForUnknownBytes() {
        Bytes bytes = new Bytes("002002"); // invalid door lock state
        Command command = CommandResolver.resolve(bytes);
        assertTrue(command instanceof Command);
    }

    @Test public void testInvalidByteLengthReturnsRawCommand() {
        Bytes bytes = new Bytes("0027011188");
        CommandResolver.resolve(bytes);

        Command command = CommandResolver.resolve(bytes);
        assertTrue(command.equals(bytes));
    }

    @Test public void testInvalidByteFormatReturnsRawCommand() {
        Bytes bytes = new Bytes("00270188");
        CommandResolver.resolve(bytes);

        Command command = CommandResolver.resolve(bytes);
        assertTrue(command.equals(bytes));
    }
}
