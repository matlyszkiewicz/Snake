/**
 * Class encapsulates segment dimensions, angle and visibility.
 * Additionally, the class determines if eaten apple is in segment, which
 * allows segment to render bigger.
 *
 * @author  Mateusz Łyszkiewicz
 */

package snake;

import javafx.stage.Screen;

class Segment {

    // Screen resolution is set during compilation based on computer hardware screen
    static final int SCREEN_WIDTH = (int) Screen.getPrimary().getVisualBounds().getWidth();
    static final int SCREEN_HEIGHT = (int) Screen.getPrimary().getVisualBounds().getHeight();

    private float x;
    private float y;
    private float angle;
    private boolean visible = false;
    private boolean full = false;

    Segment(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    Segment(Segment s) {
        this.x = s.x;
        this.y = s.y;
        this.angle = s.angle;
        this.visible = false;
        this.full = false;
    }

    // Getters

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    float getAngle() {
        return angle;
    }

    // Setters

    void setX(float x) {
        this.x = x;
    }

    void setY(float y) {
        this.y = y;
    }

    void setAngle(float angle) {
        this.angle = angle;
    }

    void set(Segment s) {
        this.x = s.x;
        this.y = s.y;
        this.angle = s.angle;
    }

    void setVisible() {
        visible = true;
    }

    void setFull() {
        full = true;
    }

    void setEmpty() {
        full = false;
    }

    // Other methods

    boolean isVisible() {
        return visible;
    }

    boolean isFull() {
        return full;
    }

    boolean equals(Segment s) {
        return (x == s.x && y == s.y && angle == s.angle);
    }

    @Override
    public String toString() {
        return "( " + x + "," + y + "<" + angle + "> )";
    }
}