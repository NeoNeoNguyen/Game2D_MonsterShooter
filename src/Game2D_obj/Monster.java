package Game2D_obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.ImageIcon;

public class Monster extends HpRender{

    private final int maxHP;

    public Monster() {
        super(new HP(20, 20));
        this.mt1 = new ImageIcon(getClass().getResource("/Game2D_image/moster01.png")).getImage();
        //this.mt2 = new ImageIcon(getClass().getResource("/Game2D_image/moster02.png")).getImage();
        //this.mt3 = new ImageIcon(getClass().getResource("/Game2D_image/moster03.png")).getImage();

        Path2D p = new Path2D.Double();
        p.moveTo(0, 0);
        p.lineTo(MONSTER_SIZE, 0);
        p.lineTo(MONSTER_SIZE, MONSTER_SIZE);
        p.lineTo(0, MONSTER_SIZE);
        monsterShap = new Area(p);
        
        this.maxHP = 20;
    }

    public static final double MONSTER_SIZE = 75;
    private double x;
    private double y;
    private float speed = 0.3f;
    private float angle = 0;
    private final Image mt1;
    //private final Image mt2;
    //private final Image mt3;
    private final Area monsterShap;

    public void changeLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;
    }

    //điều chỉnh góc xoay của monster
    public void changeAngle(float angle) {
        if (angle < 0) {
            angle = 359;
        } else if (angle > 359) {
            angle = 0;
        }
        this.angle = angle;
    }

    public void draw(Graphics2D g2) {
        AffineTransform oldTransform = g2.getTransform();
        g2.translate(x, y);
        AffineTransform tran = new AffineTransform();
        tran.rotate(Math.toRadians(angle), MONSTER_SIZE / 2, MONSTER_SIZE / 2);
        g2.drawImage(mt1, tran, null);
        //g2.drawImage(mt2, tran, null);
        //g2.drawImage(mt3, tran, null);

        Shape shap = getShape();

        //HP
        hpRender(g2, shap, y);
        
        g2.setTransform(oldTransform);

        //test
//        g2.setColor(Color.red);
//        g2.draw(shap);
//        g2.draw(shap.getBounds2D());

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

    public Area getShape() {
        AffineTransform afx = new AffineTransform();
        afx.translate(x, y);
        afx.rotate(Math.toRadians(angle), MONSTER_SIZE / 2, MONSTER_SIZE / 2);
        return new Area(afx.createTransformedShape(monsterShap));
    }

    public boolean check(int width, int height) {
        Rectangle size = getShape().getBounds();
        if (x <= -size.getWidth() || y < -size.getHeight() || x > width || y > height) {
            return false;
        } else {
            return true;
        }
    }

    public void changeSpeed(float newSpeed) {
        this.speed = newSpeed;
    }

    public void updateMaxHP(int newMaxHP) {
        //this.maxHP = newMaxHP;
        hp.setCurrentHp(maxHP); // Đặt lại máu về tối đa mới
    }
}