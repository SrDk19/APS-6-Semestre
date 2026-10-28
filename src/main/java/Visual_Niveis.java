import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

public class Visual_Niveis extends JFrame {

    protected static final Color cor_titulo = Color.decode("#FF7518");
    protected static final Color fundo_janela = Color.decode("#1C1C1E");
    protected static final Color borda_janela = Color.decode("#121214");
    protected static final Color letra = Color.decode("#AAA096");

    protected final JPanel painel = new JPanel(new BorderLayout());

    public Visual_Niveis(String titulo, String mensagem) {
        setTitle(titulo);
        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (tela.width * 0.5), (int) (tela.height * 0.5));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        painel.setBackground(fundo_janela);
        painel.setBorder(new MatteBorder(2, 2, 2, 2, borda_janela));

        JLabel notificacao = new JLabel(mensagem, JLabel.CENTER);
        notificacao.setFont(new Font("Arial", Font.BOLD, 20));
        notificacao.setForeground(letra);
        painel.add(notificacao, BorderLayout.CENTER);

        setContentPane(painel);
    }

    public static void config_Tema() {
        FlatDarkLaf.setup();
        UIManager.put("TitlePane.centerTitle", true);
        UIManager.put("TitlePane.background", fundo_janela);
        UIManager.put("TitlePane.foreground", cor_titulo);
        UIManager.put("TitlePane.inactiveBackground", fundo_janela);
        UIManager.put("TitlePane.inactiveForeground", letra);
        JFrame.setDefaultLookAndFeelDecorated(true);
        FlatDarkLaf.setup(); // Tema escuro por padrão.
        SwingUtilities.invokeLater(() -> {});
    }
}