import com.formdev.flatlaf.themes.FlatMacLightLaf;
import components.Custom.LoginGUI;

public class main {
    public static void main(String[] args) {
        FlatMacLightLaf.setup();
        LoginGUI loginGUI = new LoginGUI();
        loginGUI.initLogin();
    }
}
