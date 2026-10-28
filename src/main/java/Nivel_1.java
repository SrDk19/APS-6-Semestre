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

public class Nivel_1 extends Visual_Niveis {

    private static final Color laranja = Color.decode("#FF7518");
    private static final Color fundo_janela = Color.decode("#1C1C1E");
    private static final Color borda_janela = Color.decode("#121214");
    private static final Color texto_botao = Color.decode("#F0EBE1");
    private static final Color letra = Color.decode("#AAA096");

    public Nivel_1() {

        super("🎃Nível 1🎃", "Você abriu a Tela 1");
    }
    public static void main(String[]args) {
            
        FlatDarkLaf.setup(); // Tema escuro por padrão.
            SwingUtilities.invokeLater(() -> {
            Nivel_1 janela = new Nivel_1();
            janela.setVisible(true);
        });
    }
}