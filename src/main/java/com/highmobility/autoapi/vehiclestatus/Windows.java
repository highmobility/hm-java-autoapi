package com.highmobility.autoapi.vehiclestatus;
import com.highmobility.autoapi.Command.Identifier;
import com.highmobility.autoapi.CommandParseException;
import com.highmobility.autoapi.WindowState;

/**
 * Created by ttiganik on 14/12/2016.
 */

public class Windows extends FeatureState {
    WindowState[] windowStates;

    /**
     *
     * @return The states for the windows.
     */
    public WindowState[] getWindowStates() {
        return windowStates;
    }

    public Windows(byte[] bytes) throws CommandParseException {
        super(Identifier.WINDOWS);

        if (bytes.length < 4) throw new CommandParseException();

        int stateCountPosition = 3;
        int stateCount = bytes[stateCountPosition];

        windowStates = new WindowState[stateCount];
        int statePosition = stateCountPosition + 1;

        for (int i = 0; i < stateCount; i++) {
            WindowState.Location location = WindowState.Location.fromByte(bytes[statePosition]);
            WindowState.Position position = WindowState.Position.fromByte(bytes[statePosition + 1]);

            WindowState state = new WindowState(location, position);
            windowStates[i] = state;

            statePosition+=2;
        }
    }
}