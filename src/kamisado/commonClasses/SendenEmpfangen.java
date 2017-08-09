package kamisado.commonClasses;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.scene.input.MouseEvent;
import kamisado.client.ClientModel;

/**
 * @author Tobias Deprato
 */

public class SendenEmpfangen {
	

	
	private static final Logger logger = Logger.getLogger("");
	private static String namePW;
	private int[] ausgewähltesFeldKoordinaaten;
	
	public static void Senden(Socket clientSocket, String namePW){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("String OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(namePW);
			senden.flush();
			logger.info("Anmeldedaten gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static void Senden(Socket clientSocket, Turm t){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("Turm OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(t);
			senden.flush();
			logger.info("Turm gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static void Senden(Socket clientSocket, int[] KoordTurmFeld){
		ObjectOutputStream senden;
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("IntArray OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(KoordTurmFeld);
			senden.flush();
			logger.info("Neue Koordinaten gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static void Senden(Socket clientSocket, int[] KoordFeld, int[] KoordTurm){
		ObjectOutputStream senden;
		int[] aktiverTurmFeld = new int[4];
		aktiverTurmFeld[0]=KoordTurm[0];
		aktiverTurmFeld[1]=KoordTurm[1];
		aktiverTurmFeld[2]=KoordFeld[0];
		aktiverTurmFeld[3]=KoordFeld[1];
		
		try{
			//Stream erstellen
			senden = new ObjectOutputStream(clientSocket.getOutputStream());
			logger.info("IntArray OutputStream erstellt");
			
			//neueKoordinaten an Client senden
			senden.writeObject(aktiverTurmFeld);
			senden.flush();
			logger.info("Neue Koordinaten gesendet");
		} catch (Exception e){
			logger.info(e.toString());
		}
	}
	
	public static Turm EmpfangenTurm(Socket clientSocket){
		ObjectInputStream empfangen;
		Turm turm = null;
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("IntArray InputStream erstellt");
		
			//neueKoordinaten von Client empfangen
			Turm tmpTurm = (Turm) empfangen.readObject();
			logger.info("Neue Koordinaten erhalten");
			turm = tmpTurm;
		} catch (Exception e){
			logger.info(e.toString());
		}
		return turm;
	}
	
	public static int[] EmpfangenInt(Socket clientSocket){
		ObjectInputStream empfangen;
		int[] aktiverTurmFeld = new int[3];
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("IntArray InputStream erstellt");
		
			//neueKoordinaten von Client empfangen
			int[] inAktiverTurmFeld = (int[]) empfangen.readObject();
			logger.info("Neue Koordinaten erhalten");
			aktiverTurmFeld = inAktiverTurmFeld;
		} catch (Exception e){
			logger.info(e.toString());
		}
		return aktiverTurmFeld;
	}	
	
	public static String EmpfangenString(Socket clientSocket){
		ObjectInputStream empfangen;
		String in; 
		try{
			empfangen = new ObjectInputStream(clientSocket.getInputStream());
			logger.info("String InputStream erstellt");
		
			//neueKoordinaten von Client empfangen
			 in = (String) empfangen.readObject();
			logger.info("Name und PW erhalten");
			setString(in);		
		} catch (Exception e){
			logger.info(e.toString());
		}
		return namePW;
	}	
	public static void setString(String s){
		namePW = s;
	}

	
}
