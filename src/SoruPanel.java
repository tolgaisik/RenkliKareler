/*
 * Created on 07.May.2007
 */

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class SoruPanel extends JPanel {
    String duzen = "", sorubasligi = "", cevapbasligi = "";
    boolean kayitli = true;
    Color tablo = Color.DARK_GRAY,
            tabloIc = Color.WHITE,
            cerceve = Color.BLACK,
            disipucu = Color.WHITE,
            disarka = Color.WHITE,
            cevap = Color.BLACK,
            arkaPlan = Color.WHITE,
            icipucu = Color.BLACK,
            hints = Color.BLACK,
            c1 = Color.BLUE, c2 = Color.RED, c3 = Color.YELLOW;
    int cicekYuzde = 80;
    int hucreBoyu = 0; // rakamlarin geldigi hucrelerin en ve boyu
    Font font = null;
    boolean print = false; // yazdiriliyor mu
    int printWidth = 0, printHeight = 0; // kagit boyutlari
    int printX = 0, printY = 0; // kagitin sol ve ust bosluklari
    App cicekler;
    public int c = 0;
    public Kareler soru;
    int size,
            board_length,
            middle_space,
            left_space,
            up_space,
            right_board;

    public SoruPanel(App cicekler) {
        this.cicekler = cicekler;
        setBackground(arkaPlan);
        try {

            BufferedReader br = new BufferedReader(new FileReader("vs.dat"));
            duzen = br.readLine();
            duzenAl(duzen);
            br.close();
        } catch (IOException e) {
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cicekler.controlPanel.busy) {
            return;
        } else if (soru != null) {
            try {
                g.setFont(getFont());
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                size = soru.size;
                int component_width = this.getWidth();
                int component_height = this.getHeight();
                int size_h = this.getHeight() / (size + 2);
                int size_w = this.getWidth() / (size * 2 + 4);
                this.hucreBoyu = size_h > size_w ? size_w : size_h;
                this.board_length = size * hucreBoyu;
                this.middle_space = 2 * hucreBoyu;
                this.left_space = (component_width - (2 * size * hucreBoyu + middle_space)) / 2;
                this.up_space = (component_height - board_length) / 2;
                this.right_board = left_space + board_length + middle_space;
                g.setFont(new Font(font.getName(), Font.BOLD, hucreBoyu / 2));
                g.setColor(tabloIc);
                g.fillRect(left_space, up_space, board_length, board_length);
                g.fillRect(right_board, up_space, board_length, board_length);
                g.setColor(cerceve);
                g.drawRect(left_space, up_space, board_length, board_length);
                g.drawRect(right_board, up_space, board_length, board_length);
                g.drawRect(left_space - 1, up_space - 1, board_length + 2, board_length + 2);
                g.drawRect(right_board - 1, up_space - 1, board_length + 2, board_length + 2);
                int thing = -1;
                String[] lts = { "A", "B", "C" };
                List<List<Integer>> b = this.soru.getGameBoard();
                for (int r = 0; r < soru.size; r++) {
                    for (int c = 0; c < soru.size; c++) {
                        g.setColor(icipucu);
                        thing = b.get(r).get(c);
                        if (this.c == 1) {
                            if (b.get(r).get(c) == 0) {
                                g.setColor(c1);
                            } else if (b.get(r).get(c) == 1) {
                                g.setColor(c2);
                            } else if (b.get(r).get(c) == 2) {
                                g.setColor(c3);
                            }
                            g.fillRect(right_board + (r * hucreBoyu), up_space + (c * hucreBoyu), hucreBoyu, hucreBoyu);
                        } else if (this.c == 2) {
                            g.drawString("" + soru.getGameBoard().get(r).get(c),
                                    right_board + (int) (hucreBoyu * 0.38) + (c * hucreBoyu),
                                    up_space + (int) (hucreBoyu * 0.7) + (r * hucreBoyu));
                        } else {
                            g.drawString("" + lts[thing], right_board + (int) (hucreBoyu * 0.38) + (c * hucreBoyu),
                                    up_space + (int) (hucreBoyu * 0.7) + (r * hucreBoyu));
                        }
                        if (soru.question.get(r).get(c) != -1) {
                            if (this.c == 1) {
                                if (b.get(r).get(c) == 0) {
                                    g.setColor(c1);
                                } else if (b.get(r).get(c) == 1) {
                                    g.setColor(c2);
                                } else if (b.get(r).get(c) == 2) {
                                    g.setColor(c3);
                                }
                                g.fillRect(left_space + (r * hucreBoyu), up_space + (c * hucreBoyu), hucreBoyu,
                                        hucreBoyu);
                            } else if (this.c == 2) {
                                g.drawString("" + soru.getGameBoard().get(r).get(c),
                                        left_space + (int) (hucreBoyu * 0.38) + (c * hucreBoyu),
                                        up_space + (int) (hucreBoyu * 0.7) + (r * hucreBoyu));
                            } else if (this.c == 0) {
                                g.drawString("" + lts[thing], left_space + (int) (hucreBoyu * 0.38) + (c * hucreBoyu),
                                        up_space + (int) (hucreBoyu * 0.7) + (r * hucreBoyu));
                            }
                        }
                    }
                }
                g.setColor(cerceve);
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        g.drawRect(left_space + (i * hucreBoyu), up_space + (j * hucreBoyu), hucreBoyu, hucreBoyu);
                        g.drawRect(right_board + (i * hucreBoyu), up_space + (j * hucreBoyu), hucreBoyu, hucreBoyu);
                    }
                }

            } catch (NullPointerException NPE) {
                NPE.printStackTrace();
            }
        }

    }

    void duzenAl(String dosya) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("ayarlar/" + dosya));
            arkaPlan = new Color(Integer.parseInt(br.readLine()));
            setBackground(arkaPlan);
            cicekler.getContentPane().setBackground(arkaPlan);
            cerceve = new Color(Integer.parseInt(br.readLine()));
            tabloIc = new Color(Integer.parseInt(br.readLine()));
            tablo = new Color(Integer.parseInt(br.readLine()));
            cevap = new Color(Integer.parseInt(br.readLine()));
            disipucu = new Color(Integer.parseInt(br.readLine()));
            disarka = new Color(Integer.parseInt(br.readLine()));
            icipucu = new Color(Integer.parseInt(br.readLine()));
            font = new Font(br.readLine(), Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine()));
            sorubasligi = br.readLine();
            cevapbasligi = br.readLine();
            cicekler.setBounds(Integer.parseInt(br.readLine()),
                    Integer.parseInt(br.readLine()),
                    Integer.parseInt(br.readLine()),
                    Integer.parseInt(br.readLine()));
            br.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(cicekler, "Renk d\u00fczeni al\u0131namad\u0131", "Hata",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            kayitli = false;
        }
        setBackground(arkaPlan);
        repaint();
    }
}