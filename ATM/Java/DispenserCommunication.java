import arduino.*;
import java.util.Scanner;

public class DispenserCommunication{
	private Arduino arduino;
	
	public DispenserCommunication(){
		arduino = new Arduino("COM6", 9600);
		System.out.println("connecting to dispenser...");
		/*arduino.setPortDescription("COM3");
		arduino.setBaudRate(9600);*/
		System.out.println("Connected to dispenser: " + arduino.openConnection());
	}
	
	public void dispenseMoney(int vijf, int tien, int twintig, int vijftig){
		String vijfString = String.format("%02d", vijf);
		String tienString = String.format("%02d", tien);
		String twintigString = String.format("%02d", twintig);
		String vijftigString = String.format("%02d", vijftig);
		
		String commandString = "PRINT;" + vijfString + ";" + tienString + ";" + twintigString + ";" + vijftigString;
		arduino.serialWrite(commandString);
	}
}