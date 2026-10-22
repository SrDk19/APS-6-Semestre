import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.BorderLayout;

public class Janela3 extends JFrame {

    public Janela3() {
        setTitle("Nível 3 - Identificação Facial");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
}