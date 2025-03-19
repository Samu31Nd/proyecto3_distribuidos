import java.util.Random;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class Asteroide extends PoligonoIrreg implements Runnable {

    static private Asteroide []asteroides;
    private int radio, id_asteroide;
    public int getRadio() {
        return radio;
    }
    public Polygon astToPolygon;
    private Coordenada centro;
    public Coordenada getCentro() {
        return centro;
    }

    private final Object lock;
    private double vX, vY;

    public double getvX() {
        return vX;
    }

    public double getvY() {
        return vY;
    }

    public Asteroide(int no_lados, Coordenada centro, Coordenada posicionInicial, int radio, Object lock){
        super(posicionInicial);
        this.lock = lock;
        this.centro = centro;
        this.radio = radio;

        //velocidad
        double area = Math.PI * radio *radio;
        double velocidad = 1000/area*20;
        double angulo = Math.random() * 2 * Math.PI;

        this.vX = velocidad * Math.cos(angulo);
        this.vY = velocidad * Math.sin(angulo);

        System.out.println("\nAsteroide creado!");
    }

    @Override
    public String toString() {
        return "Asteroide!";
    }

    static public void setAsteroides(int n){
        asteroides = new Asteroide[n];
    }

    static Random rand = new Random();
    public static Asteroide newRandomAsteroide(int w, int h, Object lock){
        int no_lados = rand.nextInt(6,20);
        int radio = rand.nextInt(10 + (no_lados*2),w/(8*2));
        Asteroide a = new Asteroide(no_lados, new Coordenada(radio, radio), Coordenada.getRandCoord(radio, w - radio, radio, h - radio) , radio, lock);

        int[] xPoints = new int[no_lados];
        int[] yPoints = new int[no_lados];

        for(int i = 0; i < no_lados; i++) {
            double angle = 2* Math.PI * i / no_lados;
            int varRadio = radio + rand.nextInt(-5, 5); // Variar el radio para hacerlo irregular
            int x = radio + (int) (varRadio * Math.cos(angle));
            int y = radio + (int) (varRadio * Math.sin(angle));
            a.anadeVertice(new Coordenada(x, y));
            xPoints[i] = x; yPoints[i] = y;
        }
        a.astToPolygon = new Polygon(xPoints, yPoints, no_lados);
        return a;
    }

    public void paintAsteroid(Graphics2D g){
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(3));
        AffineTransform transform = new AffineTransform();
        transform.translate(this.posicion.getX(), this.posicion.getY());
        Shape transformedAsteroid = transform.createTransformedShape(astToPolygon);
        g.draw(transformedAsteroid);
    }

    public static int no_asteroides = 0;
    @Override
    public void run() {
        id_asteroide = no_asteroides++;
        asteroides[id_asteroide] = this;
        System.out.println(id_asteroide + ". Hilo iniciado!");
        while(true){
            synchronized(lock){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mover();
        }
    }

    private void mover(){
        posicion.x += vX;
        posicion.y += vY;


        //handle position out of window
        if(posicion.getX() < -65) posicion.x = 1280 - 76;
        if(posicion.getX() > 1280 - 76) posicion.x = -55;
        if(posicion.getY() < -70) posicion.y = 720 - 95;
        if(posicion.getY() > 720 - 95) posicion.y = -70;

        //handle impact with other object
        for(int i = 0; i < no_asteroides; i++){
            if(i == id_asteroide) continue;
            
        }
    }
}