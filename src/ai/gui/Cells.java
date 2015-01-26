package ai.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Cells extends JPanel {

	private static final long serialVersionUID = 7176886344416475173L;

	private Color defaultBackground;

	private int piece;

	private int x, y;

	private boolean clicked;

	public Cells(Color def){

		defaultBackground = def;
		clicked = false;
		piece = -1;

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if (clicked){
					setBorder(new LineBorder(Color.LIGHT_GRAY,1,true));
					clicked = false;
				} else {
					setBorder(new LineBorder(Color.GREEN,1,true));
					clicked = true;
				}
				System.out.println("["+x+"]["+y+"]");
			}
		});

		setBackground(defaultBackground);

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(40, 40);
	}

	public boolean isFree(){
		return piece == -1;
	}

	public void setWQueen(){
		piece = 1;
		repaint();
	}

	public void setBQueen(){
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

	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		BufferedImage image = null;

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
		setBackground(defaultBackground);
	}
}