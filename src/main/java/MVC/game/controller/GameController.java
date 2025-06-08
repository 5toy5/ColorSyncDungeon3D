package MVC.game.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import MVC.game.model.*;
import MVC.game.view.GameView;
import MVC.game.view.GameView3D;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


public class GameController {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private GameState gameState;
    private boolean use3DView;

    public GameController(GraphicsContext gc, Scene scene, Stage stage, boolean use3DView) throws Exception {
        this.use3DView = use3DView;
        GameView gameView = null;
        GameView3D gameView3D = null;

        if (use3DView) {
            gameView3D = new GameView3D(stage);
            scene = gameView3D.getScene();
        } else {
            gameView = new GameView(gc);
        }

        this.gameState = new GameState(gameView, gameView3D, use3DView);

        // Set up user input
        scene.setOnKeyPressed(e -> gameState.keys.put(e.getCode(), true));
        scene.setOnKeyReleased(e -> gameState.keys.put(e.getCode(), false));

        startGameLoop();
    }

    private void startGameLoop() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameState.gameOver) {
                    handleInput();
                    gameState.update();
                    gameState.render();
                } else {
                    // Handle game over
                }
            }
        }.start();

        startColorDetection();
    }

    private void handleInput() {
        if (isPressed(KeyCode.W)) gameState.player.move(0);
        if (isPressed(KeyCode.S)) gameState.player.move(Math.PI);
        if (isPressed(KeyCode.A)) gameState.player.move(-Math.PI / 2);
        if (isPressed(KeyCode.D)) gameState.player.move(Math.PI / 2);

        if (isPressed(KeyCode.Q)) gameState.player.rotate(-0.1);
        if (isPressed(KeyCode.E)) gameState.player.rotate(0.1);

        if (isPressed(KeyCode.R)) gameState.player.changeType("red");
        if (isPressed(KeyCode.G)) gameState.player.changeType("green");
        if (isPressed(KeyCode.B)) gameState.player.changeType("blue");

        if (isPressed(KeyCode.SPACE)) gameState.player.attack(gameState.attack);
    }

    private boolean isPressed(KeyCode key) {
        return gameState.keys.getOrDefault(key, false);
    }

    void startColorDetection() {
        int cameraIndex = findCamera();
        if (cameraIndex == -1) {
            System.out.println("カメラが見つかりませんでした");
            return;
        }

        VideoCapture camera = new VideoCapture(cameraIndex);
        Mat frame = new Mat();
        double width = camera.get(Videoio.CAP_PROP_FRAME_WIDTH);
        double height = camera.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        double campix = width * height;
        System.out.println("カメラ解像度: " + campix);

        new Thread(() -> {
            while (true) {
                camera.read(frame);
                if (frame.empty()) {
                    System.out.println("フレームが読み込めませんでした");
                    break;
                }

                Mat hsvImage = new Mat();
                Imgproc.cvtColor(frame, hsvImage, Imgproc.COLOR_BGR2HSV);

                Mat blueMask = new Mat();
                Mat redMask = new Mat();
                Mat greenMask = new Mat();

                Core.inRange(hsvImage, new Scalar(100, 100, 50), new Scalar(140, 255, 255), blueMask);
                Core.inRange(hsvImage, new Scalar(0, 100, 50), new Scalar(10, 255, 255), redMask);

                Mat redMask2 = new Mat();
                Core.inRange(hsvImage, new Scalar(160, 100, 50), new Scalar(180, 255, 255), redMask2);
                Core.addWeighted(redMask, 1.0, redMask2, 1.0, 0.0, redMask);

                Core.inRange(hsvImage, new Scalar(50, 150, 50), new Scalar(100, 255, 255), greenMask);

                int b = 0;
                int r = 0;
                int g = 0;

                if ((double) Core.countNonZero(blueMask) / campix > 0.125) {
                    b = Core.countNonZero(blueMask);
                }
                if ((double) Core.countNonZero(redMask) / campix > 0.125) {
                    r = Core.countNonZero(redMask);
                }
                if ((double) Core.countNonZero(greenMask) / campix > 0.125) {
                    g = Core.countNonZero(greenMask);
                }

                int maxint = Math.max(b, Math.max(r, g));
                if (b != 0 && Core.countNonZero(blueMask) == maxint) {
                    System.out.println("青色検出!");
                    gameState.player.changeType("blue");
                }
                if (r != 0 && Core.countNonZero(redMask) == maxint) {
                    System.out.println("赤色検出!");
                    gameState.player.changeType("red");
                }
                if (g != 0 && Core.countNonZero(greenMask) == maxint) {
                    System.out.println("緑色検出!");
                    gameState.player.changeType("green");
                }

                HighGui.imshow("Camera Feed", frame);
                if (HighGui.waitKey(1) == 27) {
                    break;
                }
            }

            camera.release();
            HighGui.destroyAllWindows();
        }).start();
    }

    public static int findCamera() {
        for (int i = 0; i < 10; i++) {
            VideoCapture camera = new VideoCapture(i);
            if (camera.isOpened()) {
                camera.release();
                return i;
            }
        }
        return -1;
    }
}
