import javax.swing.*;
import java.awt.*;

public class BankFrame extends JFrame{
	JFrame frame;
	
	BankPanel rfidScherm;
	BankPanel pinScherm; 
	BankPanel homeScherm;
	BankPanel changePinScherm;
	BankPanel withdrawScherm;
	BankPanel saldoScherm;
	BankPanel rekeningSelectie;
	BankPanel transactieScherm;
	BankPanel selectSaldoScherm;
	
	String[] backMessage = {"[A] Keer terug naar het hoofdmenu","[B] Keer terug naar het inlogscherm"};
	
	//constructor
	public BankFrame(){
		frame = new JFrame();
		
		//Maak frame fullscreen
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		//sluit programma als scherm gesloten word
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//geef text die op het scherm moet staan
		String[] rfidMessage = {"Houd uw pas voor de scanner"};
		String[] pinMessage = {"Voer uw pin in", "_ _ _ _", "druk op # om te bevestigen"};
		String[] menuMessage = {"[A] Toon saldo", "[B] Neem geld op", "[C] Verander pincode", "[D] Maak transactie"};
		String[] changePinMessage = {"Voer uw nieuwe pincode in", "_ _ _ _", "druk op # om te bevestigen"};
		String[] saldoMessage = {"Uw huidige saldo is:", ""};
		String[] withdrawMessage = {"Hoeveel wilt u opnemen", "____", "druk op # om te bevestigen"};
		String[] selectSaldoMessage = {"Selecteer wat u wil opnemen", "[A] 0 x \u20ac5,-" , "[B] 0 x \u20ac10,-", "[C] 0 x \u20ac20,-" , "[D] 0 x \u20ac50,-", "Druk op # om te bevestigen en 0 om te resetten"};
		String[] rekeningSelectieMessage = new String[9];
		for(int i = 0; i < rekeningSelectieMessage.length; i++){
			rekeningSelectieMessage[i] = "";
		}
		String[] transactieMessage = {"Naar welke rekening wilt u geld overmaken","BG__ - ____ - ____", "druk op # om te bevestigen"};
		
		//maak panels aan voor elk scherm
		rfidScherm = new BankPanel(rfidMessage);
		pinScherm = new BankPanel(pinMessage);
		homeScherm = new BankPanel(menuMessage);
		changePinScherm = new BankPanel(changePinMessage);
		withdrawScherm = new BankPanel(withdrawMessage);
		saldoScherm = new BankPanel(saldoMessage);
		rekeningSelectie = new BankPanel(rekeningSelectieMessage);
		transactieScherm = new BankPanel(transactieMessage);
		selectSaldoScherm = new BankPanel(selectSaldoMessage);
		
		frame.add(rfidScherm);

		frame.validate();
		frame.setVisible(true);
		System.out.println("GUI loaded");
	}
	
	
	public void showPinScherm(){
		frame.getContentPane().removeAll(); 	//Maak scherm leeg
		frame.repaint();						//en ververs
		
		pinScherm.setMainTekst(1, "_ _ _ _");	//zorg dat het scherm text laat zien op regel 1
		frame.add(pinScherm);					//Voeg pinScherm toe aan het frame
		frame.validate();						//Teken het scherm opnieuw
	}
	public void veranderInvoerPin(int pinLength, int progamStatus){ //aangeroepen als de gebruiker een getal in voert
		String toonPin;												//Dit is de string die getoont wordt
		BankPanel currentScreen;									//Dit is het scherm waar de gebruiker is en de tekst aangepast moet worden
		if (progamStatus == 1){										
			currentScreen = pinScherm;								//Als de progamStatus 1 is zit de gebruiker op het "pinScherm"
		}else{
			currentScreen = changePinScherm;						//Anders is de gebruiker op het "changePinScherm"
		}
		if(pinLength == 0){   										
			toonPin = "_ _ _ _";									//Als de lengte van de ingevoerde pin 0 is toont hij deze message
			currentScreen.midden.setBackground(Color.WHITE);		//en zorgt hij dat het scherm een witte achtergrond heeft
		} else if(pinLength == 1){
			toonPin = "* _ _ _";									//Als de lengte van de ingevoerde pin 1 toont hij deze message
		} else if(pinLength == 2){
			toonPin = "* * _ _";									//Als de lengte van de ingevoerde pin 2 toont hij deze message
		} else if(pinLength == 3){
			toonPin = "* * * _";									//Als de lengte van de ingevoerde pin 3 toont hij deze message
		}else if(pinLength == 4){
			toonPin = "* * * *";									//Als de lengte van de ingevoerde pin 4 toont hij deze message
		} else {
			toonPin = "Voer een kortere pincode in";				//Als de lengte van de ingevoerde pin groter dan 4 toon deze error tekst
			currentScreen.midden.setBackground(Color.RED);			//en maak de achtergrond van rood
		}

		currentScreen.mainTekst[1].setText(toonPin);				//Pas de tekst aan naar wat in de if statement is gegeven aan "toonPin"
		frame.validate();											//Teken het scherm opnieuw
		
	}
	//hier wordt het frame leeg gehaald en een nieuw panel toegevoeg
	public void showWithdrawScherm(){
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.add(withdrawScherm);
		frame.validate();
	}
	
	public void showRfidScherm(){
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.add(rfidScherm);
		frame.validate();
	}
	
	public void showSelectSaldoScherm(){
		frame.getContentPane().removeAll();
		frame.repaint();
		frame.add(selectSaldoScherm);
		frame.validate();
	}
	
	public void showBackMenu(){
		BankPanel backMenu = new BankPanel(backMessage);
		frame.add(backMenu);
		frame.validate();
	}
	
	public void showHomeScherm(String name){
		frame.getContentPane().removeAll();
		frame.repaint();
		homeScherm.setHeader(name); //Hier wordt de naam van de ingelogde gebruiker in de header gezet om te tonen
		frame.add(homeScherm);
		frame.validate();
	}
	
	public void showSaldoScherm(int saldo){
		frame.getContentPane().removeAll();
		frame.repaint();
		saldoScherm.mainTekst[1].setText("\u20ac"+ saldo); //Hier wordt het huidige saldo op het scherm gezet om te tonen
		frame.add(saldoScherm);
		frame.validate();
	}
	
	public void showChangePinScherm(int newOrOldPin){
		frame.getContentPane().removeAll();
		frame.repaint();
		
		if(newOrOldPin == 0){											//deze if statement kijkt of de gebruiker een zijn oude pin of een nieuwe pin
			changePinScherm.mainTekst[0].setText("Voer nieuwe pin in");	//en laat de correcte tekst aan de gebruiker zien
		} else {
			changePinScherm.mainTekst[0].setText("Voer oude pin in");

		}
		
		frame.add(changePinScherm);
		frame.validate();
	}
	
	public void updateSelectSaldo(int line, int amount){
		if(line == 1){
			selectSaldoScherm.mainTekst[line].setText("[A] " + amount + " x \u20ac5,-");
		} else if(line == 2){
			selectSaldoScherm.mainTekst[line].setText("[B] " + amount + " x \u20ac10,-");
		} else if(line == 3){
			selectSaldoScherm.mainTekst[line].setText("[C] " + amount + " x \u20ac20,-");
		} else if(line == 4){
			selectSaldoScherm.mainTekst[line].setText("[D] " + amount + " x \u20ac50,-");
		}
		frame.validate();
	}
	
	public void showInput(String invoer, int withdrawOrTransfer){
		BankPanel currentScreen;
		if (withdrawOrTransfer == 1){								//check of de gebruiker geld overmaakt of opneemt
			currentScreen = transactieScherm;
		} else {
			currentScreen = withdrawScherm;
		}
		
		if(invoer.equals("")){  									
			currentScreen.mainTekst[1].setText("____");				//Als de er niks ingevoerd laat dan dit zien 
		}else{
			currentScreen.mainTekst[1].setText(invoer);				//laat anders de door de gebruiker ingevoerde cijfers zien
		}	
	frame.validate();
	}
	
	public void showMelding(String melding, boolean error){			//Als er een melding aan de gebruiker getoond moet worden wordt deze methode gebruikt
		String[] meldingMessage = {"", melding};					//maak een String Array met de foutmelding zodat de BankPanel deze kan toevoegen 
		BankPanel meldingScherm; 									//maak een nieuw Panel aan
		meldingScherm = new BankPanel(meldingMessage);				//initialiseer de nieuwe panel met de String array
		
		if (error){													
			meldingScherm.midden.setBackground(Color.RED);			//als de melding een Error is maak de achtergrond rood
		} else {
			meldingScherm.midden.setBackground(Color.GREEN);		//als het geen error is maak de achtergrond groen
		}
		
		frame.add(meldingScherm);
		frame.validate();											//teken het scherm opnieuw

		System.out.println("Melding: " + melding);
		
		try{
			Thread.sleep(2000);										//wacht 2 seconden
		} catch (InterruptedException e){
			System.out.println("EXCEPTION");
		}
		frame.remove(meldingScherm);								//verwijder daarna de melding van het scherm
		validate();													//en teken het scherm opnieuw waardoor het originele scherm weer te zien is
	}
	
	
	public void showRekeningSelectie(String[] rekeningen){
		frame.getContentPane().removeAll();
		frame.repaint();
		for(int i = 0; i < 9; i++){
			if(i < rekeningen.length){ //kijk of i nog een rekening is in de string
				//maak de rekening in een string van het formaat "BGxx-xxx-xxx"
				String rekeningLine = (i+1) + ". " + rekeningen[i].substring(0,4) + " - " + rekeningen[i].substring(4,7) + " - " + rekeningen[i].substring(7,10);
				rekeningSelectie.mainTekst[i].setText(rekeningLine); //voeg deze tekst toe aan het scher,
			} else { //als 
				rekeningSelectie.mainTekst[i].setText("");
			}
		}
		frame.add(rekeningSelectie);
		frame.validate();
	}
	
	public void showRekeningInput(String rekeningInput){
		String rekeningLine = "";
		for(int i = 0; i < 10; i++){
			if(i < rekeningInput.length()){
				rekeningLine = rekeningLine + rekeningInput.charAt(i);
			} else {
				rekeningLine = rekeningLine + "_";
			}
			if (i == 3 || i == 6){
				rekeningLine = rekeningLine + " - ";
			}
		}
		
		transactieScherm.mainTekst[1].setText(rekeningLine);
		frame.validate();
	}
	
	public void showTransactieScherm(boolean targetRekeningSelected){
		frame.getContentPane().removeAll();
		frame.repaint();
		if(targetRekeningSelected){
			transactieScherm.mainTekst[0].setText("Geef het bedrag wat u wil overmaken");
		} else {
			transactieScherm.mainTekst[0].setText("Naar welke rekening wilt u geld overmaken");
		}
		frame.add(transactieScherm);
		frame.validate();
	}
}