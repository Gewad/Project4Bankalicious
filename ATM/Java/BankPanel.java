import javax.swing.*;
import java.awt.*;

public class BankPanel extends JPanel{
	JPanel venster;
	JPanel header;
	JPanel midden;
	JPanel footer;
	
	JLabel text;
	JLabel footText;
	JLabel[] mainTekst;
	
	public BankPanel(String[] message){
		//venster = new JPanel();
		header = new JPanel();
		
		//maak midden aan en geef layout
		midden = new JPanel();
		midden.setLayout(new BoxLayout(midden, BoxLayout.Y_AXIS));
		
		//geeft text
		footer = new JPanel();
		text = new JLabel("De Obama.cf bank");
		footText = new JLabel("Druk op * om terug te gaan");
		
		//vul een JLabel array 
		mainTekst = new JLabel[message.length];
		
		for(int i = 0; i < message.length; i++){
			//System.out.println(message[i]);
			mainTekst[i] = new JLabel(message[i], JLabel.CENTER);				//Maak JLabel gelijk aan de message
			mainTekst[i].setFont(mainTekst[i].getFont().deriveFont(40.0f));		//Maak tekst groter
			mainTekst[i].setAlignmentX(Component.CENTER_ALIGNMENT);				//centreer de tekst
			mainTekst[i].setBorder(BorderFactory.createEmptyBorder(80,0,0,0));	//Meer ruimte tussen de tekst

			midden.add(mainTekst[i]);
		}
		
		
		text.setFont(text.getFont().deriveFont(64.0f));
		footText.setFont(footText.getFont().deriveFont(30.0f));
		
		//maak header mooi
		header.setBackground(Color.MAGENTA);
		header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,10,0, Color.BLACK), BorderFactory.createEmptyBorder(20,0,20,0)));
		
		//maak footer mooi
		footer.setBackground(Color.MAGENTA);
		footer.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(10,0,0,0, Color.BLACK), BorderFactory.createEmptyBorder(20,0,20,0)));
		
		midden.setBackground(Color.WHITE);

		//geef layout manager
		this.setLayout(new BorderLayout());
		
		footer.add(footText);
		header.add(text);
		this.add(footer, BorderLayout.SOUTH);
		this.add(header, BorderLayout.NORTH);
		this.add(midden, BorderLayout.CENTER);		
	}
	
	public void setMainTekst(int i, String tekst){
		mainTekst[i].getText();
		mainTekst[i].setText(tekst);
	}
	
	public void setHeader(String headerText){
		text.setText("Welkom " + headerText);
	}
}
	