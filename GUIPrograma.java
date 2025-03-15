import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GUIPrograma extends JFrame {

    public GUIPanel panel;

    public GUIPrograma(int no_asteroides){
        setTitle("Simulador de asteroides");
        setSize(1280,720);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new GUIPanel(this.getSize(), no_asteroides);
        add(panel);
    }
}


class GUIPanel extends JPanel implements ActionListener {

    Timer timer;
    Graphics2D canvas;
    static Asteroide []asteroides;

    private final int WIDTH, HEIGHT;
    public GUIPanel(Dimension size, int no_asteroides){
        this.WIDTH = ((int) size.getWidth()) - 76;
        this.HEIGHT = ((int)size.getHeight()) - 97;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        timer = new Timer(10, this);
        timer.start();

        asteroides = new Asteroide[no_asteroides];
        Thread []hilos = new Thread[no_asteroides];
        for(int i = 0; i < no_asteroides; i++){
            asteroides[i] = Asteroide.newRandomAsteroide(WIDTH, HEIGHT);
            hilos[i] = new Thread(asteroides[i]);
            hilos[i].start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        canvas = (Graphics2D) g;
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        reDrawBackground();
        reDrawAsteroids();
    }

    void reDrawBackground(){
        canvas.setColor(Color.BLACK);
        canvas.fillRect(0,0,WIDTH, HEIGHT);
    }

    void reDrawAsteroids(){
        for(int i = 0; i < asteroides.length; i++){
            asteroides[i].paintAsteroid(canvas);
        }
    }
    
}