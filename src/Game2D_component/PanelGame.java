package Game2D_component;

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
        });
        initObjectGame();
        initKeyboard();
        initBullet();
        thread.start();
    }

    //monster
    private void addMonster() {
        Random ran = new Random();
        int locationY = ran.nextInt(height - 50) + 25;
        Monster monster = new Monster();
        monster.changeLocation(0, locationY);
        monster.changeAngle(0);
        monster01.add(monster);

        int locationY2 = ran.nextInt(height - 50) + 25;
        Monster monter2 = new Monster();
        monter2.changeLocation(width, locationY2);
        monter2.changeAngle(180);
        monster01.add(monter2);

    }

    private void initObjectGame() {
        sound = new Sound();
        player = new Player();
        player.changeLocation(150, 150);
        monster01 = new ArrayList<>();
        //monster02 = new ArrayList<>();
        //monster03 = new ArrayList<>();
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
//                    for(int i =0;i < monster02.size(); i++){
//                        Monster monster2 = monster02.get(i);                        
//                        if( monster2!= null){
//                            monster2.update();
//                        }
//                    }
//                    for(int i =0;i < monster03.size(); i++){
//                        Monster monster3 = monster03.get(i);                        
//                        if( monster3!= null){
//                            monster3.update();
//                        }
//                    }

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

    //check shooter monster
    private void checkBullets(Bullet bullet) {
        for (int i = 0; i < monster01.size(); i++) {
            Monster monster = monster01.get(i);
            if (monster != null) {
                Area area = new Area(bullet.getShape());
                area.intersect(monster.getShape());

                if (!area.isEmpty()) {
                    boomEffects.add(new Effect(bullet.getCenterX(), bullet.getCenterY(), 3, 5, 60, 0.5f, new Color(230, 207, 105)));
                    if (!monster.updateHP(bullet.getSize())) { //Test HP
                        score++;
                        monster01.remove(monster);
                        sound.soundDestroy();
                        double x = monster.getX() + monster.MONSTER_SIZE / 2;
                        double y = monster.getX() + monster.MONSTER_SIZE / 2;
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 1, new Color(32, 178, 169)));
                        boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(203, 207, 105)));
                        boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 255, 70)));
                        boomEffects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));
                    } else{
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
        g2.setColor(new Color(30, 30, 30));
        g2.fillRect(0, 0, width, height);
    }

    private void drawGame() {
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
//        for(int i = 0; i<monster02.size();i++){
//            Monster monster = monster02.get(i);
//            if(monster != null){
//                monster.draw(g2);
//            }
//        }
//        for(int i = 0; i<monster03.size();i++){
//            Monster monster = monster03.get(i);
//            if(monster != null){
//                monster.draw(g2);
//            }
//        }

        for (int i = 0; i < boomEffects.size(); i++) {
            Effect boomEffect = boomEffects.get(i);
            if (boomEffect != null) {
                boomEffect.draw(g2);
            }
        }

        g2.setColor(Color.white);
        g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
        g2.drawString("Score: " + score, 10, 20);

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
}
