/*
 * Proyecto no.2
 * Sanchez Leyva Eduardo Samuel
 * Grupo 7CM2 Sistemas Distribuidos
 */
import java.util.Random;

public class Coordenada {

    public double x, y;
  
    public Coordenada(double x, double y) {
      this.x = x;
      this.y = y;
    }
  
    // Metodo getter de x
    public int getX() {
      return (int)x;
    }
  
    // Metodo getter de y
    public int getY() {
      return (int)y;
    }
  
    @Override
    public String toString() {
      return "[" + (int)x + "," + (int)y + "]";
    }

    /*
     * c1 - c2
     */
    static Coordenada substract(Coordenada c1, Coordenada c2){
      return new Coordenada(c1.x - c2.x,c1.y-c2.y);
    }

    static Random rand = new Random();
    static final int despBorder = 80;
    static Coordenada getRandCoord(int xBound, int yBound){
      int x = rand.nextInt(50,xBound-50);
      int y = rand.nextInt(50,yBound-50);
        return new Coordenada(x, y);
    }

    static Coordenada getRandCoord(int xStart, int xBound, int yStart, int yBound){
      int x = rand.nextInt(xStart,xBound);
      int y = rand.nextInt(yStart,yBound);
        return new Coordenada(x, y);
    }

    public static Coordenada Clone(Coordenada c){
      return new Coordenada(c.x, c.y);
    }
  
  }
  
