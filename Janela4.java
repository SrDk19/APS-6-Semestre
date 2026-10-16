import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.BorderLayout;

public class Janela4 extends JFrame {
    public Janela4() {
        setTitle("Tela 4");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Você abriu a Tela 4", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label, BorderLayout.CENTER);
    }
}
