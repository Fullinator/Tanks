package testing;

import drawable.Clouds;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import terrain.Sand;
import terrain.Terrain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by nicho on 3/4/2016.
 */
public class CloudsTest {
	private Terrain terrain;

	@Before
	public void setUp() throws Exception {
		terrain = new Sand(500, 500, 1, 0, new String[] {"test name"});
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testQueryImage() throws Exception {
		Clouds cloud = new Clouds(terrain, terrain.getXTerrain(), terrain.getYTerrain());
		BufferedImage correctImage = ImageIO.read(getClass().getResourceAsStream("/img/tempCloud.png"));
		assertEquals(cloud.queryImage().getWidth(), correctImage.getWidth());
		assertEquals(cloud.queryImage().getHeight(), correctImage.getHeight());
		for (int x = 0; x < cloud.queryImage().getWidth(); x++) {
			for (int y = 0; y < cloud.queryImage().getHeight(); y++) {
				assertEquals(correctImage.getRGB(x, y), cloud.queryImage().getRGB(x, y));
			}
		}
	}
}