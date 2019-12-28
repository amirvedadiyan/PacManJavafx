package ir.ac.kntu.MovingObjects;

import ir.ac.kntu.Direction;

public class Pacman extends MovingClass {

    public Pacman(Point location, Point velocity, Direction direction) {
        super(location, velocity, direction);
    }

    public Pacman(Point location) {
        super(location);
    }
}
