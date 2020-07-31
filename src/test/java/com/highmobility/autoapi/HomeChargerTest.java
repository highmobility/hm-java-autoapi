/*
 * The MIT License
 *
 * Copyright (c) 2014- High-Mobility GmbH (https://high-mobility.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.highmobility.autoapi;

import com.highmobility.autoapi.property.Property;
import com.highmobility.autoapi.value.ActiveState;
import com.highmobility.autoapi.value.Coordinates;
import com.highmobility.autoapi.value.EnabledState;
import com.highmobility.autoapi.value.NetworkSecurity;
import com.highmobility.autoapi.value.PriceTariff;
import com.highmobility.autoapi.value.measurement.ElectricPotentialDifference;
import com.highmobility.utils.ByteUtils;
import com.highmobility.value.Bytes;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomeChargerTest extends BaseTest {
    Bytes bytes = new Bytes(
            COMMAND_HEADER + "006001" +
                    "01000401000102" +
                    "02000401000101" +
                    "03000401000101" +
                    "04000701000441380000" +
                    "05000401000101" +
                    "08000401000101" +
                    "09000F01000C436861726765722037363132" +
                    "0A000401000103" +
                    "0B000D01000A5A57337641524E554265" +
                    "0D000401000101" +
                    "0E00070100043F000000" +
                    "0F00070100043F800000" +
                    "10000701000400000000" +
                    "110013010010404A428F9F44D445402ACF562174C4CE" +
                    "12000D01000A00409000000003455552" +
                    "12001001000D023E99999A0006526970706C65"
    );

    // charge current ok, after that should be max charge current

    @Test
    public void state() {
        Command command = CommandResolver.resolve(bytes);
        HomeCharger.State state = (HomeCharger.State) command;
        testState(state);
    }

    private void testState(HomeCharger.State state) {
        assertTrue(state.getChargingStatus().getValue() == HomeCharger.ChargingStatus.CHARGING);
        assertTrue(state.getAuthenticationMechanism().getValue() == HomeCharger.AuthenticationMechanism.APP);
        assertTrue(state.getPlugType().getValue() == HomeCharger.PlugType.TYPE_2);
        assertTrue(state.getChargingPower().getValue().getValue() == 11.5f);
        assertTrue(state.getSolarCharging().getValue() == ActiveState.ACTIVE);

        assertTrue(state.getWifiHotspotEnabled().getValue() == EnabledState.ENABLED);
        assertTrue(state.getWifiHotspotSSID().getValue().equals("Charger 7612"));
        assertTrue(state.getWiFiHotspotSecurity().getValue() == NetworkSecurity.WPA2_PERSONAL);
        assertTrue(state.getWiFiHotspotPassword().getValue().equals("ZW3vARNUBe"));

        assertTrue(state.getAuthenticationState().getValue() == HomeCharger.AuthenticationState.AUTHENTICATED);
        assertTrue(state.getChargeCurrentDC().getValue().getValue() == .5f);
        assertTrue(state.getMaximumChargeCurrent().getValue().getValue() == 1f);
        assertTrue(state.getMinimumChargeCurrent().getValue().getValue() == 0f);

        assertTrue(state.getCoordinates().getValue().getLatitude() == 52.520008);
        assertTrue(state.getCoordinates().getValue().getLongitude() == 13.404954);

        assertTrue(state.getPriceTariff(PriceTariff.PricingType.STARTING_FEE).getValue().getPrice() == 4.5f);
        assertTrue(state.getPriceTariff(PriceTariff.PricingType.STARTING_FEE).getValue().getCurrency().equals("EUR"));

        assertTrue(state.getPriceTariff(PriceTariff.PricingType.PER_KWH).getValue().getPrice() == .3f);
        assertTrue(state.getPriceTariff(PriceTariff.PricingType.PER_KWH).getValue().getCurrency()
                .equals("Ripple"));

        assertTrue(TestUtils.bytesTheSame(state, bytes));
    }

    @Test
    public void build() {
        HomeCharger.State.Builder builder = new HomeCharger.State.Builder();

        builder.setChargingStatus(new Property(HomeCharger.ChargingStatus.CHARGING));
        builder.setAuthenticationMechanism(new Property(HomeCharger.AuthenticationMechanism.APP));
        builder.setPlugType(new Property(HomeCharger.PlugType.TYPE_2));
        builder.setChargingPowerKW(new Property(11.5f));
        builder.setSolarCharging(new Property(ActiveState.ACTIVE));
        builder.setWifiHotspotEnabled(new Property(EnabledState.ENABLED));
        builder.setWifiHotspotSSID(new Property("Charger 7612"));
        builder.setWiFiHotspotSecurity(new Property(NetworkSecurity.WPA2_PERSONAL));
        builder.setWiFiHotspotPassword(new Property("ZW3vARNUBe"));
        builder.setAuthenticationState(new Property(HomeCharger.AuthenticationState.AUTHENTICATED));
        builder.setChargeCurrentDC(new Property(.5f));
        builder.setMaximumChargeCurrent(new Property(1f));
        builder.setMinimumChargeCurrent(new Property(0f));
        builder.setCoordinates(new Property(new Coordinates(52.520008d, 13.404954d)));

        builder.addPriceTariff(new Property(new PriceTariff(
                PriceTariff.PricingType.STARTING_FEE, 4.5d, "EUR")));

        builder.addPriceTariff(new Property(new PriceTariff(
                PriceTariff.PricingType.PER_KWH, .3d, "Ripple")));

        HomeCharger.State state = builder.build();
        testState(state);
    }

    @Test
    public void get() {
        byte[] waitingForBytes = ByteUtils.bytesFromHex(COMMAND_HEADER + "006000");
        byte[] commandBytes = new HomeCharger.GetState().getByteArray();

        assertTrue(Arrays.equals(waitingForBytes, commandBytes));
    }

    @Test
    public void setChargeCurrent() {
        Bytes waitingForBytes = new Bytes(COMMAND_HEADER + "006001" +
                "0E00070100043f000000");

        Command commandBytes = new HomeCharger.SetChargeCurrent(new ElectricPotentialDifference(.5d, ElectricPotentialDifference.Unit.VOLTS));
        assertTrue(bytesTheSame(commandBytes, waitingForBytes));

        setRuntime(CommandResolver.RunTime.JAVA);
        HomeCharger.SetChargeCurrent command =
                (HomeCharger.SetChargeCurrent) CommandResolver.resolve(waitingForBytes);
        assertTrue(command.getChargeCurrentDC().getValue().getValue() == .5f);
    }

    @Test
    public void setPriceTariffs() {
        Bytes bytes = new Bytes(
                COMMAND_HEADER + "006001" +
                        "12000D01000A00409000000003455552" +
                        "12000D01000A023e99999a0003455552");

        PriceTariff[] tariffs = new PriceTariff[2];

        tariffs[0] = new PriceTariff(PriceTariff.PricingType.STARTING_FEE, 4.5d, "EUR");
        tariffs[1] = new PriceTariff(PriceTariff.PricingType.PER_KWH, .3d, "EUR");

        Command cmd = new HomeCharger.SetPriceTariffs(tariffs);
        assertTrue(bytesTheSame(cmd, bytes));

        setRuntime(CommandResolver.RunTime.JAVA);
        HomeCharger.SetPriceTariffs command =
                (HomeCharger.SetPriceTariffs) CommandResolver.resolve(bytes);
        assertTrue(command.getPriceTariffs().length == 2);

        assertTrue(command.getPriceTariffs()[0].getValue().getPricingType() == PriceTariff.PricingType.STARTING_FEE);
        assertTrue(command.getPriceTariffs()[0].getValue().getCurrency().equals("EUR"));
        assertTrue(command.getPriceTariffs()[0].getValue().getPrice() == 4.5f);

        assertTrue(command.getPriceTariffs()[1].getValue().getPricingType() == PriceTariff.PricingType.PER_KWH);
        assertTrue(command.getPriceTariffs()[1].getValue().getCurrency().equals("EUR"));
        assertTrue(command.getPriceTariffs()[1].getValue().getPrice() == .3f);
    }

    // TODO: 25/09/2019 cannot build this from yml
    /*    @Test
    public void failSamePriceTariffTypes() {
        PriceTariff[] tariffs = new PriceTariff[2];
        tariffs[0] = new PriceTariff(PriceTariff.PricingType.PER_KWH, 4.5f, "EUR");
        tariffs[1] = new PriceTariff(PriceTariff.PricingType.PER_KWH, .3f, "EUR");
        assertThrows(IllegalArgumentException.class, () -> {
            new SetPriceTariffs(tariffs).getByteArray();
        });
    }*/

    @Test
    public void accept1CharTariff() {
        PriceTariff tariff = new PriceTariff(PriceTariff.PricingType.PER_KWH, 4.5d, "E");
        assertTrue(tariff.getCurrency().equals("E"));
    }

    @Test
    public void activateSolarCharging() {
        Bytes waitingForBytes = new Bytes(COMMAND_HEADER + "006001" +
                "05000401000101");
        Command commandBytes = new HomeCharger.ActivateDeactivateSolarCharging(ActiveState.ACTIVE);
        assertTrue(bytesTheSame(commandBytes, waitingForBytes));

        setRuntime(CommandResolver.RunTime.JAVA);
        HomeCharger.ActivateDeactivateSolarCharging command =
                (HomeCharger.ActivateDeactivateSolarCharging)
                        CommandResolver.resolve(waitingForBytes);
        assertTrue(command.getSolarCharging().getValue() == ActiveState.ACTIVE);
    }

    @Test
    public void enableWifi() {
        Bytes waitingForBytes = new Bytes(COMMAND_HEADER + "006001" +
                "08000401000100");

        byte[] commandBytes =
                new HomeCharger.EnableDisableWiFiHotspot(EnabledState.DISABLED).getByteArray();
        assertTrue(waitingForBytes.equals(commandBytes));

        setRuntime(CommandResolver.RunTime.JAVA);
        HomeCharger.EnableDisableWiFiHotspot command =
                (HomeCharger.EnableDisableWiFiHotspot) CommandResolver.resolve
                        (waitingForBytes);
        assertTrue(command.getWifiHotspotEnabled().getValue() == EnabledState.DISABLED);
    }

    @Test
    public void authenticate() {
        Bytes waitingForBytes = new Bytes(COMMAND_HEADER + "006001" +
                "0D000401000100");

        Bytes commandBytes =
                new HomeCharger.AuthenticateExpire(HomeCharger.AuthenticationState.UNAUTHENTICATED);
        assertTrue(waitingForBytes.equals(commandBytes));

        setRuntime(CommandResolver.RunTime.JAVA);
        HomeCharger.AuthenticateExpire command =
                (HomeCharger.AuthenticateExpire) CommandResolver.resolve(waitingForBytes);
        assertTrue(command.getAuthenticationState().getValue() == HomeCharger.AuthenticationState.UNAUTHENTICATED);
    }
}
