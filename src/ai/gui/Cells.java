package ai.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Cells that extend JPanels for the GUI.
 * 
 * @author Michael
 *
 */
public class Cells extends JPanel {

	private static final long serialVersionUID = 7176886344416475173L;

	private Color defaultBackground;

	private int piece;
	
	public Cells(Color def){
		defaultBackground = def;
		piece = -1;
		setBackground(defaultBackground);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(40, 40);
	}

	public boolean isFree(){
		return piece == -1;
	}

	public void setWhitePiece(){
		piece = 1;
		repaint();
	}

	public void setBlackPiece(){
		piece = 2;
		repaint();
	}

	public void setArrow(){
		piece = 3;
		repaint();
	}

	public void setFree(){
		piece = -1;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		BufferedImage image = null;

		// Place white queen image
		if (piece == 1){
			try{
				image = ImageIO.read(this.getClass().getResourceAsStream("/rsz_wq.png"));
			} catch (IOException e){
				e.printStackTrace();
				System.exit(-1);
			}
			g.drawImage(image,0,0,null);
			return;
		}
		// Black queen image
		if (piece == 2){
			try{
				image =  ImageIO.read(this.getClass().getResourceAsStream("/rsz_bq.png"));
			} catch (IOException e){
				e.printStackTrace();
				System.exit(-1);
			}
			g.drawImage(image,0,0,null);
			return;
		}
		// Arrow image
		if (piece == 3){
			try{
				image =  ImageIO.read(this.getClass().getResourceAsStream("/rsz_arrow.jpg"));
			} catch (IOException e){
				e.printStackTrace();
				System.exit(-1);
			}
			g.drawImage(image,0,0,null);
			return;
		}
		// Clear space
		setBackground(defaultBackground);
	}
}