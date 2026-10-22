import javax.swing.JFrame;

public class Janela_Teste extends JFrame{
    public Janela_Teste(){
        setTitle("teste");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(600,400);
        
    }
    public static void main(String[]args)
    {
        Janela_Teste J = new Janela_Teste();
        J.setVisible(true);
    }

}