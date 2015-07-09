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
		
		// Groeﬂe zum Testen: 1074818 + 500000
		
		List<String> lengthone = new ArrayList <String>();
		List<String> lengthtwo = new ArrayList <String>();
		List<String> lengththree = new ArrayList <String>();
		
		for (int i = 0; i < rules.size(); i++){
			Rule rul1 = new Rule (rules.get(i));
			String accs = rul1.getArruracy();
			double acc = Double.parseDouble(accs); 
			//Regeln der Laenge 1
			if (i < 330 && acc > 0.0){
				lengthone.add(rules.get(i));
			}
			//Regeln der Laenge 2
			if (i > 330 && i <= 18150*2 && acc > 0.0){
				lengthtwo.add(rules.get(i));
			}
			//Regeln der Laenge 3 
			if (i > 18150*2 && acc > 0.0){
				lengththree.add(rules.get(i));
			}
		}
		
		System.out.println(lengthone.size());
		System.out.println(lengthtwo.size());
		System.out.println(lengththree.size());

		

	}

}
