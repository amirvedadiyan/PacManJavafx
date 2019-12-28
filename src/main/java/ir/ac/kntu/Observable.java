package ir.ac.kntu;

public interface Observable {
    void addObserver(Observer observer);
    void updateAllObservers();
}
