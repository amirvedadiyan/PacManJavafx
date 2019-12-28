package ir.ac.kntu.MovingObjects;

import ir.ac.kntu.Direction;

public class MovingClass {
    private Point location;
    private Point velocity;
    private Direction direction;

    public MovingClass(Point location, Point velocity, Direction direction) {
        this.location = location;
        this.velocity = velocity;
        this.direction = direction;
    }

    public MovingClass(Point location) {
        this.location = location;
        velocity = new Point();
        direction = Direction.RIGHT;
    }

    public MovingClass() {
        this.location = new Point();
        this.velocity = new Point();
        this.direction = Direction.RIGHT;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Point getVelocity() {
        return velocity;
    }

    public void setVelocity(Point velocity) {
        this.velocity = velocity;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
