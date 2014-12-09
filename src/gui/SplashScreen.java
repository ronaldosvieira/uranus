package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class SplashScreen extends JWindow {

	private static final long serialVersionUID = 1L;
	private int duration;
    
    public SplashScreen(int d) {
        duration = d;
    }
    
    // Este eh um metodo simples para mostrar uma tela de apresentacao

    // no centro da tela durante a quantidade de tempo passada no construtor

    public void showSplash() {        
        JPanel content = (JPanel)getContentPane();
        content.setBackground(Color.white);
        
        // Configura a posicao e o tamanho da janela
        int width = 450;
        int height = 253;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width-width)/2;
        int y = (screen.height-height)/2;
        setBounds(x,y,width,height);
        
        // Constroi o splash screen
        JLabel label = new JLabel(new ImageIcon(getClass().getResource("/img/uranus.jpg")));
        content.add(label, BorderLayout.CENTER);

        // Torna visivel
        setVisible(true);
        
        // Espera ate que os recursos estejam carregados
        try { Thread.sleep(duration); } catch (Exception e) {}        
        setVisible(false);        
    }
    
    public void showSplashAndExit() {        
        showSplash();
        System.exit(0);        
    }
}