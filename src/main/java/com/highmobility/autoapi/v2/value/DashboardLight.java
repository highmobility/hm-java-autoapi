// TODO: license
package com.highmobility.autoapi.v2.value;

import com.highmobility.autoapi.v2.CommandParseException;
import com.highmobility.autoapi.v2.property.PropertyValueObject;
import com.highmobility.value.Bytes;

public class DashboardLight extends PropertyValueObject {
    public static final int SIZE = 2;

    Name name;
    State state;

    /**
     * @return The name.
     */
    public Name getName() {
        return name;
    }

    /**
     * @return The state.
     */
    public State getState() {
        return state;
    }

    public DashboardLight(Name name, State state) {
        super(2);
        update(name, state);
    }

    public DashboardLight() {
        super();
    } // needed for generic ctor

    @Override public void update(Bytes value) throws CommandParseException {
        super.update(value);
        if (bytes.length < 2) throw new CommandParseException();

        int bytePosition = 0;
        name = Name.fromByte(get(bytePosition));
        bytePosition += 1;

        state = State.fromByte(get(bytePosition));
    }

    public void update(Name name, State state) {
        this.name = name;
        this.state = state;

        bytes = new byte[getLength()];

        int bytePosition = 0;
        set(bytePosition, name.getByte());
        bytePosition += 1;

        set(bytePosition, state.getByte());
    }

    public void update(DashboardLight value) {
        update(value.name, value.state);
    }

    @Override public int getLength() {
        return 1 + 1;
    }

    public enum Name {
        HIGH_BEAM((byte)0x00),
        LOW_BEAM((byte)0x01),
        HAZARD_WARNING((byte)0x02),
        BRAKE_FAILURE((byte)0x03),
        HATCH_OPEN((byte)0x04),
        FUEL_LEVEL((byte)0x05),
        ENGINE_COOLANT_TEMPERATURE((byte)0x06),
        BATTERY_CHARGING_CONDITION((byte)0x07),
        ENGINE_OIL((byte)0x08),
        POSITION_LIGHTS((byte)0x09),
        FRONT_FOG_LIGHT((byte)0x0a),
        REAR_FOG_LIGHT((byte)0x0b),
        PARK_HEATING((byte)0x0c),
        ENGINE_INDICATOR((byte)0x0d),
        SERVICE_CALL((byte)0x0e),
        TRANSMISSION_FLUID_TEMPERATURE((byte)0x0f),
        TRANSMISSION_FAILURE((byte)0x10),
        ANTI_LOCK_BRAKE_FAILURE((byte)0x11),
        WORN_BRAKE_LININGS((byte)0x12),
        WINDSCREEN_WASHER_FLUID((byte)0x13),
        TIRE_FAILURE((byte)0x14),
        ENGINE_OIL_LEVEL((byte)0x15),
        ENGINE_COOLANT_LEVEL((byte)0x16),
        STEERING_FAILURE((byte)0x17),
        ESC_INDICATION((byte)0x18),
        BRAKE_LIGHTS((byte)0x19),
        ADBLUE_LEVEL((byte)0x1a),
        FUEL_FILTER_DIFF_PRESSURE((byte)0x1b),
        SEAT_BELT((byte)0x1c),
        ADVANCED_BRAKING((byte)0x1d),
        ACC((byte)0x1e),
        TRAILER_CONNECTED((byte)0x1f),
        AIRBAG((byte)0x20),
        ESC_SWITCHED_OFF((byte)0x21),
        LANE_DEPARTURE_WARNING_OFF((byte)0x22);
    
        public static Name fromByte(byte byteValue) throws CommandParseException {
            Name[] values = Name.values();
    
            for (int i = 0; i < values.length; i++) {
                Name state = values[i];
                if (state.getByte() == byteValue) {
                    return state;
                }
            }
    
            throw new CommandParseException();
        }
    
        private byte value;
    
        Name(byte value) {
            this.value = value;
        }
    
        public byte getByte() {
            return value;
        }
    }

    public enum State {
        INACTIVE((byte)0x00),
        INFO((byte)0x01),
        YELLOW((byte)0x02),
        RED((byte)0x03);
    
        public static State fromByte(byte byteValue) throws CommandParseException {
            State[] values = State.values();
    
            for (int i = 0; i < values.length; i++) {
                State state = values[i];
                if (state.getByte() == byteValue) {
                    return state;
                }
            }
    
            throw new CommandParseException();
        }
    
        private byte value;
    
        State(byte value) {
            this.value = value;
        }
    
        public byte getByte() {
            return value;
        }
    }
}