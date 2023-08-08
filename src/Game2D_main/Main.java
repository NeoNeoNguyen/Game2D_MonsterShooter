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

        public Main() {
            init();
        }

        private void init() {
            setTitle("Game2D Monster Shooter");
            setSize(1366, 768);
            setLocationRelativeTo(null);
            setResizable(false);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            PanelGame panelGame = new PanelGame(null);
            add(panelGame);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                    panelGame.start();
                }
    //            public void windowClosed(WindowEvent e) {
    //                if (currentPlayer != null) {
    //                    // Lưu điểm số của người chơi vào cơ sở dữ liệu
    //                    currentPlayer.setScore(panelGame.getPlayerScore()); // Giả sử có phương thức getPlayerScore() trong PanelGame để lấy điểm số
    //                    PlayerScore.updateScore(currentPlayer.getPhone_number(), currentPlayer.getScore());
    //                }
    //            }

            });
        }


    }
