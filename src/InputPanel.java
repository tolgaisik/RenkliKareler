import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.lang.Runnable;

public class InputPanel extends JPanel {
    JTextField questionInput;
    Thread thread;
    App application;
    JButton create;

    InputPanel(App _application) {
        application = _application;
        questionInput = new JTextField(100);
        questionInput.setColumns(45);
        String[] hint = new String[13];
        for (int i = 0; i <= 12; i++) {
            hint[i] = Integer.toString(i);
        }
        String[] dimension = new String[4];
        for (int i = 0; i < 4; i++) {
            dimension[i] = Integer.toString(i + 4);
        }
        String[] arrows = new String[17];
        for (int i = 0; i <= 16; i++) {
            arrows[i] = Integer.toString(i);
        }
        create = new JButton("Yeni Soru");
        create.addActionListener((ActionEvent e) -> {
            generateGame();
        });
        add(questionInput);
        add(create);
    }

    void generateGame() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                application.soruPanel.soru = null;
                try {
                    application.soruPanel.soru = new Kareler(questionInput.getText());
                } catch (Kareler.InvalidSolutionException e) {

                } catch (Kareler.SolutionDoesNotExistException e) {

                } catch (Exception e) {

                } finally {
                    application.soruPanel.repaint();
                }
            }
        });
        thread.start();
    }
}
