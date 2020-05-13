/**
 * Class encapsulates containers with snake positions.
 *
 * @author Mateusz Łyszkiewicz
 */

package snake;

import java.util.ArrayList;
import java.util.LinkedList;
import static java.lang.Math.*;

import static snake.Segment.SCREEN_HEIGHT;
import static snake.Segment.SCREEN_WIDTH;

class Snake {
    private static final int STEP = 1;
    private static final int FOOD_TOLERANCE = 30;
    private static final int COLLISION_TOLERANCE = 28;
    private static final int MAX_GHOSTS_ON_SEGMENT = 42;

    // Segments are phisically visible snake segments.
    private LinkedList<Segment> segments = new LinkedList<>();
    // Ghosts are invisible snake's segments, which agregate positions for the next frames.
    private LinkedList<Segment> ghosts = new LinkedList<>();
    // Apples are fruits "in" the snake.
    private LinkedList<Segment> apples = new LinkedList<>();

    // At first is only head
    private int visibleSegments = 1;

    // Constructor creates only head
    Snake() {
        segments.add(new Segment(
                SCREEN_WIDTH >> 1,
                SCREEN_HEIGHT >> 1,
                0));
        getHead().setVisible();
    }

    private Segment getHead() {
        return segments.get(0);
    }

    // New segment is created with head actual position
    void addSegment() {
        segments.add(new Segment(getHead()));
    }

    void addHeadAngle(float angle) {
        getHead().setAngle(getHead().getAngle() + angle);
    }

    // Checking if an apple is in head range
    boolean isFood(Apple apple) {

        boolean food = false;

        if (abs(apple.getX() - getHead().getX()) < FOOD_TOLERANCE
                && abs(apple.getY() - getHead().getY()) < FOOD_TOLERANCE) {
            apples.add(new Segment(getHead()));
            food = true;
        }

        return food;
    }

    // Snake's collisions rules
    boolean isCollision() {

        boolean collision = false;

        // Snake's collision with screen border
        if (getHead().getX() > SCREEN_WIDTH - COLLISION_TOLERANCE
                || getHead().getX() < COLLISION_TOLERANCE
                || getHead().getY() > SCREEN_HEIGHT + 40 - COLLISION_TOLERANCE
                || getHead().getY() < COLLISION_TOLERANCE)
            collision = true;

        // Snake's collision with its others segments
        Segment s1 = segments.get(0);
        if (!collision) {
            for (int i = 1; i < segments.size(); i++) {
                Segment s2 = segments.get(i);
                if (s1.isVisible() && s2.isVisible()) {
                    if (abs(s1.getX() - s2.getX()) < COLLISION_TOLERANCE
                            && abs(s1.getY() - s2.getY()) < COLLISION_TOLERANCE) {
                        collision = true;
                        break;
                    }
                }
            }
        }

        return collision;
    }

    void move() {

        // One new ghost per frame
        ghosts.add(new Segment(getHead()));

        // Max ghosts limit
        if (ghosts.size() > MAX_GHOSTS_ON_SEGMENT * (segments.size() - 1))
            ghosts.remove(0);

        // Apple as last segment
        if (apples.size() > 0
                && ghosts.size() >= MAX_GHOSTS_ON_SEGMENT * visibleSegments) {

            if (apples.get(0).equals(ghosts.get(0))) {
                segments.get(segments.size() - apples.size()).setVisible();
                segments.get(segments.size() - apples.size() - 1).setEmpty();
                visibleSegments++;
                apples.remove(0);
            }
        }

        // Segments move
        for (int i = 1; i < segments.size(); i++)
            if (segments.get(i).isVisible())
                segments.get(i).set(ghosts.get((ghosts.size() - 1) - (MAX_GHOSTS_ON_SEGMENT * i - 1)));

        // Segments growth after apple eating
        if (apples.size() > 0)
            for (int i = 0; i < segments.size(); i++) {
                for (Segment s : apples)
                    if (segments.get(i).equals(s) &&
                            segments.get(i).isVisible()) {
                        segments.get(i).setFull();
                        if (i > 0)
                            segments.get(i - 1).setEmpty();
                    }
            }

        // Snake moving - steps per frame
        float a = (float) sin(getHead().getAngle()) * STEP;
        float b = (float) cos(getHead().getAngle()) * STEP;

        getHead().setX(getHead().getX() + a);
        getHead().setY(getHead().getY() + b);
    }

    ArrayList<Segment> snakeList() {
        return new ArrayList<>(segments);
    }
}