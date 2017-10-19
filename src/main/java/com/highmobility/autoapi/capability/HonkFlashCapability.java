package com.highmobility.autoapi.capability;

import com.highmobility.autoapi.Command;
import com.highmobility.autoapi.CommandParseException;

/**
 * Created by ttiganik on 13/12/2016.
 */

public class HonkFlashCapability extends FeatureCapability {
    AvailableCapability.Capability honkHornCapability;
    AvailableCapability.Capability flashLightsCapability;
    AvailableCapability.Capability emergencyFlasherCapability;

    public AvailableCapability.Capability getHonkHornCapability() {
        return honkHornCapability;
    }

    public AvailableCapability.Capability getFlashLightsCapability() {
        return flashLightsCapability;
    }

    public AvailableCapability.Capability getEmergencyFlasherCapability() {
        return emergencyFlasherCapability;
    }

    public HonkFlashCapability(byte[] bytes) throws CommandParseException {
        super(Command.Identifier.HONK_FLASH);
        if (bytes.length != 6) throw new CommandParseException();
        honkHornCapability = AvailableCapability.Capability.fromByte(bytes[3]);
        flashLightsCapability = AvailableCapability.Capability.fromByte(bytes[4]);
        emergencyFlasherCapability = AvailableCapability.Capability.fromByte(bytes[5]);
    }

    public HonkFlashCapability(AvailableCapability.Capability honkHornCapability,
                               AvailableCapability.Capability flashLightsCapability,
                               AvailableCapability.Capability emergencyFlasherCapability) {
        super(Command.Identifier.HONK_FLASH);
        this.honkHornCapability = honkHornCapability;
        this.flashLightsCapability = flashLightsCapability;
        this.emergencyFlasherCapability = emergencyFlasherCapability;
    }

    @Override public byte[] getBytes() {
        byte[] bytes = getBytesWithCapabilityCount(3);
        bytes[3] = honkHornCapability.getByte();
        bytes[4] = flashLightsCapability.getByte();
        bytes[5] = emergencyFlasherCapability.getByte();

        return bytes;
    }
}
