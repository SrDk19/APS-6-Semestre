import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Janela_Teste extends JFrame{
    public Janela_Teste(){
        setTitle("teste");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        Dimension tamanho_tela = Toolkit.getDefaultToolkit().getScreenSize();
        int largura = ( int ) ( tamanho_tela.width * 0.5 );
        int altura = ( int ) ( tamanho_tela.height * 0.5 );
        setSize(largura, altura);
         setLocationRelativeTo(null);
    }
    public static void main(String[]args)
    {
        Janela_Teste janela = new Janela_Teste();
        janela.setVisible(true);
    }
}