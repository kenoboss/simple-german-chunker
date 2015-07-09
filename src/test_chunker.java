/*
 * Diese File ist Inhalt des Programms "Simple-German-Chunker"
 * Hier werden die trainierten Regeln aus train_chunker auf das 
 * letzte 1/3 des Corpus angewendet. 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class test_chunker {

	public static void main(String[] args) {
		
		Date date = new Date();
		System.out.println(date.toString());
		final double timeStart = System.currentTimeMillis();

		//Alle Pfadangaben
		File auswertung = new File("results/regel_auswertung.txt");
		File rules_trainedtxt = new File ("results/trained_rules.txt");

		File chunked_corpus = new File("ressources/chunked_corpus.txt");
		File tagged_corpus = new File("ressources/tagged_corpus.txt");


		List<String> corpus_chunked = new ArrayList<String>();
		List<String> corpus_tagged = new ArrayList<String>();
		List<String> rules = new ArrayList<String>();
		List<String> trained_rules = new ArrayList<String>();
		
		// START EINLESEN
		final long timeStartReading = System.currentTimeMillis();
		try {
			// Einlesen des chunk-getaggten Korpus
			BufferedReader incc  = new BufferedReader(new InputStreamReader(new FileInputStream (chunked_corpus), "UTF8"));			
			String line = null;

			while (( line = incc.readLine()) != null) {
				corpus_chunked.add(line);
			}
			incc.close();

			// Einlesen des POS-getaggten Korpus
			BufferedReader intc  = new BufferedReader(new InputStreamReader(new FileInputStream (tagged_corpus), "UTF8"));
			while (( line = intc.readLine()) != null) {
				corpus_tagged.add(line);
			}
			intc.close();
			
			// Einlesen der trainierten Regeln
			BufferedReader intr = new BufferedReader (new InputStreamReader (new FileInputStream (auswertung),"UTF8"));
			while ((line = intr.readLine()) != null){
				rules.add(line);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();  
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Dauer des Einlesens
		final long timeEndReading = System.currentTimeMillis(); 
		final long timeReading = (timeEndReading - timeStartReading)/1000;
		System.out.println("Dauer des Einlesens: " + timeReading + " Sek."); 
		// ENDE EINLESEN
		
		// Groeße zum Testen: 1074818 + 500000
		
		// Regeln auslesen, die ACC > 0 haben
		for (int i = 0; i < rules.size(); i++){
			Rule rul1 = new Rule (rules.get(i));
			String acc1 = rul1.getArruracy(); //Muss noch in Rule.java implementiert werden
			double acc2 = Double.parseDouble(acc1);
			if ( acc2 > 0 ){
				trained_rules.add(rul1.toString());
			}
		}
		
		// trainierte Regeln in trained_rules.txt schreiben
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(rules_trainedtxt);

			for (int i=0;i<trained_rules.size();i++){
				printWriter.println(trained_rules.get(i));
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter != null ) {
				printWriter.close();
			}
		}
		
		// Anwendung der Regeln für den Test
		
		

	}

}
