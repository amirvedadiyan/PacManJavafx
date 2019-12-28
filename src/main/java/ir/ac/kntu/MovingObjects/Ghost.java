package ir.ac.kntu.MovingObjects;

import ir.ac.kntu.Direction;
import ir.ac.kntu.Views;

public class Ghost extends MovingClass {

    private Views views;

    public Ghost(Point location, Point velocity, Direction direction, Views views) {
        super(location, velocity, direction);
        this.views = views;
    }

    public Ghost(Point location, Point velocity, Direction direction) {
        super(location, velocity, direction);
    }

    public Ghost(Point location, Views views) {
        super(location);
        this.views = views;
    }


    public Views getViews() {
        return views;
    }

    public void setViews(Views views) {
        this.views = views;
    }
}
