import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;


public class TokenTest {

	@Test
	@Ignore
	public void testToken() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSetToken() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSetTag() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSetId() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetCtag() {
		String [] text = {"Trier_NN_0_BN", "ist_VFIN_1_BN"};
		Token tok1 = new Token(text[0]);
		tok1.setCtag("BN");
		equals ("BN"==tok1.getCtag());
		Token tok2 = new Token(text[1]);
		tok2.setCtag("BV");
		equals ("BV"==tok2.getCtag());
	}

	@Test
	public void testGetToken() {
		String [] text = {"Trier_NN_0_BN", "ist_VFIN_1_BN"};
		Token tok1 = new Token(text[0]);
		equals ("Trier"==tok1.getToken());
		Token tok2 = new Token(text[1]);
		equals ("ist"==tok2.getToken());
	}

	@Test
	public void testGetTag() {
		String [] text = {"Trier_NN_0_BN", "ist_VFIN_1_BN"};
		Token tok1 = new Token(text[0]);
		equals ("NN"==tok1.getTag());
		Token tok2 = new Token(text[1]);
		equals ("VFIN"==tok2.getTag());
	}

	@Test
	public void testGetId() {
		String [] text = {"Trier_NN_0_BN", "ist_VFIN_1_BN"};
		Token tok1 = new Token(text[0]);
		equals ("0"==tok1.getId());
		Token tok2 = new Token(text[1]);
		equals ("1"==tok2.getId());
	}

	@Test
	public void testGetCtag() {
		String [] text = {"Trier_NN_0_BN", "ist_VFIN_1_BN"};
		Token tok1 = new Token(text[0]);
		equals ("BN"==tok1.getCtag());
		Token tok2 = new Token(text[1]);
		equals ("BN"==tok2.getCtag());
	}

}
