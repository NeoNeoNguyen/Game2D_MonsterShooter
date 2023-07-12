package Game2D_main;

import Game2D_component.PanelGame;
import java.awt.BorderLayout;
import javax.swing.JFrame;


public class Main extends JFrame{
    
    public Main(){
        init();
    }
    
    private void init(){
        setTitle("Game2D Monster Shooter");
        setSize(1366,768);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        PanelGame panelGame = new PanelGame();
        add(panelGame);
    }
    
    
    public static void main(String[] args) {
        Main main = new Main();
        main.setVisible(true);
    }
}
