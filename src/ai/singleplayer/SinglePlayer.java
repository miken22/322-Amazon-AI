package ai.singleplayer;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ubco.ai.games.GameMessage;
import ubco.ai.games.GamePlayer;

/**
 * Single player implementation of the Game of Amazons. Currently a work in progress
 * there is no goal check and does not have a method for scoring.
 * 
 * The game is console based, each player takes turns typing their move into the bottom
 * text pane with the following format:
 * 
 * [a-j][0-9]-[a-j][0-9]-[a-j][0-9]
 * 
 * A sample starting move for white is: d0-d4-d6 (move from d0 -> d4, arrow to d6)
 * 
 * Board is 2d array, first position marks column, second marks row.
 * 
 * 
 * @author Mike Nowicki
 *
 */
public class SinglePlayer extends JFrame implements GamePlayer {

	private static final long serialVersionUID = 6270779398069277942L;

	private JFrame frame;

	private JScrollPane scrollChat;
	private JScrollPane scrollLog;
	
	private JTextPane chat;
	private JTextPane moveLog ;
	private JTextArea input;

	private Style userStyle;
	private Style agentStyle;
	
	private StyledDocument chatTextarea;
	private StyledDocument moveTextarea;
		
	private JButton send;
	private JButton clear;
	
	private JMenuBar menu;
	private JMenu file;
	private JMenuItem exit;
	
	private JLabel whosTurn;
	private Timer gameTimer;
	
	private Font font;
	
	private int rows;
	private int columns;
	private Board board;
	
	private Cells[][] guiBoard;
	private boolean player1Turn = true;

	private boolean useAI = false;
	private boolean finished = false;
	
	public final int WQUEEN = 1;
	public final int BQUEEN = 2;
	public final int ARROW = 3;
	public final int FREE = -1;	
			
	@Override
	public boolean handleMessage(String arg0) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleMessage(GameMessage arg0) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args){
		SinglePlayer sp = new SinglePlayer(10,10);
		sp.init();	
	}
	
	public SinglePlayer(int row, int col){
		board = new Board(row,col);
		guiBoard = new Cells[row][col];		
		rows = row;
		columns = col;
	}

	public void init(){
		createFrame();
		drawBoard();
		initializePositions();
		
		gameTimer.startTiming();
		
		whosTurn.setText("Whites move");
	}	
	
	private void createFrame(){
		
		Container c;
		
		frame = new JFrame();		
		menu = new JMenuBar();
		
		file = new JMenu("File");
		exit = new JMenuItem("Exit");
		
		whosTurn = new JLabel("New game");
		gameTimer = new Timer();
		gameTimer.setForeground(Color.RED);
		
		send = new JButton("Send");
		clear = new JButton("Clear");
		
		moveLog = new JTextPane();
		scrollLog = new JScrollPane(moveLog);
		chat = new JTextPane();
		scrollChat = new JScrollPane(chat);
		input = new JTextArea();
		
		userStyle = chat.addStyle("userin", null);
		agentStyle = chat.addStyle("agentstyle",null);
		
		send.addActionListener(new ButtonListener());
		clear.addActionListener(new ButtonListener());
		
		exit.addActionListener(new MenuListener(1));
		
		try {
			font = Font.createFont(0,this.getClass().getResourceAsStream("/Trebuchet MS.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		Border b = new LineBorder(Color.LIGHT_GRAY,1,true);
		
		frame = new JFrame("Game of Amazons");
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(725, 675);
		frame.setResizable(false);
		frame.setJMenuBar(menu);
		
		whosTurn.setBounds(245,0,200,30);
		font = font.deriveFont(Font.BOLD,15);
		whosTurn.setFont(font);
		
		gameTimer.setBounds(50, 0, 250, 20);
		font = font.deriveFont(Font.PLAIN,15);
		gameTimer.setFont(font);
		gameTimer.setBounds(50, 0, 250, 20);
		
		menu.setBackground(new Color(244,244,244));
		menu.add(file);
		file.add(exit);
				
		chat.setEditable(false);
		chat.setContentType("text/html");
		chat.setBorder(b);
		chat.setBackground(new Color(252,252,252));
		font = font.deriveFont(Font.PLAIN,15);
		chat.setFont(font);

		scrollChat.setBounds(550, 250, 170, 300);
		scrollChat.setBackground(new Color(252,252,252));
		scrollChat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Chat History:"));
		
		moveLog.setEditable(false);
		moveLog.setContentType("text/html");
		moveLog.setBorder(b);
		moveLog.setBackground(new Color(252,252,252));
		font = font.deriveFont(Font.PLAIN,15);
		moveLog.setFont(font);
		
		scrollLog.setBounds(550, 0, 170, 250);
		scrollLog.setBackground(new Color(252,252,252));
		scrollLog.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Move History:"));
		
		input.setLineWrap(true);
		input.setBorder(b);
		input.setFont(font);
		input.setBounds(0, 550, 550, 100);
		input.setBackground(new Color(252,252,252));
		input.addKeyListener(new TextListener());
		input.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Enter a move or chat:"));		
		
		chatTextarea = chat.getStyledDocument();
		moveTextarea = moveLog.getStyledDocument();
		
		send.setBounds(550, 550, 170, 50);
		send.setBorder(b);
		send.setBackground(new Color(250,250,250));
		send.setFocusPainted(false);
		clear.setBounds(550, 600, 170, 50);
		clear.setBorder(b);
		clear.setBackground(new Color(250,250,250));
		clear.setFocusPainted(false);
		
		c = frame.getContentPane();
		c.add(whosTurn);
		c.add(gameTimer);
		c.add(scrollChat);
		c.add(scrollLog);
		c.add(input);
		c.add(send);
		c.add(clear);
		
	}
	
	private void drawBoard(){
				
		Color c1 = new Color(219, 169, 1);
		Color c2 = new Color(255, 229, 204);	
		
        for (int i = rows-1; i >= 0; i--) {
           
        	for (int j = columns-1; j >= 0; j--) {
            	
            	Cells cell;
            	
            	if (i % 2 == 0){
            		if (j % 2 == 0){
            			cell = new Cells(c2);
            		} else {
            			cell = new Cells(c1);
            		}
            	} else {
            		if (j % 2 == 0){
            			cell = new Cells(c1);
            		} else {
            			cell = new Cells(c2);
            		}
            	}
            	
            	cell.setBounds(j * 50 + 50, 500 - i * 50, 50, 50);
            	cell.setBorder(new LineBorder(Color.LIGHT_GRAY,1,true));
            	cell.setXY(i,j);	// This is weird but it has to be backwards because the gui is tilted, is only for reference anyways never used.
            	guiBoard[i][j] = cell;
            	frame.add(cell);
            }
        }
        
        JPanel tile = new JPanel();
        tile.setBackground(Color.BLACK);
        tile.setBounds(0,0,50,50);
        frame.add(tile);
        
        /*
         * I hate GUI's, this is the ugliest hack in the world I could come up with for labeling
         * the grid but Java randomly decides not to draw some/all of the tiles. I hope someone knows
         * a better solution.. 
         */
        char[] letters = {'a','b','c','d','e','f','g','h','i','j'};
        for (int i = 0; i < columns; i++){
        	JLabel label = new JLabel();
        	label.setBackground(Color.BLACK);
        	label.setBounds(70 + i * 50,10, 50,50);
        	label.setText("" + letters[i]);
        	frame.add(label);
        }
        
        // Columns are used as integers but map to a char value
        for (int i = rows-1; i >= 0; i--){
        	JLabel label = new JLabel();
        	label.setBackground(Color.BLACK);
        	label.setBounds(25,510-i*50, 50,40);
        	label.setText("" + i);
        	frame.add(label);
        }
        
        frame.getContentPane().repaint();
        frame.repaint();
		
	}
	
	private void initializePositions(){
		
		guiBoard[0][3].setWQueen();
		guiBoard[0][6].setWQueen();
		guiBoard[3][0].setWQueen();
		guiBoard[3][9].setWQueen();
		
		guiBoard[6][0].setBQueen();
		guiBoard[6][9].setBQueen();
		guiBoard[9][3].setBQueen();
		guiBoard[9][6].setBQueen();	

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		input.requestFocus();
	}
		
	
	
	// Count the amount of available space for each player, largest area = winner
	private int determineWinner() {
		// Probably use the same idea as goal state checking.
		return 1;
	}

		
	/*
	   Flood-fill (node, target-color, replacement-color):
		 1. If target-color is equal to replacement-color, return.
		 2. Set Q to the empty queue.
		 3. Add node to the end of Q.
		 4. While Q is not empty: 
		 5.     Set n equal to the last element of Q.
		 6.     Remove last element from Q.
		 7.     If the color of n is equal to target-color:
		 8.         Set the color of n to replacement-color and mark "n" as processed.
		 9.         Add west node to end of Q if west has not been processed yet.
		 10.        Add east node to end of Q if east has not been processed yet.
		 11.        Add north node to end of Q if north has not been processed yet.
		 12.        Add south node to end of Q if south has not been processed yet.
		 13. Return.
	 */
	private boolean isFinished(int player) {		
		return false;
	}


	/**
	 * This class controls game play, alternating player turns and handling any updates. All game logic must
	 * be handled within the ActionListener class.
	 * 
	 * 
	 * @author mike-nowicki
	 *
	 */
	public class ButtonListener implements ActionListener{
		String in = "";
		String conversation = "";
		String out = "";
        Document doc ;
        
		@Override
		public void actionPerformed(ActionEvent e) {
			
			in = input.getText();
			
			// Clear user input if clear button is pressed
			JButton b = (JButton)e.getSource();
			b.setBackground(new Color(253,253,253));
			if(e.getSource() == clear){
				input.setText("");
				b.setBackground(new Color(250,250,250));
			// If user does not enter anything the chatbot will not do anything.
			
			} else if(in.length() == 0){
				input.requestFocus();
				b.setBackground(new Color(250,250,250));
				return;
			} else {
				
				if (in.contains("-")){
					handleMove();
				} else {
					addText();
					input.setText("");
				}

				b.setBackground(new Color(250,250,250));
			}
			
			input.requestFocus();
			
			if (finished){
				int winner = determineWinner();
				JOptionPane.showMessageDialog(frame,"Game over, Player " + winner + " won!", "Game over", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
			
		}
		
		private void handleMove(){
			
			String from;
			String to;
			String arrow;
			
			String[] splitIn = in.split("-");
			
			if(splitIn.length > 3){
				JOptionPane.showMessageDialog(frame,"Invalid move format, try again.", "Invalid", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			from = splitIn[0];			
			to = splitIn[1];
			arrow = splitIn[2];
			
			// Get information about source of move (convert character to int value)
			char fromCol = from.toLowerCase().charAt(0);
			int fCol = Utility.getColumn(fromCol);
			int fRow = from.charAt(1)-48;
			
			char toCol = to.toLowerCase().charAt(0);
			int tCol = Utility.getColumn(toCol);
			int tRow = to.charAt(1)-48;
			
			char arrCol = arrow.toLowerCase().charAt(0);
			int aCol = Utility.getColumn(arrCol);
			int aRow = arrow.charAt(1)-48;
			
			if (player1Turn){
				
				// Check starting from an owned piece
				if (board.getPiece(fRow, fCol) != WQUEEN){
					JOptionPane.showMessageDialog(frame,"Must start with your own piece.", "Invalid", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// Verify move is valid (straight/diagonal, no obstructions)
				if (!moveIsValid(fRow,fCol,tRow,tCol,false)){
					return;
				}

				// Use same logic to validate arrow throws.
				if (!moveIsValid(tRow,tCol,aRow,aCol,true)){
					return;
				}
							
				board.freeSquare(fRow,fCol);
				board.placeMarker(tRow,tCol, WQUEEN);
				board.placeMarker(aRow, aCol, ARROW);
				
				board.updateWhitePositions(fRow, fCol, tRow, tCol);
				
				guiBoard[fRow][fCol].setFree();
				guiBoard[tRow][tCol].setWQueen();
				guiBoard[aRow][aCol].setArrow();
							
				updateMoveLog(in,WQUEEN);
				
				frame.repaint();
				input.setText("");
				
				player1Turn = false;
				
				whosTurn.setText("Blacks move.");
				frame.repaint();
								
				if(isFinished(WQUEEN)){
					finished = true;
				}
				
				return;
				
			} else {
				
				// Check starting from an owned piece
				if (board.getPiece(fRow, fCol) != BQUEEN){
					JOptionPane.showMessageDialog(frame,"Must start with your own piece.", "Invalid", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// Verify move is valid (straight/diagonal, no obstructions)
				if (!moveIsValid(fRow,fCol,tRow,tCol,false)){
					return;
				}

				// Use same logic to validate arrow throws.
				if (!moveIsValid(tRow,tCol,aRow,aCol,true)){
					return;
				}
				
				board.freeSquare(fRow,fCol);
				board.placeMarker(tRow,tCol, BQUEEN);
				board.placeMarker(aRow, aCol, ARROW);

				board.updateBlackPositions(fRow, fCol, tRow, tCol);
				
				guiBoard[fRow][fCol].setFree();
				guiBoard[tRow][tCol].setBQueen();
				guiBoard[aRow][aCol].setArrow();

				updateMoveLog(in,BQUEEN);
				
				frame.repaint();
				input.setText("");
				
				player1Turn = true;
				
				whosTurn.setText("Whites move.");
				frame.repaint();
								
				if(isFinished(BQUEEN)){
					finished = true;
				}
				
				return;
			}
		}
			
		private boolean moveIsValid(int sX, int sY, int dX, int dY, boolean isArrow){
					
			// Check horizontal move, make sure no obstacles
			if (sX == dX){
				
				if (sY == dY){

					// No move, just throw arrow, not allowed to throw into your own tile
					if (isArrow){
						JOptionPane.showMessageDialog(frame,"Illegal move, cannot shoot arrow at your own position.", "Invalid", JOptionPane.WARNING_MESSAGE);
						return false;
					} else {
						// Otherwise you are not moving, still valid I guess?
						return true;
					}
				}
				
				// Get change in column, find if positive/negative, use to increment to new position checking each tile on the way.
				int deltaY = dY - sY;
				deltaY = deltaY / Math.abs(deltaY);
				
				while(sY != dY){
					sY += deltaY;
					
					if (board.isMarked(dX,sY)){
						JOptionPane.showMessageDialog(frame,"Illegal move at [" + dX + "][" + sY + "].", "Invalid", JOptionPane.WARNING_MESSAGE);
						return false;
					}
				}
				return true;
				
			}
			
			// Check vertical move, same thing as above
			if (sY == dY){
				
				int deltaX = dX - sX;
				deltaX = deltaX / Math.abs(deltaX);
				
				while (sX != dX){
					sX += deltaX;
					if (board.isMarked(sX, dY)){
						JOptionPane.showMessageDialog(frame,"Illegal move at [" + sX + "][" + dY + "].", "Invalid", JOptionPane.WARNING_MESSAGE);
						return false;
					}
				}
				return true;
			}
			
			// Diagonal checks
			if(sX > dX && sY > dY){	
				// This is the moving from a square to one to its lower left
				int temp = sX;
				sX = dX;
				dX = temp;	
				
				temp = sY;
				sY = dY;
				dY = temp;	
				return checkEasyDiagonal(sX, sY, dX, dY);
			} else if (sX < dX && sY < dY){
				// Case where we move from a square to one to its upper right
				return checkEasyDiagonal(sX, sY, dX, dY);
			} else {
				// The other two diagonal directions
				return oppositeDiagonal(sX, sY, dX, dY);
			}
		}
		
		/**
		 * Simple rise/run calculation to check that the diagonal is valid. We must take as many steps
		 * left/right as we do up/down to have a valid diagonal move. Must be sure to swap start, end
		 * nodes if sX and sY > dX and dY for the algorithm to hold.
		 * 
		 * @param sX - Smaller x value.
		 * @param sY - Smaller y value.
		 * @param dX - Larger x value.
		 * @param dY - Larger y value.
		 * @return
		 */
		private boolean checkEasyDiagonal(int sX, int sY, int dX, int dY){
			
			while (sX != dX || sY != dY){
				sX++;
				sY++;
				if (board.isMarked(sX, sY)){
					JOptionPane.showMessageDialog(frame,"Illegal diagonal move.", "Invalid", JOptionPane.WARNING_MESSAGE);
					return false;
				}
			}
			return true;
		}
		
		/**
		 * Simple calculation to check the other two diagonal directions. We determine if we are moving up left
		 * or down right, in either case deltaX = -(deltaY) so we compute them and iteratively move along the diagonal
		 * like the other check does.
		 * 
		 * @param sX - Starting x value.
		 * @param sY - Starting y value.
		 * @param dX - Ending x value.
		 * @param dY - Ending y value.
		 * @return
		 */
		private boolean oppositeDiagonal(int sX, int sY, int dX, int dY){
			
			int deltaX = dX - sX;
			deltaX = deltaX/Math.abs(deltaX);
			
			int deltaY = deltaX/(-1);
			
			while (sX != dX || sY != dY){
				sX += deltaX;
				sY += deltaY;
				if (board.isMarked(sX, sY)){
					JOptionPane.showMessageDialog(frame,"Illegal diagonal move.", "Invalid", JOptionPane.WARNING_MESSAGE);
					return false;
				}
			}
			return true;
		}
		
		private void addText(){
			doc = chat.getDocument();
			input.setText("");
			
			StyleConstants.setForeground(userStyle, Color.red);
	        try { 
	        	chatTextarea.insertString(chatTextarea.getLength(), "\r\nUser: ",userStyle); 
			} catch (BadLocationException e1){}
			
	        StyleConstants.setForeground(userStyle, Color.black);
	        try { 
	        	chatTextarea.insertString(chatTextarea.getLength(), in, userStyle); 
			} catch (BadLocationException e1){}

			chat.select(doc.getLength(), doc.getLength());
			
		}

		private void updateMoveLog(String move, int pid) {

			doc = moveLog.getDocument();
	        
	        StyleConstants.setForeground(userStyle, Color.red);
	        try { 
	        	moveTextarea.insertString(moveTextarea.getLength(), "\r\nPlayer " + pid + ": ",userStyle); 
			} catch (BadLocationException e1){}

			StyleConstants.setForeground(userStyle, Color.black);
	        try { 
	        	moveTextarea.insertString(moveTextarea.getLength(), in, userStyle); 
			} catch (BadLocationException e1){}

			chat.select(doc.getLength(), doc.getLength());
		}
	}
	
	private class MenuListener implements ActionListener {
		private int id;
		public MenuListener(int a){
			this.id = a;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (id==1){
				System.exit(0);
			}	
		}
	}
	
	public class TextListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
			// Identifies the user's use of return to submit a question.
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				e.consume();
				send.doClick();
			}
			input.setFocusAccelerator(e.getKeyChar());
		}

		// Unused methods
		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}
	}
}
