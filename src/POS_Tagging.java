import java.io.BufferedWriter;
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

		// Initialize the tagger
		MaxentTagger tagger = new MaxentTagger("taggers/german-hgc.tagger");

		// Input of a test file
		String input = new String(Files.readAllBytes(Paths.get("input.txt")));

		// The tagged string
		String tagged = tagger.tagString(input);
		
		String [] split_tagged = tagged.split(" ");

		// Output the result
		PrintWriter pWriter = null;
		try {
			pWriter = new PrintWriter(new BufferedWriter(new FileWriter("tagged.txt")));
			for (int i=0;i<split_tagged.length;i++){
				pWriter.println(split_tagged[i]+"_"+i+"_BN");
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
}