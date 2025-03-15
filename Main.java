public class Main {

    public static void main(String[] args) {
        if(args.length < 1) {
            throw new Error("Falta argumento de n asteroides");
        }
        int n = Integer.parseInt(args[0]);
        if (n < 1) throw new Error("n muy pequeño, insertar mayor a 1!");
        //System.out.println(n);
        

        GUIPrograma gui = new GUIPrograma(n);
        gui.setVisible(true);
    }
}