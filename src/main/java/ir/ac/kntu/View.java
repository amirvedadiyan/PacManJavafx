package ir.ac.kntu;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class View implements Observer {
    private Group root;
    private ImageView[][] imageViews;
    private Image wallImage;
    private Image smallDotImage;
    private Image bitDotImage;
    private Image pacmanUp;
    private Image pacmanDown;
    private Image pacmanRight;
    private Image pacmanLeft;
    private Image ghost1;
    private Image ghost2;
    private Image ghost3;
    private Image ghost4;
    private Image ghost5;
    private int row;
    private int column;


    public final static double CELL_WIDTH = 20.0;
    public final static double CELL_HEIGHT = 20.0;

    //Constructor that Init Images
    public View(Group root) {
        this.root = root;
        try {
            this.wallImage = new Image(new FileInputStream("src\\res\\wall.png"));
            this.smallDotImage = new Image(new FileInputStream("src\\res\\smalldot.png"));
            this.bitDotImage = new Image(new FileInputStream("src\\res\\bigdot.png"));
            this.pacmanUp = new Image(new FileInputStream("src\\res\\pacmanUp.gif"));
            this.pacmanDown = new Image(new FileInputStream("src\\res\\pacmanDown.gif"));
            this.pacmanRight = new Image(new FileInputStream("src\\res\\pacmanRight.gif"));
            this.pacmanLeft = new Image(new FileInputStream("src\\res\\pacmanLeft.gif"));
            this.ghost1 = new Image(new FileInputStream("src\\res\\ghost1.gif"));
            this.ghost2 = new Image(new FileInputStream("src\\res\\ghost2.gif"));
            this.ghost3 = new Image(new FileInputStream("src\\res\\ghost3.gif"));
            this.ghost4 = new Image(new FileInputStream("src\\res\\ghost4.gif"));
            this.ghost5 = new Image(new FileInputStream("src\\res\\ghost5.gif"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Make First Board of Game
    public void MakeBoard(Views[][] firstViewsPlace) {
        row = firstViewsPlace.length;
        column = firstViewsPlace[0].length;
        System.out.println(row + " " + column);
        //Set Images
        imageViews = new ImageView[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                ImageView temp = new ImageView();
                temp.setX((double) j * CELL_WIDTH);
                temp.setY((double) i * CELL_HEIGHT);
                temp.setFitWidth(CELL_WIDTH);
                temp.setFitHeight(CELL_HEIGHT);
                //First Image Of Board
                switch (firstViewsPlace[i][j]) {
                    case NONE:
                        temp.setImage(null);
                        break;
                    case SMALL_DOT:
                        temp.setImage(smallDotImage);
                        break;
                    case BIG_DOT:
                        temp.setImage(bitDotImage);
                        break;
                    case WALL:
                        temp.setImage(wallImage);
                        break;
                    case GHOST1:
                        temp.setImage(ghost1);
                        break;
                    case GHOST2:
                        temp.setImage(ghost2);
                        break;
                    case GHOST3:
                        temp.setImage(ghost3);
                        break;
                    case GHOST4:
                        temp.setImage(ghost4);
                        break;
                    case GHOST5:
                        temp.setImage(ghost5);
                        break;
                    case PAC_MAN:
                        temp.setImage(pacmanRight);
                        break;
                }
                root.getChildren().add(temp);
                imageViews[i][j] = temp;
            }
        }
    }

    //When Views Change Update Called
    @Override
    public void update(Observable changeObservable) {
        Model model = (Model) changeObservable;
        Views[][] updatedViewsPlace = model.newViewPlaces();
        //Update Board
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                Views updateView = updatedViewsPlace[i][j];
                //WALL DOT NONE
                switch (updateView) {
                    case WALL:
                        imageViews[i][j].setImage(wallImage);
                        break;
                    case SMALL_DOT:
                        imageViews[i][j].setImage(smallDotImage);
                        break;
                    case BIG_DOT:
                        imageViews[i][j].setImage(bitDotImage);
                        break;
                    case GHOST1:
                        imageViews[i][j].setImage(ghost1);
                        break;
                    case GHOST2:
                        imageViews[i][j].setImage(ghost2);
                        break;
                    case GHOST3:
                        imageViews[i][j].setImage(ghost3);
                        break;
                    case GHOST4:
                        imageViews[i][j].setImage(ghost4);
                        break;
                    case GHOST5:
                        imageViews[i][j].setImage(ghost5);
                        break;
                    case NONE:
                        imageViews[i][j].setImage(null);
                        break;
                }
                //PAC MAN
                if (updateView == Views.PAC_MAN) {
                    Direction pacmanDirection = model.pacManDirection();
                    switch (pacmanDirection) {
                        case UP:
                            imageViews[i][j].setImage(pacmanUp);
                            break;
                        case DOWN:
                            imageViews[i][j].setImage(pacmanDown);
                            break;
                        case RIGHT:
                            imageViews[i][j].setImage(pacmanRight);
                            break;
                        case LEFT:
                            imageViews[i][j].setImage(pacmanLeft);
                            break;
                    }
                }

//                }
            }
        }
    }

    //Setter and Getter
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

}
