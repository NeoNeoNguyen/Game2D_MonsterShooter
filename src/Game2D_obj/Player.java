package Game2D_obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.ImageIcon;

public class Player extends HpRender {

    public Player() {
        super(new HP(50, 50));
        this.image = new ImageIcon(getClass().getResource("/Game2D_image/player.png")).getImage();
        this.image_speed = new ImageIcon(getClass().getResource("/Game2D_image/player_speed.png")).getImage();

        Path2D p = new Path2D.Double();
        p.moveTo(0, 0);
        p.lineTo(PLAYER_SIZE - 23, 5);
        p.lineTo(PLAYER_SIZE - 23, PLAYER_SIZE);
        p.lineTo(5, PLAYER_SIZE);
        playerShap = new Area(p);
    }

    public static final double PLAYER_SIZE = 90;
    private double x;
    private double y;

    private final float MAX_SPEED = 1f;
    private float speed = 0f;

    private float angle = 0f;
    private final Image image;
    private final Image image_speed;
    private boolean speedUp;
    private boolean alive = true;

    private final Area playerShap;

    public void changeLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;
    }

    //điều chỉnh góc xoay của player
    public void changeAngle(float angle) {
        if (angle < 0) {
            angle = 359;
        } else if (angle > 359) {
            angle = 0;
        }
        this.angle = angle;
    }

    //
    public void draw(Graphics2D g2) {
        AffineTransform olTransform = g2.getTransform();
        g2.translate(x, y);
        AffineTransform tran = new AffineTransform();
        tran.rotate(Math.toRadians(angle), PLAYER_SIZE / 2, PLAYER_SIZE / 2);

        g2.drawImage(speedUp ? image_speed : image, tran, null);
        hpRender(g2, getShape(), y);

        g2.setTransform(olTransform);

        //test shap
//        g2.setColor(Color.red);
//        g2.draw(getShape());
    }

    //Tạo phương thức get x, y và lấy giá trị angle làm phương hướng
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    public void speedUp() {
        speedUp = true;
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        } else {
            speed += 0.01f;
        }
    }

    public void speedDown() {
        speedUp = false;
        if (speed <= 0) {
            speed = 0;
        } else {
            speed -= 0.003f;
        }
    }

    public Area getShape() {
        AffineTransform afx = new AffineTransform();
        afx.translate(x, y);
        afx.rotate(Math.toRadians(angle), PLAYER_SIZE / 2, PLAYER_SIZE / 2);
        return new Area(afx.createTransformedShape(playerShap));
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void reset() {
        alive = true;
        resetHP();
        angle = 0;
        speed = 0;
    }
}
