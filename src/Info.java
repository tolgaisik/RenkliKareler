
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Info extends JFrame implements ActionListener {
    JButton edit, save;
    JTextArea text;
    JPanel pnl;
    Info() {
        
        setTitle("Bilgiler");
        setBounds(200,200,400,400);
        getContentPane().setLayout(new BorderLayout());
        setVisible(true);
        pnl = new JPanel();
        edit = new JButton("Duzenle");
        save = new JButton("Kaydet");
        edit.addActionListener(this);
        save.addActionListener(this);
        text = new JTextArea();
        text.setVisible(true);
		text.setEditable(false);
		text.setBackground(java.awt.Color.WHITE);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
        text.setFont(new Font("Arial",Font.BOLD,14));
        text.setPreferredSize(new Dimension(400,323));
		try {
            String str = "";
		    FileReader input = new FileReader("info.txt");
            int i; 
            while ((i=input.read()) != -1) 
                str += (char)i;
		    text.setText(str);
			input.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        add(text, BorderLayout.NORTH);
        pnl.add(save);
        pnl.add(edit);
        add(pnl, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if(source == edit) {
            text.setEditable(true);
        }
        else if(source == save) {
            text.setEditable(false);
            try {
                FileWriter wr = new FileWriter("info.txt");
                String s = text.getText();
                wr.write(s);
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
}
