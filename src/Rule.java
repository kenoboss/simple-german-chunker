
public class Rule {

	private String [] position = new String [5];
	private String [] postag = new String [5];
	private String chunktag;
	//	private String accuracy;

	public Rule(String text) {
		String [] rule = text.split("=>");
		this.chunktag = rule[1];
//		if (rule[2] != null){
//			String [] eval = rule[2].split("Acc: ");
//			this.accuracy = eval[1];
//		}

		String [] headrule = rule[0].split(";");
		for (int i = 0; i < headrule.length; i++) {
			String [] diffrule = headrule[i].split("=");
			this.position[i] = diffrule[0];
			this.postag[i] = diffrule[1];
		}
	}


	public void setPosition(String token) {
		this.position = position;
	}

	public void setPostag(String token) {
		this.postag = postag;
	}

	public void setChunktag(String token) {
		this.chunktag = chunktag;
	}

//	public void setAccuracy(String token) {
//		this.accuracy = accuracy;
//	}

	public String [] getPosition() {
		return position;
	}

	public String [] getPostag() {
		return postag;
	}

	public String getChunktag() {
		return chunktag;
	}

//	public String getArruracy() {
//		return accuracy;
//	}


}
