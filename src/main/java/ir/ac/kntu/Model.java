package ir.ac.kntu;

import ir.ac.kntu.MovingObjects.Ghost;
import ir.ac.kntu.MovingObjects.Pacman;
import ir.ac.kntu.MovingObjects.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Model implements Observable {

    private List<Observer> observers;
    private Views[][] objectsLocation;
    private Pacman pacman;
    private Pacman pacman2;
    private Ghost ghost1;
    private Ghost ghost2;
    private Ghost ghost3;
    private Ghost ghost4;
    private Ghost ghost5;
    private int row;
    private int column;
    private int score;
    private int dots;
    private boolean isLost;
    private boolean isWon;
    private boolean isBigDotEaten;


    //Helper Pac Man
    public static int firstXHelper = 5;
    public static int firstYHelper = 2;
    private boolean isHelperDead;


    //Constructor That Read a File and found objects location
    public Model() {

        initMap();
        observers = new ArrayList<>();

        this.isLost = false;
        this.isBigDotEaten = false;
        this.score = 0;

    }

    public void initMap() {
        //Calculate row and column
        int tempRow = 0;
        int tempColumn = 0;
        Point firstPacManLocation = new Point();
        Point firstGhost1Location = new Point();
        Point firstGhost2Location = new Point();
        Point firstGhost3Location = new Point();
        Point firstGhost4Location = new Point();
        Point firstGhost5Location = new Point();
        File map = new File("C:\\Users\\Amir\\Desktop\\p2-pacman-master\\map.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                lineScanner.next();
                tempColumn++;
            }
            tempRow++;
        }
        tempColumn = tempColumn / tempRow;
        row = tempRow;
        column = tempColumn;
        scanner.close();
        //Place Views in objectsLocation
        Scanner scanner2 = null;
        try {
            scanner2 = new Scanner(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        objectsLocation = new Views[tempRow][tempColumn];
        int irow = 0;
        while (scanner2.hasNextLine()) {
            int icolumn = 0;
            String line = scanner2.nextLine();
            Scanner lineScanner = new Scanner(line);
            while (lineScanner.hasNext()) {
                String value = lineScanner.next();
                switch (value) {
                    case "P":
                        objectsLocation[irow][icolumn] = Views.PAC_MAN;
                        firstPacManLocation.setX(irow);
                        firstPacManLocation.setY(icolumn);
                    case "W":
                        objectsLocation[irow][icolumn] = Views.WALL;
                        break;
                    case "S":
                        objectsLocation[irow][icolumn] = Views.SMALL_DOT;
                        dots++;
                        break;
                    case "B":
                        objectsLocation[irow][icolumn] = Views.BIG_DOT;
                        dots++;
                        break;
                    case "1":
                        objectsLocation[irow][icolumn] = Views.GHOST1;
                        firstGhost1Location.setX(irow);
                        firstGhost1Location.setY(icolumn);
                        break;
                    case "2":
                        objectsLocation[irow][icolumn] = Views.GHOST2;
                        firstGhost2Location.setX(irow);
                        firstGhost2Location.setY(icolumn);
                        break;
                    case "3":
                        objectsLocation[irow][icolumn] = Views.GHOST3;
                        firstGhost3Location.setX(irow);
                        firstGhost3Location.setY(icolumn);
                        break;
                    case "4":
                        objectsLocation[irow][icolumn] = Views.GHOST4;
                        firstGhost4Location.setX(irow);
                        firstGhost4Location.setY(icolumn);
                        break;
                    case "5":
                        objectsLocation[irow][icolumn] = Views.GHOST5;
                        firstGhost5Location.setX(irow);
                        firstGhost5Location.setY(icolumn);
                        break;
                    default:
                        objectsLocation[irow][icolumn] = Views.NONE;
                        break;
                }
                icolumn++;
            }
            irow++;
        }
        scanner.close();

        //Init Moving Objects
        pacman = new Pacman(firstPacManLocation);
        ghost1 = new Ghost(firstGhost1Location, Views.GHOST1);
        ghost2 = new Ghost(firstGhost2Location, Views.GHOST2);
        ghost3 = new Ghost(firstGhost3Location, Views.GHOST3);
        ghost4 = new Ghost(firstGhost4Location, Views.GHOST4);
        ghost5 = new Ghost(firstGhost5Location, Views.GHOST5);

    }

    public void initHelperPacMan() {
        pacman2 = new Pacman(new Point(firstXHelper, firstYHelper));
        objectsLocation[firstXHelper][firstYHelper] = Views.PAC_MAN;
    }

    //Moving Pac Man's Handel's if indicator == 1 it's main pac man if indicator == 2 it's helper pac man
    public void handelMoveUp(int indicator) {
        Pacman tempPacMan = null;
        if (indicator == 1) {
            tempPacMan = pacman;
        } else {
            tempPacMan = pacman2;
        }
        Point currentPacManLocation = tempPacMan.getLocation();
        int currentX = currentPacManLocation.getX();
        int currentY = currentPacManLocation.getY();
        Direction currentPacManDirection = tempPacMan.getDirection();

        //Check for End of Map
        if (currentX < 0) {
            tempPacMan.setDirection(Direction.UP);
            return;
        }

        //Check for it's Possible TODO:Finish it
        switch (objectsLocation[currentX - 1][currentY]) {
            case WALL:
                break;
            case SMALL_DOT:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX - 1][currentY] = Views.PAC_MAN;
                tempPacMan.setLocation(new Point(currentX - 1, currentY));
                score += 20;
                dots--;
                if (dots <= 0) {
                    isWon = true;
                }
                break;
            case BIG_DOT:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX - 1][currentY] = Views.PAC_MAN;
                tempPacMan.setLocation(new Point(currentX - 1, currentY));
                score += 30;
                dots--;
                if (dots <= 0) {
                    isWon = true;
                }
                isBigDotEaten = true;
                break;
            case GHOST1:
            case GHOST2:
            case GHOST3:
            case GHOST4:
            case GHOST5:
                if (!isBigDotEaten) {
                    if (indicator == 1) {
                        isLost = true;
                    } else  if(indicator == 2){
                        pacman2 = null;
                        isHelperDead = true;
                    }else if(indicator == 3){
                        isLost = true;
                    }
                }
                break;
            default:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX - 1][currentY] = Views.PAC_MAN;
                tempPacMan.setLocation(new Point(currentX - 1, currentY));
                break;
        }
        tempPacMan.setDirection(Direction.UP);
        updateAllObservers();
    }

    public void handelMoveDown(int indicator) {
        Pacman tempPacMan = null;
        if (indicator == 1) {
            tempPacMan = pacman;
        } else {
            tempPacMan = pacman2;
        }

        Point currentPacManLocation = tempPacMan.getLocation();
        int currentX = currentPacManLocation.getX();
        int currentY = currentPacManLocation.getY();
        Direction currentPacManDirection = tempPacMan.getDirection();

        //Check for End of Map
        if (currentX >= row) {
            tempPacMan.setDirection(Direction.DOWN);
            return;
        }

        //Check for it's Possible TODO:Finish it
        switch (objectsLocation[currentX + 1][currentY]) {
            case WALL:
                break;
            case SMALL_DOT:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX + 1][currentY] = Views.PAC_MAN;
                tempPacMan.setLocation(new Point(currentX + 1, currentY));
                score += 20;
                dots--;
                if (dots <= 0) {
                    isWon = true;
                }
                break;
            case BIG_DOT:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX + 1][currentY] = Views.PAC_MAN;
                tempPacMan.setLocation(new Point(currentX + 1, currentY));
                score += 30;
                dots--;
                if (dots <= 0) {
                    isWon = true;
                }
                isBigDotEaten = true;
                break;
            case GHOST1:
            case GHOST2:
            case GHOST3:
            case GHOST4:
            case GHOST5:
                if (!isBigDotEaten) {
                    if (indicator == 1) {
                        isLost = true;
                    } else  if(indicator == 2){
                        pacman2 = null;
                        isHelperDead = true;
                    }else if(indicator == 3){
                        isLost = true;
                    }
                }
                break;
            default:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX + 1][currentY] = Views.PAC_MAN;
                tempPacMan.setLocation(new Point(currentX + 1, currentY));
                break;
        }
        tempPacMan.setDirection(Direction.DOWN);
        updateAllObservers();
    }

    public void handelMoveRight(int indicator) {
        Pacman tempPacMan = null;
        if (indicator == 1) {
            tempPacMan = pacman;
        } else {
            tempPacMan = pacman2;
        }

        Point currentPacManLocation = tempPacMan.getLocation();
        int currentX = currentPacManLocation.getX();
        int currentY = currentPacManLocation.getY();
        Direction currentPacManDirection = tempPacMan.getDirection();

        //Check for End of Map
        if (currentY >= column - 1) {
            tempPacMan.setDirection(Direction.RIGHT);
            return;
        }

        switch (objectsLocation[currentX][currentY + 1]) {
            case WALL:

                break;
            case SMALL_DOT:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX][currentY + 1] = Views.PAC_MAN;
                tempPacMan.setLocation(new Point(currentX, currentY + 1));
                score += 20;
                dots--;
                if (dots <= 0) {
                    isWon = true;
                }
                break;
            case BIG_DOT:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX][currentY + 1] = Views.PAC_MAN;
                tempPacMan.setLocation(new Point(currentX, currentY + 1));
                score += 30;
                dots--;
                if (dots <= 0) {
                    isWon = true;
                }
                isBigDotEaten = true;
                break;
            case GHOST1:
            case GHOST2:
            case GHOST3:
            case GHOST4:
            case GHOST5:
                if (!isBigDotEaten) {
                    if (indicator == 1) {
                        isLost = true;
                    } else  if(indicator == 2){
                        pacman2 = null;
                        isHelperDead = true;
                    }else if(indicator == 3){
                        isLost = true;
                    }
                }
                break;
            default:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX][currentY + 1] = Views.PAC_MAN;
                tempPacMan.setLocation(new Point(currentX, currentY + 1));
                break;
        }
        tempPacMan.setDirection(Direction.RIGHT);
        updateAllObservers();
    }

    public void handelMoveLeft(int indicator) {
        Pacman tempPacMan = null;
        if (indicator == 1) {
            tempPacMan = pacman;
        } else {
            tempPacMan = pacman2;
        }

        Point currentPacManLocation = tempPacMan.getLocation();
        int currentX = currentPacManLocation.getX();
        int currentY = currentPacManLocation.getY();
        Direction currentPacManDirection = tempPacMan.getDirection();

        //Check for End of Map
        if (currentY <= 0) {
            tempPacMan.setDirection(Direction.LEFT);
            return;
        }

        //Check for it's Possible TODO:Finish it
        switch (objectsLocation[currentX][currentY - 1]) {
            case WALL:

                break;
            case SMALL_DOT:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX][currentY - 1] = Views.PAC_MAN;
                tempPacMan.setLocation(new Point(currentX, currentY - 1));
                score += 20;
                dots--;
                if (dots <= 0) {
                    isWon = true;
                }
                break;
            case BIG_DOT:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX][currentY - 1] = Views.PAC_MAN;
                tempPacMan.setLocation(new Point(currentX, currentY - 1));
                score += 30;
                dots--;
                if (dots <= 0) {
                    isWon = true;
                }
                isBigDotEaten = true;
                break;
            case GHOST1:
            case GHOST2:
            case GHOST3:
            case GHOST4:
            case GHOST5:
                if (!isBigDotEaten) {
                    if (indicator == 1) {
                        isLost = true;
                    } else  if(indicator == 2){
                        pacman2 = null;
                        isHelperDead = true;
                    }else if(indicator == 3){
                        isLost = true;
                    }
                }
                break;
            default:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX][currentY - 1] = Views.PAC_MAN;
                tempPacMan.setLocation(new Point(currentX, currentY - 1));
                break;
        }
        tempPacMan.setDirection(Direction.LEFT);
        updateAllObservers();
    }

    public void helperPacManMove() {
        Random random = new Random();
        int direct = random.nextInt(4);
        switch (direct) {
            case 0:
                handelMoveUp(2);
                break;
            case 1:
                handelMoveDown(2);
                break;
            case 2:
                handelMoveRight(2);
                break;
            case 3:
                handelMoveLeft(2);
                break;
        }
    }


    //Moving Ghost's

    //ghost 1 -> random left or right
    public void ghost1Move(int indicator) {
        Random random = new Random();
        int direct = random.nextInt(2);
        switch (direct) {
            case 0:
                handelMoveRightGhost(ghost1,indicator);
                break;
            case 1:
                handelMoveLeftGhost(ghost1,indicator);
                break;
        }
    }

    //ghost 2 -> random up or down
    public void ghost2Move(int indicator) {
        Random random = new Random();
        int direct = random.nextInt(2);
        switch (direct) {
            case 0:
                handelMoveUpGhost(ghost2,indicator);
                break;
            case 1:
                handelMoveDownGhost(ghost2,indicator);
                break;
        }
    }

    //ghost 3 -> random up or down or left or right
    public void ghost3Move(int indicator) {
        Random random = new Random();
        int direct = random.nextInt(4);
        switch (direct) {
            case 0:
                handelMoveUpGhost(ghost3,indicator);
                break;
            case 1:
                handelMoveDownGhost(ghost3,indicator);
                break;
            case 2:
                handelMoveRightGhost(ghost3,indicator);
                break;
            case 3:
                handelMoveLeftGhost(ghost3,indicator);
                break;
            default:
                handelMoveUpGhost(ghost3,indicator);
                break;
        }
    }

    //ghost 4 -> intelligent ghost
    public void ghost4Move(int indicator) {
        Direction direction = findIntelligentDirection(ghost4.getLocation().getX(), ghost4.getLocation().getY());
        switch (direction) {
            case UP:
                handelMoveUpGhost(ghost4,indicator);
                break;
            case DOWN:
                handelMoveDownGhost(ghost4,indicator);
                break;
            case RIGHT:
                handelMoveRightGhost(ghost4,indicator);
                break;
            case LEFT:
                handelMoveLeftGhost(ghost4,indicator);
                break;
        }
    }

    private Direction findIntelligentDirection(int currentX, int currentY) {
        int pacManCurrentX = pacman.getLocation().getX();
        int pacManCurrentY = pacman.getLocation().getY();
        double upDistance = Double.MAX_VALUE;
        double downDistance = Double.MAX_VALUE;
        double rightDistance = Double.MAX_VALUE;
        double leftDistance = Double.MAX_VALUE;
        //Check up
        if (currentX < 0 || objectsLocation[currentX - 1][currentY] == Views.WALL || objectsLocation[currentX - 1][currentY] == Views.GHOST1 || objectsLocation[currentX - 1][currentY] == Views.GHOST2 || objectsLocation[currentX - 1][currentY] == Views.GHOST3) {

        } else {
            upDistance = calDistance(currentX - 1, currentY, pacManCurrentX, pacManCurrentY);
        }
        //Check Down
        if (currentX >= row || objectsLocation[currentX + 1][currentY] == Views.WALL || objectsLocation[currentX + 1][currentY] == Views.GHOST1 || objectsLocation[currentX + 1][currentY] == Views.GHOST2 || objectsLocation[currentX + 1][currentY] == Views.GHOST3) {

        } else {
            downDistance = calDistance(currentX + 1, currentY, pacManCurrentX, pacManCurrentY);
        }
        //Check Right
        if (currentY >= column - 1 || objectsLocation[currentX][currentY + 1] == Views.WALL || objectsLocation[currentX][currentY + 1] == Views.GHOST1 || objectsLocation[currentX][currentY + 1] == Views.GHOST2 || objectsLocation[currentX][currentY + 1] == Views.GHOST3) {

        } else {
            rightDistance = calDistance(currentX, currentY + 1, pacManCurrentX, pacManCurrentY);
        }
        //Check Left
        if (currentY <= 0 || objectsLocation[currentX][currentY - 1] == Views.WALL || objectsLocation[currentX][currentY - 1] == Views.GHOST1 || objectsLocation[currentX][currentY - 1] == Views.GHOST2 || objectsLocation[currentX][currentY - 1] == Views.GHOST3) {

        } else {
            leftDistance = calDistance(currentX, currentY - 1, pacManCurrentX, pacManCurrentY);
        }

        //Decide
        double minDistance = min4(upDistance, downDistance, rightDistance, leftDistance);
        if (Math.abs(minDistance - upDistance) < 0.1) {
            return Direction.UP;
        } else if (Math.abs(minDistance - downDistance) < 0.1) {
            return Direction.DOWN;
        } else if (Math.abs(minDistance - rightDistance) < 0.1) {
            return Direction.RIGHT;
        } else if (Math.abs(minDistance - leftDistance) < 0.1) {
            return Direction.LEFT;
        } else {
            return Direction.RIGHT;
        }
    }

    public double calDistance(int x1, int y1, int x2, int y2) {
        double deltaX2 = Math.pow((x1 - x2), 2);
        double deltaY2 = Math.pow((y1 - y2), 2);
        return Math.sqrt(deltaX2 + deltaY2);
    }

    public double min4(double x1, double x2, double x3, double x4) {
        double min1 = Math.min(x1, x2);
        double min2 = Math.min(x3, x4);
        return Math.min(min1, min2);
    }

    public void handelMoveUpGhost(Ghost ghost, int indicator) {
        Point currentPacManLocation = ghost.getLocation();
        int currentX = currentPacManLocation.getX();
        int currentY = currentPacManLocation.getY();


        //Check for End of Map
        if (currentX < 0) {
            return;
        }

        //Check for it's Possible TODO:Finish it
        switch (objectsLocation[currentX - 1][currentY]) {
            case WALL:
            case GHOST1:
            case GHOST2:
            case GHOST3:
            case GHOST4:
            case GHOST5:

                break;
            case SMALL_DOT:
                objectsLocation[currentX][currentY] = Views.SMALL_DOT;
                objectsLocation[currentX - 1][currentY] = ghost.getViews();
                ghost.setLocation(new Point(currentX - 1, currentY));
                break;
            case BIG_DOT:
                objectsLocation[currentX][currentY] = Views.BIG_DOT;
                objectsLocation[currentX - 1][currentY] = ghost.getViews();
                ghost.setLocation(new Point(currentX - 1, currentY));
                break;
            case PAC_MAN:
                if (pacman2 == null) {
                    isLost = true;
                } else {
                    if (whichPacMan(currentX - 1, currentY) == pacman) {
                        isLost = true;
                    } else {
                        if(indicator == 2){
                            pacman2 = null;
                            isHelperDead = true;
                        }else{
                            isLost = true;
                        }
                    }
                }
                break;
            default:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX - 1][currentY] = ghost.getViews();
                ghost.setLocation(new Point(currentX - 1, currentY));
                break;
        }
        updateAllObservers();
    }

    public void handelMoveDownGhost(Ghost ghost, int indicator) {
        Point currentPacManLocation = ghost.getLocation();
        int currentX = currentPacManLocation.getX();
        int currentY = currentPacManLocation.getY();


        //Check for End of Map
        if (currentX >= row) {

            return;
        }

        //Check for it's Possible TODO:Finish it
        switch (objectsLocation[currentX + 1][currentY]) {
            case WALL:
            case GHOST1:
            case GHOST2:
            case GHOST3:
            case GHOST4:
            case GHOST5:
                break;
            case SMALL_DOT:
                objectsLocation[currentX][currentY] = Views.SMALL_DOT;
                objectsLocation[currentX + 1][currentY] = ghost.getViews();
                ghost.setLocation(new Point(currentX + 1, currentY));
                break;
            case BIG_DOT:
                objectsLocation[currentX][currentY] = Views.BIG_DOT;
                objectsLocation[currentX + 1][currentY] = ghost.getViews();
                ghost.setLocation(new Point(currentX + 1, currentY));
                break;
            case PAC_MAN:
                if (pacman2 == null) {
                    isLost = true;
                } else {
                    if (whichPacMan(currentX - 1, currentY) == pacman) {
                        isLost = true;
                    } else {
                        if(indicator == 2){
                            pacman2 = null;
                            isHelperDead = true;
                        }else{
                            isLost = true;
                        }
                    }
                }
                break;
            default:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX + 1][currentY] = ghost.getViews();
                ghost.setLocation(new Point(currentX + 1, currentY));
                break;
        }
        updateAllObservers();
    }

    public void handelMoveRightGhost(Ghost ghost, int indicator) {
        Point currentPacManLocation = ghost.getLocation();
        int currentX = currentPacManLocation.getX();
        int currentY = currentPacManLocation.getY();

        //Check for End of Map
        if (currentY >= column - 1) {
            return;
        }

        //Check for it's Possible TODO:Finish it
        switch (objectsLocation[currentX][currentY + 1]) {
            case WALL:
            case GHOST1:
            case GHOST2:
            case GHOST3:
            case GHOST4:
            case GHOST5:

                break;
            case SMALL_DOT:
                objectsLocation[currentX][currentY] = Views.SMALL_DOT;
                objectsLocation[currentX][currentY + 1] = ghost.getViews();
                ghost.setLocation(new Point(currentX, currentY + 1));
                break;
            case BIG_DOT:
                objectsLocation[currentX][currentY] = Views.BIG_DOT;
                objectsLocation[currentX][currentY + 1] = ghost.getViews();
                ghost.setLocation(new Point(currentX, currentY + 1));
                break;
            case PAC_MAN:
                if (pacman2 == null) {
                    isLost = true;
                } else {
                    if (whichPacMan(currentX - 1, currentY) == pacman) {
                        isLost = true;
                    } else {
                        if(indicator == 2){
                            pacman2 = null;
                            isHelperDead = true;
                        }else{
                            isLost = true;
                        }
                    }
                }
                break;
            default:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX][currentY + 1] = ghost.getViews();
                ghost.setLocation(new Point(currentX, currentY + 1));
                break;
        }
        updateAllObservers();
    }

    public void handelMoveLeftGhost(Ghost ghost, int indicator) {
        Point currentPacManLocation = ghost.getLocation();
        int currentX = currentPacManLocation.getX();
        int currentY = currentPacManLocation.getY();


        //Check for End of Map
        if (currentY <= 0) {

            return;
        }

        //Check for it's Possible TODO:Finish it
        switch (objectsLocation[currentX][currentY - 1]) {
            case WALL:
            case GHOST1:
            case GHOST2:
            case GHOST3:
            case GHOST4:
            case GHOST5:

                break;
            case SMALL_DOT:
                objectsLocation[currentX][currentY] = Views.SMALL_DOT;
                objectsLocation[currentX][currentY - 1] = ghost.getViews();
                ghost.setLocation(new Point(currentX, currentY - 1));
                break;
            case BIG_DOT:
                objectsLocation[currentX][currentY] = Views.BIG_DOT;
                objectsLocation[currentX][currentY - 1] = ghost.getViews();
                ghost.setLocation(new Point(currentX, currentY - 1));
                break;
            case PAC_MAN:
                if (pacman2 == null) {
                    isLost = true;
                } else {
                    if (whichPacMan(currentX - 1, currentY) == pacman) {
                        isLost = true;
                    } else {
                        if(indicator == 2){
                            pacman2 = null;
                            isHelperDead = true;
                        }else{
                            isLost = true;
                        }
                    }
                }
                break;
            default:
                objectsLocation[currentX][currentY] = Views.NONE;
                objectsLocation[currentX][currentY - 1] = ghost.getViews();
                ghost.setLocation(new Point(currentX, currentY - 1));
                break;
        }
        updateAllObservers();
    }

    public Pacman whichPacMan(int ghostLocationX, int ghostLocationY) {
        if(ghostLocationX == pacman.getLocation().getX() && ghostLocationY == pacman.getLocation().getY()){
            return pacman;
        }else{
            return pacman2;
        }
    }

    public Views[][] newViewPlaces() {
        return objectsLocation;
    }

    public Direction pacManDirection() {
        return pacman.getDirection();
    }

    public Direction ghost1Direction() {
        return ghost1.getDirection();
    }

    public Direction ghost2Direction() {
        return ghost2.getDirection();
    }

    public Direction ghost3Direction() {
        return ghost3.getDirection();
    }

    public Direction ghost4Direction() {
        return ghost4.getDirection();
    }

    public Direction ghost5Direction() {
        return ghost5.getDirection();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void updateAllObservers() {
        for (Observer temp : observers) {
            temp.update(this);
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDots() {
        return dots;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean lost) {
        isLost = lost;
    }

    public boolean isBigDotEaten() {
        return isBigDotEaten;
    }

    public void setBigDotEaten(boolean bigDotEaten) {
        isBigDotEaten = bigDotEaten;
    }

    public void changeGhostImages() {
        ghost1.setViews(Views.GHOST5);
        ghost2.setViews(Views.GHOST5);
        ghost3.setViews(Views.GHOST5);
        ghost4.setViews(Views.GHOST5);

        objectsLocation[ghost1.getLocation().getX()][ghost1.getLocation().getY()] = Views.GHOST5;
        objectsLocation[ghost2.getLocation().getX()][ghost2.getLocation().getY()] = Views.GHOST5;
        objectsLocation[ghost3.getLocation().getX()][ghost3.getLocation().getY()] = Views.GHOST5;
        objectsLocation[ghost4.getLocation().getX()][ghost4.getLocation().getY()] = Views.GHOST5;
    }

    public void resetGhostImages() {
        ghost1.setViews(Views.GHOST1);
        ghost2.setViews(Views.GHOST2);
        ghost3.setViews(Views.GHOST3);
        ghost4.setViews(Views.GHOST4);

        objectsLocation[ghost1.getLocation().getX()][ghost1.getLocation().getY()] = Views.GHOST1;
        objectsLocation[ghost2.getLocation().getX()][ghost2.getLocation().getY()] = Views.GHOST2;
        objectsLocation[ghost3.getLocation().getX()][ghost3.getLocation().getY()] = Views.GHOST3;
        objectsLocation[ghost4.getLocation().getX()][ghost4.getLocation().getY()] = Views.GHOST4;
    }

    public boolean isWon() {
        return isWon;
    }

    public void setWon(boolean won) {
        isWon = won;
    }

    public boolean isHelperDead() {
        return isHelperDead;
    }

    public void setHelperDead(boolean helperDead) {
        isHelperDead = helperDead;
    }

    public Direction helperPacManDirection() {
        return pacman2.getDirection();
    }
}
