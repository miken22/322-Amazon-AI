package single;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Cells extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7176886344416475173L;

    private Color defaultBackground;
    
    private boolean wQueen;
    private boolean bQueen;
    private boolean arrow;
    
    private int x, y;
    
	public Cells(Color def){
		
		defaultBackground = def;
		wQueen = false;
		bQueen = false;
		arrow = false;
		
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
            	System.out.println("X: " + x + " Y: " + y);
            }
        });
		
		setBackground(defaultBackground);
		
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(40, 40);
	}
	
	public boolean isFree(){
		return (wQueen || bQueen || arrow);
	}
	
	public void setWQueen(){
		wQueen = true;
		repaint();
	}
	
	public void setBQueen(){
		bQueen = true;
		repaint();
	}
	
	public void setArrow(){
		arrow = true;
		repaint();
	}
	
	public void setFree(){
		wQueen = false;
		bQueen = false;
		arrow = false;
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
		
		if (wQueen){
			try{
				image = ImageIO.read(this.getClass().getResourceAsStream("/rsz_wq.png"));
			} catch (IOException e){
				e.printStackTrace();
				System.exit(-1);
			}
			g.drawImage(image,0,0,null);
			return;
		}
		
		if (bQueen){
			try{
				image =  ImageIO.read(this.getClass().getResourceAsStream("/rsz_bq.png"));
			} catch (IOException e){
				e.printStackTrace();
				System.exit(-1);
			}
			g.drawImage(image,0,0,null);
			return;
		}
		if (arrow){
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