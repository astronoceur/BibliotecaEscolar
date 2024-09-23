import javax.swing.*;
import gui.Sistema;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        ImageIcon customIcon = new ImageIcon("gui/nyan-cat.gif");
        ImageIcon customIcon2 = new ImageIcon("gui/imagemicone.png");

        UIManager.getDefaults().put("OptionPane.background", new Color(240, 240, 240));

        Color corFundoPanel = new Color(250, 250, 250);
        UIManager.put("Panel.background", corFundoPanel);

        Color corFundoButton = new Color(173, 216, 230);
        UIManager.put("Button.background", corFundoButton);

        Color corTextoButton = new Color(50, 50, 50);
        UIManager.put("Button.foreground", corTextoButton);

        Font fonteButton = new Font("Arial", Font.BOLD, 15);
        UIManager.put("Button.font", fonteButton);

        Color corFundoTextField = new Color(255, 255, 255);
        UIManager.put("TextField.background", corFundoTextField);

        Color corTextoTextField = new Color(0, 0, 0);
        UIManager.put("TextField.foreground", corTextoTextField);

        Font fonteTextField = new Font("Arial", Font.BOLD, 18);
        UIManager.put("OptionPane.messageFont", fonteTextField);
        UIManager.put("OptionPane.buttonFont", fonteTextField);
        UIManager.put("TextField.font", fonteTextField);
        UIManager.put("Label.font", fonteTextField);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(customIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setPreferredSize(new Dimension(customIcon.getIconWidth(), customIcon.getIconHeight()));
        panel.setLayout(new GridBagLayout());
        
        JLabel label = new JLabel("<html><div style='text-align: center; font-size: 20px;'>PROJETO REALIZADO POR:<br>Alyson Souza Silva<br>Luiza Marinho Diniz Schirmer<br>Diego Santos Silva</div></html>", SwingConstants.CENTER);
        label.setOpaque(true); // Permite definir um fundo no JLabel
        label.setBackground(new Color(255, 255, 255, 255)); // Fundo branco
        label.setForeground(Color.BLACK); // Cor do texto
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Espaçamento interno

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false); // Deixa o painel transparente
        textPanel.add(label, BorderLayout.CENTER);

        panel.add(textPanel, new GridBagConstraints());



        JOptionPane.showMessageDialog(null, panel, "AP2 DE PROJETO DE PROGRAMAS", JOptionPane.PLAIN_MESSAGE, customIcon2);

        while (true) {
            String[] loginOptions = { "Login", "Registro", "Sair" };
            int loginChoice = JOptionPane.showOptionDialog(null, "Escolha uma opção:", "Login/Registro",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, loginOptions, loginOptions[0]);

            if (loginChoice == 0) {
                if (Sistema.login()) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Login falhou. Por favor, tente novamente.");
                }
            } else if (loginChoice == 1) {
                Sistema.register();
            } else if (loginChoice == 2) {
                System.exit(0);
            }
        }
    }
}
