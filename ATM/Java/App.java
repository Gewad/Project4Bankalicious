import javax.swing.*;
import java.awt.*;
import java.lang.Integer;

public class App{
	public static void main(String[] args) throws Exception {
		//setup
		App app = new App();
		ServerCommunication server = new ServerCommunication("145.24.222.245", 8989, "BG100002");//SERVER
		//ServerCommunication server = new ServerCommunication("145.24.235.86", 20000, "BG100002");//PAULS LAPTOP
		//Authenticate authenticate = new Authenticate("3CE10685","1225");
		DispenserCommunication dispenser = new DispenserCommunication();
		SerialTest communicatie = new SerialTest();
		BankFrame scherm = new BankFrame();
		communicatie.initialize();
		System.out.println("Started");
		
		String recievedData;
		String tag;
		String UID = "";
		String rekening = "";
		String targetRekening = "BG";
		String pinString = "";
		String tempPin = "";
		String pinCorrect = "";
		String saldoInvoer = ""; 
		
		int nextProgramStatus = 0;
		int programStatus = 0;
		int memProgramStatus = 0;
		int newOrOldPin = 0;
		int withdrawState = 0;
		int pinLength = 0;
		int vijfEuro = 0;
		int tienEuro = 0;
		int twintigEuro = 0;
		int vijftigEuro = 0;
		
		boolean targetRekeningSelected = false;
		
		while (true){ //herhaal het hele programma

		recievedData = communicatie.getData();

				switch (programStatus) {	
					case 0: //lees en controleer UID van RFID kaart 
						if(recievedData.equals("UID")){
							tag = recievedData;
							System.out.println("De tag is: " + tag);
							recievedData = " ";
							Thread.sleep(50); //wacht 50 milliseconde om het volgende bericht op te halen
							recievedData = communicatie.getData();
							System.out.println("De UID is: " + recievedData);
							
							int serverRespons = server.checkUID(recievedData);
							
							if(serverRespons == 0){
								//kaart is herkend
								System.out.println("CARD RECOGNIZED");
								scherm.showPinScherm();
								programStatus = 1;
								UID = recievedData;
								scherm.veranderInvoerPin(pinLength, programStatus);
							} else {
								//er is een foutmelding door de server gegeven toon deze aan de user
								app.showError(serverRespons, scherm);
								scherm.showRfidScherm();
							}
						}
					break;
					//EINDE CASE 0
					
					case 1: //lees en controleer de PIN code
						if (recievedData.equals("KEY")){
							tag = recievedData;
							System.out.println("De tag is: " + tag);
							Thread.sleep(40); //wacht 50 milliseconde om het volgende bericht op te halen
							recievedData = communicatie.getData();
							if(recievedData.equals("*")){
								pinString = "";
								pinLength = 0;
								scherm.veranderInvoerPin(pinLength, programStatus);
								scherm.showRfidScherm();
								programStatus = 0;
							} else if (recievedData.equals("#")){
								int serverRespons = server.checkPin(UID,pinString);
								
								if(serverRespons == 0){
									server.getData(UID, pinString);
									System.out.println("LOGIN SUCCESSFUL");
									pinCorrect = pinString;
									pinString = "";
									pinLength = 0;
									String name = server.getAccountData().getName() + " " + server.getAccountData().getSurname();
									scherm.showHomeScherm(name);
									programStatus = 2;
								} else {
									System.out.println("LOGIN FAILED");
									app.showError(serverRespons, scherm);
									pinString = "";
									pinLength = 0;
									scherm.showRfidScherm();
									programStatus = 0;
								}
								
							} else {
								
								if (checkIfNumber(recievedData)){
								pinString = pinString + recievedData;
								pinLength++;
								scherm.veranderInvoerPin(pinLength, programStatus);
								if(pinLength > 4){
									Thread.sleep(2000);
									pinString = "";
									pinLength = 0;
									scherm.veranderInvoerPin(pinLength, programStatus);
								}
								System.out.println("ingevoerde pin: " + pinString);
								}
							}
							System.out.println("De PIN is: " + recievedData);
						}
					break;
					//EINDE CASE 1
						
					case 2: //homescherm functies
						if (recievedData.equals("KEY")){
							tag = recievedData;
							System.out.println("De tag is: " + tag);
							Thread.sleep(40); //wacht 50 milliseconde om het volgende bericht op te halen
							recievedData = communicatie.getData();
							//haal lijst op met rekeningen
							int rekeningAmount = server.getAccountData().getRekeningAmount();
							
							String[] rekeningen = new String[rekeningAmount];
							
							for(int i = 0; i < rekeningAmount; i++){
								rekeningen[i] = server.getAccountData().getRekening(i);
							}
							
							switch(recievedData){
								case "A": 
									
									scherm.showRekeningSelectie(rekeningen);
									programStatus = 7;
									nextProgramStatus = 3;
									break;
									
								case "B": 
									//laat eerst een rekening selecteren om van af te schrijven
									scherm.showRekeningSelectie(rekeningen);
									programStatus = 7;
									nextProgramStatus = 4;
									break;
									
								case "C": 
									scherm.showChangePinScherm(0);
									System.out.println("Change PIN");
									programStatus = 5;
									break;
									
								case "D":
									
									scherm.showRekeningSelectie(rekeningen);
									programStatus = 7;
									nextProgramStatus = 8;
									break;
									
								case "*": 
									scherm.showRfidScherm();
									programStatus = 0;
									break;
								default:
									break;
							}
							
						}
					break;
					//EINDE CASE 2
						
					case 3: //toon het saldo van de klant
						if (recievedData.equals("KEY")){
							tag = recievedData;
							System.out.println("De tag is: " + tag);
							Thread.sleep(40); //wacht 50 milliseconde om het volgende bericht op te halen
							recievedData = communicatie.getData();
							if(recievedData.equals("*")){
								rekening = "";
								memProgramStatus = programStatus;
								scherm.showBackMenu();
								programStatus = 6;
							}
						}
					break;
					//EINDE CASE 3
					
					case 4: //neem geld op
						if (recievedData.equals("KEY")){
							tag = recievedData;
							System.out.println("De tag is: " + tag);
							Thread.sleep(40); //wacht 50 milliseconde om het volgende bericht op te halen
							recievedData = communicatie.getData();
							if(recievedData.equals("*")){
								saldoInvoer = "";
								scherm.showInput(saldoInvoer, 0);
								memProgramStatus = programStatus;
								scherm.showBackMenu();
								programStatus = 6;
							} 
							switch(withdrawState){
								case 0:
									if(recievedData.equals("#")){
										withdrawState = 1;
										scherm.showPinScherm();
									} else if (recievedData.equals("0")){
										vijfEuro = 0;
										scherm.updateSelectSaldo(1, vijfEuro);
										tienEuro = 0;
										scherm.updateSelectSaldo(2, tienEuro);
										twintigEuro = 0;
										scherm.updateSelectSaldo(3, twintigEuro);
										vijftigEuro = 0;
										scherm.updateSelectSaldo(4, vijftigEuro);
									} else {
										if(recievedData.equals("A")){
											vijfEuro++;
											scherm.updateSelectSaldo(1, vijfEuro);
										} else if (recievedData.equals("B")){
											tienEuro++;
											scherm.updateSelectSaldo(2, tienEuro);
										} else if (recievedData.equals("C")){
											twintigEuro++;
											scherm.updateSelectSaldo(3, twintigEuro);
										} else if (recievedData.equals("D")){
											vijftigEuro++;
											scherm.updateSelectSaldo(4, vijftigEuro);
										}
									}
									break;
								case 1:
									if(recievedData.equals("#")){							
										int serverRespons = server.checkPin(UID, pinString);
										
										if(serverRespons == 0){
											
											dispenser.dispenseMoney(vijfEuro,tienEuro,twintigEuro,vijftigEuro);
											saldoInvoer = String.valueOf((vijfEuro * 5) + (tienEuro * 10) + (twintigEuro * 20) + (vijftigEuro * 50));
											
											System.out.println("SaldoInvoer = " + saldoInvoer);
											serverRespons = server.withdraw(UID, pinCorrect, rekening, saldoInvoer);
											
											if(serverRespons == 0){
												scherm.showMelding("\u20ac"+ saldoInvoer + " opgenomen van uw rekening", false);
												PrinterClass print = new PrinterClass(server.getAccountData().getName(), server.getAccountData().getSurname(), "BG100002", saldoInvoer);
												//print bon
											} else {
												app.showError(serverRespons, scherm);
											}
										
										} else {
											app.showError(serverRespons,scherm);
										}
										String name = server.getAccountData().getName() + " " + server.getAccountData().getSurname();;
										pinString = "";
										pinLength = 0;
										saldoInvoer = "";
										withdrawState = 0;
										vijfEuro = 0;
										scherm.updateSelectSaldo(1, vijfEuro);
										tienEuro = 0;
										scherm.updateSelectSaldo(2, tienEuro);
										twintigEuro = 0;
										scherm.updateSelectSaldo(3, twintigEuro);
										vijftigEuro = 0;
										scherm.updateSelectSaldo(4, vijftigEuro);
										scherm.showInput(saldoInvoer, 0);
										scherm.showHomeScherm(name);								
										programStatus = 2;
									}
									else {
										pinString = pinString + recievedData;
										pinLength++;
										scherm.veranderInvoerPin(pinLength, 1);
											if(pinLength > 4){
												Thread.sleep(2000);
												pinString = "";
												pinLength = 0;
												scherm.veranderInvoerPin(pinLength, programStatus);
											}
									}
									break;
							}
						}
					break;
					//EINDE CASE 4
						
					case 5: //verander pincode
						if (recievedData.equals("KEY")){
							tag = recievedData;
							System.out.println("De tag is: " + tag);
							Thread.sleep(40); //wacht 50 milliseconde om het volgende bericht op te halen
							recievedData = communicatie.getData();
							if(recievedData.equals("*")){
								pinString = "";
								tempPin = "";
								pinLength = 0;
								scherm.veranderInvoerPin(pinLength,programStatus);
								newOrOldPin = 0;
								memProgramStatus = programStatus;
								scherm.showBackMenu();
								programStatus = 6;
							}

							switch(newOrOldPin){
								case 1: //check if old PIN is correct
									if (recievedData.equals("#")){
										
										//roep de server aan om de pincode te weizigen
										int serverRespons = server.changePin(server.getAccountData().getCardID(), tempPin, pinString);
										
										if(serverRespons == 0){ 
											scherm.showMelding("Pin is gewijzigd", false);
											pinCorrect = pinString;
										} else {
											app.showError(serverRespons, scherm);
										}
										//verwijder alle opgeslagen data en keer terug naar het homescherm
										
										tempPin = "";
										pinString = "";
										pinLength = 0;
										scherm.veranderInvoerPin(pinLength,programStatus);
										newOrOldPin = 0;
										String name = server.getAccountData().getName() + " " + server.getAccountData().getSurname();;
										scherm.showHomeScherm(name);
										programStatus = 2;
									
									} else {
										pinString = pinString + recievedData;
										pinLength++;
										scherm.veranderInvoerPin(pinLength, programStatus);
											if(pinLength > 4){
												Thread.sleep(2000);
												pinString = "";
												pinLength = 0;
												scherm.veranderInvoerPin(pinLength, programStatus);
											}
										System.out.println("ingevoerde pin: " + pinString);
									}
								break;
									
								case 0: //Enter new pin
									if (checkIfNumber(recievedData)){
										pinString = pinString + recievedData;
										pinLength++;
										scherm.veranderInvoerPin(pinLength, programStatus);
											if(pinLength > 4){
												Thread.sleep(2000);
												pinString = "";
												pinLength = 0;
												scherm.veranderInvoerPin(pinLength, programStatus);
											}
										} else if(recievedData.equals("#")){
										if(pinLength >= 4){
											tempPin = pinString;
											newOrOldPin = 1;
											scherm.showChangePinScherm(1);
											pinString = "";
											pinLength = 0;
											scherm.veranderInvoerPin(pinLength,programStatus);
										} else {
											//enter longer PIN
										}
									}
								break;
							}
						}
					break;
					//EINDE CASE 5
					
					case 6://terugmenu
						if (recievedData.equals("KEY")){
							tag = recievedData;
							System.out.println("De tag is: " + tag);
							Thread.sleep(40); //wacht 50 milliseconde om het volgende bericht op te halen
							recievedData = communicatie.getData();
							switch(recievedData){
								case "A"://naar hoofdmenu
									
									String name = server.getAccountData().getName() + " " + server.getAccountData().getSurname();;
									scherm.showHomeScherm(name);
									programStatus = 2;
								break;
								case "B"://naar inlogscherm
									scherm.showRfidScherm();
									programStatus = 0;
									pinCorrect = "";
									UID = "";
								break;
								
								default:
								break;
							}
						}
					break;
					//EINDE CASE 6
					
					case 7: //rekening selecteren
						if (recievedData.equals("KEY")){
							tag = recievedData;
							System.out.println("De tag is: " + tag);
							Thread.sleep(40); //wacht 50 milliseconde om het volgende bericht op te halen
							recievedData = communicatie.getData();
							if(recievedData.equals("*")){
								memProgramStatus = programStatus;
								scherm.showBackMenu();
								programStatus = 6;
							} else if(checkIfNumber(recievedData)){ //Er moet een nummer in zijn gedrukt
								
								int selectionInt = java.lang.Integer.parseInt(recievedData, 10);
								selectionInt--;
								
								if(selectionInt < server.getAccountData().getRekeningAmount()){ //zorg ervoor dat de geselecteerd keuze een rekening bevat
									rekening = server.getAccountData().getRekening(selectionInt);	//haal de rekening op uit de plek van de ingedrukte knop
									
									//Er is een rekening geselecteerd ga nu naar de goede programStatus
									if(nextProgramStatus == 3){
										scherm.showSaldoScherm(server.getSaldo(UID, pinCorrect, rekening));
										programStatus = nextProgramStatus;
									} else if (nextProgramStatus == 8){
										scherm.showTransactieScherm(false);
										scherm.showRekeningInput(targetRekening);
										programStatus = nextProgramStatus;
									} else if (nextProgramStatus == 4){
										scherm.showSelectSaldoScherm();
										programStatus = nextProgramStatus;
									}
								} else{
								scherm.showMelding("Fout bij selecteren van rekening", true);
								String name = server.getAccountData().getName() + " " + server.getAccountData().getSurname();;
								scherm.showHomeScherm(name);
								programStatus = 2;
								nextProgramStatus = 0;
								}
							}
						}
					break;
					//EINDE CASE 7
					
					case 8: //geld overmaken
						if (recievedData.equals("KEY")){
							tag = recievedData;
							System.out.println("De tag is: " + tag);
							Thread.sleep(40); //wacht 50 milliseconde om het volgende bericht op te halen
							recievedData = communicatie.getData();
							if(recievedData.equals("*")){
								memProgramStatus = programStatus;
								targetRekening = "BG";
								scherm.showRekeningInput(targetRekening);
								
								targetRekeningSelected = false;
								
								saldoInvoer = "";
								scherm.showInput(saldoInvoer, 1);
								
								scherm.showBackMenu();
								programStatus = 6;

							} else {
								if(targetRekeningSelected){
									if(recievedData.equals("#")){
										//String amount = "0";
										int serverRespons = server.transfer(UID, pinCorrect, rekening, targetRekening, saldoInvoer);
										
										if(serverRespons == 0){
											scherm.showMelding("\u20ac "+ saldoInvoer + " overgemaakt naar " + targetRekening, false);
										} else {
											app.showError(serverRespons, scherm);
										}
										
										saldoInvoer = "";
										scherm.showInput(saldoInvoer, 1);
										
										rekening = "";
										targetRekening = "BG";
										scherm.showRekeningInput(targetRekening);
										
										targetRekeningSelected = false;
										
										String name = server.getAccountData().getName() + " " + server.getAccountData().getSurname();;
										scherm.showHomeScherm(name);
										programStatus = 2;
										
									} else if(checkIfNumber(recievedData)){
										saldoInvoer = saldoInvoer + recievedData;
										scherm.showInput(saldoInvoer, 1);
									}
								} else {
									if(recievedData.equals("#")){
										targetRekeningSelected = true;
										scherm.showTransactieScherm(targetRekeningSelected);
										scherm.showInput(saldoInvoer, 1);
									} else {
										//er staat BG__ - ____ - ____
										targetRekening = targetRekening + recievedData;
										scherm.showRekeningInput(targetRekening);
									}
								}
							}
						}
					break;
					//EINDE CASE 8
					
				}//Sluit de Switch
			
		}//sluit de endless While loop
		
	}//sluit de PSVM
	
	public static boolean checkIfNumber(String recievedData){
		if(recievedData.equals("1") || recievedData.equals("2") || recievedData.equals("3") || recievedData.equals("4") || recievedData.equals("5") || recievedData.equals("6") || recievedData.equals("7") || recievedData.equals("8") || recievedData.equals("9") || recievedData.equals("0")){
			return true;
		} else {
			return false;
		}
	}
	
	public void showError(int errorCode, BankFrame scherm){
		switch(errorCode){
			case 0:
				break;
			case 1: 
				scherm.showMelding("Deze pas is niet herkend", true);
				break;
			case 2:
				scherm.showMelding("Deze pas is geblokkeerd", true);
				break;
			case 3:
				scherm.showMelding("Te weinig saldo", true);
				break;
			case 4:
				scherm.showMelding("Dit rekeningnummer is niet herkend", true);
				break;
			case 5:
				scherm.showMelding("U heeft een ongeldige pincode ingevoerd", true);
				break;
			case 7:
				scherm.showMelding("De pincode is incorrect", true);
				break;
			case 9:
				scherm.showMelding("Overwachte server respons", true);
				break;
			default:
				scherm.showMelding("Er is een onbekende error opgetreden", true);
				break;	
		} //Sluit error switch
		
	}//sluit showError methode
	
}