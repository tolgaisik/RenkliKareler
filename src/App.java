
/*
 * Created on 07.May.2007
 */
import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.BorderLayout;
/**
 * @author Fatih Keles
 */
@SuppressWarnings("serial")
public class App extends JFrame
{
	public SoruPanel soruPanel;
	ControlPanel controlPanel;
    InputPanel inputPanel;
    public App()
    {   
    	setTitle("Renkli Kareler");
        setBounds(100,100,600,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        controlPanel = new ControlPanel(this);
        soruPanel = new SoruPanel(this);
        inputPanel = new InputPanel(this);
        createRootPane();
        getContentPane().add(soruPanel,BorderLayout.CENTER);
        getContentPane().add(controlPanel,BorderLayout.SOUTH);
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        setVisible(true);
    }
    
    public static void main(String[] args) 
    {
    	try {
	        UIManager.setLookAndFeel(
	        		"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	    } catch (Exception e) {}
        new App();
    }
    public void setTitle(int i)
    {
    	setTitle(i+"");
    }
}
