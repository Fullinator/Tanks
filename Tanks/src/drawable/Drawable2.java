package drawable;

import java.awt.Point;
import java.awt.image.BufferedImage;

public interface Drawable2 {
	Point getLocation();
	int getX();
	int getY();
	BufferedImage queryImage();
}
