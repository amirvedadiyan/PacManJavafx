package ir.ac.kntu;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static javafx.application.Application.launch;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Group root = new Group();
        Scene scene = new Scene(root,380,420);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("PacMan");
        primaryStage.show();
        InputStream inputStream = new FileInputStream("pacman_beginning.wav");
        AudioStream audioStream = new AudioStream(inputStream);
        AudioPlayer.player.start(audioStream);
        Controller controller = new Controller(root,scene,1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
