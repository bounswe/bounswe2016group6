import static org.junit.Assert.*;

import org.junit.Test;

public class EsrefServletTest {
	EsrefServlet es = new EsrefServlet();

	@Test
	public void testDistance() {
		assertEquals((int)0.0, (int)es.distance(0.0, 0.0, 0.0, 0.0));
		assertEquals((int)5.0, (int)es.distance(0.0, 0.0, 3.0, 4.0));
	}

	@Test
	public void testParseData() {
		fail("Not yet implemented");
	}

	@Test
	public void testSortByPark() {
		fail("Not yet implemented");
	}

	@Test
	public void testSemanticRanking() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSelectedParks() {
	}

	@Test
	public void testQueryData() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertData() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteData() {
		fail("Not yet implemented");
	}

	@Test
	public void testListData() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() {
		fail("Not yet implemented");
	}

}
