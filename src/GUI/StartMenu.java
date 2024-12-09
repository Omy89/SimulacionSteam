package GUI;

import javax.swing.JFrame;
import Code.DataManager;
import java.awt.CardLayout;
import javax.swing.JPanel;

public class StartMenu extends JFrame {

    CardLayout cardlayout;
    JPanel cardpanel;
    DataManager userManager;

    public StartMenu() {
        setSize(600, 450);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Steam");

        cardlayout = new CardLayout();
        cardpanel = new JPanel(cardlayout);

        userManager = new DataManager();

        //paneles
        Login login = new Login(cardlayout, cardpanel, userManager);
        Register register = new Register(cardlayout, cardpanel, userManager);

        cardpanel.add(login, "login");
        cardpanel.add(register, "register");

        add(cardpanel);
        cardlayout.show(cardpanel, "login");
        setVisible(true);
    }

    public static void main(String[] args) {
        StartMenu steam = new StartMenu();
    }
}
