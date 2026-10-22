import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.BorderLayout;

public class Janela4 extends JFrame {

    public Janela4() {
        setTitle("Tela 4 - Cadastro Facial");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titulo = new JLabel("Escolha o nível para cadastro", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnNivel2 = new JButton("Nível 2");
        JButton btnNivel3 = new JButton("Nível 3");

        btnNivel2.addActionListener(e -> CadastroFacial.executar("nivel2"));
        btnNivel3.addActionListener(e -> CadastroFacial.executar("nivel3"));

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        painel.add(titulo, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        painel.add(btnNivel2, gbc);
        gbc.gridx = 1;
        painel.add(btnNivel3, gbc);

        add(painel, BorderLayout.CENTER);
    }
}