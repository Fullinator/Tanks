package testing;

import drawable.Tank;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import terrain.Sand;
import terrain.Terrain;

import static org.junit.Assert.*;

/**
 * Created by nicho on 3/4/2016.
 */
public class TerrainTest {

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testCreateTerrainSpecificItems() throws Exception {

	}

	@Test
	public void testCurrentTank() throws Exception {
		Terrain terrain = new Sand(500, 500, 2, 0, new String[] {"name1", "name2"});
		Tank a = terrain.currentTank();
		terrain.nextPlayerTurn();
		assertNotEquals(a, terrain.currentTank());
	}

	@Test
	public void testGetTerrain() throws Exception {

	}

	@Test
	public void testSetDrawable() throws Exception {

	}

	@Test
	public void testFindY() throws Exception {

	}

	@Test
	public void testGetXTerrain() throws Exception {
		Terrain terrain = new Sand(500, 700, 2, 0, new String[] {"name1", "name2"});
		assertEquals(terrain.getXTerrain(), 500);
	}

	@Test
	public void testGetYTerrain() throws Exception {
		Terrain terrain = new Sand(500, 700, 2, 0, new String[] {"name1", "name2"});
		assertEquals(terrain.getYTerrain(), 700);
	}

	@Test
	public void testGenerate() throws Exception {

	}

	@Test
	public void testFindPlacement() throws Exception {

	}

	@Test
	public void testCreateClouds() throws Exception {

	}

	@Test
	public void testCreateTanks() throws Exception {

	}

	@Test
	public void testFill() throws Exception {

	}

	@Test
	public void testDamage() throws Exception {

	}

	@Test
	public void testPaintComponent() throws Exception {

	}

	@Test
	public void testNextPlayerTurn() throws Exception {

	}

	@Test
	public void testCreateTopMenu() throws Exception {

	}

	@Test
	public void testFire() throws Exception {

	}

	@Test
	public void testHideTopMenu() throws Exception {

	}

	@Test
	public void testShowTopMenu() throws Exception {

	}

	@Test
	public void testGetGameStatus() throws Exception {

	}

	@Test
	public void testShowPlayerStats() throws Exception {

	}

	@Test
	public void testHidePlayerStats() throws Exception {

	}

	@Test
	public void testPause() throws Exception {

	}

	@Test
	public void testUnPause() throws Exception {

	}

	@Test
	public void testKeyPressed() throws Exception {

	}

	@Test
	public void testKeyReleased() throws Exception {

	}

	@Test
	public void testKeyTyped() throws Exception {

	}
}