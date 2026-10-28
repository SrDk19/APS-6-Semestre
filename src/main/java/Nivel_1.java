import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.Toolkit;

public class Nivel_1 extends JFrame {
    
    private static final Color laranja = Color.decode("#FF7518");
    private static final Color fundo_janela = Color.decode("#1C1C1E");
    private static final Color borda_janela = Color.decode("#121214");
    private static final Color texto_botao = Color.decode("#F0EBE1");
    private static final Color letra = Color.decode("#AAA096");

    public Nivel_1() {
    
        setTitle("🎃Nível 1🎃");
        Dimension tamanho_tela = Toolkit.getDefaultToolkit().getScreenSize();
        int largura = (int) (tamanho_tela.width * 0.5);
        int altura = (int) (tamanho_tela.height * 0.5);
        setSize(largura, altura);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(fundo_janela);
        painel.setBorder(new MatteBorder(2, 2, 2, 2, borda_janela));

        JLabel notificacao = new JLabel("Você abriu a Tela 1", JLabel.CENTER);
        notificacao.setFont(new Font("Arial", Font.BOLD, 20));
        notificacao.setForeground(letra);
        painel.add(notificacao, BorderLayout.CENTER);
        add(painel);

        JLabel voltar = new JLabel("Voltar");
        voltar.setFont(new Font("Arial", Font.BOLD, 16));
        voltar.setForeground(texto_botao);
        painel.add(voltar, BorderLayout.SOUTH);
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