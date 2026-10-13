import javax.swing.JFrame;

public class janela extends JFrame{
    public janela(){
        setTitle("teste");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    public static void main(String[]args)
    {
        janela J = new janela();
        J.setVisible(true);
    }

}