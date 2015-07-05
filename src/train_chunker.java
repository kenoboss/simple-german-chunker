/*
 * Diese File ist Inhalt des Programms "Simple-German-Chunker"
 * Hier werden die verschiedenen Regeln für den Chunker erzeugt und 
 * auch auf das Trainingskorpus angewendet.
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

public class train_chunker {

	public static void main(String[] args) {
		
		Date date = new Date();
		System.out.println(date.toString());
		final double timeStart = System.currentTimeMillis();

		//Alle Pfadangaben
		File rule_file = new File("results/rules.txt");
		File auswertung = new File("results/regel_auswertung.txt");

		File chunked_corpus = new File("ressources/chunked_corpus.txt");
		File tagged_corpus = new File("ressources/tagged_corpus.txt");
		File tagPos = new File("ressources/tagPos.txt");


		List<String> corpus_chunked = new ArrayList<String>();
		List<String> corpus_tagged = new ArrayList<String>();
		List<String> postag = new ArrayList<String>();

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

			// Einlesen der POS-Tags in ArrayList
			BufferedReader inpt  = new BufferedReader(new InputStreamReader(new FileInputStream (tagPos), "UTF8"));			
			while (( line = inpt.readLine()) != null) {
				postag.add(line);
			}
			inpt.close();

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


		// START REGELERSTELLUNG
		final long timeStartRules = System.currentTimeMillis(); 
		List<String> rulesP0 = new ArrayList<String>();			// Regeln an der Stelle: 0 				done
		List<String> rulesP0m1 = new ArrayList<String>();		// Regeln an der Stelle: -1,0			done
		List<String> rulesP0m1m2 = new ArrayList<String>();		// Regeln an der Stelle: -2,-1,0		done
		List<String> rulesP0p1 = new ArrayList<String>();		// Regeln an der Stelle: 0,1			done
		List<String> rulesP0p1m1 = new ArrayList<String>();		// Regeln an der Stelle: -1,0,1			done
		List<String> rulesP0p1p2 = new ArrayList<String>();		// Regeln an der Stelle: 0,1,2			done

		/*Problem: Bei den folgenden Regeln werden mehrere 10 Millionen Regeln erstellt
		 * Selbst mit 4096MB zugewiesenem Arbeitsspeicher dauert es mehrere Minuten zum 
		 * Erstellen der Regeln. 
		 * Daher wurde das Erstellen dieser Regeln auskommentiert. Zeile 127-149 und Zeile 184-192
		 */
		List<String> rulesP0p1p2m1 = new ArrayList<String>();	// Regeln an der Stelle: -1,0,1,2		done
		List<String> rulesP0p1m1m2 = new ArrayList<String>();	// Regeln an der Stelle: -2,-1,0,1		done
		List<String> rulesP0p1p2m1m2 = new ArrayList<String>();	// Regeln an der Stelle: -2,-1,0,1,2	done

		// Chunk-Tag, die verwendet werden: Nominal-,Verbal- und Präpositionalchunk
		List<String> ctags = new ArrayList<String>();
		ctags.add("B-NC");
		ctags.add("B-VC");
		ctags.add("B-PC");
		ctags.add("I-NC");
		ctags.add("I-VC");
		ctags.add("I-PC");

		// Erstellung der Regeln an der Stelle: 0
		for (int i = 0; i < postag.size(); i++){
			for (int j = 0; j < ctags.size(); j++){
				rulesP0.add("0="+postag.get(i)+"=>"+ctags.get(j));
			}
		}
		System.out.println("Anzahl der Regeln 0: "+rulesP0.size());

		/*
		 * Regeln sich auf mehrere Positionen beziehen benötigen keinen B-XC.
		 */
		// Erstellung der Regeln an der Stelle: -1,0
		for (int i=0; i< rulesP0.size();i++){
			for (int j=0; j<postag.size();j++){
				rulesP0m1.add("-1="+postag.get(j)+","+rulesP0.get(i));
			}
		}
		System.out.println("Anzahl der Regeln -1,0: "+rulesP0m1.size());

		// Erstellung der Regeln an der Stelle: -2,-1,0
		for (int i=0; i<rulesP0m1.size();i++){
			for (int j=0; j<postag.size();j++){
				rulesP0m1m2.add("-2="+postag.get(j)+","+rulesP0m1.get(i));
			}
		}
		System.out.println("Anzahl der Regeln -2,-1,0: "+rulesP0m1m2.size());

		// Erstellung der Regeln an der Stelle: 0,1
		for (int i=0; i< rulesP0.size();i++){
			String[] rulesP0split = new String [rulesP0.size()];	
			rulesP0split=rulesP0.get(i).split("=>");
			for (int j=0; j<postag.size();j++){
				rulesP0p1.add(rulesP0split[0]+",1="+postag.get(j)+"=>"+rulesP0split[1]);
			}
		}
		System.out.println("Anzahl der Regeln 0,1: "+rulesP0p1.size());

		// Erstellung der Regeln an der Stelle: -1,0,1
		for (int i=0; i<rulesP0p1.size();i++){
			for (int j=0; j<postag.size();j++){
				rulesP0p1m1.add("-1="+postag.get(j)+","+rulesP0p1.get(i));
			}
		}
		System.out.println("Anzahl der Regeln -1,0,1: "+rulesP0p1m1.size());

		// Erstellung der Regeln an der Stelle: 0,1,2
		for (int i=0; i< rulesP0p1.size();i++){
			String[] rulesP0p1split = new String [rulesP0p1.size()];	
			rulesP0p1split=rulesP0p1.get(i).split("=>");
			for (int j=0; j<postag.size();j++){
				rulesP0p1p2.add(rulesP0p1split[0]+",2="+postag.get(j)+"=>"+rulesP0p1split[1]);
			}
		}
		System.out.println("Anzahl der Regeln 0,1,2: "+rulesP0p1p2.size());

//		// Erstellung der Regeln an der Stelle: -1,0,1,2
//		for (int i=0; i< rulesP0p1p2.size();i++){
//			for (int j=0; j<postag.size();j++){
//				rulesP0p1p2m1.add("-1="+postag.get(j)+","+rulesP0p1p2.get(i));
//			}
//		}
//		System.out.println("Anzahl der Regeln -1,0,1,2: "+rulesP0p1p2m1.size());
//
//		// Erstellung der Regeln an der Stelle: -2,-1,0,1
//		for (int i=0; i<rulesP0p1m1.size();i++){
//			for (int j=0; j<postag.size();j++){
//				rulesP0p1m1m2.add("-2="+postag.get(j)+","+rulesP0p1m1.get(i));
//			}
//		}
//		System.out.println("Anzahl der Regeln -2,-1,0,1: "+rulesP0p1m1m2.size());
//
//		// Erstellung der Regeln an der Stelle: -2,-1,0,1,2
//		for (int i=0; i< rulesP0p1p2m1.size();i++){
//			for (int j=0; j<postag.size();j++){
//				rulesP0p1p2m1m2.add("-2="+postag.get(j)+","+rulesP0p1p2m1.get(i));
//			}
//		}
//		System.out.println("Anzahl der Regeln -2,-1,0,1,2: "+rulesP0p1p2m1m2.size());

		
		// Summe aller Regeln
		long anzahlrules = rulesP0.size()+rulesP0m1.size()+rulesP0m1m2.size()+rulesP0p1.size()+
				rulesP0p1m1.size()+rulesP0p1p2.size()+rulesP0p1p2m1.size()+rulesP0p1p2m1m2.size()+
				rulesP0p1m1m2.size();
		System.out.println("Anzahl aller Regeln: "+anzahlrules);

		// Regeln werden in die Datei 'rules.txt' geschrieben
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(rule_file);

			for (int i=0;i<rulesP0.size();i++){
				printWriter.println(rulesP0.get(i));
			}

			for (int i=0;i<rulesP0m1.size();i++){
				printWriter.println(rulesP0m1.get(i));
			}

			for (int i=0;i<rulesP0m1m2.size();i++){
				printWriter.println(rulesP0m1m2.get(i));
			}

			for (int i=0;i<rulesP0p1.size();i++){
				printWriter.println(rulesP0p1.get(i));
			}
			for (int i=0;i<rulesP0p1m1.size();i++){
				printWriter.println(rulesP0p1m1.get(i));
			}
			for (int i=0;i<rulesP0p1p2.size();i++){
				printWriter.println(rulesP0p1p2.get(i));
			}
//			for (int i=0;i<rulesP0p1p2m1.size();i++){
//				printWriter.println(rulesP0p1p2m1.get(i));
//			}
//			for (int i=0;i<rulesP0p1m1m2.size();i++){
//				printWriter.println(rulesP0p1m1m2.get(i));
//			}
//			for (int i=0;i<rulesP0p1p2m1m2.size();i++){
//				printWriter.println(rulesP0p1p2m1m2.get(i));
//			}


		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter != null ) {
				printWriter.close();
			}
		}

		final long timeEndRules = System.currentTimeMillis(); 
		final long timeRules = (timeEndRules - timeStartRules)/1000;
		System.out.println("Dauer der Regelerstellung " + timeRules + " Sek."); 
		// ENDE REGELERSTELLUNG

		// START TEST REGELN AUF CORPUS
		float [] frequP0 = new float [rulesP0.size()];
		float [] succP0 = new float [rulesP0.size()];
		for (int i = 0; i < rulesP0.size(); i++){
			frequP0[i] = 0;
			succP0[i] = 0;
		}

		float [] frequP0M1 = new float [rulesP0m1.size()];
		float [] succP0M1 = new float [rulesP0m1.size()];
		for (int i = 0; i < rulesP0m1.size(); i++){
			frequP0M1[i] = 0;
			succP0M1[i] = 0;
		}

		// Testen der Regeln 
		final long timeStartFirstUse = System.currentTimeMillis();
		PrintWriter printWriter2 = null;
		try{
			// Algorithmus zum Testen der erzeugten Regeln Position = 0
			for (int i = 0; i< corpus_tagged.size()-500000; i++){ 
				/*
				 * Regeln sollten nicht auf den kompletten Korpus angewendet werden, eher auf 2/3 
				 * des Corpus. 
				 */
				Token tok1 = new Token (corpus_tagged.get(i));
				String pos = tok1.getTag();

				Token tok2 = new Token (corpus_chunked.get(i));
				String chunk = tok2.getCtag();

				for (int j = 0; j < rulesP0.size(); j++){
					Rule rul1 = new Rule(rulesP0.get(j));
					String rulpos = rul1.getPostag()[0];
					String rulchunk = rul1.getChunktag();

					if (pos.equals(rulpos)){
						frequP0[j]++;
						if (chunk.equals(rulchunk)){
							succP0[j]++;
						}
					}
				}
			}


			// Algorithmus zum Testen der erzeugten Regeln Position = -1,0
			for (int i = 1; i< 200; i++){ 	
				//			for (int i = 1; i < rulesP0m1.size()-500000; i++){
				/*
				 * Regeln sollten nicht auf den kompletten Korpus angewendet werden, eher auf 2/3 
				 * des Corpus. 
				 */
				Token tokp1 = new Token (corpus_tagged.get(i-1));
				String pos1 = tokp1.getTag();
				Token tokp2 = new Token (corpus_tagged.get(i));
				String pos2 = tokp2.getTag();


				Token tokc1 = new Token (corpus_chunked.get(i));
				String chunk = tokc1.getCtag();

				for (int m = 0; m < rulesP0m1.size(); m++){
					Rule rul1 = new Rule(rulesP0m1.get(m));
					String rulpos1 = rul1.getPostag()[0];
					String rulpos2 = rul1.getPostag()[1];

					String rulchunk = rul1.getChunktag();

					if (pos1.equals(rulpos1) && pos2.equals(rulpos2)){
						frequP0M1[m]++;
						if (chunk.equals(rulchunk)){
							succP0M1[m]++;
						}
					}
					else{
					}
				}
			}
		}
		finally{
			if ( printWriter2 != null ) {
				printWriter2.close();
			}
		}
		// Dauer der Tests der ersten Regeln 
		final long timeEndFirstUse = System.currentTimeMillis(); 
		final long timeFirstUse = (timeEndFirstUse - timeStartFirstUse)/1000;
		System.out.println("Dauer des ersten Durchlaufes der Regeln: " + timeFirstUse + " Sek."); 


		// Auswertung der Regeln auf ihre Frequenz und Genauigkeit
		final long timeStartTesting = System.currentTimeMillis();
		PrintWriter printWriter3 = null;
		try{
			printWriter3 = new PrintWriter(auswertung);
			for (int i = 0; i<rulesP0.size();i++){
				if (frequP0[i]>0){
					printWriter3.println(rulesP0.get(i)+"=> Freq: "+frequP0[i]+" Succ: "+succP0[i]+" Acc: "+(succP0[i]/frequP0[i])*100);
				}
				else{
					printWriter3.println(rulesP0.get(i)+"=> Freq: "+frequP0[i]+" Succ: "+succP0[i]);
				}
			}
			for (int i = 0; i<rulesP0m1.size();i++){
				if (frequP0M1[i]>0){
					printWriter3.println(rulesP0m1.get(i)+"=> Freq: "+frequP0M1[i]+" Succ: "+succP0M1[i]+" Acc: "+(succP0M1[i]/frequP0M1[i])*100);
				}
				else{
					printWriter3.println(rulesP0m1.get(i)+"=> Freq: "+frequP0M1[i]+" Succ: "+succP0M1[i]);
				}
			}

		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter3 != null ) {
				printWriter3.close();
			}
		}
		// Dauer der Evaluation der Regeln
		final long timeEndTesting = System.currentTimeMillis(); 
		final long timeTesting = (timeEndTesting - timeStartTesting)/1000;
		System.out.println("Dauer der Bewertung der Regeln: " + timeTesting + " Sek."); 

		// Berechnung der Vollständigkeit und Genauigkeit aller Regeln
		// Regeln mit der Position 0 
		float summeFreqP0=0;
		float summeSuccP0=0;

		for (int p = 0; p< frequP0.length;p++){
			summeFreqP0=summeFreqP0+frequP0[p];
			summeSuccP0=summeSuccP0+succP0[p];
		}
		float summeAccP0=(summeSuccP0/summeFreqP0)*100;
		System.out.println("Regeln an der Position 0\t Genauigkeit: "+summeAccP0+" %");

		// Regeln mit der Position -1,0
		float summeFreqP0M1=0;
		float summeSuccP0M1=0;

		for (int p = 0; p< frequP0.length;p++){
			summeFreqP0M1=summeFreqP0M1+frequP0M1[p];
			summeSuccP0M1=summeSuccP0M1+succP0M1[p];
		}
		float summeAccP0M1=(summeSuccP0M1/summeFreqP0M1)*100;
		System.out.println("Regeln an der Position -1,0\t Genauigkeit: "+summeAccP0M1+" %");

		// ENDE TEST REGELN AUF CORPUS


		final double timeEnd = System.currentTimeMillis(); 
		final double time = ((timeEnd - timeStart)/1000)/60;
		System.out.println("_________________________________________");
		System.out.println("Dauer des Programms: " + time + " Min."); 
		Date date2 = new Date();
		System.out.println(date2.toString());
	}
}