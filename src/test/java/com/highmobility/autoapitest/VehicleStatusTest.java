package com.highmobility.autoapitest;

import com.highmobility.autoapi.Command;
import com.highmobility.autoapi.CommandParseException;
import com.highmobility.autoapi.CommandResolver;
import com.highmobility.autoapi.ControlMode;
import com.highmobility.autoapi.GetVehicleStatus;
import com.highmobility.autoapi.TrunkState;
import com.highmobility.autoapi.VehicleStatus;
import com.highmobility.autoapi.WindowsState;
import com.highmobility.autoapi.property.CommandProperty;
import com.highmobility.autoapi.property.IntegerProperty;
import com.highmobility.autoapi.property.PowerTrain;
import com.highmobility.autoapi.property.TrunkLockState;
import com.highmobility.autoapi.property.TrunkPosition;
import com.highmobility.utils.Bytes;

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static com.highmobility.autoapi.property.ControlMode.STARTED;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by ttiganik on 15/09/16.
 */
public class VehicleStatusTest {
    byte[] bytes = Bytes.bytesFromHex(
            "0011010100114a46325348424443374348343531383639020001010300065479706520580400064d79204361720500064142433132330600085061636B6167652B07000207E108000C4573746f72696c20426c617509000200DC0A0001050B00010599000B002101010001000200010199000700270101000102"
    );

    com.highmobility.autoapi.VehicleStatus vehicleStatus;

    @Before
    public void setup() {
        Command command = CommandResolver.resolve(bytes);
        if (command != null && command instanceof VehicleStatus) {
            vehicleStatus = (VehicleStatus) command;
        } else {
            fail();
        }
    }

    @Test
    public void states_size() {
        assertTrue(vehicleStatus.getStates().length == 2);
    }

    @Test
    public void properties() {
        assertTrue(vehicleStatus.getVin().equals("JF2SHBDC7CH451869"));
        assertTrue(vehicleStatus.getPowerTrain() == PowerTrain.ALLELECTRIC);
        assertTrue(vehicleStatus.getModelName().equals("Type X"));
        assertTrue(vehicleStatus.getName().equals("My Car"));
        assertTrue(vehicleStatus.getLicensePlate().equals("ABC123"));

        assertTrue(vehicleStatus.getSalesDesignation().equals("Package+"));
        assertTrue(vehicleStatus.getModelYear() == 2017);
        assertTrue(vehicleStatus.getColorName().equals("Estoril Blau"));
        assertTrue(vehicleStatus.getPower() == 220);
        assertTrue(vehicleStatus.getNumberOfDoors() == 5);
        assertTrue(vehicleStatus.getNumberOfSeats() == 5);

        assertTrue(vehicleStatus.getState(TrunkState.TYPE) != null);
    }

    @Test public void get() throws CommandParseException {
        byte[] bytes = Bytes.bytesFromHex("001100");
        byte[] commandBytes = new GetVehicleStatus().getBytes();
        assertTrue(Arrays.equals(bytes, commandBytes));

        Command command = CommandResolver.resolve(bytes);
        assertTrue(command instanceof GetVehicleStatus);
    }

    @Test public void trunkState() {
        Command command = getState(TrunkState.class);
        if (command == null) fail();
        if (command.is(TrunkState.TYPE) == false) fail();
        TrunkState trunkState = (TrunkState) command;
        assertTrue(trunkState.getLockState() == TrunkLockState.UNLOCKED);
        assertTrue(trunkState.getPosition() == TrunkPosition.OPEN);
    }

    @Test public void controlMode() {
        Command command = getState(ControlMode.class);
        if (command == null) fail();
        if (command.is(ControlMode.TYPE) == false) fail();
        ControlMode state = (ControlMode) command;
        assertTrue(state.getMode() == STARTED);
    }

    Command getState(Class forClass) {
        for (int i = 0; i < vehicleStatus.getStates().length; i++) {
            Command command = vehicleStatus.getStates()[i];
            if (command.getClass().equals(forClass)) return command;
        }

        return null;
    }

    VehicleStatus.Builder getVehicleStatusBuilderWithoutSignature() throws
            UnsupportedEncodingException {
        VehicleStatus.Builder builder = new VehicleStatus.Builder();
        builder.setVin("JF2SHBDC7CH451869");
        builder.setPowerTrain(PowerTrain.ALLELECTRIC);
        builder.setModelName("Type X");
        builder.setName("My Car");
        builder.setLicensePlate("ABC123");
        builder.setSalesDesignation("Package+");
        builder.setModelYear(2017);
        builder.setColor("Estoril Blau");
//        builder.setPower(220);
        // add an unknown property (power)
        builder.addProperty(new IntegerProperty((byte) 0x09, 220, 2));
        builder.setNumberOfDoors(5).setNumberOfSeats(5);

        TrunkState.Builder trunkState = new TrunkState.Builder();
        trunkState.setLockState(TrunkLockState.UNLOCKED);
        trunkState.setPosition(TrunkPosition.OPEN);
        builder.addProperty(new CommandProperty(trunkState.build()));

        ControlMode.Builder controlCommand = new ControlMode.Builder();
        controlCommand.setMode(STARTED);
        builder.addProperty(new CommandProperty(controlCommand.build()));
        return builder;
    }

    @Test public void create() throws UnsupportedEncodingException {
        VehicleStatus status = getVehicleStatusBuilderWithoutSignature().build();
        byte[] command = status.getBytes();
        assertTrue(Arrays.equals(command, Bytes.bytesFromHex
                ("0011010100114a46325348424443374348343531383639020001010300065479706520580400064d79204361720500064142433132330600085061636B6167652B07000207E108000C4573746f72696c20426c617509000200DC0A0001050B00010599000B002101010001000200010199000700270101000102")));
    }

    @Test public void createWithSignature() throws UnsupportedEncodingException {
        VehicleStatus.Builder builder = getVehicleStatusBuilderWithoutSignature();
        byte[] nonce = Bytes.bytesFromHex("324244433743483436");
        builder.setNonce(nonce);
        byte[] signature = Bytes.bytesFromHex
                ("4D2C6ADCEF2DC5631E63A178BF5C9FDD8F5375FB6A5BC05432877D6A00A18F6C749B1D3C3C85B6524563AC3AB9D832AFF0DB20828C1C8AB8C7F7D79A322099E6");
        builder.setSignature(signature);

        VehicleStatus status = builder.build();
        byte[] command = status.getBytes();
        assertTrue(Arrays.equals(command, command));
        assertTrue(Arrays.equals(status.getNonce(), nonce));
        assertTrue(Arrays.equals(status.getSignature(), signature));
    }

    @Test public void maiduTest() {
        byte[] bytes = Bytes.bytesFromHex
                ("0011010A000105991B002001010003000100010003010000010003020001010003030001");
        CommandResolver.resolve(bytes);
    }

    @Test public void testFloatOverFlow() {
        byte[] bytes = Bytes.bytesFromHex
                ("00110101001131484D43393342343348454232413433430200010003000661206E616D6504001047656E6572616C20456D756C61746F72050009422D484D2D3637323799003C00230101000100020002001E03000150040004BF19999A050004BF19999A060002000007000200000800016409000200000A0004000000000B00010099001F0053010100010102000100030004001525150300040115251504000319370099004800240101000441B800000200044190000003000441B8000004000441B0000006000100070001000800010009000441B80000050001000A000FF80800080008000800080008000800990083003301010003000BB80200020012030002000004000200000500015006000200C8070004410C000008000440C66666090001000A000B00401333334220000000000A000B01401333334220000000000A000B02401333334220000000000A000B03401333334220000000000B0004414000000C0004000000000D000200000E0002000099001B00200101000300000001000301000001000302000001000303000099000700410101000100990007003501010001009900070040010100010099000700480101000100990011005401010004461C4000020004447A00009900150036010100010002000100030001000400030000FF99000E00340101000201900200030075309900360031010100084252167241569C87020025416C6578616E646572706C61747A2C203130313738204265726C696E2C204765726D616E7999000C005201010002000002000100990007005801010001009900230047010100010002000003000004000812020E12332101A405000812020E12332101A499004B0057010100050000000000010005010000000002000100030001000400010005000100060004000000000700040000000008000100090001000A000200000A000201000B0001000C00010099000C00270101000101020002000099000B00250101000100020001009900210056010100030000000100030100000100030200000100030300000100030400009900070046010100010099000E0050010100081108120F043A01A499000B0021010100010102000100990015003001010008425210E741561BEA0200044252147D9900070055010100010099001C0045010100020000010002010001000203000100020400010002050099002A0042010100010002000100030001000400013205000122060001000700010008000800010107000001A4");
        VehicleStatus vs = (VehicleStatus) CommandResolver.resolve(bytes);
        // one window property will fail to parse
        assertTrue(vs.getState(WindowsState.TYPE).getProperties().length == 5);
        assertTrue(((WindowsState)vs.getState(WindowsState.TYPE)).getWindowProperties().length == 4);
    }
}
