package ir.ac.kntu;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.*;
import java.sql.Time;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    final private static double FRAMES_PER_SECOND = 5.0;
    private Scene scene;
    private View view;
    private Model model;
    private KeyCode keyPressed;
    private Timer timer;
    private int loopCounter;
    private int mode;
    private int surviveCounter = 3;

    //Labels
    private Label scoreLabel;
    private Label heartLabel;
    private Label gameOverLabel;

    public Controller(Group root, Scene scene, int mode) {
        this.view = new View(root);
        this.model = new Model();
        this.scene = scene;
        keyPressed = KeyCode.SPACE;
        scene.setOnKeyPressed(keyEvent -> {
            keyPressed = keyEvent.getCode();
            handelKeyPressed();
        });

        switch (mode){
            case 1:
                break;
            case 2:
                model.initHelperPacMan();
                break;
            case 3:
                model.initHelperPacMan();
                addKeyHandler();
                break;
        }
        view.MakeBoard(model.newViewPlaces());
        model.addObserver(this.view);


        startTimer();

        scoreLabel = new Label("Score : 0");
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setLayoutX(5);
        scoreLabel.setLayoutY(0);

        heartLabel = new Label("Hearts: 5");
        heartLabel.setTextFill(Color.ORANGERED);
        heartLabel.setFont(Font.font("Arial", 15));
        heartLabel.setLayoutX(300);
        heartLabel.setLayoutY(0);

        gameOverLabel = new Label("Game Over");
        gameOverLabel.setTextFill(Color.RED);
        gameOverLabel.setLayoutX(110);
        gameOverLabel.setLayoutY(180);
        gameOverLabel.setFont(Font.font("Arial", 30));
        gameOverLabel.setVisible(false);


        root.getChildren().addAll(scoreLabel, heartLabel, gameOverLabel);
        this.loopCounter = 0;
        this.mode = mode;
    }

    private void handelKeyPressed() {
        switch (keyPressed) {
            case UP:
                model.handelMoveUp(1);
                break;
            case DOWN:
                model.handelMoveDown(1);
                break;
            case RIGHT:
                model.handelMoveRight(1);
                break;
            case LEFT:
                model.handelMoveLeft(1);
                break;
        }
    }

    private void addKeyHandler() {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case W:
                        model.handelMoveUp(3);
                        break;
                    case S:
                        model.handelMoveDown(3);
                        break;
                    case D:
                        model.handelMoveRight(3);
                        break;
                    case A:
                        model.handelMoveLeft(3);
                        break;
                }
            }
        });
    }

    private void startTimer() {
        this.timer = new java.util.Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {

                        if (!model.isBigDotEaten()) {
                            model.ghost1Move(mode);
                            model.ghost2Move(mode);
                            model.ghost3Move(mode);
                            model.ghost4Move(mode);
                        } else {
                            loopCounter++;
                            model.changeGhostImages();
                            if (loopCounter % 20 == 0) {
                                model.setBigDotEaten(false);
                                model.resetGhostImages();
                            }
                        }

                        //Update Score Label
                        scoreLabel.setText("Score : " + String.valueOf(model.getScore()));

                        //Check for loos
                        if (model.isLost()) {
                            surviveCounter--;
                            model.initMap();
                            model.setLost(false);
                            lostVoice();
                            heartLabel.setText("Hearts: " + surviveCounter);
                            if (surviveCounter == 0) {
                                timer.cancel();
                                gameOverLabel.setText("Game Over\nScore: " + model.getScore());
                                gameOverLabel.setVisible(true);
                                writeScoreToFile();
                                model.setScore(0);
                            }
                        }

                        //Check for Win
                        if (model.isWon()) {
                            gameOverLabel.setText("You Won!");
                            gameOverLabel.setVisible(true);
                        }

                        //Helper Move
                        if(mode == 2 && !model.isHelperDead()){
                            model.helperPacManMove();
                        }
                    }

                });

            }
        };
        long frameTimeInMilliseconds = (long) (1000.0 / FRAMES_PER_SECOND);
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }

    public void runVoice() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("pacman_chomp.wav");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AudioStream audioStream = null;
        try {
            audioStream = new AudioStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AudioPlayer.player.start(audioStream);
    }

    public void lostVoice(){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("pacman_death.wav");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AudioStream audioStream = null;
        try {
            audioStream = new AudioStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AudioPlayer.player.start(audioStream);
    }

    public void writeScoreToFile() {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        PrintWriter printWriter = null;
        try {

            Date date = new Date(System.currentTimeMillis());
            fileWriter = new FileWriter("PacManScore " + String.valueOf(date.getDay() + date.getMonth() + date.getYear()) + ".txt", false);
            bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);
            printWriter.write("Score : " + model.getScore());
        } catch (IOException e) {
            System.err.println("An Error Occurred while writing");
            e.printStackTrace();
        } finally {
            printWriter.flush();
            try {
                if (printWriter == null) {
                    printWriter.close();
                }
                if (bufferedWriter == null) {
                    bufferedWriter.close();
                }
                if (fileWriter == null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.err.println("An error occurred while closing " +
                        "writers.");
                e.printStackTrace();
            }
        }
    }

}
