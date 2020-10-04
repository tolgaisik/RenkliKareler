/*
 * Created on 10.Nis.2007
 */

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
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
	JButton yenile, yazdir, kopyala, cik, ayarlar;
	JTextArea aciklamalar;
	JCheckBox _sum,_mult,_sub;
	JComboBox boyut, copylist,type,letcolnum;
	private String[] boyutlar = {"6", "8", "10", "12","6","9","12"};
	private String[] types = {"2","3"};
	private String[] colors = {"Renk", "Harf", "Rakam"};
	Cicekler cicekler;
	JPanel buttonPanel;
	private int n, f,typ,tur;
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

		type = new JComboBox<String>(types);
		type.addActionListener(this);
		type.setSelectedIndex(0);

		boyut = new JComboBox<String>(boyutlar);
		boyut.addActionListener(this);
		boyut.setSelectedIndex(0);


		letcolnum = new JComboBox<String>(colors);
		letcolnum.addActionListener(this);
		

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
		aciklamalar.setText("Boş karelerin tümünü kırmızı ve mavi renklerle öyle boyayınız ki: Her sırada ve kolonda eşit sayıda kırmızı ve mavi kare bulunsun. Hiçbir sırada ve kolonda aynı renkli 3 kare yan yana bulunmasın.");
		add(aciklamalar,BorderLayout.CENTER);
		buttonPanel = new JPanel();
		buttonPanel.add(new JLabel("Boyut:"));
		buttonPanel.add(boyut);
		buttonPanel.add(new JLabel("Tür Adedi:"));
		buttonPanel.add(type);
		buttonPanel.add(new JLabel("Şekil"));
		buttonPanel.add(letcolnum);
		buttonPanel.add(yenile);
		buttonPanel.add(ayarlar);
		buttonPanel.add(copylist);
		buttonPanel.add(kopyala);
		buttonPanel.add(yazdir);
		buttonPanel.add(cik);

		add(buttonPanel,BorderLayout.SOUTH);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if(source==boyut)
		{
			int i = boyut.getSelectedIndex();
		}
		else if (source == type) {
			
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
			n = (Integer.parseInt(boyutlar[boyut.getSelectedIndex()]));
			this.tur = letcolnum.getSelectedIndex();
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
	
	public void kopyala()
	{
		int copymode = copylist.getSelectedIndex();
		int w = cicekler.soruPanel.getWidth(), h = cicekler.soruPanel.getHeight();
		BufferedImage image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		cicekler.soruPanel.paintComponent(image.createGraphics());
		if (copymode==1) {
			BufferedImage newimage = new BufferedImage(cicekler.soruPanel.hucreBoyu*(cicekler.soruPanel.soru.size+1)+2,h*2+cicekler.soruPanel.hucreBoyu,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = newimage.createGraphics();
			graphics.setColor(Color.white);
			graphics.fillRect(0, 0, newimage.getWidth(), newimage.getHeight());
			cicekler.soruPanel.paintComponent(graphics);
			graphics.translate(-image.getWidth()+cicekler.soruPanel.hucreBoyu*(cicekler.soruPanel.soru.size+1)+2, h+cicekler.soruPanel.hucreBoyu);
			cicekler.soruPanel.paintComponent(graphics);
			image = newimage;
		}
		else if(copymode==2) image = image.getSubimage(0, 0, cicekler.soruPanel.hucreBoyu*(cicekler.soruPanel.soru.size+1)+2, h);
		else if(copymode==3) image = image.getSubimage(w-cicekler.soruPanel.hucreBoyu*(cicekler.soruPanel.soru.size+1)-2, 0, cicekler.soruPanel.hucreBoyu*(cicekler.soruPanel.soru.size+1)+2, h);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				new ImageSelection(image), null);
	}

}
