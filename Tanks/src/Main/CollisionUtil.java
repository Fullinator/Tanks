package Main;

public class CollisionUtil {

	/**Check if a given line segment is colliding with a rectangle.
	 * A line segment is made up of 2 points:
	 *  Point1 has coordinates (point1x, point1y)
	 *  Point2 has coordinates (point2x, point2y)
	 * 
	 * A Rectangle is defined by specifying 2 points: Top left corner and bottom right corner.
	 *  TopLeftCorner     has coordinates (rect1x, rect1y)
	 *  BottomRightCorner has coordinates (rect1x, rect1y)
	 *  
	 *  This method returns true if the line segment is colliding with the rectangle,
	 *  false otherwise
	 * 
	 * @param point1x X coordinate of point 1
	 * @param point1y Y coordinate of point 1
	 * @param point2x X coordinate of point 2
	 * @param point2y Y coordinate of point 2
	 * @param rect1x leftmost (smallest) point in rectangle
	 * @param rect1y topmost (smallest) point in a rectangle
	 * @param rect2x rightmost (highest) point in a rectangle
	 * @param rect2y bottommost (highest) point in a rectangle
	 * @return True if intersection exists, false otherwise
	 */
	public static boolean lineSegmentRectangleIntersection(float point1x, float point1y, float point2x, float point2y, float rect1x, float rect1y, float rect2x, float rect2y) {
		//Do a few checks just to make sure the user entered the sides of the rectangle correctly
		if (rect1x > rect2x) {
			//Swap these values
			float tmp = rect2x;
			rect2x = rect1x;
			rect1x = tmp;
		}

		//We'll assume we're using java swing, where the top of the screen
		//has a y position of 0
		if (rect1y > rect2y) {
			//Swap these values
			float tmp = rect2y;
			rect2y = rect1y;
			rect1y = tmp;
		}

		//Draw a rectangle around the line segment to see if it collides
		//If there's no collision here, theres definitely no collision
		if (!rectangleRectangleIntersection(rect1x, rect1y, rect2x, rect2y, point1x, point1y, point2x, point2y)) {
			return false;
		}
		
		//Now check the distances from each point to the line
		//If there is ever something of a different sign, there is collision
		//TOP LEFT
		float distance = distanceToLine(rect1x,rect1y, point1x, point1y, point2x, point2y);
		//If 2 distances are the same sign (pos or neg), then we can multiply them to get a positive number
		//If the prodict is negative, they are different
		
		//TOP RIGHT
		if (distance * distanceToLine(rect2x,rect1y, point1x, point1y, point2x, point2y) <= 0) {
			return true;
		}
		
		//BOTTOM LEFT
		if (distance * distanceToLine(rect1x,rect2y, point1x, point1y, point2x, point2y) <= 0) {
			return true;
		}
		
		//BOTTOM RIGHT
		if (distance * distanceToLine(rect2x,rect2y, point1x, point1y, point2x, point2y) <= 0) {
			return true;
		}
		
		//Looks like they're all on the same side!
		//No collision here!
		return false;
	}

	
	/**Check if 2 rectangles are colliding
	 * This returns true if the 2 specified rectangles are intersecting.
	 * 
	 * Each rectangle is specified by 2 points which are the topleft corner and bottomright corner
	 * 
	 * @param rect1point1x Rectangle 1's x coordinate of top left corner
	 * @param rect1point1y Rectangle 1's y coordinate of top left corner
	 * @param rect1point2x Rectangle 1's x coordinate of bottom right corner
	 * @param rect1point2y Rectangle 1's y coordinate of bottom right corner
	 * @param rect2point1x Rectangle 2's x coordinate of top left corner
	 * @param rect2point1y Rectangle 2's y coordinate of top left corner
	 * @param rect2point2x Rectangle 2's x coordinate of bottom right corner
	 * @param rect2point2y Rectangle 2's y coordinate of bottom right corner
	 * @return Returns true if rectangles are intersecting, false otherwise
	 */
	public static boolean rectangleRectangleIntersection(float rect1point1x, float rect1point1y, float rect1point2x, float rect1point2y,float rect2point1x, float rect2point1y, float rect2point2x, float rect2point2y) {
		//Do a few checks just to make sure the user entered the sides of the rectangle correctly
		if (rect1point1x > rect1point2x) {
			//Swap these values
			float tmp = rect1point1x;
			rect1point1x = rect1point2x;
			rect1point2x = tmp;
			//System.out.println("Had to swap x positions for rectangle 1");
		}

		//We'll assume we're using java swing, where the top of the screen
		//has a y position of 0
		if (rect1point1y > rect1point2y) {
			//Swap these values
			float tmp = rect1point1y;
			rect1point1y = rect1point2y;
			rect1point2y = tmp;
			//System.out.println("Had to swap y positions for rectangle 1");
		}

		//Do a few checks just to make sure the user entered the sides of the rectangle correctly
		if (rect2point1x > rect2point2x) {
			//Swap these values
			float tmp = rect2point1x;
			rect2point1x = rect2point2x;
			rect2point2x = tmp;
			
			//System.out.println("Had to swap x positions for rectangle 2");
		}

		//We'll assume we're using java swing, where the top of the screen
		//has a y position of 0
		if (rect2point1y > rect2point2y) {
			//Swap these values
			float tmp = rect2point1y;
			rect2point1y = rect2point2y;
			rect2point2y = tmp;
			//System.out.println("Had to swap y positions for rectangle 2");
		}



		//Do some simple rejection tests
		//Rightmost point to left of rectangle
		if (rect1point2x < rect2point1x) { return false; }

		//Leftmost point to right of rectangle
		if (rect1point1x > rect2point2x) { return false; }

		//Bottom point above rectangle
		if (rect1point2y < rect2point1y) { return false; }

		//Topmost point below rectangle
		if (rect1point1y > rect2point2y) { return false; }

		return true;
	}
	

	/**Get signed distance to line
	 * 
	 * @param pointx
	 * @param pointy
	 * @param line1x
	 * @param line1y
	 * @param line2x
	 * @param line2y
	 * @return
	 */
	private static float distanceToLine(float pointx, float pointy, float line1x, float line1y, float line2x, float line2y) {
		//Source http://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
		//return (y2-y1)x0 - (x2-x1)y0 + x2y1 - y2x1
		return ((line2y - line1y) * pointx) - ((line2x-line1x) * pointy) + (line2x*line1y) - (line2y*line1x);
	}
}
