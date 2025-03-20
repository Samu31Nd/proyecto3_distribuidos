import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
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
    static Asteroide[] asteroides;
    static Object lock = new Object();

    private final int WIDTH, HEIGHT;
    private Image backgroundImage;

    public GUIPanel(Dimension size, int no_asteroides) {
        this.WIDTH = ((int) size.getWidth()) - 76;
        this.HEIGHT = ((int) size.getHeight()) - 97;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);

        // Cargar la imagen de fondo
        backgroundImage = new ImageIcon("background.jpg").getImage();

        timer = new Timer(10, this);
        timer.start();

        asteroides = new Asteroide[no_asteroides];
        Asteroide.setAsteroides(no_asteroides);
        Thread[] hilos = new Thread[no_asteroides];
        for (int i = 0; i < no_asteroides; i++) {
            asteroides[i] = Asteroide.newRandomAsteroide(WIDTH, HEIGHT, lock);
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

        try {
            Thread.sleep(50); // tiempo para que los hilos de asteroides estén listos
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (lock) {
            lock.notifyAll(); // Despierta a todos los hilos
        }
    }

    void reDrawBackground() {
        // Dibujar la imagen de fondo escalada al tamaño del panel
        if (backgroundImage != null) {
            canvas.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, this);
        } else {
            canvas.setColor(Color.BLACK);
            canvas.fillRect(0, 0, WIDTH, HEIGHT);
        }
    }

    void reDrawAsteroids() {
        canvas.setStroke(new BasicStroke(3));
        for (Asteroide asteroide : asteroides) {
            asteroide.paintAsteroid(canvas);
        }
    }
}
