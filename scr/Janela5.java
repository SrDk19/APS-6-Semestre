import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.BorderLayout;

public class Janela5 extends JFrame {

    public Janela5() {
        setTitle("Nível 2 - Identificação Facial");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Aguardando identificação facial (Nível 2)...", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        add(label, BorderLayout.CENTER);

        LeitorFacial.executar("nivel2", nome -> {
            label.setText("Bem-vindo, " + nome + "! (Nível 2)");
            JOptionPane.showMessageDialog(this,
                    "Acesso Nível 2 liberado para: " + nome,
                    "Acesso concedido",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }
}