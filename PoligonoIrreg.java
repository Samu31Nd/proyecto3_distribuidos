import java.util.ArrayList;
import java.util.List;

public class PoligonoIrreg {

  List<Coordenada> vertices;
  Coordenada posicion;

  PoligonoIrreg(Coordenada posicion) {
    vertices = new ArrayList<>();
    this.posicion = posicion;
  }

  public String getNoVertices() {
    return "Vertices totales: " + vertices.size();
  }

  public void anadeVertice(Coordenada newC) {
    vertices.add(newC);
    System.out.println("Se agregó el vertice: " + newC);
  }

  @Override
  public String toString() {
    String coordenadasString = "";
    for (Coordenada c : vertices) {
      coordenadasString += c + "\n";
    }
    return "Los vertices del polígono irregular son:\n" +
        coordenadasString +
        getNoVertices();
  }
}
