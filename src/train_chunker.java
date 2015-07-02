/*
 * Diese File ist Inhalt des Programms "Simple-German-Chunker"
 * Hier werden die verschiedenen Regeln für den Chunker erzeugt und 
 * auch auf das Trainingskorpus angewendet.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class train_chunker {

	public static void main(String[] args) {

		final long timeStart = System.currentTimeMillis();
		final long timeStartReading = System.currentTimeMillis();

		//Alle Pfadangaben
		File rule_file = new File("results/rules.txt");
		File anwendung = new File("resultsanwendung.txt");
		File auswertung = new File("results/regel_auswertung.txt");

		File chunked_corpus = new File("ressources/chunked_corpus.txt");
		File tagged_corpus = new File("ressources/tagged_corpus.txt");
		File tagPos = new File("ressources/tagPos.txt");


		//Erstellung von Arrays und Listen
		//		String [] corpus_chunked = new String [1574818];
		//		String [] corpus_tagged = new String [1574818];
		//		String [] postag = new String[50];

		List<String> corpus_chunked = new ArrayList<String>();
		List<String> corpus_tagged = new ArrayList<String>();
		List<String> postag = new ArrayList<String>();

		try {
			// Einlesen des chunk-getaggten Korpus
			BufferedReader incc  = new BufferedReader(new InputStreamReader(new FileInputStream (chunked_corpus), "UTF8"));			
			String line = null;

			int indexcc = 0;
			while (( line = incc.readLine()) != null) {
				corpus_chunked.add(line);
				indexcc++;
			}
			incc.close();

			// Einlesen des POS-getaggten Korpus
			BufferedReader intc  = new BufferedReader(new InputStreamReader(new FileInputStream (tagged_corpus), "UTF8"));
			int indextc = 0;
			while (( line = intc.readLine()) != null) {
				corpus_tagged.add(line);
				indextc++;
			}
			intc.close();

			// Einlesen der POS-Tags in ArrayList
			BufferedReader inpt  = new BufferedReader(new InputStreamReader(new FileInputStream (tagPos), "UTF8"));			
			int indexpt = 0;
			while (( line = inpt.readLine()) != null) {
				postag.add(line);
				indexpt++;
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

		//		String [] ctags = {"B-NC", "I-NC", "B-VC", "I-VC", "B-PC", "I-PC"};
		//		String [] rules = new String [ctags.length*postag.size()];

		List<String> ctags = new ArrayList<String>();
		ctags.add("B-NC");
		ctags.add("I-NC");
		ctags.add("B-VC");
		ctags.add("I-VC");
		ctags.add("B-PC");
		ctags.add("I-PC");
		List<String> rulesP0 = new ArrayList<String>();

		// Erstellen der Regeln: Für POS-Tags an Positionen -2,-1,0,1,2
		// Erstellen der Regeln an Position 0
		final long timeStartFirstRules = System.currentTimeMillis();
		PrintWriter printWriter = null;
		try{
			printWriter = new PrintWriter(rule_file);

			for (int j = 0; j < postag.size(); j++){
				for (int k = 0; k < ctags.size(); k++){
					printWriter.println("0="+postag.get(j)+"=>"+ctags.get(k));
					rulesP0.add("0="+postag.get(j)+"=>"+ctags.get(k));
				}
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter != null ) {
				printWriter.close();
			}
		}
		// Dauer des Erstellens der ersten Regeln 
		final long timeEndFirstRules = System.currentTimeMillis(); 
		final long timeFirstRules = (timeEndFirstRules - timeStartFirstRules)/1000;
		System.out.println("Dauer des Erstellen der Regeln: " + timeFirstRules + " Sek."); 

		float [] freq = new float [rulesP0.size()];
		float [] succ = new float [rulesP0.size()];
		for (int n = 0; n < rulesP0.size(); n++){
			freq[n] = 0;
			succ[n] = 0;
		}

		// Anwendung der ersten Regeln 
		final long timeStartFirstUse = System.currentTimeMillis();
		PrintWriter printWriter2 = null;
		try{
			//printWriter = new PrintWriter(anwendung);
			//List<String> use = new LinkedList<String>();

			// Algorithmus zur Anwendung der erzeugten Regeln mit Regeln der Länge 1 
			for (int i = 0; i< corpus_tagged.size(); i++){
				Token tok1 = new Token (corpus_tagged.get(i));
				String pos = tok1.getTag();

				Token tok2 = new Token (corpus_chunked.get(i));
				String chunk = tok2.getCtag();

				for (int m = 0; m < rulesP0.size(); m++){
					Rule rul1 = new Rule(rulesP0.get(m));
					String rulpos = rul1.getPostag()[0];
					String rulchunk = rul1.getChunktag();

					if (pos.equals(rulpos)){
						freq[m]++;
						if (chunk.equals(rulchunk)){
							//printWriter2.println(corpus_tagged[i]+"_"+rulchunk);
							succ[m]++;
						}
					}
					else{
						//printWriter2.println(corpus_tagged[i]);
					}
				}
			}
		}
		finally{
			if ( printWriter2 != null ) {
				printWriter2.close();
			}
		}
		// Dauer des Erstellens der ersten Regeln 
		final long timeEndFirstUse = System.currentTimeMillis(); 
		final long timeFirstUse = (timeEndFirstUse - timeStartFirstUse)/1000;
		System.out.println("Dauer des ersten Durchlaufes der Regeln: " + timeFirstUse + " Sek."); 


		// Auswertung der Regeln auf ihre Frequenz und Genauigkeit
		final long timeStartTesting = System.currentTimeMillis();
		PrintWriter printWriter3 = null;
		try{
			printWriter3 = new PrintWriter(auswertung);
			for (int o = 0; o<rulesP0.size();o++){
				if (freq[o]>0){
					printWriter3.println(rulesP0.get(o)+"=> Freq: "+freq[o]+" Succ: "+succ[o]+" Acc: "+(succ[o]/freq[o])*100);
				}
				else{
					printWriter3.println(rulesP0.get(o)+"=> Freq: "+freq[o]+" Succ: "+succ[o]);
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
		float summeFreq=0;
		float summeSucc=0;

		for (int p = 0; p< freq.length;p++){
			summeFreq=summeFreq+freq[p];
			summeSucc=summeSucc+succ[p];
		}
		float summeAcc=(summeSucc/summeFreq)*100;
		System.out.println("Regeln an der Position 0\t Genauigkeit: "+summeAcc+" %");


		
		//Erstellen der Regeln für die Positionen -1,0
		final long timeStartMoreRules = System.currentTimeMillis();
		List<String> rulesPM1 = new ArrayList<String>();
		PrintWriter printWriter4 = null;
		try{
			printWriter4 = new PrintWriter(rule_file);

			for (int j=0; j< rulesP0.size();j++){
				printWriter4.println(rulesP0.get(j));
				for (int k=0; k<postag.size();k++){
					rulesPM1.add("-1="+postag.get(k)+","+rulesP0.get(j));
					printWriter4.println("-1="+postag.get(k)+","+rulesP0.get(j));
				}
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter4 != null ) {
				printWriter4.close();
			}
		}

		//Erstellen der Regeln für die Positionen -2,-1,0
		List<String> rulesPM2 = new ArrayList<String>();
		PrintWriter printWriter5 = null;
		try{
			printWriter5 = new PrintWriter(rule_file);
			for (int i=0;i<rulesP0.size();i++){
				printWriter5.println(rulesP0.get(i));
			}
			for (int i=0;i<rulesPM1.size();i++){
				printWriter5.println(rulesPM1.get(i));
			}
			for (int j=0; j< rulesPM1.size();j++){
				for (int k=0; k<postag.size();k++){
					rulesPM2.add("-2="+postag.get(k)+","+rulesPM1.get(j));
					printWriter5.println("-2="+postag.get(k)+","+rulesPM1.get(j));
				}
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter5 != null ) {
				printWriter5.close();
			}
		}
		
		final long timeEndMoreRules = System.currentTimeMillis(); 
		final long timeMoreRules = (timeEndMoreRules - timeStartMoreRules)/1000;
		System.out.println("Dauer des Erstellens der Regeln -2,-1,0: " + timeTesting + " Sek."); 

		final long timeEnd = System.currentTimeMillis(); 
		final long time = (timeEnd - timeStart)/1000;
		System.out.println("_________________________________________");
		System.out.println("Dauer des Programms: " + time + " Sek."); 
	}
}