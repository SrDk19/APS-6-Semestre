import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.BorderLayout;

public class Janela5 extends JFrame {
    public Janela5() {
        setTitle("Tela 2");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Você abriu a Tela 2", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label, BorderLayout.CENTER);
    }
}
