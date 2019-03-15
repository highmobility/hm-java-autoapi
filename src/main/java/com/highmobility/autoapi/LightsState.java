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

import com.highmobility.autoapi.value.Color;
import com.highmobility.autoapi.property.Property;
import com.highmobility.autoapi.value.lights.FogLight;
import com.highmobility.autoapi.value.lights.FrontExteriorLightState;
import com.highmobility.autoapi.value.lights.InteriorLamp;
import com.highmobility.autoapi.value.lights.LightLocation;
import com.highmobility.autoapi.value.lights.ReadingLamp;
import com.highmobility.autoapi.value.Location;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * This command is sent when a Get Lights State message is received by the car.
 */
public class LightsState extends Command {
    public static final Type TYPE = new Type(Identifier.LIGHTS, 0x01);

    private static final byte IDENTIFIER_FRONT_EXTERIOR_LIGHT_STATE = 0x01;
    private static final byte IDENTIFIER_REAR_EXTERIOR_LIGHT_ACTIVE = 0x02;
    private static final byte IDENTIFIER_AMBIENT_COLOR = 0x04;
    private static final byte IDENTIFIER_REVERSE_LIGHT_ACTIVE = 0x05;
    private static final byte IDENTIFIER_EMERGENCY_BRAKE_LIGHT_ACTIVE = 0x06;

    private static final byte IDENTIFIER_FOG_LIGHTS = 0x07;
    private static final byte IDENTIFIER_READING_LAMPS = 0x08;
    private static final byte IDENTIFIER_INTERIOR_LAMPS = 0x09;

    Property<FrontExteriorLightState> frontExteriorLightState =
            new Property(FrontExteriorLightState.class,
                    IDENTIFIER_FRONT_EXTERIOR_LIGHT_STATE);
    Property<Boolean> rearExteriorLightActive =
            new Property(Boolean.class, IDENTIFIER_REAR_EXTERIOR_LIGHT_ACTIVE);

    Property<Color> ambientColor = new Property(Color.class,
            IDENTIFIER_AMBIENT_COLOR);

    // l7
    Property<Boolean> reverseLightActive = new Property(Boolean.class,
            IDENTIFIER_REVERSE_LIGHT_ACTIVE);
    Property<Boolean> emergencyBrakeLightActive =
            new Property(Boolean.class, IDENTIFIER_EMERGENCY_BRAKE_LIGHT_ACTIVE);

    // l9
    Property<FogLight>[] fogLights;
    Property<ReadingLamp>[] readingLamps;
    Property<InteriorLamp>[] interiorLamps;

    /**
     * @return The front exterior light state.
     */
    @Nullable public Property<FrontExteriorLightState> getFrontExteriorLightState() {
        return frontExteriorLightState;
    }

    /**
     * @return The rear exterior light state.
     */
    @Nullable public Property<Boolean> isRearExteriorLightActive() {
        return rearExteriorLightActive;
    }

    /**
     * @return The ambient color, in rgb values.
     */
    @Nullable public Property<Color> getAmbientColor() {
        return ambientColor;
    }

    /**
     * @return The reverse light state.
     */
    @Nullable public Property<Boolean> isReverseLightActive() {
        return reverseLightActive;
    }

    /**
     * @return The emergency brake light state.
     */
    @Nullable public Property<Boolean> isEmergencyBrakeLightActive() {
        return emergencyBrakeLightActive;
    }

    /**
     * @return The fog lights.
     */
    public Property<FogLight>[] getFogLights() {
        return fogLights;
    }

    /**
     * Get the fog light at a location.
     *
     * @param location The light location.
     * @return The fog light.
     */
    @Nullable public Property<FogLight> getFogLight(LightLocation location) {
        for (Property<FogLight> fogLight : fogLights) {
            if (fogLight.getValue() != null && fogLight.getValue().getLocation() == location)
                return fogLight;
        }

        return null;
    }

    /**
     * @return The reading lamps.
     */
    public Property<ReadingLamp>[] getReadingLamps() {
        return readingLamps;
    }

    /**
     * Get the reading lamp at a location.
     *
     * @param location The light location.
     * @return The reading lamp.
     */
    @Nullable public Property<ReadingLamp> getReadingLamp(Location location) {
        for (Property<ReadingLamp> readingLamp : readingLamps) {
            if (readingLamp.getValue() != null && readingLamp.getValue().getLocation() == location)
                return readingLamp;
        }

        return null;
    }

    /**
     * @return The interior lamps.
     */
    public Property<InteriorLamp>[] getInteriorLamps() {
        return interiorLamps;
    }

    /**
     * Get the interior lamp at a location.
     *
     * @param location The lamp location.
     * @return The interior lamp.
     */
    @Nullable public Property<InteriorLamp> getInteriorLamp(LightLocation location) {
        for (Property<InteriorLamp> interiorLamp : interiorLamps) {
            if (interiorLamp.getValue() != null && interiorLamp.getValue().getLocation() == location)
                return interiorLamp;
        }
        return null;
    }

    LightsState(byte[] bytes) {
        super(bytes);

        ArrayList<Property> fogLights = new ArrayList<>();
        ArrayList<Property> interiorLamps = new ArrayList<>();
        ArrayList<Property> readingLamps = new ArrayList<>();

        while (propertyIterator.hasNext()) {
            propertyIterator.parseNext(p -> {
                switch (p.getPropertyIdentifier()) {
                    case IDENTIFIER_FRONT_EXTERIOR_LIGHT_STATE:
                        return frontExteriorLightState.update(p);
                    case IDENTIFIER_REAR_EXTERIOR_LIGHT_ACTIVE:
                        return rearExteriorLightActive.update(p);
                    case IDENTIFIER_AMBIENT_COLOR:
                        return this.ambientColor.update(p);
                    case IDENTIFIER_REVERSE_LIGHT_ACTIVE:
                        return reverseLightActive.update(p);
                    case IDENTIFIER_EMERGENCY_BRAKE_LIGHT_ACTIVE:
                        return emergencyBrakeLightActive.update(p);
                    case IDENTIFIER_FOG_LIGHTS:
                        Property fogLight = new Property(FogLight.class, p);
                        fogLights.add(fogLight);
                        return fogLight;
                    case IDENTIFIER_READING_LAMPS:
                        Property readingLamp = new Property(ReadingLamp.class, p);
                        readingLamps.add(readingLamp);
                        return readingLamp;
                    case IDENTIFIER_INTERIOR_LAMPS:
                        Property interiorLamp = new Property(InteriorLamp.class, p);
                        interiorLamps.add(interiorLamp);
                        return interiorLamp;
                }

                return null;
            });

            this.fogLights = fogLights.toArray(new Property[0]);
            this.readingLamps = readingLamps.toArray(new Property[0]);
            this.interiorLamps = interiorLamps.toArray(new Property[0]);
        }
    }

    @Override public boolean isState() {
        return true;
    }

    private LightsState(Builder builder) {
        super(builder);
        frontExteriorLightState = builder.frontExteriorLightState;
        rearExteriorLightActive = builder.rearExteriorLightActive;
        ambientColor = builder.ambientColor;
        reverseLightActive = builder.reverseLightActive;
        emergencyBrakeLightActive = builder.emergencyBrakeLightActive;
        fogLights = builder.fogLights.toArray(new Property[0]);
        readingLamps = builder.readingLamps.toArray(new Property[0]);
        interiorLamps = builder.interiorLamps.toArray(new Property[0]);
    }

    public static final class Builder extends Command.Builder {
        private Property<FrontExteriorLightState> frontExteriorLightState;
        private Property<Boolean> rearExteriorLightActive;
        private Property<Color> ambientColor;

        private Property<Boolean> reverseLightActive;
        private Property<Boolean> emergencyBrakeLightActive;

        private ArrayList<Property<FogLight>> fogLights = new ArrayList<>();
        private ArrayList<Property<ReadingLamp>> readingLamps = new ArrayList<>();
        private ArrayList<Property<InteriorLamp>> interiorLamps = new ArrayList<>();

        public Builder() {
            super(TYPE);
        }

        /**
         * @param frontExteriorLightState The front exterior light state.
         * @return The builder.
         */
        public Builder setFrontExteriorLightState(Property<FrontExteriorLightState> frontExteriorLightState) {
            this.frontExteriorLightState = frontExteriorLightState;
            addProperty(frontExteriorLightState.setIdentifier(IDENTIFIER_FRONT_EXTERIOR_LIGHT_STATE));
            return this;
        }

        /**
         * @param rearExteriorLightActive Whether exterior lights are active.
         * @return The builder.
         */
        public Builder setRearExteriorLightActive(Property<Boolean> rearExteriorLightActive) {
            this.rearExteriorLightActive = rearExteriorLightActive;
            rearExteriorLightActive.setIdentifier(IDENTIFIER_REAR_EXTERIOR_LIGHT_ACTIVE);
            addProperty(rearExteriorLightActive);
            return this;
        }

        /**
         * @param ambientColor The ambient color, in rgba values from 0-255.
         * @return The builder.
         */
        public Builder setAmbientColor(Property<Color> ambientColor) {
            this.ambientColor = ambientColor;
            addProperty(ambientColor.setIdentifier(IDENTIFIER_AMBIENT_COLOR));
            return this;
        }

        /**
         * @param reverseLightActive The reverse light state.
         * @return The builder.
         */
        public Builder setReverseLightActive(Property<Boolean> reverseLightActive) {
            this.reverseLightActive = reverseLightActive;
            reverseLightActive.setIdentifier(IDENTIFIER_REVERSE_LIGHT_ACTIVE);
            addProperty(reverseLightActive);
            return this;
        }

        /**
         * @param emergencyBrakeLightActive The emergency brake light state.
         * @return The builder.
         */
        public Builder setEmergencyBrakeLightActive(Property<Boolean> emergencyBrakeLightActive) {
            this.emergencyBrakeLightActive = emergencyBrakeLightActive;
            emergencyBrakeLightActive.setIdentifier(IDENTIFIER_EMERGENCY_BRAKE_LIGHT_ACTIVE);
            addProperty(emergencyBrakeLightActive);
            return this;
        }

        /**
         * Add a fog light.
         *
         * @param fogLight The fog light.
         * @return The builder.
         */
        public Builder addFogLight(Property<FogLight> fogLight) {
            fogLight.setIdentifier(IDENTIFIER_FOG_LIGHTS);
            this.fogLights.add(fogLight);
            addProperty(fogLight);
            return this;
        }

        /**
         * @param fogLights The lights.
         * @return The builder.
         */
        public Builder setFogLights(Property<FogLight>[] fogLights) {
            this.fogLights.clear();

            for (Property<FogLight> lamp : fogLights) {
                addFogLight(lamp);
            }

            return this;
        }

        /**
         * Add a reading lamp.
         *
         * @param readingLamp The fog light.
         * @return The builder.
         */
        public Builder addReadingLamp(Property<ReadingLamp> readingLamp) {
            readingLamp.setIdentifier(IDENTIFIER_READING_LAMPS);
            this.readingLamps.add(readingLamp);
            addProperty(readingLamp);
            return this;
        }

        /**
         * @param readingLamps The lights.
         * @return The builder.
         */
        public Builder setReadingLamps(Property<ReadingLamp>[] readingLamps) {
            this.readingLamps.clear();

            for (Property<ReadingLamp> lamp : readingLamps) {
                addReadingLamp(lamp);
            }

            return this;
        }

        /**
         * Add a interior lamp.
         *
         * @param interiorLamp The fog light.
         * @return The builder.
         */
        public Builder addInteriorLamp(Property<InteriorLamp> interiorLamp) {
            interiorLamp.setIdentifier(IDENTIFIER_INTERIOR_LAMPS);
            this.interiorLamps.add(interiorLamp);
            addProperty(interiorLamp);
            return this;
        }

        /**
         * @param interiorLamps The lights.
         * @return The builder.
         */
        public Builder setInteriorLamps(Property<InteriorLamp>[] interiorLamps) {
            this.interiorLamps.clear();

            for (Property<InteriorLamp> lamp : interiorLamps) {
                addInteriorLamp(lamp);
            }

            return this;
        }

        public LightsState build() {
            return new LightsState(this);
        }
    }
}