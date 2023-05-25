package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class KapeButton extends JButton {
    private static final Color DEFAULT_COLOR = new Color(67, 181, 129);
    private static final Color HOVER_COLOR = new Color(57, 157, 109);
    private static final Color TEXT_COLOR = Color.WHITE;

    public KapeButton(String text) {
        super(text);
        initButton();
    }

    private void initButton() {
        setForeground(TEXT_COLOR);
        setBackground(DEFAULT_COLOR);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(HOVER_COLOR);
            }
            public void mosueExited(MouseEvent e) {
                setBackground(DEFAULT_COLOR);
            }
        });

        setPreferredSize(new Dimension(100, 30));
        setMargin(new Insets(5, 10, 5, 10));
    }
}
