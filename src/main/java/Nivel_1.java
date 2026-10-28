import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.SwingUtilities;

public class Nivel_1 extends Visual_Niveis {

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