/** 
 * Class encapsulates apple dimensions declared by x and y.
 *
 * Class provides constructors to generate object between the screen
 * borders except the actual snake's body.
 *
 * @author  Mateusz Łyszkiewicz
 */

package snake;

import java.util.ArrayList;
import java.util.Random;

import static snake.Segment.SCREEN_HEIGHT;
import static snake.Segment.SCREEN_WIDTH;

class Apple {

    // Collision tolerance to generate an apple on free field (on the whole screen except the actual snake)
    private static final int COLLISION_TOLERANCE = 100;

    private float x, y;

    Apple() {
        Random generator = new Random();
        x = COLLISION_TOLERANCE + generator.nextFloat() * (SCREEN_WIDTH - COLLISION_TOLERANCE);
        y = COLLISION_TOLERANCE + generator.nextFloat() * (SCREEN_HEIGHT - COLLISION_TOLERANCE);
    }

    Apple(ArrayList<Segment> segments) {
        Random generator = new Random();
        do {
            x = COLLISION_TOLERANCE + generator.nextFloat() * (SCREEN_WIDTH - 2 * COLLISION_TOLERANCE);
            y = COLLISION_TOLERANCE + generator.nextFloat() * (SCREEN_HEIGHT - 2 * COLLISION_TOLERANCE);
        } while (isCollision(segments));
    }

    private boolean isCollision(ArrayList<Segment> segments) {

	    boolean collision = false;

        for (Segment s : segments) {
            if (Math.abs(s.getX() - x) < COLLISION_TOLERANCE && Math.abs(s.getY() - y) < COLLISION_TOLERANCE) {
                collision = true;
                break;
            }
        }

        return collision;
    }

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    public String toString() {
        return "( " + x + "," + y + " )";
    }
}