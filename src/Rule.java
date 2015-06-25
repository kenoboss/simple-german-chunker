
public class Rule {

	private String position;
	private String postag;
	private String chunktag;

	public Rule(String text) {
		String [] rule = text.split("_");
		this.position = rule[0];
		this.postag = rule[1];
		this.chunktag = rule[2];

	}
	//[POS.0=ADJD] = B-NC 

	public void setPosition(String token) {
		this.position = position;
	}
	
	public void setPostag(String token) {
		this.postag = postag;
	}
	
	public void setChunktag(String token) {
		this.chunktag = chunktag;
	}


	public String getPosition() {
		return position;
	}
	
	public String getPostag() {
		return postag;
	}
	
	public String getChunktag() {
		return chunktag;
	}


}
