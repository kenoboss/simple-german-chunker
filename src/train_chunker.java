import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;



public class train_chunker {

	public static void main(String[] args) {

		final long timeStart = System.currentTimeMillis();

		String [] corpus_chunked = new String [1574818];
		String [] corpus_tagged = new String [1574818];
		String [] postag = new String[50];

		try {
			// Pfadangaben für verschiedene Dateien 
			File chunked_corpus = new File("newCorporus/chunked_corpus.txt");
			File tagged_corpus = new File("newCorporus/tagged_corpus.txt");
			File tagPos = new File("newCorporus/tagPos.txt");
			
			File file = new File("newCorporus/rules.txt");
			File anwendung = new File("newCorporus/anwendung.txt");
			File auswertung = new File("newCorporus/regel_auswertung.txt");

			// Einlesen des Chunk getaggten Corpus
			BufferedReader incc  = new BufferedReader(new InputStreamReader(new FileInputStream (chunked_corpus), "UTF8"));			
			String line = null;
			int indexcc = 0;
			while (( line = incc.readLine()) != null) {
				corpus_chunked[indexcc] = line;
				indexcc++;
			}
			incc.close();

			// Einlesen des POS getaggten Corpus
			BufferedReader intc  = new BufferedReader(new InputStreamReader(new FileInputStream (tagged_corpus), "UTF8"));
			int indextc = 0;
			while (( line = intc.readLine()) != null) {
				corpus_tagged[indextc] = line;
				indextc++;
			}
			intc.close();

			// Einlesen der POS Tags zum Erstellen der Regeln
			BufferedReader inpt  = new BufferedReader(new InputStreamReader(new FileInputStream (tagPos), "UTF8"));			
			int indexpt = 0;
			while (( line = inpt.readLine()) != null) {
				postag[indexpt] = line;
				indexpt++;
			}
			inpt.close();

			// Erstellen der Regeln für alle POS-Tags an der Stelle 0
			String [] ctags = {"B-NC", "I-NC", "B-VC", "I-VC", "B-PC", "I-PC"};
			String [] rules = new String [ctags.length*postag.length];
			PrintWriter printWriter = null;
			try{
				printWriter = new PrintWriter(file);
				int l = 0;
				for (int j = 0; j < postag.length; j++){
					for (int k = 0; k < ctags.length; k++){
						printWriter.println("0="+postag[j]+"=>"+ctags[k]);
						rules[l]="0="+postag[j]+"=>"+ctags[k];
						l++;
					}
				}


				float [] freq = new float [rules.length];
				float [] succ = new float [rules.length];
				for (int n = 0; n < rules.length; n++){
					freq[n] = 0;
					succ[n] = 0;
				}

				// Erster Durchlauf der erzeugten Regeln rules[] auf das POS getaggte Corpus
				// PROBLEM: Erzeugt eine mehr als 10GB große Datei
				// mögliche Lösung: 1. Durchlauf nicht als txt ausgeben 2. Durchlauf als verkettete Liste speichern
				printWriter = new PrintWriter(anwendung);
				for (int i = 0; i< corpus_tagged.length; i++){

					Token tok1 = new Token (corpus_tagged[i]);
					String pos = tok1.getTag();

					Token tok2 = new Token (corpus_chunked[i]);
					String chunk = tok2.getCtag();

					for (int m = 0; m < rules.length; m++){
						Rule rul1 = new Rule(rules[m]);
						String rulpos = rul1.getPostag()[0];
						String rulchunk = rul1.getChunktag();

						if (pos.equals(rulpos)){
							freq[m]++;
							if (chunk.equals(rulchunk)){
								printWriter.println(corpus_tagged[i]+"_"+rulchunk);
								succ[m]++;
							}
						}
						else{
							printWriter.println(corpus_tagged[i]);
						}
					}
				}

				// Bewertung der Regeln mit Freq und Acc
				printWriter = new PrintWriter(auswertung);
				for (int o = 0; o<rules.length;o++){
					if (freq[o]>0){
						printWriter.println(rules[o]+"=> Freq: "+freq[o]+" Succ: "+succ[o]+" Acc: "+(succ[o]/freq[o])*100);
					}
					else{
						printWriter.println(rules[o]+"=> Freq: "+freq[o]+" Succ: "+succ[o]);
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

		} catch (FileNotFoundException e) {
			e.printStackTrace();  
		} catch (IOException e) {
			e.printStackTrace();
		}		

		// Ausgabe der Dauer des Programms
		final long timeEnd = System.currentTimeMillis(); 
		final long time = (timeEnd - timeStart)/1000;
		System.out.println("Dauer des Programms: " + time + " Sek."); 


	}
}