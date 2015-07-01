import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;



public class train_chunker {

	public static void main(String[] args) {

		final long timeStart = System.currentTimeMillis();

//		String [] corpus_chunked = new String [1574818];
//		String [] corpus_tagged = new String [1574818];
//		String [] postag = new String[50];
		
		List<String> corpus_chunked = new ArrayList<String>();
		List<String> corpus_tagged = new ArrayList<String>();
		List<String> postag = new ArrayList<String>();
		
		

		try {
			// Pfadangaben für verschiedene Dateien 
			File chunked_corpus = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/newCorporus/chunked_corpus.txt");
			File tagged_corpus = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/newCorporus//tagged_corpus.txt");
			File tagPos = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/newCorporus/tagPos.txt");
			
			File ruleFiles = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/newCorporus/rules.txt");
			File anwendung = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/newCorporus/anwendung.txt");
			File auswertung = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/newCorporus/regel_auswertung.txt");

			// Einlesen des Chunk getaggten Corpus
			BufferedReader incc  = new BufferedReader(new InputStreamReader(new FileInputStream (chunked_corpus), "UTF8"));			
			String line = null;
			int indexcc = 0;
			while (( line = incc.readLine()) != null) {
				corpus_chunked.add(line);
				indexcc++;
			}
			incc.close();

			// Einlesen des POS getaggten Corpus
			BufferedReader intc  = new BufferedReader(new InputStreamReader(new FileInputStream (tagged_corpus), "UTF8"));
			int indextc = 0;
			while (( line = intc.readLine()) != null) {
				corpus_tagged.add(line);
				indextc++;
			}
			intc.close();

			// Einlesen der POS Tags zum Erstellen der Regeln
			BufferedReader inpt  = new BufferedReader(new InputStreamReader(new FileInputStream (tagPos), "UTF8"));			
			int indexpt = 0;
			while (( line = inpt.readLine()) != null) {
				postag.add(line);
				indexpt++;
			}
			inpt.close();

			// Erstellen der Regeln für alle POS-Tags an der Stelle 0
//			String [] ctags = {"B-NC", "I-NC", "B-VC", "I-VC", "B-PC", "I-PC"};
//			String [] rules = new String [ctags.length*postag.size()];
			
			List<String> ctags = new ArrayList<String>();
			ctags.add("B-NC");
			ctags.add("I-NC");
			ctags.add("B-VC");
			ctags.add("I-VC");
			ctags.add("B-PC");
			ctags.add("I-PC");
			
			List<String> rules = new ArrayList<String>();
			PrintWriter printWriter = null;
			try{
				printWriter = new PrintWriter(ruleFiles);
				int l = 0;
				for (int j = 0; j < postag.size(); j++){
					for (int k = 0; k < ctags.size(); k++){
						printWriter.println("0="+postag.get(j)+"=>"+ctags.get(k)+"=> Freq: "+0.0+" Succ: "+0.0+" Acc: "+100.0);
						rules.add("0="+postag.get(j)+"=>"+ctags.get(k)+"=> Freq: "+0.0+" Succ: "+0.0+" Acc: "+100.0);
						l++;
					}
				}
				//rules[o]+"=> Freq: "+freq[o]+" Succ: "+succ[o]+" Acc: "+(succ[o]/freq[o])*100)


				float [] freq = new float [rules.size()];
				float [] succ = new float [rules.size()];
				for (int n = 0; n < rules.size(); n++){
					freq[n] = 0;
					succ[n] = 0;
				}

				// Erster Durchlauf der erzeugten Regeln rules[] auf das POS getaggte Corpus
				// PROBLEM: Erzeugt eine mehr als 10GB große Datei
				// mögliche Lösung: 1. Durchlauf nicht als txt ausgeben 2. Durchlauf als verkettete Liste speichern
				
				//printWriter = new PrintWriter(anwendung);
				//List<String> use = new LinkedList<String>();
				for (int i = 0; i< corpus_tagged.size(); i++){

					Token tok1 = new Token (corpus_tagged.get(i));
					String pos = tok1.getTag();

					Token tok2 = new Token (corpus_chunked.get(i));
					String chunk = tok2.getCtag();

					for (int m = 0; m < rules.size(); m++){
						Rule rul1 = new Rule(rules.get(m));
						String rulpos = rul1.getPostag()[0];
						String rulchunk = rul1.getChunktag();

						if (pos.equals(rulpos)){
							freq[m]++;
							if (chunk.equals(rulchunk)){
								//printWriter.println(corpus_tagged[i]+"_"+rulchunk);
								succ[m]++;
							}
						}
						else{
							//printWriter.println(corpus_tagged[i]);
						}
					}
				}

				// Bewertung der Regeln mit Freq und Acc
				printWriter = new PrintWriter(auswertung);
				for (int o = 0; o<rules.size();o++){
					if (freq[o]>0){
						printWriter.println(rules.get(o)+"=> Freq: "+freq[o]+" Succ: "+succ[o]+" Acc: "+(succ[o]/freq[o])*100);
					}
					else{
						printWriter.println(rules.get(o)+"=> Freq: "+freq[o]+" Succ: "+succ[o]);
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
		final long time = (((timeEnd - timeStart)/1000)/60)/60;
		System.out.println("Dauer des Programms: " + time + " Stunden."); 


	}
}