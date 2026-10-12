import javax.swing.JFrame;

public class Janela extends JFrame{
    public Janela(){
        setTitle("teste");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    public static void main(String[]args)
    {
        Janela J = new Janela();
        J.setVisible(true);
    }

}