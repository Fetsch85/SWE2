package kamisado.client;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import kamisado.client.anmeldefenster.AnmeldefensterController;
import kamisado.commonClasses.Spielbrett;
import kamisado.commonClasses.Feld;
import kamisado.commonClasses.SendenEmpfangen;
import kamisado.commonClasses.Turm;

public class ClientModel {
	
	protected Spielbrett spielbrett;
	private ClientController controller;

	protected Socket clientSocket;
	private boolean amLaufen = true;
	private static String name;
	private static String pw;
	private static String ipAdresse;
	private int port = 444;
	private int[] ausgewähltesFeldKoordinaten =null;
	private int[] neueKoordinaten= new int[2];
	private Feld f= null;
	private int[] turmKoordinaten;

	
	
	private final Logger logger = Logger.getLogger("");
	
	public ClientModel(ClientController controller) { 
		this.controller = controller;
	}

	public void Verbinden(String ipAdresse, String name, String pw) {
		 String namePW = name + ","+ pw;
		 this.name = AnmeldefensterController.getName();
		 this.pw = AnmeldefensterController.getPasswort();
		try{
			//Verbindung mit Server herstellen
			this.clientSocket = new Socket(ipAdresse, port);
			logger.info(ipAdresse + " über Port " + port + " verbunden");
			
			SendenEmpfangen.Senden(clientSocket, namePW);
			
			//Koordinaten Thread erstellen
			Runnable koord  = new Runnable() {
				@Override
				public void run() {
					
						
						
						while(amLaufen == true){
							try{
							
							KoordinatenEmpfangen();
							logger.info("Koordinaten empfangen auf Client");
														
						}catch (Exception e){
							logger.info(e.toString());
							break;
					}
					}
				}
			}; 
			Thread c = new Thread(koord);
			c.start();
			logger.info("Koordinaten Thread gestartet");
			
		} catch (Exception e){
			logger.info(e.toString());
		}
	}	
	
	public void KoordinatenEmpfangen(){
		int[] AktiverTurmFeld = SendenEmpfangen.EmpfangenInt(clientSocket);
		logger.info("Array erhalten auf Client");
		if(AktiverTurmFeld[2] != 99){
			logger.info("Feld Array");
			neueKoordinaten[0] = AktiverTurmFeld[0];
			neueKoordinaten[1] = AktiverTurmFeld[1];
			logger.info("neues Array erstellt");
		logger.info("Koordinaten empfangen"); 
		Feld f = getFeld(neueKoordinaten);
		controller.zugMachen(f);
		}
		else{
			logger.info("Turm Array");
			neueKoordinaten[0] = AktiverTurmFeld[0];
			neueKoordinaten[1] = AktiverTurmFeld[1];
			logger.info("neues Array erstellt");
			Turm[] türme = Spielbrett.getTürme();
			for(int i = 0; i < türme.length; i++){
				logger.info("Turm wird gesucht");
				if(türme[i].getKoordinaten()[0] == neueKoordinaten[0] &&
						türme[i].getKoordinaten()[1] == neueKoordinaten[1]){
						logger.info("Turm gefunden");
						controller.ersterZug(türme[i]);
				}
			}
		}
	}
	
	public void TurmEmpfangen(){
		Turm t = SendenEmpfangen.EmpfangenTurm(clientSocket);
		Spielbrett.setAktiverTurmKoordinaten(t.getKoordinaten());
		logger.info("Koordinaten empfangen");
		controller.ersterZug(t);
	}
	
	public void KoordinatenSenden(){
		int[] tmpKoordinaten = new int[3];
		tmpKoordinaten[0] = ausgewähltesFeldKoordinaten[0];
		tmpKoordinaten[1] = ausgewähltesFeldKoordinaten[1];
		tmpKoordinaten[0] = 80;
		SendenEmpfangen.Senden(clientSocket, tmpKoordinaten);
		logger.info("Daten gesendet");
	}
	
	public void TurmSenden(){
		int[] tmpTürme = new int[3];
		tmpTürme[0] = turmKoordinaten[0];
		tmpTürme[1] = turmKoordinaten[1];
		tmpTürme[2] = 99;
		SendenEmpfangen.Senden(clientSocket, tmpTürme);
		logger.info("Turm vom Client gesendet");
	}
	
	
	
	public void UpdateSpielfeld(Turm[] türme, Turm[]alteTürme){
	
		
		Platform.runLater(new Runnable(){
					@Override
					public void run(){
					
						spielbrett.getPane().getChildren().removeAll(alteTürme);
						for (int i = 0; i < spielbrett.getFelder().length; i++){
				    		for (int j = 0; j < spielbrett.getFelder().length; j++){
				    			spielbrett.getFelder()[i][j].setFeldBesetzt(false);
				    		}
						}	
						
						
						for(int i = 0; i < türme.length; i++){
							int xKoords = türme[i].getKoordinaten()[0];
							int yKoords = türme[i].getKoordinaten()[1];
							
							// Turmdurchmesser und Turmbreite definieren
							türme[i].setRadius(spielbrett.TURMDURCHMESSER);
							türme[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
							
							// Türme bei den Koordinaten platzieren, Rand- und Füllfarbe definieren und Felder besetzen
							spielbrett.getPane().add(türme[i], xKoords, yKoords);
							türme[i].setFill(Color.valueOf(türme[i].getFüllFarbe()));
							türme[i].setStroke(Color.valueOf(türme[i].getStrokeFarbe()));
							spielbrett.getFelder()[xKoords][yKoords].setFeldBesetzt(true);
							
						}
						Spielbrett.setTürme(türme);
					}
		});
	}
					
	
	public void clientAnhalten(){
		if(clientSocket != null){
			try{
				clientSocket.close();
				logger.info("client Thread beendet");
			} catch (Exception e){
				logger.info(e.toString());
			}
		}
		
		
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getIP(){
		return this.ipAdresse;
	}
	public void setIP(String ipAdresse){
		this.ipAdresse = ipAdresse;
	}
	
	public Feld getFeld(int[] feldKoordinaten){
		
		Feld[][] felder = Spielbrett.getFelder();
		for(int i = 0; i < felder.length; i++){
			for(int j = 0; j < felder.length; j++){
				if(felder[i][j].getKoordinaten() == feldKoordinaten){
					this.f = felder[i][j];
				}
			}
		}
		return f;
		
	}
	
	/** Überprüfen, ob zwei int-Arrays gleich sind
	 * @param Koordinaten des ersten Arrays
	 * @param Koordinaten des zweiten Arrays
	 * @return true oder false
	 */
	public boolean koordVergleich(int[] koord1, int[] koord2){
		if(koord1[0]==koord2[0] && koord1[1]==koord2[1]){
			return true;
		}
		return false;
	}
	
	/** Randbreite aller Türme zurücksetzen
	 * 
	 */
	public void turmStrokeWidthZurücksetzen(){
		for (int i = 0; i < Spielbrett.getTürme().length; i++){
			Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
		}	
	}
	
	/** Spielbrett initialisieren
	 * @param spielbrett
	 */
	public void setSpielbrett(Spielbrett Spielbrett){
		this.spielbrett = Spielbrett;
	}
	
	public Spielbrett getSpielbrett (){
		return this.spielbrett;
	}
	
	/** Turmfarbe (schwarz/weiss) basierend auf dessen Koordinaten herausfinden
	 * @param turmKoordinaten
	 * @param türme
	 * @return Turmfarbe
	 */
	public Color getTurmFarbe(int[]turmKoordinaten, Turm[]türme){
		for (int i = 0; i < türme.length; i++){
			if(koordVergleich(türme[i].getKoordinaten(), turmKoordinaten)==true // ausgewählter Turm herausfinden
					&& (türme[i].getStroke()==Color.BLACK)){				// herausfinden ob der Turm schwarz ist
				return Color.BLACK;
			}
		}
		return Color.WHITE;
	}
	
	/** Den ausgewählten Turm bewegen
	 * @param ausgewähltesFeld
	 * @param k (Position des betroffenen Turms im Turm-Array
	 */
	public void turmBewegen(Feld ausgewähltesFeld, int k){
		int xKoords = ausgewähltesFeld.getKoordinaten()[0];
		int yKoords = ausgewähltesFeld.getKoordinaten()[1];
		// den zu bewegenden Turm von der Gridpane entfernen und das Feld freigeben
		spielbrett.getPane().getChildren().remove(Spielbrett.getTürme()[k]);
		spielbrett.getFelder()[spielbrett.getAktiverTurmKoordinaten()[0]][spielbrett.getAktiverTurmKoordinaten()[1]].setFeldBesetzt(false);

		// neue Koordinaten des Turms setzen, den Turm der Gridpane hinzufügen und das Feld besetzen
		Spielbrett.getTürme()[k].setKoordinaten(ausgewähltesFeld.getKoordinaten());
		spielbrett.setAktiverTurmKoordinaten(ausgewähltesFeld.getKoordinaten());
		spielbrett.getPane().add(Spielbrett.getTürme()[k], xKoords, yKoords);
		spielbrett.getFelder()[xKoords][yKoords].setFeldBesetzt(true);
		
		spielbrett.setTurmBewegt(true);
	}

	/** Zukünftiger gegnerischer Turm definieren
	 * @param k (Position des betroffenen Turms im Turm-Array)
	 * @param ausgewähltesFeld
	 * @param nächsterAktiverTurm
	 * @return aktualisierter nächsterAktiverTurm
	 */
	public int[] setNächsterGegnerischerTurm(int k, Feld ausgewähltesFeld, int[]nächsterAktiverTurm){
		// Nächster gegnerischer Turm falls der vorherige Turm schwarz war
		if(Spielbrett.getTürme()[k].getStroke()==Color.BLACK){
			for (int i = 0; i < Spielbrett.getTürme().length; i++){
				Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);	//Formatierung aller Türme zurücksetzen
				if (Spielbrett.getTürme()[i].getStroke()==Color.WHITE
						&& Spielbrett.getTürme()[i].getFill()==ausgewähltesFeld.getFill()){
					möglicheFelderAnzeigen(Spielbrett.getTürme()[i].getKoordinaten());
					nächsterAktiverTurm = Spielbrett.getTürme()[i].getKoordinaten();
					Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
					if(spielbrett.getMöglicheFelder().size()==0){		
						Spielbrett.setBlockiert(true);
					} else{
						Spielbrett.setBlockadenCounter(0);
						Spielbrett.setBlockadenVerursacher(null);
					}
				}
			}	
		}else{		// Nächster gegnerischer Turm falls der vorherige Turm weiss war
			for (int i = 0; i < Spielbrett.getTürme().length; i++){
				Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);	//Formatierung aller Türme zurücksetzen
				if (Spielbrett.getTürme()[i].getStroke()==Color.BLACK
						&& Spielbrett.getTürme()[i].getFill()==ausgewähltesFeld.getFill()){
					möglicheFelderAnzeigen(Spielbrett.getTürme()[i].getKoordinaten());
					nächsterAktiverTurm = Spielbrett.getTürme()[i].getKoordinaten();
					Spielbrett.getTürme()[i].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
					if(spielbrett.getMöglicheFelder().size()==0){		
						Spielbrett.setBlockiert(true);
					} else{
						Spielbrett.setBlockadenCounter(0);
						Spielbrett.setBlockadenVerursacher(null);
					}
				}
			}	
		}
		return nächsterAktiverTurm;
	}
		
	/** Nächster gegnerischer Turm definieren im Fall einer Blockade
	 * @param aktueller nächsterAktiverTurm
	 * @return Koordinaten des nächsten gegnerischen Turms
	 * @author Raphaela Rey
	 */
	public int[] setNächsterGegnerischerTurmBlockade(int[]nächsterAktiverTurm){
		turmStrokeWidthZurücksetzen();
		if(getTurmFarbe(nächsterAktiverTurm, Spielbrett.getTürme()) == Color.BLACK){
			for (int m = 0; m < Spielbrett.getTürme().length; m++){
				Feld aktivesFeld = spielbrett.getFelder()[nächsterAktiverTurm[0]][nächsterAktiverTurm[1]];
				if (Spielbrett.getTürme()[m].getStroke()==Color.WHITE
						&& (Spielbrett.getTürme()[m].getFill()==aktivesFeld.getFill())){
					nächsterAktiverTurm = Spielbrett.getTürme()[m].getKoordinaten(); 
					möglicheFelderAnzeigen(Spielbrett.getTürme()[m].getKoordinaten());
					Spielbrett.getTürme()[m].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
					
					Spielbrett.setBlockiert(false);
					if(Spielbrett.getBlockadenVerursacher()==Color.WHITE){
						Spielbrett.setBlockadenCounter(0);
					}
					Spielbrett.setBlockadenVerursacher(Color.WHITE); // der Spieler, der als letzter gefahren ist ist der Verursacher
					Spielbrett.setBlockadenCounter(Spielbrett.getBlockadenCounter()+1);
					break;
				}
			} 
		} 
		if(getTurmFarbe(nächsterAktiverTurm, Spielbrett.getTürme()) == Color.WHITE
				&& Spielbrett.isBlockiert()==true){
			for (int m = 0; m < Spielbrett.getTürme().length; m++){
				Spielbrett.getTürme()[m].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
				if (Spielbrett.getTürme()[m].getStroke()==Color.BLACK
						&& Spielbrett.getTürme()[m].getFill()==spielbrett.getFelder()[nächsterAktiverTurm[0]][nächsterAktiverTurm[1]].getFill()){
					nächsterAktiverTurm = Spielbrett.getTürme()[m].getKoordinaten(); 
					möglicheFelderAnzeigen(Spielbrett.getTürme()[m].getKoordinaten());
					Spielbrett.getTürme()[m].setStrokeWidth(spielbrett.STROKEWIDTHAUSGEWÄHLTERTURM);
					
					Spielbrett.setBlockiert(false);
					if(Spielbrett.getBlockadenVerursacher()==Color.BLACK){
						Spielbrett.setBlockadenCounter(0);
					}
					Spielbrett.setBlockadenVerursacher(Color.BLACK);
					Spielbrett.setBlockadenCounter(Spielbrett.getBlockadenCounter()+1);
					break;
				} 
			}
		}										
		return nächsterAktiverTurm;
	}
	
	/** Gewinner definieren
	 * @param ausgewähltesFeld
	 * @return die Gewinnerfarbe oder null, falls niemand gewonnen hat
	 */
	public Color gewinnerDefinieren(Feld ausgewähltesFeld){
		for(int l = 0; l < Spielbrett.GEWINNERFELDERSCHWARZ.length; l++){
			int [] koordGewinnerFeld = {Spielbrett.GEWINNERFELDERSCHWARZ[l][0], Spielbrett.GEWINNERFELDERSCHWARZ[l][1]};
			if(koordVergleich(ausgewähltesFeld.getKoordinaten(), koordGewinnerFeld)){
				return Color.BLACK;
			}
		}	
		for(int m = 0; m < Spielbrett.GEWINNERFELDERWEISS.length; m++){
			int [] koordGewinnerFeld = {Spielbrett.GEWINNERFELDERWEISS[m][0], Spielbrett.GEWINNERFELDERWEISS[m][1]};
			if(koordVergleich(ausgewähltesFeld.getKoordinaten(), koordGewinnerFeld)){
				return Color.WHITE;
			}
		}
		return null;
	}
	
	/** Ganzes Spielbrett zurücksetzen, nachdem jemand gewonnen hat, was folgendes beinhaltet:
	 * - mögliche Felder leeren - Gewinner löschen - turmBesetzt zurücksetzen 
	 * - alle Türme von der Gridpane entfernen und an den ursprünglichen Platz setzen
	 * @param möglicheFelder
	 * @param felder
	 * @param türme
	 */
	public void spielZurücksetzen(ArrayList<int[]> möglicheFelder, Feld[][]felder, Turm[] türme){ 
		möglicheFelderLeeren(möglicheFelder, felder);
		// Gewinner löschen, alle Türme vom Spielbrett entfernen und die Felder freigeben
		spielbrett.setGewinner(null);
		spielbrett.getPane().getChildren().removeAll(türme);
		for (int i = 0; i < spielbrett.getFelder().length; i++){
    		for (int j = 0; j < spielbrett.getFelder().length; j++){
    			spielbrett.getFelder()[i][j].setFeldBesetzt(false);
    		}
		}	
		spielbrett.setTurmBewegt(false);
		// Die Türme an ihren ursprünglichen Platz setzen
		for(int p = 0; p < Spielbrett.getTürme().length; p++){
			if(Spielbrett.getTürme()[p].getStroke()==Color.BLACK){
				for(int l = 0; l < spielbrett.getFelder()[7].length; l++){
					if(Spielbrett.getTürme()[p].getFill()==spielbrett.getFelder()[l][7].getFill()){
						Spielbrett.getTürme()[p].setKoordinaten(spielbrett.getFelder()[l][7].getKoordinaten());
						Spielbrett.getTürme()[p].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
						spielbrett.getPane().add(Spielbrett.getTürme()[p], l, 7);	
						spielbrett.getFelder()[l][7].setFeldBesetzt(true);
					}
				}
			}else{
				for(int l = 0; l < spielbrett.getFelder()[0].length; l++){
					if(Spielbrett.getTürme()[p].getFill()==spielbrett.getFelder()[l][0].getFill()){
						Spielbrett.getTürme()[p].setKoordinaten(spielbrett.getFelder()[l][0].getKoordinaten());
						Spielbrett.getTürme()[p].setStrokeWidth(spielbrett.STROKEWIDTHTÜRMESTANDARD);
						spielbrett.getPane().add(Spielbrett.getTürme()[p], l, 0);	
						spielbrett.getFelder()[l][0].setFeldBesetzt(true);
					}
				}
			}
		}	
	}
	
	/** ArrayList mögliche Felder leeren
	 * @param möglicheFelder
	 * @param felder
	 * @return geleerte ArrayList
	 */
	public ArrayList<int[]> möglicheFelderLeeren(ArrayList<int[]> möglicheFelder, Feld[][]felder){
		ArrayList<int[]> toRemove = new ArrayList<>();
		Iterator<int[]> iter = möglicheFelder.iterator();
		while (iter.hasNext()){
			int[]koords = iter.next();
			int xKoord = koords[0];
			int yKoord = koords[1];
			felder[xKoord][yKoord].setStroke(Color.BLACK);
			felder[xKoord][yKoord].setStrokeWidth(1);
			felder[xKoord][yKoord].setStrokeType(StrokeType.CENTERED);	
			toRemove.add(koords);
		}
		möglicheFelder.removeAll(toRemove);
		return möglicheFelder;		
	}
	
	/** Mögliche Felder (geradeaus, diagonal rechts und diagonal links) anzeigen
	 * Dazu wird zuerst die bestehende liste gelöscht, damit nur keine alten möglichen Felder gespeichert sind. 
	 * Dann werden die aktuellen möglichen Feder mit der Suppportmethode "möglicheFelderHinzufügen" hinzugefügt. 
	 * @param turmKoordinaten
	 * @return ArrayList der möglichen Felder
	 */
	public ArrayList<int[]> möglicheFelderAnzeigen(int[] turmKoordinaten){		
		möglicheFelderLeeren(spielbrett.getMöglicheFelder(), spielbrett.getFelder());
		möglicheFelderHinzufügen(turmKoordinaten, Spielbrett.getTürme(), spielbrett.getFelder(), spielbrett.getMöglicheFelder());
		return spielbrett.getMöglicheFelder();
	}
	
	/** Mögliche Felder der ArrayList hinzufügen (Supportmethode)
	 * @param turmKoordinaten
	 * @param türme
	 * @param felder
	 * @param möglicheFelder
	 */
	private void möglicheFelderHinzufügen(int[] turmKoordinaten, 
			Turm[]türme, Feld[][]felder, ArrayList<int[]> möglicheFelder){
		int xKoords = turmKoordinaten[0];											
		int yKoords = turmKoordinaten[1];	
		// TODO Raphaela Löschen wenn nicht funktioniert
//		for(int i = 1; i < felder[0].length; i++){
//			Feld möglichGeradeaus;
//			Feld möglichDiagRechts;
//			Feld möglichDiagLinks;
//			try {
//				möglichGeradeaus = felder[xKoords][yKoords-i];
//			} catch (ArrayIndexOutOfBoundsException e) {
//				break;
//			}
//			try {
//				möglichDiagRechts = felder[xKoords+i][yKoords-i];
//			} catch (ArrayIndexOutOfBoundsException e) {
//				break;
//			}
//			try {
//				möglichDiagLinks = felder[xKoords-i][yKoords-i];
//			} catch (ArrayIndexOutOfBoundsException e) {
//				break;
//			}
//			
//			Feld[] möglich = {möglichGeradeaus, möglichDiagRechts, möglichDiagLinks};
//			
//			for(int j = 0; j < möglich.length; j++){
//				if(möglich[j].istFeldBesetzt()==true){
//					bereitsEinFeldBesetzt = true;
//				}
//			}
//			for(int k = 0; k < möglich.length; k++){
//				if(möglich[k].istFeldBesetzt()==false
//					&& bereitsEinFeldBesetzt==false){
//					möglicheFelder.add(möglich[k].getKoordinaten());
//					möglich[k].setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
//					möglich[k].setStrokeType(StrokeType.INSIDE);
//				}
//			}	
//		}
		
		
		// Mögliche Felder falls der Turm schwarz ist				
		if((getTurmFarbe(turmKoordinaten, türme)==Color.BLACK)){
			// Mögliche Felder geradeaus
			boolean bereitsEinFeldBesetzt = false;

			for(int i = 1; i < felder[0].length; i++){
				try {
					Feld möglichGeradeaus = felder[xKoords][yKoords-i];
					if(möglichGeradeaus.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichGeradeaus.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						möglicheFelder.add(möglichGeradeaus.getKoordinaten());
						möglichGeradeaus.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichGeradeaus.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			bereitsEinFeldBesetzt = false;
			
			// Mögliche Felder rechts diagonal
			for(int i = 1; i < felder[0].length; i++){
				try {
					Feld möglichDiagRechts = felder[xKoords+i][yKoords-i];
					if(möglichDiagRechts.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichDiagRechts.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						möglicheFelder.add(möglichDiagRechts.getKoordinaten());
						möglichDiagRechts.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichDiagRechts.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			bereitsEinFeldBesetzt = false;

			// Mögliche Felder links diagonal 
			for(int i = 1; i < felder[0].length; i++){
				try {
					Feld möglichDiagLinks = felder[xKoords-i][yKoords-i];
					if(möglichDiagLinks.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichDiagLinks.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						möglicheFelder.add(möglichDiagLinks.getKoordinaten());
						möglichDiagLinks.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichDiagLinks.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
		}
		
		// Mögliche Felder falls der Turm weiss ist
		if((getTurmFarbe(turmKoordinaten, türme)==Color.WHITE)){
			boolean bereitsEinFeldBesetzt = false;
			// Mögliche Felder geradeaus
			for(int i = 1; i < felder[0].length; i++){
				try {
					Feld möglichGeradeaus = felder[xKoords][yKoords+i];
					if(möglichGeradeaus.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichGeradeaus.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						möglicheFelder.add(möglichGeradeaus.getKoordinaten());
						möglichGeradeaus.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichGeradeaus.setStroke(Color.WHITE);
						möglichGeradeaus.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			bereitsEinFeldBesetzt = false;
			// Mögliche Felder rechts diagonal 
			for(int i = 1; i < felder[0].length; i++){
				try {
					Feld möglichDiagRechts = felder[xKoords+i][yKoords+i];
					if(möglichDiagRechts.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichDiagRechts.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						möglicheFelder.add(möglichDiagRechts.getKoordinaten());
						möglichDiagRechts.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichDiagRechts.setStroke(Color.WHITE);
						möglichDiagRechts.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}
			bereitsEinFeldBesetzt = false;
			// Mögliche Felder links diagonal 
			for(int i = 1; i < felder[0].length; i++){
				try {
					Feld möglichDiagLinks = felder[xKoords-i][yKoords+i];
					if(möglichDiagLinks.istFeldBesetzt()==true){
						bereitsEinFeldBesetzt = true;
					}
					if(möglichDiagLinks.istFeldBesetzt()==false
							&& bereitsEinFeldBesetzt==false){
						möglicheFelder.add(möglichDiagLinks.getKoordinaten());
						möglichDiagLinks.setStrokeWidth(spielbrett.STROKEWIDTHMÖGLICHEFELDER);
						möglichDiagLinks.setStroke(Color.WHITE);
						möglichDiagLinks.setStrokeType(StrokeType.INSIDE);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					break;
				}
			}				
		}
	}
	public int[] getAusgewähltesFeldKoordinaaten() {
		return ausgewähltesFeldKoordinaten;
	}
	public void setAusgewähltesFeldKoordinaaten(int[] ausgewähltesFeldKoordinaaten) {
		this.ausgewähltesFeldKoordinaten = ausgewähltesFeldKoordinaaten;
	}
	
	public int[] getTurmKoordinaten() {
		return turmKoordinaten;
	}

	public void setTurmKoordinaten(int[] turmKoordinaten) {
		this.turmKoordinaten = turmKoordinaten;
	}

}
