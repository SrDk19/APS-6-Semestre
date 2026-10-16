import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.BorderLayout;

public class Janela3 extends JFrame {
    public Janela3() {
        setTitle("Tela 3");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Você abriu a Tela 3", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label, BorderLayout.CENTER);
    }
}