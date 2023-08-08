package Game2D_component;

import Game2D_data.PlayerScore;
import static Game2D_data.PlayerScore.addScore;
import Game2D_data.Score;
import Game2D_obj.Bullet;
import Game2D_obj.Effect;
import Game2D_obj.Monster;
import Game2D_obj.Player;
import Game2D_sound.Sound;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.management.timer.Timer;
import javax.swing.JFrame;

public class PanelGame extends JComponent {

    //private Image image;
    private Graphics2D g2;
    private BufferedImage image;
    private int width;
    private int height;
    private Thread thread;
    private boolean start = true;
    private Key key;
    private int shotTime;

    //Game FPS
    private final int FPS = 60;
    private final int TARGET_TIME = 1000000000 / FPS;

    //Game Obj
    private Sound sound;
    private Player player;
    private List<Bullet> bullets;
    private List<Monster> monster01;
    //private List<Monster> monster02;
    //private List<Monster> monster03;
    private List<Effect> boomEffects;
    private int score = 0;
    private String playerName;

    private long startTimeMillis; // Biến để lưu thời điểm bắt đầu chơi

    private Player loggedInPlayer; // Thông tin người chơi đang đăng nhập
    private int playerScore; // Điểm số của người chơi

    //tăng level game
    private int currentLevel = 1; // Màn chơi hiện tại
    private int monstersDefeated = 0; // Số lượng quái vật đã tiêu diệt

    public void setLoggedInPlayer(Player player) {
        loggedInPlayer = player;
    }

    public PanelGame(Player player) {
        this.loggedInPlayer = player;
    }

    public void start() {
        width = getWidth();
        height = getHeight();

        //this.image = (BufferedImage) new ImageIcon(getClass().getResource("/Game2D_image/background.png")).getImage();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Gán thời điểm bắt đầu khi chơi
                startTimeMillis = System.currentTimeMillis();
                while (start) {
                    long startTime = System.nanoTime();
                    drawBackground();
                    drawGame();
                    render();
                    long time = System.nanoTime() - startTime;
                    if (time < TARGET_TIME) {
                        long sleep = (TARGET_TIME - time) / 1000000;
                        sleep(sleep);
                    }
                }
            }

            private String generateRandomName() {
                String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                int nameLength = new Random().nextInt(8) + 1; // Từ 1 đến 8 ký tự
                StringBuilder randomName = new StringBuilder();
                for (int i = 0; i < nameLength; i++) {
                    int randomIndex = new Random().nextInt(characters.length());
                    randomName.append(characters.charAt(randomIndex));
                }
                return randomName.toString();
            }
        });
        setLoggedInPlayer(loggedInPlayer);
        initObjectGame();
        initKeyboard();
        initBullet();
        thread.start();
    }

    //monster
    private void addMonster() {
//        Random ran = new Random();
//        int locationY = ran.nextInt(height - 50) + 25;
//        Monster monster = new Monster();
//        monster.changeLocation(0, locationY);
//        monster.changeAngle(0);
//        monster01.add(monster);
//
//        int locationY2 = ran.nextInt(height - 50) + 25;
//        Monster monter2 = new Monster();
//        monter2.changeLocation(width, locationY2);
//        monter2.changeAngle(180);
//        monster01.add(monter2);

        Random ran = new Random();
        int locationY = ran.nextInt(height - 50) + 25;
        Monster monster = new Monster();

        // Tùy theo level, bạn có thể thay đổi thông số của quái vật
        if (currentLevel == 2) {
            // Thay đổi tốc độ di chuyển và máu của quái vật tại level 2
            monster.changeSpeed(0.5f); // Điều chỉnh tốc độ di chuyển
            monster.updateMaxHP(30); // Điều chỉnh máu tối đa
        }

        monster.changeLocation(0, locationY);
        monster.changeAngle(0);
        monster01.add(monster);

        int locationY2 = ran.nextInt(height - 50) + 25;
        Monster monster2 = new Monster();

        // Tùy theo level, bạn có thể thay đổi thông số của quái vật
        if (currentLevel == 2) {
            // Thay đổi tốc độ di chuyển và máu của quái vật tại level 2
            monster2.changeSpeed(0.5f); // Điều chỉnh tốc độ di chuyển
            monster2.updateMaxHP(30); // Điều chỉnh máu tối đa
        }

        monster2.changeLocation(width, locationY2);
        monster2.changeAngle(180);
        monster01.add(monster2);

    }

    private void initObjectGame() {
        sound = new Sound();
        player = new Player();
        player.changeLocation(150, 150);
        monster01 = new ArrayList<>();
        boomEffects = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    addMonster();
                    sleep(3000);
                }
            }
        }).start();

    }

    private void resetGame() {
        monster01.clear();
        bullets.clear();
        player.changeLocation(150, 150);
        player.reset();
        score = 0;
        playerScore = 0;
    }

    //key
    private void initKeyboard() {
        key = new Key();
        requestFocus();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    key.setKey_left(true);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    key.setKey_right(true);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    key.setKey_space(true);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    key.setKey_j(true);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    key.setKey_k(true);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKey_enter(true);
                }
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    key.setKey_left(false);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    key.setKey_right(false);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    key.setKey_space(false);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    key.setKey_j(false);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    key.setKey_k(false);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKey_enter(false);
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                float s = 0.5f;
                while (start) {
                    if (player.isAlive()) {
                        float angle = player.getAngle();
                        if (key.isKey_left()) {
                            angle -= s;
                        }
                        if (key.isKey_right()) {
                            angle += s;
                        }

                        if (key.isKey_j() || key.isKey_k()) {
                            if (shotTime == 0) {
                                if (key.isKey_j()) {
                                    bullets.add(0, new Bullet(player.getX(), player.getY(), player.getAngle(), 5, 3f));
                                } else {
                                    bullets.add(0, new Bullet(player.getX(), player.getY(), player.getAngle(), 20, 3f));
                                }
                                sound.soundShoot();
                            }
                            shotTime++;
                            if (shotTime == 15) {
                                shotTime = 0;
                            }
                        } else {
                            shotTime = 0;
                        }

                        if (key.isKey_space()) {
                            player.speedUp();
                        } else {
                            player.speedDown();
                        }
                        player.update();
                        player.changeAngle(angle);
                    } else {
                        if (key.isKey_enter()) {
                            resetGame();
                        }
                    }

                    for (int i = 0; i < monster01.size(); i++) {
                        Monster monster1 = monster01.get(i);
                        if (monster1 != null) {
                            monster1.update();
                            if (!monster1.check(width, height)) {
                                monster01.remove(monster1);
                            } else {
                                if (player.isAlive()) {
                                    checkPlayer(monster1);
                                }
                            }
                        }
                    }
                    sleep(5);
                }
            }
        }).start();
    }

    //bullet
    private void initBullet() {
        bullets = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    for (int i = 0; i < bullets.size(); i++) {
                        Bullet bullet = bullets.get(i);
                        if (bullet != null) {
                            bullet.update();
                            checkBullets(bullet);
                            if (!bullet.check(width, height)) {
                                bullets.remove(bullet);
                            }
                        } else {
                            bullets.remove(bullet);
                        }
                    }
                    //Them effect 
                    for (int i = 0; i < boomEffects.size(); i++) {
                        Effect boomEffect = boomEffects.get(i);
                        if (boomEffect != null) {
                            boomEffect.update();
                            if (!boomEffect.check()) {
                                boomEffects.remove(boomEffect);
                            }
                        } else {
                            boomEffects.remove(boomEffect);
                        }
                    }
                    sleep(1);
                }
            }
        }).start();
    }

//    //check shooter monster
//    private void checkBullets(Bullet bullet) {
//        for (int i = 0; i < monster01.size(); i++) {
//            Monster monster = monster01.get(i);
//            if (monster != null) {
//                Area area = new Area(bullet.getShape());
//                area.intersect(monster.getShape());
//
//                if (!area.isEmpty()) {
//                    boomEffects.add(new Effect(bullet.getCenterX(), bullet.getCenterY(), 3, 5, 60, 0.5f, new Color(230, 207, 105)));
//                    if (!monster.updateHP(bullet.getSize())) { //Test HP
//                        score++;
//                        monster01.remove(monster);
//                        sound.soundDestroy();
//                        double x = monster.getX() + monster.MONSTER_SIZE / 2;
//                        double y = monster.getX() + monster.MONSTER_SIZE / 2;
//                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
//                        boomEffects.add(new Effect(x, y, 5, 5, 75, 1, new Color(32, 178, 169)));
//                        boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(203, 207, 105)));
//                        boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 255, 70)));
//                        boomEffects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));
//                    } else {
//                        sound.soundHit();
//                    }
//
//                    bullets.remove(bullet);
//                }
//            }
//        }
//    }
    private void checkBullets(Bullet bullet) {
        for (int i = 0; i < monster01.size(); i++) {
            Monster monster = monster01.get(i);
            if (monster != null) {
                Area area = new Area(bullet.getShape());
                area.intersect(monster.getShape());

                if (!area.isEmpty()) {
                    boomEffects.add(new Effect(bullet.getCenterX(), bullet.getCenterY(), 3, 5, 60, 0.5f, new Color(230, 207, 105)));
                    if (!monster.updateHP(bullet.getSize())) {
                        score++;
                        monstersDefeated++;

                        // Nếu số lượng quái vật đã tiêu diệt đạt đến một mốc nào đó, tăng level
                        if (monstersDefeated >= 5) {
                            currentLevel++;
                            monstersDefeated = 0; // Đặt lại số lượng quái vật đã tiêu diệt
                            // Xóa hết quái vật và đạn
                            monster01.clear();
                            bullets.clear();
                            // Thêm quái vật mới ở level 2
                            addMonsterLevel2();
                        }

                        monster01.remove(monster);
                        sound.soundDestroy();

                        double x = monster.getX() + monster.MONSTER_SIZE / 2;
                        double y = monster.getX() + monster.MONSTER_SIZE / 2;
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 1, new Color(32, 178, 169)));
                        boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(203, 207, 105)));
                        boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 255, 70)));
                        boomEffects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));
                    } else {
                        sound.soundHit();
                    }

                    bullets.remove(bullet);
                }
            }
        }
    }

    private void checkPlayer(Monster monster) {
        if (monster != null) {
            Area area = new Area(player.getShape());
            area.intersect(monster.getShape());
            if (!area.isEmpty()) {
                double monterHp = monster.getHP();
                if (!monster.updateHP(player.getHP())) {
                    monster01.remove(monster);
                    sound.soundDestroy();
                    double x = monster.getX() + monster.MONSTER_SIZE / 2;
                    double y = monster.getX() + monster.MONSTER_SIZE / 2;
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 1, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(203, 207, 105)));
                    boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 255, 70)));
                    boomEffects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));
                }
                if (!player.updateHP(monterHp)) {
                    player.setAlive(false);
                    sound.soundDestroy();
//                    // Lưu điểm số của người chơi đăng nhập vào cơ sở dữ liệu
//                    if (currentPlayer != null) {
//                        playerScore = score;
//                        Score score = new Score();
//                        PlayerScore.addScore(score);
//                        // Lưu thời gian chơi vào cơ sở dữ liệu
//                        long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;
//                        // Định dạng thời gian thành chuỗi
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String formattedTime = dateFormat.format(new Date(elapsedTimeMillis));

                    double x = player.getX() + player.PLAYER_SIZE / 2;
                    double y = player.getX() + player.PLAYER_SIZE / 2;
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 5, 5, 75, 1, new Color(32, 178, 169)));
                    boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(203, 207, 105)));
                    boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 255, 70)));
                    boomEffects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));
                }
            }
        }
    }

    //Game background
    private void drawBackground() {
//        // Load hình ảnh từ tệp ảnh, ví dụ:
//        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/Game2D_image/background.png"));
//        Image background = backgroundImage.getImage();
//
//        // Vẽ hình ảnh lên màn hình game
//        g2.drawImage(background, 0, 0, width, height, this);

        ImageIcon backgroundImage;
        if (currentLevel == 1) {
            backgroundImage = new ImageIcon(getClass().getResource("/Game2D_image/background.png"));
        } else {
            backgroundImage = new ImageIcon(getClass().getResource("/Game2D_image/background_2.png"));
        }
        Image background = backgroundImage.getImage();

        // Vẽ hình ảnh lên màn hình game
        g2.drawImage(background, 0, 0, width, height, this);
    }

    private void drawGame() {

        // Vẽ hình ảnh nền
        drawBackground();

        if (player.isAlive()) {
            player.draw(g2);
        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet != null) {
                bullet.draw(g2);
            }
        }
        for (int i = 0; i < monster01.size(); i++) {
            Monster monster = monster01.get(i);
            if (monster != null) {
                monster.draw(g2);
            }
        }

        for (int i = 0; i < boomEffects.size(); i++) {
            Effect boomEffect = boomEffects.get(i);
            if (boomEffect != null) {
                boomEffect.draw(g2);
            }
        }

        g2.setColor(Color.white);
        g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
        g2.drawString("Score: " + score, 10, 40);

        // Vẽ tên người chơi bên trên bên trái màn hình
        g2.setColor(Color.white);
        g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
        FontMetrics gm = g2.getFontMetrics();
        gm = g2.getFontMetrics();
        if (loggedInPlayer != null) {
            g2.drawString("Player: " + loggedInPlayer.getName(), 10, 20);
        }

        if (!player.isAlive()) {
            String text = "GAME OVER";
            String textKey = "Press key enter to Continue ...";
            g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r2 = fm.getStringBounds(text, g2);
            double textWidth = r2.getWidth();
            double textHeight = r2.getHeight();
            double x = (width - textWidth) / 2;
            double y = (height - textHeight) / 2;
            g2.drawString(text, (int) x, (int) y + fm.getAscent());
            g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
            fm = g2.getFontMetrics();
            r2 = fm.getStringBounds(textKey, g2);
            textWidth = r2.getWidth();
            textHeight = r2.getHeight();
            x = (width - textWidth) / 2;
            y = (height - textHeight) / 2;
            g2.drawString(textKey, (int) x, (int) y + fm.getAscent() + 50);

            String scoreText = "Your Score: " + score;
            g2.setFont(getFont().deriveFont(Font.BOLD, 20f));
            fm = g2.getFontMetrics();
            r2 = fm.getStringBounds(scoreText, g2);
            textWidth = r2.getWidth();
            textHeight = r2.getHeight();
            x = (width - textWidth) / 2;
            y = (height - textHeight) / 2;
            g2.setColor(Color.white); // Đảm bảo vùng vẽ có màu khác màu trắng để có thể nhìn thấy
            g2.drawString(scoreText, (int) x, (int) y + fm.getAscent() + 100);
        }
    }

    private void render() {
        Graphics g = getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }

    private void sleep(long speed) {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException ex) {
            System.err.println(ex);
        }
    }

    public int getPlayerScore() {
        return playerScore;
    }

    private void addMonsterLevel2() {
        Random ran = new Random();
        int locationY = ran.nextInt(height - 50) + 25;
        Monster monster = new Monster();
        monster.changeSpeed(0.8f); // Điều chỉnh tốc độ di chuyển
        monster.updateMaxHP(50); // Điều chỉnh máu tối đa
        monster.changeLocation(0, locationY);
        monster.changeAngle(0);
        monster01.add(monster);

        int locationY2 = ran.nextInt(height - 50) + 25;
        Monster monster2 = new Monster();
        monster2.changeSpeed(0.8f); // Điều chỉnh tốc độ di chuyển
        monster2.updateMaxHP(50); // Điều chỉnh máu tối đa
        monster2.changeLocation(width, locationY2);
        monster2.changeAngle(180);
        monster01.add(monster2);
    }
}
