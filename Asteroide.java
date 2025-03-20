import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;
import java.awt.Shape;
import java.awt.Color;

public class Asteroide extends PoligonoIrreg implements Runnable {
    static Random rand = new Random();
    public static int no_asteroides = 0;
    static private Asteroide []asteroides;
    
    private final Object lock;
    public Polygon astToPolygon;
    private int radio, id_asteroide;
    private double vX, vY, axisScaling;
    public boolean colisionando, desaparecio;

    public Asteroide(int no_lados, Coordenada posicionInicial, int radio, Object lock){
        super(posicionInicial);
        this.lock = lock;
        this.radio = radio;
        this.axisScaling = 1.00f;

        //velocidad
        double area = Math.PI * radio *radio;
        double velocidad = 1000/area*10;
        double angulo = Math.random() * 2 * Math.PI;

        this.vX = velocidad * Math.cos(angulo);
        this.vY = velocidad * Math.sin(angulo);
        this.colisionando = false;
        this.desaparecio = false;
    }

    static public void setAsteroides(int n){
        asteroides = new Asteroide[n];
    }

    public static Asteroide newRandomAsteroide(int w, int h, Object lock){
        int no_lados = rand.nextInt(6,20);
        int radio = rand.nextInt(10 + (no_lados*2),w/(8*2));
        Coordenada posicion;
        boolean valido;
    
        do {
            valido = true;
            posicion = Coordenada.getRandCoord(radio, w - radio, radio, h - radio);
            for (int i = 0; i < no_asteroides; i++) {
                double distancia = Math.sqrt(
                    Math.pow(asteroides[i].posicion.x - posicion.x, 2) + 
                    Math.pow(asteroides[i].posicion.y - posicion.y, 2)
                );
    
                if (distancia < radio + asteroides[i].radio) {
                    valido = false;
                    break;
                }
            }
        } while (!valido);
    
        Asteroide a = new Asteroide(no_lados, posicion, radio, lock);

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
        if(this.desaparecio) return;
        AffineTransform transform = new AffineTransform();
        transform.translate(this.posicion.getX(), this.posicion.getY());
        transform.scale(axisScaling, axisScaling);
        Shape transformedAsteroid = transform.createTransformedShape(astToPolygon);
        g.setColor(Color.WHITE);
        g.draw(transformedAsteroid);
        g.setColor(new Color(0, 0, 0, 100));
        g.fill(transformedAsteroid);
    }

    @Override
    public void run() {
        id_asteroide = no_asteroides++;
        asteroides[id_asteroide] = this;
        while(true){
            synchronized(lock){
                try { lock.wait(); } 
                catch (InterruptedException e) 
                { e.printStackTrace(); }
            }
            mover();
            manejarColision();
            if(desaparecio) break;
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
    }

    private void manejarColision(){
        for(int i = 0; i < no_asteroides; i++){
            if(i == id_asteroide || asteroides[i] == null) continue;

            Coordenada coordAux = Coordenada.Clone(asteroides[i].posicion);
            double aux1 = this.posicion.x - coordAux.x;
            double aux2 = this.posicion.y - coordAux.y;
            double distance = Math.sqrt(aux1*aux1 + aux2*aux2);

            if (distance < (this.radio + asteroides[i].radio)) {
                if (this.colisionando) continue;
                else {
                    this.colisionando = true;
                    asteroides[i].colisionando = true;
                }
            
                System.out.println("[" + i + ", " + this.id_asteroide + "] Colision detectada!!");
                
                this.posicion.x += (this.radio/3) * Math.signum(this.vX);
                this.posicion.y += (this.radio/3) * Math.signum(this.vY);

                try {
                    asteroides[i].posicion.x += (asteroides[i].radio / 3) * Math.signum(asteroides[i].vX);
                    asteroides[i].posicion.y += (asteroides[i].radio / 3) * Math.signum(asteroides[i].vY);
                    asteroides[i].radio /= 2;
                    asteroides[i].axisScaling /= 2;
                    asteroides[i].vX *= 1.4;
                    asteroides[i].vY *= 1.4;
                } catch(NullPointerException e){

                } finally {
                    this.radio /= 2;
                    this.axisScaling /= 2;
                    this.vX *= 1.4;
                    this.vY *= 1.4;
                }

            } else if (distance > this.radio * 1.5) {  // Si ya se alejo bastante, se puede volver a detectar colisión
                this.colisionando = false;
                asteroides[i].colisionando = false;
            }

            if(this.radio < 7){
                System.out.println("Desaparecio "+id_asteroide);
                this.desaparecio = true;
                asteroides[id_asteroide] = null;
                return;
            }
        }
    }
}