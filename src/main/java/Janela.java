import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Janela extends JFrame {

    public Janela() {
        setTitle("teste");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JButton botao1 = new JButton("nivel 1");
        JButton botao2 = new JButton("Nivel 2");
        JButton botao3 = new JButton("nivel 3");
        JButton botao4 = new JButton("Adicionar Usuario");

        // Ações dos botões
        botao1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Nivel_1().setVisible(true);
            }
        });

        botao2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Nivel_2().setVisible(true);
            }
        });

        botao3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Nivel_3().setVisible(true);
            }
        });

        botao4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Janela_Cadastro().setVisible(true);
            }
        });

        // Linha 0: botões 1, 2 e 3
        gbc.gridy = 0;
        gbc.gridx = 0; painel.add(botao1, gbc);
        gbc.gridx = 1; painel.add(botao2, gbc);
        gbc.gridx = 2; painel.add(botao3, gbc);

        // Linha 1: botão 4 (embaixo do botão 2)
        gbc.gridy = 1;
        gbc.gridx = 1; painel.add(botao4, gbc);

        add(painel);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new Janela().setVisible(true);
    }
}