/*
 * HMKit Auto API - Auto API Parser for Java
 * Copyright (C) 2018 High-Mobility <licensing@high-mobility.com>
 *
 * This file is part of HMKit Auto API.
 *
 * HMKit Auto API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HMKit Auto API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HMKit Auto API.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.highmobility.autoapi;

import com.highmobility.autoapi.property.ByteEnum;
import com.highmobility.autoapi.property.Property;
import com.highmobility.autoapi.property.PropertyInteger;
import com.highmobility.value.Bytes;

/**
 * The Failure Message capability
 */
public class FailureMessage {
    public static final int IDENTIFIER = Identifier.FAILURE_MESSAGE;

    public static final byte PROPERTY_FAILED_MESSAGE_ID = 0x01;
    public static final byte PROPERTY_FAILED_MESSAGE_TYPE = 0x02;
    public static final byte PROPERTY_FAILURE_REASON = 0x03;
    public static final byte PROPERTY_FAILURE_DESCRIPTION = 0x04;
    public static final byte PROPERTY_FAILED_PROPERTY_IDS = 0x05;

    /**
     * The failure message state
     */
    public static class State extends SetCommand {
        PropertyInteger failedMessageID = new PropertyInteger(PROPERTY_FAILED_MESSAGE_ID, false);
        PropertyInteger failedMessageType = new PropertyInteger(PROPERTY_FAILED_MESSAGE_TYPE, false);
        Property<FailureReason> failureReason = new Property(FailureReason.class, PROPERTY_FAILURE_REASON);
        Property<String> failureDescription = new Property(String.class, PROPERTY_FAILURE_DESCRIPTION);
        Property<Bytes> failedPropertyIDs = new Property(Bytes.class, PROPERTY_FAILED_PROPERTY_IDS);
    
        /**
         * @return Capability identifier of the failed message
         */
        public PropertyInteger getFailedMessageID() {
            return failedMessageID;
        }
    
        /**
         * @return Message type of the failed message
         */
        public PropertyInteger getFailedMessageType() {
            return failedMessageType;
        }
    
        /**
         * @return The failure reason
         */
        public Property<FailureReason> getFailureReason() {
            return failureReason;
        }
    
        /**
         * @return Failure description
         */
        public Property<String> getFailureDescription() {
            return failureDescription;
        }
    
        /**
         * @return Array of failed property identifiers
         */
        public Property<Bytes> getFailedPropertyIDs() {
            return failedPropertyIDs;
        }
    
        /**
         * Understand whether a specific message failed
         *
         * @param capabilityIdentifier The command capability identifier
         * @param propertyIdentifier   The property identifier
         * @return Whether the command failed.
         */
        public boolean getPropertyFailed(Integer capabilityIdentifier, byte propertyIdentifier) {
            if ((getFailedMessageID().getValue() != null && getFailedPropertyIDs().getValue() != null) &&
                    capabilityIdentifier.equals(getFailedMessageID().getValue())) {
                Bytes failedIds = getFailedPropertyIDs().getValue();
    
                for (Byte failedId : failedIds) {
                    if (failedId == propertyIdentifier) return true;
                }
            }
    
            return false;
        }
    
        /**
         * Understand whether a set/get command failed
         *
         * @param identifier The command identifier
         * @param type       The command type
         * @return Whether the command failed.
         */
        public boolean getCommandFailed(Integer identifier, Integer type) {
            if (identifier == getFailedMessageID().getValue() &&
                    type == getFailedMessageType().getValue()) {
                return true;
            }
    
            return false;
        }
    
        State(byte[] bytes) throws CommandParseException {
            super(bytes);
            while (propertyIterator.hasNext()) {
                propertyIterator.parseNext(p -> {
                    switch (p.getPropertyIdentifier()) {
                        case PROPERTY_FAILED_MESSAGE_ID: return failedMessageID.update(p);
                        case PROPERTY_FAILED_MESSAGE_TYPE: return failedMessageType.update(p);
                        case PROPERTY_FAILURE_REASON: return failureReason.update(p);
                        case PROPERTY_FAILURE_DESCRIPTION: return failureDescription.update(p);
                        case PROPERTY_FAILED_PROPERTY_IDS: return failedPropertyIDs.update(p);
                    }
    
                    return null;
                });
            }
        }
    
        private State(Builder builder) {
            super(builder);
    
            failedMessageID = builder.failedMessageID;
            failedMessageType = builder.failedMessageType;
            failureReason = builder.failureReason;
            failureDescription = builder.failureDescription;
            failedPropertyIDs = builder.failedPropertyIDs;
        }
    
        public static final class Builder extends SetCommand.Builder {
            private PropertyInteger failedMessageID;
            private PropertyInteger failedMessageType;
            private Property<FailureReason> failureReason;
            private Property<String> failureDescription;
            private Property<Bytes> failedPropertyIDs;
    
            public Builder() {
                super(IDENTIFIER);
            }
    
            public State build() {
                return new State(this);
            }
    
            /**
             * @param failedMessageID Capability identifier of the failed message
             * @return The builder
             */
            public Builder setFailedMessageID(Property<Integer> failedMessageID) {
                this.failedMessageID = new PropertyInteger(PROPERTY_FAILED_MESSAGE_ID, false, 2, failedMessageID);
                addProperty(this.failedMessageID);
                return this;
            }
            
            /**
             * @param failedMessageType Message type of the failed message
             * @return The builder
             */
            public Builder setFailedMessageType(Property<Integer> failedMessageType) {
                this.failedMessageType = new PropertyInteger(PROPERTY_FAILED_MESSAGE_TYPE, false, 1, failedMessageType);
                addProperty(this.failedMessageType);
                return this;
            }
            
            /**
             * @param failureReason The failure reason
             * @return The builder
             */
            public Builder setFailureReason(Property<FailureReason> failureReason) {
                this.failureReason = failureReason.setIdentifier(PROPERTY_FAILURE_REASON);
                addProperty(this.failureReason);
                return this;
            }
            
            /**
             * @param failureDescription Failure description
             * @return The builder
             */
            public Builder setFailureDescription(Property<String> failureDescription) {
                this.failureDescription = failureDescription.setIdentifier(PROPERTY_FAILURE_DESCRIPTION);
                addProperty(this.failureDescription);
                return this;
            }
            
            /**
             * @param failedPropertyIDs Array of failed property identifiers
             * @return The builder
             */
            public Builder setFailedPropertyIDs(Property<Bytes> failedPropertyIDs) {
                this.failedPropertyIDs = failedPropertyIDs.setIdentifier(PROPERTY_FAILED_PROPERTY_IDS);
                addProperty(this.failedPropertyIDs);
                return this;
            }
        }
    }

    public enum FailureReason implements ByteEnum {
        UNSUPPORTED_CAPABILITY((byte) 0x00),
        UNAUTHORISED((byte) 0x01),
        INCORRECT_STATE((byte) 0x02),
        EXECUTION_TIMEOUT((byte) 0x03),
        VEHICLE_ASLEEP((byte) 0x04),
        INVALID_COMMAND((byte) 0x05),
        PENDING((byte) 0x06),
        RATE_LIMIT((byte) 0x07);
    
        public static FailureReason fromByte(byte byteValue) throws CommandParseException {
            FailureReason[] values = FailureReason.values();
    
            for (int i = 0; i < values.length; i++) {
                FailureReason state = values[i];
                if (state.getByte() == byteValue) {
                    return state;
                }
            }
    
            throw new CommandParseException();
        }
    
        private byte value;
    
        FailureReason(byte value) {
            this.value = value;
        }
    
        @Override public byte getByte() {
            return value;
        }
    }
}