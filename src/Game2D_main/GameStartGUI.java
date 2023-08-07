package Game2D_main;

import Game2D_component.PanelGame;
import Game2D_data.PlayerScore;
import Game2D_obj.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameStartGUI extends JFrame {

    private Player currentPlayer; // Thông tin người chơi đang đăng nhập

    public GameStartGUI() {
        initUI();
    }

    private void initUI() {
        setTitle("Game2D Monster Shooter");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JButton playButton = new JButton("Play Game");
        JButton loginButton = new JButton("Login Player");
        JButton registerButton = new JButton("Register Player");
        JButton leaderboardButton = new JButton("Leaderboard");
        JButton instructionsButton = new JButton("Instructions");

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPlayer != null) {
                    // Mở cửa sổ trò chơi sau khi nhấn nút "Play Game"
                    dispose(); // Đóng cửa sổ hiện tại
                    PanelGame panelGame = new PanelGame(currentPlayer); // Truyền thông tin người chơi
                    JFrame gameFrame = new JFrame();
                    gameFrame.add(panelGame);
                    gameFrame.setSize(1366, 768);
                    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    gameFrame.setVisible(true);
                    gameFrame.setLocationRelativeTo(null);
                    panelGame.start();
                } else {
                    // Người chơi chưa đăng nhập, hiển thị cửa sổ trò chơi không lưu trữ
                    dispose(); // Đóng cửa sổ hiện tại
                    PanelGame panelGame = new PanelGame(null); // Truyền null khi không có người chơi
                    JFrame gameFrame = new JFrame();
                    gameFrame.add(panelGame);
                    gameFrame.setSize(1366, 768);
                    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    gameFrame.setVisible(true);
                    gameFrame.setLocationRelativeTo(null);
                    panelGame.start();
                }
            }
        });

//        loginButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                // Hiển thị cửa sổ đăng nhập
//                String username = JOptionPane.showInputDialog(GameStartGUI.this, "Enter your username:");
//                String password = JOptionPane.showInputDialog(GameStartGUI.this, "Enter your password:");
//
//                // Kiểm tra đăng nhập
//                if (PlayerScore.login(username, password)) {
//                    // Đăng nhập thành công, hiển thị cửa sổ trò chơi
//                    dispose();
////                    PanelGame panelGame = new PanelGame();
//                    PanelGame panelGame;
//                    if (currentPlayer != null) {
//                        panelGame = new PanelGame(currentPlayer);
//                    } else {
//                        panelGame = new PanelGame(null);
//                    }
//                    JFrame gameFrame = new JFrame();
//                    gameFrame.add(panelGame);
//                    gameFrame.setSize(1366, 768);
//                    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    gameFrame.setVisible(true);
//                    gameFrame.setLocationRelativeTo(null);
//                    panelGame.start();
//                } else {
//                    JOptionPane.showMessageDialog(GameStartGUI.this, "Invalid username or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Hiển thị cửa sổ đăng nhập
                String username = JOptionPane.showInputDialog(GameStartGUI.this, "Enter your username:");
                String password = JOptionPane.showInputDialog(GameStartGUI.this, "Enter your password:");

                // Kiểm tra đăng nhập
                if (PlayerScore.login(username, password)) {
                    // Lấy thông tin người chơi từ cơ sở dữ liệu
                    Player currentPlayer = PlayerScore.getPlayerInfo(username);

                    // Gán thông tin người chơi vào biến currentPlayer
                    GameStartGUI.this.currentPlayer = currentPlayer;

                    // Đăng nhập thành công, hiển thị cửa sổ trò chơi
                    dispose();
                    PanelGame panelGame = new PanelGame(currentPlayer); // Truyền thông tin người chơi
                    JFrame gameFrame = new JFrame();
                    gameFrame.add(panelGame);
                    gameFrame.setSize(1366, 768);
                    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    gameFrame.setVisible(true);
                    gameFrame.setLocationRelativeTo(null);
                    panelGame.start();
                } else {
                    JOptionPane.showMessageDialog(GameStartGUI.this, "Invalid username or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Bổ sung code để xử lý sự kiện khi người chơi nhấn nút "Register" trong cửa sổ đăng ký
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Hiển thị cửa sổ đăng ký
                String username = JOptionPane.showInputDialog(GameStartGUI.this, "Enter a new username:");
                String phone = JOptionPane.showInputDialog(GameStartGUI.this, "Enter your phone number:");
                String password = JOptionPane.showInputDialog(GameStartGUI.this, "Enter a password for your account:");

                // Tạo đối tượng Player và đặt giá trị cho name, phone_number, và password
                Player newPlayer = new Player();
                newPlayer.setName(username);
                newPlayer.setPhone_number(phone);
                newPlayer.setPassword(password);

                // Thực hiện đăng ký và lưu thông tin người chơi vào cơ sở dữ liệu
                boolean success = PlayerScore.register(newPlayer);
                if (success) {
                    JOptionPane.showMessageDialog(GameStartGUI.this, "Registration successful! You can now log in.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(GameStartGUI.this, "Registration failed. Please try again.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        leaderboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Hiển thị cửa sổ xếp hạng
                List<PlayerScore.ScoreInfo> leaderboard = PlayerScore.getLeaderboard();

                StringBuilder leaderboardText = new StringBuilder("Leaderboard:\n");
                for (int i = 0; i < leaderboard.size(); i++) {
                    PlayerScore.ScoreInfo scoreInfo = leaderboard.get(i);
                    leaderboardText.append(i + 1).append(". ").append(scoreInfo.getPlayerName()).append(" - ")
                            .append(scoreInfo.getPlayerScore()).append(" points in ").append(scoreInfo.getPlayerTime())
                            .append(" seconds\n");
                }

                JOptionPane.showMessageDialog(GameStartGUI.this, leaderboardText.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        instructionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Hiển thị hướng dẫn sau khi nhấn nút "Instructions"
                String instructions = "Welcome to Monster Shooter!\n\n"
                        + "Use 'A' and 'D' to move left and right.\n"
                        + "Press 'Space' to speed up.\n"
                        + "Press 'J' to shoot a small bullet.\n"
                        + "Press 'K' to shoot a large bullet.\n"
                        + "Avoid monsters and shoot them to earn points!";
                JOptionPane.showMessageDialog(GameStartGUI.this, instructions, "Instructions", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.add(playButton);
        panel.add(loginButton);
        panel.add(registerButton);
        panel.add(leaderboardButton);
        panel.add(instructionsButton);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GameStartGUI gameStartGUI = new GameStartGUI();
                gameStartGUI.setVisible(true);
            }
        });
    }
}
