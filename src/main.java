import com.formdev.flatlaf.FlatDarkLaf;
import components.Custom.LoginGUI;

public class main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        LoginGUI loginGUI = new LoginGUI();
        loginGUI.initLogin();
    }
}
