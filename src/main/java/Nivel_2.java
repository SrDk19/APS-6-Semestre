import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;

public class Nivel_2 extends Nivel_1 {

    public Nivel_2() {
        setTitle("Nível 2 - Identificação Facial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension tamanho_tela = Toolkit.getDefaultToolkit().getScreenSize();
        int largura = (int) (tamanho_tela.width * 0.5);
        int altura = (int) (tamanho_tela.height * 0.5);
        setSize(largura, altura);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new BorderLayout());
        add(painel);

        JLabel label = new JLabel("Aguardando identificação facial (Nível 2)...", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        add(label, BorderLayout.CENTER);

        LeitorFacial.executar("Nível 2", nome -> {
            label.setText("Bem-vindo, " + nome + "! (Nível 2)");
            JOptionPane.showMessageDialog(this, "Acesso Nível 2 liberado para: " + nome, "Acesso concedido", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}