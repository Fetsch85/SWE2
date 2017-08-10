package kamisado.Server;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kamisado.commonClasses.SendenEmpfangen;
import kamisado.commonClasses.Turm;
import kamisado.commonClasses.Spielbrett;

/**
 * @author Tobias Deprato
 */

public class Client {
	
	private Socket clientSocket;
	private ServerModel model;
//	protected final static ObservableList<Client> clients = FXCollections.observableArrayList();
	private final Logger logger = Logger.getLogger("");
	

	protected Client(ServerModel model, Socket socket, String name) {
		this.model = model;
		this.clientSocket = socket;
		
		String eingang = SendenEmpfangen.EmpfangenString(clientSocket);
		
		String[] teile = eingang.split(",");
		String namePW = teile[1] +"," + teile[2];
		String meldung;
		logger.info("Anmeldedaten erhalten: " + namePW);
		
		if(teile[0].equals("anmelden") ){
			meldung = model.AnmeldungPrüfen(namePW);
		} else if (teile[0].equals("registrieren")) {
			meldung = model.RegistrierungPrüfen(namePW);
		} else if (teile[0].equals("löschen")){
			meldung = model.LöschenPrüfen(namePW);
		} else {
			meldung = "Fehler";
		}
		model.setMeldung(meldung);
		SendenEmpfangen.Senden(clientSocket, meldung);
		
//		this.clients.add(Client.this);
//		logger.info("Neuer Client zu Liste hinzugefügt " + clientSocket);
		
//		Runnable a = new Runnable() {
//			@Override
//			public void run() {
//				try{
//					
//				while(true) {
					
					model.EmpfangenServer(clientSocket);
					
					// wenn clientsocket.inputstream.readobject instanceOf türme
					//else instanceof String
					
//				}
//				} catch (Exception e){
//					e.toString();
//				}
//			}
//		}; 
//		Thread b = new Thread(a);
//		b.start();
//		logger.info("Thread gestartet");
//			
	}
	
	public Socket getSocket(Client client){
		return clientSocket;
	}
	
//	public void EmpfangenServer (){
//		
//		
//		try{
//			ObjectInputStream empfangen = new ObjectInputStream(this.clientSocket.getInputStream());
//		
//		logger.info("available is: " + empfangen.available());;
//		Object neuEmpfangen = empfangen.readObject();
//		if( neuEmpfangen instanceof Turm[]){
//			Turm[] tmpTürme = (Turm[]) empfangen.readObject();
//			
//			for (Client c : clients) {
//				SendenEmpfangen.Senden(, tmpTürme);
//				logger.info("neue Türme gesendet an" + clientSocket.getInetAddress().getHostName());
//			}
//		} else if (neuEmpfangen instanceof String){
//			String tmpMeldung = (String) empfangen.readObject();
//			
//			for (Client c : clients) {
//				SendenEmpfangen.Senden(c.clientSocket, tmpMeldung);
//				logger.info("neuer String " + tmpMeldung + " gesendet an " + clientSocket.getInetAddress().getHostName());
//			}
//		} else if (neuEmpfangen instanceof Boolean){
//			boolean tmpBol = (boolean) empfangen.readObject();
//			
//			for (Client c : clients) {
//				SendenEmpfangen.Senden(c.clientSocket, tmpBol);
//				logger.info("neue boolean gesendet an" + clientSocket.getInetAddress().getHostName());						}
//		} else{
//			logger.info("hat nicht funktioniert so");
//		}
//		
//		logger.info("Daten Empfangen von Client ");
//		} catch (Exception e) {
//			logger.info(e.toString());
//		}
//		
//	
//	}

	
        
}

