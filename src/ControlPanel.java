/*
 * Created on 10.Nis.2007
 */

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JColorChooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * @author Fatih Keles
 */

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements ActionListener
{
	JButton yenile, yazdir, kopyala, cik, ayarlar,renk1,renk2,renk3,info;
	JTextArea aciklamalar;
	JCheckBox _sum,_mult,_sub;
	JComboBox boyut, copylist,type,letcolnum;
	private String[] boyutlar = {"6", "8", "10", "12"};
	private String[] boy = {"6","9","12"};
	private String[] types = {"2","3"};
	private String[] colors = {"Harf","Renk","Rakam"};
	Cicekler cicekler;
	JPanel buttonPanel;
	public int n, f,typ,tur;
	boolean busy = false;
	private Thread thread = null;
	private String[] kopyalamaSecenekleri = {"Yan Yana", "Alt Alta", "Soru", "Cevap"};
	public ControlPanel(Cicekler cicekler)
	{
		this.cicekler = cicekler;

		yenile = new JButton("Yeni Soru");
		yenile.addActionListener(this);
		
		yazdir = new JButton("Yazd\u0131r");
		yazdir.addActionListener(this);
		
		kopyala = new JButton("Kopyala");
		kopyala.addActionListener(this);
		
		ayarlar = new JButton("Ayarlar");
		ayarlar.addActionListener(this);
		
		cik = new JButton("\u00c7\u0131k");
		cik.addActionListener(this);

		renk1 = new JButton("Renk 1");
		renk1.addActionListener(this);

		renk2 = new JButton("Renk 2");
		renk2.addActionListener(this);

		renk3 = new JButton("Renk 3");
		renk3.addActionListener(this);
		
		type = new JComboBox<String>(types);
		type.addActionListener(this);
		type.setSelectedIndex(0);

		boyut = new JComboBox<String>(boyutlar);
		boyut.addActionListener(this);
		boyut.setSelectedIndex(0);

		info = new JButton("?");
		info.addActionListener(this);

	

		letcolnum = new JComboBox<String>(colors);
		letcolnum.addActionListener(this);
		letcolnum.setSelectedIndex(1);

		copylist = new JComboBox(kopyalamaSecenekleri);
		
		aciklamalar = new JTextArea();
		setLayout(new BorderLayout());
		aciklamalar.setVisible(true);
		aciklamalar.setEditable(true);
		aciklamalar.setBackground(java.awt.Color.WHITE);
		aciklamalar.setLineWrap(true);
		aciklamalar.setWrapStyleWord(true);
		aciklamalar.setFont(new Font("Verdana",Font.PLAIN,12));
		aciklamalar.setPreferredSize(new java.awt.Dimension(200,60));
		aciklamalar.setText("Bo\u015f karelerin t\u00fcm\u00fcn\u00fc k\u0131rm\u0131z\u0131 ve mavi renklerle \u00f6yle boyay\u0131n\u0131z ki: Her s\u0131rada ve kolonda e\u015fit say\u0131da k\u0131rm\u0131z\u0131 ve mavi kare bulunsun. Hi\u00e7bir s\u0131rada ve kolonda ayn\u0131 renkli 3 kare yan yana bulunmas\u0131n.");
		add(aciklamalar,BorderLayout.CENTER);
		buttonPanel = new JPanel();
		
		buttonPanel.add(new JLabel("T\u00fcr Adedi:"));
		buttonPanel.add(type);
		buttonPanel.add(new JLabel("Boyut:"));
		buttonPanel.add(boyut);

		buttonPanel.add(new JLabel("Sekil:"));
		buttonPanel.add(letcolnum);
		
		buttonPanel.add(yenile);
		buttonPanel.add(ayarlar);
		buttonPanel.add(copylist);
		buttonPanel.add(kopyala);
		//buttonPanel.add(kopyala);
		buttonPanel.add(yazdir);
		buttonPanel.add(cik);
		buttonPanel.add(renk1);
		buttonPanel.add(renk2);
		buttonPanel.add(renk3);
		buttonPanel.add(info);
		add(buttonPanel,BorderLayout.SOUTH);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if(source==boyut)
		{
			int i = boyut.getSelectedIndex();
		}
		else if(source == renk1) {
			Color c = JColorChooser.showDialog(null,"Renk 1",cicekler.soruPanel.c1);
			if(c != null) {
				cicekler.soruPanel.c1 = c;
				cicekler.soruPanel.repaint();
			}
		}
		else if(source == info) {
			Info inf = new Info();
		}
		else if(source == renk2) {
			Color c4 = JColorChooser.showDialog(null,"Renk 2",cicekler.soruPanel.c2);
			if(c4 != null) {
				cicekler.soruPanel.c2 = c4;cicekler.soruPanel.repaint();
			}
		}
		else if(source == renk3) {
			Color c5 = JColorChooser.showDialog(null,"Renk 3",cicekler.soruPanel.c3);
			if(c5 != null) {
				cicekler.soruPanel.c3 = c5;cicekler.soruPanel.repaint();
			}
		}
		else if (source == type && boyut != null) {
			if(type.getSelectedIndex() == 1) {
				boyut.removeAllItems();
				for (int i = 0; i < boy.length; i++) {
					boyut.addItem(boy[i]);
				}
			
			}
			else {
				boyut.removeAllItems();
				for (int i = 0; i < boyutlar.length; i++) {
					boyut.addItem(boyutlar[i]);
				}
				
			}
		}
		else if(source==yenile)
		{
			if(busy)
			{
				busy = false;
				thread.stop();
				yenile.setText("Yeni Soru");
				cicekler.soruPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				return;
			}
			this.typ = type.getSelectedIndex();
			cicekler.soruPanel.c = letcolnum.getSelectedIndex();
			n = typ == 1 ? (Integer.parseInt(boy[boyut.getSelectedIndex()])) : (Integer.parseInt(boyutlar[boyut.getSelectedIndex()]));
			this.tur = letcolnum.getSelectedIndex();
			System.out.println(this.tur);
			thread = new Thread(){
				public void run()
				{
					busy = true;
					yenile.setText("\u0130ptal");
					cicekler.soruPanel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					cicekler.soruPanel.soru = new Kareler(n,typ+2);
					yenile.setText("Yeni Soru");
					cicekler.soruPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					busy = false;
					cicekler.soruPanel.repaint();
					
				}
			};
			thread.start();
			
		}
		else if(source==cik)
		{
			System.exit(0);
		}
		else if(source==ayarlar)
		{
			new Ayarlar(cicekler);
		}
		else if(source==yazdir)
		{
			PrintJob pj = Toolkit.getDefaultToolkit().getPrintJob(cicekler,"", new java.util.Properties());
			if(pj==null) return;
			Graphics g = pj.getGraphics();
			g.setFont(new Font("Times New Roman",1,18));
			int x = 0, y = 40;
			g.drawString("\u00c7i\u00e7ekler", x+30, y);
			y += 25;
			g.setFont(new Font("Times New Roman",0,12));

			int width = pj.getPageDimension().width - 60;
			
			java.util.StringTokenizer st = new java.util.StringTokenizer(aciklamalar.getText());
			java.awt.FontMetrics fm = g.getFontMetrics();
			while (st.hasMoreTokens())
			{
				String temp = st.nextToken();
				if ((x+fm.stringWidth(temp)+30)>(30+width))
				{
					x = 0;
					y += 15;
				}
				g.drawString(temp,x+30,y);
				x += (fm.stringWidth(temp+" "));
			}
			
			cicekler.soruPanel.printWidth = pj.getPageDimension().width - 50;
			cicekler.soruPanel.printHeight = pj.getPageDimension().height - 50;
			cicekler.soruPanel.printX = 25;
			cicekler.soruPanel.printY = y + 30;
			cicekler.soruPanel.print = true;
			cicekler.soruPanel.paintComponent(g);
			cicekler.soruPanel.print = false;
			cicekler.soruPanel.printX = 0;
			cicekler.soruPanel.printY = 0;
			cicekler.soruPanel.printWidth = 0;
			cicekler.soruPanel.printHeight = 0;
			
			pj.end();
		}
		else if(source==kopyala)
		{
			kopyala();
		}
		
	}
	
	public void kopyala() {
		int copy_mode = copylist.getSelectedIndex();
			BufferedImage image =
				new BufferedImage(cicekler.soruPanel.getWidth(),
						cicekler.soruPanel.getHeight(),
						BufferedImage.TYPE_INT_RGB);
			
			cicekler.soruPanel.paintComponent(image.createGraphics());
			BufferedImage question = image.getSubimage(cicekler.soruPanel.left_space-1, cicekler.soruPanel.up_space-1, cicekler.soruPanel.board_length+3, cicekler.soruPanel.board_length+3);
			BufferedImage answer = image.getSubimage(cicekler.soruPanel.right_board-1, cicekler.soruPanel.up_space-1, cicekler.soruPanel.board_length+3, cicekler.soruPanel.board_length+3);
			if(copy_mode == 0)
			{
				image = new BufferedImage(question.getWidth()+answer.getWidth()+cicekler.soruPanel.middle_space,
						question.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				Graphics g = image.getGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, image.getWidth(), image.getHeight());
				g.drawImage(question, 0, 0, this);
				g.drawImage(answer, question.getWidth()+cicekler.soruPanel.middle_space,0, this);
			}
			else if(copy_mode == 1)
			{
				image = new BufferedImage(question.getWidth(),
						question.getHeight()+answer.getHeight()+cicekler.soruPanel.middle_space,
						BufferedImage.TYPE_INT_RGB);
				Graphics g = image.getGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, image.getWidth(), image.getHeight());
				g.drawImage(question, 0, 0, this);
				g.drawImage(answer, 0, question.getHeight()+cicekler.soruPanel.middle_space, this);
			}
			if(copy_mode == 2)
			{
				image = question;
			}
			else if(copy_mode == 3)
			{
				image = answer;
			}
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
					new ImageSelection(image), null);
	}
	public class T extends Thread{
		public boolean exit = false;
		public void run() {
			exit = true;
			busy = true;
			yenile.setText("\u0130ptal");
			cicekler.soruPanel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			cicekler.soruPanel.soru = new Kareler(n,typ+2);
			yenile.setText("Yeni Soru");
			cicekler.soruPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			busy = false;
			cicekler.soruPanel.repaint();
		}
	}

}
