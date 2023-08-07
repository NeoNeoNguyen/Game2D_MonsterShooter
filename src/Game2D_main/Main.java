package Game2D_main;

import Game2D_component.PanelGame;
import Game2D_data.PlayerScore;
import Game2D_data.Score;
import Game2D_obj.Player;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends JFrame {

    private Player currentPlayer; // Thông tin người chơi đang đăng nhập
    
    

    public Main() {
        init();
    }
    

//    private void init() {
//        setTitle("Game2D Monster Shooter");
//        setSize(1366, 768);
//        setLocationRelativeTo(null);
//        setResizable(false);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//        PanelGame panelGame = new PanelGame();
//        add(panelGame);
//        addWindowListener(new WindowAdapter() {
//            @Override
////            public void windowOpened(WindowEvent e) {
////                panelGame.start();
////            }
//            public void windowClosed(WindowEvent e) {
//                if (currentPlayer != null) {
//                    // Lưu điểm số của người chơi vào cơ sở dữ liệu
//                    currentPlayer.setScore(panelGame.getPlayerScore()); // Giả sử có phương thức getPlayerScore() trong PanelGame để lấy điểm số
//                    PlayerScore.updateScore(currentPlayer.getPhone_number(), currentPlayer.getScore());
//                }
//            }
//            
//        });
//    }
    private void init() {
        setTitle("Game2D Monster Shooter");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        if (currentPlayer != null) {
            PanelGame panelGame = new PanelGame(currentPlayer);
            add(panelGame);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    // Lưu điểm số của người chơi vào cơ sở dữ liệu
                    int playerScore = panelGame.getPlayerScore();

                    // Lấy thời gian hiện tại
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

                    // Định dạng thời gian thành chuỗi
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedTime = dateFormat.format(new Date(currentTimestamp.getTime()));

                    // Tạo một đối tượng Score mới và truyền thông tin
                    Score score = new Score(
                            new Timestamp(System.currentTimeMillis()).toString(),
                            currentPlayer.getPhone_number(),
                            playerScore,
                            "0" // Thời gian chưa được xử lý ở mã của bạn, bạn cần cập nhật đúng giá trị thời gian ở đây
                    );
                    PlayerScore.addScore(score);
                }
            });
        }
    }
}
