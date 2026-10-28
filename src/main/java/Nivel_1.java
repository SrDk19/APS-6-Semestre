import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;

public class Nivel_1 extends JFrame {
    public Nivel_1() {
        setTitle("Nível 1");
        Dimension tamanho_tela = Toolkit.getDefaultToolkit().getScreenSize();
        int largura = (int) (tamanho_tela.width * 0.5);
        int altura = (int) (tamanho_tela.height * 0.5);
        setSize(largura, altura);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Você abriu a Tela 1", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label, BorderLayout.CENTER);
    }
    public static void main(String[]args)
    {
        FlatDarkLaf.setup(); // Tema escuro por padrão.
        SwingUtilities.invokeLater(() -> {
            Nivel_1 janela = new Nivel_1();
            janela.setVisible(true);
        });
    }
}