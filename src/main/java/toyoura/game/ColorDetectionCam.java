////package toyoura.game;
//
//import org.opencv.core.*;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.videoio.VideoCapture;
//import org.opencv.highgui.HighGui;
//import java.util.*;
//
//public class ColorDetectionCam {
//    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
//
//    static {
//        // ネイティブライブラリのパスを設定
////        System.setProperty("java.library.path", "native");
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//    }
//    public static void main(String[] args) {
//        VideoCapture camera = new VideoCapture(0); // カメラを開く
//        if (!camera.isOpened()) {
//            System.out.println("カメラが開けませんでした");
//            return;
//        }
//
//        Mat frame = new Mat(); // カメラ映像を格納するためのMatオブジェクト
//
//        while (true) {
//            camera.read(frame);
//            if (frame.empty()) {
//                System.out.println("フレームが読み込めませんでした");
//                break;
//            }
//
//            Mat hsvImage = new Mat();
//            Imgproc.cvtColor(frame, hsvImage, Imgproc.COLOR_BGR2HSV); // HSV色空間に変換
//
//            // 色範囲を指定してマスクを作成
//            Mat blueMask = new Mat();
//            Mat redMask = new Mat();
//            Mat greenMask = new Mat();
//
//            // 青色の範囲 (例: HSVで100-140の範囲)
//            Core.inRange(hsvImage, new Scalar(100, 100, 50), new Scalar(140, 255, 255), blueMask);
//
//            // 赤色の範囲 (例: HSVで0-10と160-180の範囲)
//            Core.inRange(hsvImage, new Scalar(0, 100, 50), new Scalar(10, 255, 255), redMask);
//
//            // 2つ目の赤色範囲を検出してredMask2に格納
//            Mat redMask2 = new Mat();
//            Core.inRange(hsvImage, new Scalar(160, 100, 50), new Scalar(180, 255, 255), redMask2);
//
//            // 2つの赤色マスクを統合
//            Core.addWeighted(redMask, 1.0, redMask2, 1.0, 0.0, redMask);
//
//            // 緑色の範囲 (例: HSVで20-30の範囲)
//            Core.inRange(hsvImage, new Scalar(50, 100, 50), new Scalar(100, 255, 255), greenMask);
//
//
//            int b=0;
//            int r=0;
//            int g=0;
//            // 各色の検出結果を確認
//            if (Core.countNonZero(blueMask) > 5000) {
//                b=Core.countNonZero(blueMask);
//            }
//            if (Core.countNonZero(redMask) > 5000) {
//                r=Core.countNonZero(redMask);
//            }
//            if (Core.countNonZero(greenMask) > 5000) {
//                g=Core.countNonZero(greenMask);
//            }
//            int maxint = Math.max(b,Math.max(r,g));
//            if (b!=0 && Core.countNonZero(blueMask) == maxint) {
//                System.out.println("青色検出!");
//            }
//            if (r!=0 && Core.countNonZero(redMask) == maxint ) {
//                System.out.println("赤色検出!");
//            }
//            if (g!=0 && Core.countNonZero(greenMask) == maxint) {
//                g=Core.countNonZero(greenMask);
//                System.out.println("緑色検出!");
//            }
//
//            // カメラ映像を表示
//            HighGui.imshow("Camera Feed", frame);
//
//            // キー入力待ち（1ms）
//            if (HighGui.waitKey(1) == 27) { // ESCキーで終了
//                break;
//            }
//        }
//
//        camera.release();
//        HighGui.destroyAllWindows();
//    }
//}
//
