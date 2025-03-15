import java.util.Random;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class Asteroide extends PoligonoIrreg implements Runnable {

    private int no_lados, radio;
    //solo la forma, el angulo y posicion es otra cosa
    public Polygon astToPolygon;
    private Coordenada centro;

    public Asteroide(int no_lados, Coordenada centro, Coordenada posicionInicial, int radio){
        super(posicionInicial);

        this.no_lados = no_lados;
        this.centro = centro;
        this.radio = radio;

        System.out.println("\nAsteroide creado!");
    }

    @Override
    public String toString() {
        return "Asteroide!";
    }

    static Random rand = new Random();
    public static Asteroide newRandomAsteroide(int w, int h){
        int no_lados = rand.nextInt(6,20);
        int radio = rand.nextInt(10 + (no_lados*2),w/(8*2));
        Asteroide a = new Asteroide(no_lados, new Coordenada(radio, radio), Coordenada.getRandCoord(radio, w - radio, radio, h - radio) , radio);

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

    public static int no_threads = 0;
    @Override
    public void run() {
        System.out.println(no_threads++ + ". Hilo iniciado!");
    }
}
