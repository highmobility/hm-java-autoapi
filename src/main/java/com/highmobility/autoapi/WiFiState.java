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

import com.highmobility.autoapi.property.Property;
import com.highmobility.autoapi.value.EnabledState;
import com.highmobility.autoapi.value.ConnectionState;
import com.highmobility.autoapi.value.NetworkSecurity;

/**
 * The wi fi state
 */
public class WiFiState extends SetCommand {
    public static final Identifier IDENTIFIER = Identifier.WI_FI;

    public static final byte IDENTIFIER_STATUS = 0x01;
    public static final byte IDENTIFIER_NETWORK_CONNECTED = 0x02;
    public static final byte IDENTIFIER_NETWORK_SSID = 0x03;
    public static final byte IDENTIFIER_NETWORK_SECURITY = 0x04;

    Property<EnabledState> status = new Property(EnabledState.class, IDENTIFIER_STATUS);
    Property<ConnectionState> networkConnection = new Property(ConnectionState.class, IDENTIFIER_NETWORK_CONNECTED);
    Property<String> networkSSID = new Property(String.class, IDENTIFIER_NETWORK_SSID);
    Property<NetworkSecurity> networkSecurity = new Property(NetworkSecurity.class, IDENTIFIER_NETWORK_SECURITY);

    /**
     * @return The status
     */
    public Property<EnabledState> getStatus() {
        return status;
    }

    /**
     * @return The network connected
     */
    public Property<ConnectionState> getNetworkConnection() {
        return networkConnection;
    }

    /**
     * @return The network SSID
     */
    public Property<String> getNetworkSSID() {
        return networkSSID;
    }

    /**
     * @return The network security
     */
    public Property<NetworkSecurity> getNetworkSecurity() {
        return networkSecurity;
    }

    WiFiState(byte[] bytes) throws CommandParseException {
        super(bytes);
        while (propertyIterator.hasNext()) {
            propertyIterator.parseNext(p -> {
                switch (p.getPropertyIdentifier()) {
                    case IDENTIFIER_STATUS: return status.update(p);
                    case IDENTIFIER_NETWORK_CONNECTED: return networkConnection.update(p);
                    case IDENTIFIER_NETWORK_SSID: return networkSSID.update(p);
                    case IDENTIFIER_NETWORK_SECURITY: return networkSecurity.update(p);
                }

                return null;
            });
        }
    }

    @Override public boolean isState() {
        return true;
    }

    private WiFiState(Builder builder) {
        super(builder);

        status = builder.status;
        networkConnection = builder.networkConnection;
        networkSSID = builder.networkSSID;
        networkSecurity = builder.networkSecurity;
    }

    public static final class Builder extends SetCommand.Builder {
        private Property<EnabledState> status;
        private Property<ConnectionState> networkConnection;
        private Property<String> networkSSID;
        private Property<NetworkSecurity> networkSecurity;

        public Builder() {
            super(IDENTIFIER);
        }

        public WiFiState build() {
            return new WiFiState(this);
        }

        /**
         * @param status The status
         * @return The builder
         */
        public Builder setStatus(Property<EnabledState> status) {
            this.status = status.setIdentifier(IDENTIFIER_STATUS);
            addProperty(this.status);
            return this;
        }
        
        /**
         * @param networkConnection The network connected
         * @return The builder
         */
        public Builder setNetworkConnection(Property<ConnectionState> networkConnection) {
            this.networkConnection = networkConnection.setIdentifier(IDENTIFIER_NETWORK_CONNECTED);
            addProperty(this.networkConnection);
            return this;
        }
        
        /**
         * @param networkSSID The network SSID
         * @return The builder
         */
        public Builder setNetworkSSID(Property<String> networkSSID) {
            this.networkSSID = networkSSID.setIdentifier(IDENTIFIER_NETWORK_SSID);
            addProperty(this.networkSSID);
            return this;
        }
        
        /**
         * @param networkSecurity The network security
         * @return The builder
         */
        public Builder setNetworkSecurity(Property<NetworkSecurity> networkSecurity) {
            this.networkSecurity = networkSecurity.setIdentifier(IDENTIFIER_NETWORK_SECURITY);
            addProperty(this.networkSecurity);
            return this;
        }
    }
}