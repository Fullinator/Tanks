package drawable;

import java.awt.Point;
import java.awt.image.BufferedImage;

public interface Drawable {
	Point getLocation();
	int getX();
	int getY();
	BufferedImage queryImage();
}
