/**
 * The class is the snake creator.
 *
 * @author  Mateusz Łyszkiewicz
 */

package snake;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.StrokeLineCap;

import java.util.ArrayList;

import static snake.Segment.SCREEN_HEIGHT;
import static snake.Segment.SCREEN_WIDTH;

public class SnakeController {

    private static Snake snake;
    private static Apple apple;
    private static ActualDirection actualDirection;

    private static RadialGradient segmentGradient;
    private static RadialGradient eyeGradient;
    private static RadialGradient appleGradient;

    private static final int SEGMENT_SIZE = 45;
    private static final int APPLE_SIZE = (int) (SEGMENT_SIZE * .8);
    private static final float ANGLE = 0.02f;
    private static final int MAX_POINTS = 9999;

    private static long startTime;
    private static long startStepTime;
    private static long score;
    private static short directionCounter;

    public enum Direction {
        LEFT, RIGHT, NONE
    }

    // The class stores actual snake direction
    private static class ActualDirection {
        private Direction direction;

        private void setDirection(final Direction direction) {
            this.direction = direction;
        }

        private Direction getDirection() {
            return direction;
        }
    }
	
    // Game constructor
    public SnakeController() {
        snake = new Snake();
        apple = new Apple();
        actualDirection = new ActualDirection();
        gradientsIitialize();
        startTime = System.currentTimeMillis();
        startStepTime = startTime;
    }

    // Gradients for segments etc.
    private void gradientsIitialize() {
        appleGradient = new RadialGradient(0,
                0,
                0,
                0,
                APPLE_SIZE / 1.5,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.RED),
                new Stop(1, Color.BLACK));

        segmentGradient = new RadialGradient(0,
                0,
                0,
                0,
                SEGMENT_SIZE / 1.3,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(0, 255, 0)),
                new Stop(1, Color.BLACK));

        eyeGradient = new RadialGradient(0,
                0,
                0,
                0,
                SEGMENT_SIZE / 1.2,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.BLACK),
                new Stop(1, Color.WHITE));
    }

    public int getSize() {
        return snake.snakeList().size();
    }

    public long getGameTime() {
        return System.currentTimeMillis() - startTime;
    }

    public long getScore() {
        return score;
    }

    public void setActualDirection(Direction direction) {
        actualDirection.setDirection(direction);
    }

    private void checkDirection() {
        if (actualDirection.getDirection() == Direction.LEFT)
            snake.addHeadAngle(ANGLE);
        else if (actualDirection.getDirection() == Direction.RIGHT)
            snake.addHeadAngle(-ANGLE);
        else
            shaking();
    }

    // Snake head shake
    private void shaking() {
        if (directionCounter % 5 == 0)
            if (directionCounter < 80)
                snake.addHeadAngle(ANGLE);
            else
                snake.addHeadAngle(-ANGLE);

        directionCounter++;

        if (directionCounter == 160)
            directionCounter = 0;
    }

    private boolean isCollision() {
        return snake.isCollision();
    }

    private boolean isFood() {
        return snake.isFood(apple);
    }

    public boolean moveUpdate(int speed) {
        for (int i = 0; i < speed; i++) {
            checkDirection();
            if (isFood()) {
                apple = new Apple(snake.snakeList());
                snake.addSegment();
                score += MAX_POINTS - (System.currentTimeMillis() - startStepTime);
                startStepTime = System.currentTimeMillis();
            }
            snake.move();
        }
        return !isCollision();
    }

    public void drawUpdate(GraphicsContext gc) {
        gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT + 50);
        drawApple(gc);
        drawSnake(gc);
    }

    private void drawApple(GraphicsContext appleGC) {

        appleGC.translate(apple.getX(), apple.getY());
        signApple(appleGC);

        appleGC.setStroke(Color.BLACK);
        appleGC.setLineWidth(3);
        appleGC.setFill(appleGradient);
        appleGC.fillOval(-APPLE_SIZE >> 1, -APPLE_SIZE >> 1, APPLE_SIZE, APPLE_SIZE);
        appleGC.strokeOval(-APPLE_SIZE >> 1, -APPLE_SIZE >> 1, APPLE_SIZE, APPLE_SIZE);

        appleGC.setLineCap(StrokeLineCap.ROUND);
        appleGC.setLineWidth(6);
        appleGC.strokeLine((-APPLE_SIZE >> 1) + APPLE_SIZE * .5, (-APPLE_SIZE >> 1) + APPLE_SIZE * .1, (-APPLE_SIZE >> 1) + APPLE_SIZE * .6, (-APPLE_SIZE >> 1) - APPLE_SIZE * .2);

        appleGC.setTransform(1, 0, 0, 1, 0, 0);
    }

    private void signApple(GraphicsContext appleRingGC) {

        float radius = (float) (System.currentTimeMillis() % 1000) / 7;

        appleRingGC.setStroke(Color.RED);
        appleRingGC.setLineWidth(4);
        appleRingGC.strokeOval(-radius / 2, -radius / 2, radius, radius);
    }

    private void drawSnake(GraphicsContext snakeGC) {

        ArrayList<Segment> segments = snake.snakeList();
        boolean first = true;
        int a;

        for (Segment s : segments) {

            if (s.isVisible()) {
                snakeGC.translate(s.getX(), s.getY());
                snakeGC.rotate(Math.toDegrees(-s.getAngle()));

                if (s.isFull())
                    a = (int) (SEGMENT_SIZE * 1.3);
                else
                    a = SEGMENT_SIZE;

                double secondSize = 1 - segments.indexOf(s) * .015;
                if (secondSize < .1)
                    secondSize = .1;

                snakeGC.setLineWidth(3);
                snakeGC.setStroke(Color.BLACK);
                snakeGC.setFill(segmentGradient);
                snakeGC.fillRoundRect((-a >> 1) * secondSize, -a >> 1, a * secondSize, a, a / 1.5, a / 1.5);
                snakeGC.strokeRoundRect((-a >> 1) * secondSize, -a >> 1, a * secondSize, a, a / 1.5, a / 1.5);

                if (first) {
                    snakeGC.setFill(eyeGradient);

                    if (s.isFull())
                        a = (int) (SEGMENT_SIZE * 1.6);
                    else
                        a = SEGMENT_SIZE;
                    snakeGC.fillOval(-a * .3 - a * .5, a * .2, a * .6, a * .6);
                    snakeGC.strokeOval(-a * .3 - a * .5, a * .2, a * .6, a * .6);
                    snakeGC.fillOval(-a * .3 + a * .5, a * .2, a * .6, a * .6);
                    snakeGC.strokeOval(-a * .3 + a * .5, a * .2, a * .6, a * .6);
                    snakeGC.setFill(Color.BLACK);
                    snakeGC.fillOval(-a * .1 - a * .6, a * .5, a * .2, a * .2);
                    snakeGC.fillOval(-a * .1 + a * .6, a * .5, a * .2, a * .2);

                    first = false;
                }
                snakeGC.setTransform(1, 0, 0, 1, 0, 0);
            }
        }
    }
}




