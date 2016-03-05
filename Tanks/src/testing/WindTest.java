package testing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import physics.Wind;

import static org.junit.Assert.*;

/**
 * Created by nicho on 3/4/2016.
 */
public class WindTest {

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetWindSpeed() throws Exception {
		Wind wind = new Wind(30);
		assertEquals(wind.getWindSpeed(), 30, 0.01);
	}

	@Test
	public void testSetWindSpeed() throws Exception {
		Wind wind = new Wind(30);
		assertEquals(wind.getWindSpeed(), 30, 0.01);
		wind.setWindSpeed(50);
		assertEquals(wind.getWindSpeed(), 50, 0.01);
	}
}