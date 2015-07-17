/**
 * Diese Datei ist Teil des Projektes "Simple-German-Chunker"
 *
 * Beschreibt den Aufbau einer Regel.
 * Beispiel: -1=POS-Tag;0=POS-Tag;1=POS-Tag=>Chunk-Tag=> Freq: 0.0 Succ: 0.0 Acc: 0.0
 */
public class Rule {


	private String [] position = new String [5];
	private String [] postag = new String [5];
	private String chunktag;
	private String accuracy;

	/**
	 * Die Regel wird hier in ihre einzelnen Bestandteile zerlegt:
	 * <ul>
	 * <li>Position(en)</li>
	 * <li>POS-Tag(s)</li>
	 * <li>Chunk-Tag</li>
	 * <li>Accuracy</li>
	 * </ul>
	 *
	 * Success und Frequency werden nicht ausgelesen, da sie fuer den
	 * weiteren Gebrauch des Chunkers nicht benoetigt werden. // Umlaute gehören zum Deutschen!
	 *
	 * @param text einzelne Regel z.b 0=ADJA=>B-NC=> Freq: 57850.0 Succ: 12019.0 Acc: 20.776146
	 */
	public Rule(String text) {
		String [] rule = text.split("=>");
		this.chunktag = rule[1];
		if (rule.length == 3){
			String [] eval = rule[2].split("Acc: ");
			this.accuracy = eval[eval.length-1];
		}

		String [] headrule = rule[0].split(";");
		for (int i = 0; i < headrule.length; i++) {
			String [] diffrule = headrule[i].split("=");
			this.position[i] = diffrule[0];
			this.postag[i] = diffrule[1];
		}
	}

	/**
	 * Gibt die Position zurueck
	 * @return position
	 */
	public String [] getPosition() {
		return position;
	}

	/**
	 * Gibt das POS-Tag zurueck
	 * @return postag
	 */
	public String [] getPostag() {
		return postag;
	}

	/**
	 * Gibt das Chunk-Tag zurueck
	 * @return chunktag
	 */
	public String getChunktag() {
		return chunktag;
	}

	/**
	 * Gibt die Accuracy zurueck
	 * @return accuracy
	 */
	public String getArruracy() {
		return accuracy;
	}


}
