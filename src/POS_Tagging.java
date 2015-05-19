import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POS_Tagging {
	public static void main(String[] args) throws IOException,
	ClassNotFoundException {
		
		final long timeStart = System.currentTimeMillis();

		// Initialize the tagger
		MaxentTagger tagger = new MaxentTagger("taggers/german-fast.tagger");

		File folder = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/justTexts");

		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {

				// Input of a test file
				String input = new String(Files.readAllBytes(Paths.get("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/justTexts/"+listOfFiles[i].getName())), "UTF-8");

				// The tagged string
				String tagged = tagger.tagString(input);

				String [] split_tagged = tagged.split(" ");

				// Output the result
				PrintWriter pWriter = null;
				try {
					pWriter = new PrintWriter(new BufferedWriter(new FileWriter("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/corporaStandford/"+listOfFiles[i].getName())));
					for (int j=0;j<split_tagged.length;j++){
						pWriter.println(split_tagged[j]+"_"+j+"_BN");
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} finally {
					if (pWriter != null){
						pWriter.flush();
						pWriter.close();
					}
				}
				
				
			}
			final long timeEnd = System.currentTimeMillis(); 
			final long time = (timeEnd - timeStart)/1000;
			System.out.println("Writining File # "+i+" Time: " + time + " Sek."); 
		}
		final long timeEnd = System.currentTimeMillis(); 
		final long time = (timeEnd - timeStart)/1000;
		System.out.println("Dauer des Programms: " + time + " Sek."); 
	}
}