import static org.junit.Assert.*;

import java.util.EmptyStackException;

import org.junit.Ignore;
import org.junit.Test;


public class RuleTest {

	@Test
	@Ignore
	public void testRule() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	@Ignore
	public void testSetPosition() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	@Ignore
	public void testSetPostag() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	@Ignore
	public void testSetChunktag() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetPosition() {
		String [] text = {"0=ADJA=>I-NC","0=APPRART=>I-PC"};
		Rule rul1 = new Rule(text[0]);
		if(equals("1"==rul1.getPosition()[0])){
			
		}
		else{
			fail("Not yet implemented");
		}
		

	}

	@Test
	@Ignore
	public void testGetPostag() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	@Ignore
	public void testGetChunktag() {
		fail("Not yet implemented"); // TODO
	}

}
