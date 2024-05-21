package projetopl;

import javax.swing.SwingUtilities;

public class ProjetoPL {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SimplexGUI frame = new SimplexGUI();
                frame.setVisible(true);
            }
        });
    }
}
