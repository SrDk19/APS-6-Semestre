import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;

public class Nivel_3 extends JFrame {

    public Nivel_3() {
        setTitle("Nível 3 - Identificação Facial");
        Dimension tamanho_tela = Toolkit.getDefaultToolkit().getScreenSize();
        int largura = (int) (tamanho_tela.width * 0.5);
        int altura = (int) (tamanho_tela.height * 0.5);
        setSize(largura, altura);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        

        JLabel label = new JLabel("Aguardando identificação facial (Nível 3)...", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        add(label, BorderLayout.CENTER);

        LeitorFacial.executar("nivel3", nome -> {
            label.setText("Bem-vindo, " + nome + "! (Nível 3)");
            JOptionPane.showMessageDialog(this,
                    "Acesso Nível 3 liberado para: " + nome,
                    "Acesso concedido",
                    JOptionPane.INFORMATION_MESSAGE);
            });
    }
    public static void main(String[]args)
    {
        Nivel_3 janela = new Nivel_3();
        janela.setVisible(true);
    }
}