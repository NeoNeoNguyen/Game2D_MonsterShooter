package Game2D_obj;


public class HP {
    private double MAX_HP;
    private double currentHp;

    public double getMAX_HP() {
        return MAX_HP;
    }

    public void setMAX_HP(double MAX_HP) {
        this.MAX_HP = MAX_HP;
    }

    public double getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(double currentHp) {
        this.currentHp = currentHp;
    }

    public HP(double MAX_HP, double currentHp) {
        this.MAX_HP = MAX_HP;
        this.currentHp = currentHp;
    }

    public HP() {
    }

}
