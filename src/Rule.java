
public class Rule {

	private String [] position = new String [5];
	private String [] postag = new String [5];
	private String chunktag;

	public Rule(String text) {
		String [] rule = text.split("=>");
		this.chunktag = rule[1];
		if (rule[0].matches(",")){
			String [] headrule = rule[0].split(",");
			for (int i = 0; i<headrule.length-1; i++){
				String [] diffrule = headrule[i].split("=");
				this.position[i] = diffrule[0];
				this.postag[i] = diffrule[1];
			}
		}
		else{
			String [] diffrule = rule[0].split("=");
			this.position[0] = diffrule[0];
			this.postag[0] = diffrule[1];
		}
		
		

	}
	//[POS.0=ADJD]=B-NC 

	public void setPosition(String token) {
		this.position = position;
	}
	
	public void setPostag(String token) {
		this.postag = postag;
	}
	
	public void setChunktag(String token) {
		this.chunktag = chunktag;
	}


	public String [] getPosition() {
		return position;

	}
	
	public String [] getPostag() {
		return postag;
	}
	
	public String getChunktag() {
		return chunktag;
	}


}