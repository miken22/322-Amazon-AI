package single;

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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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

import ai.Agent;
import ai.Board;
import ubco.ai.games.GameMessage;
import ubco.ai.games.GamePlayer;

public class SinglePlayer extends JFrame implements GamePlayer {

	/**
	 * 
	 */
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

	private Font font;
	
	private int rows;
	private int columns;
	private Board board;
	
	private Cells[][] guiBoard;
	private boolean player1Turn = true;
	private boolean player2Turn = false;
	private boolean useAI = false;
		
	
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
	
	private void createFrame(){
		
		Container c;
		
		frame = new JFrame();		
		menu = new JMenuBar();
		
		file = new JMenu("File");
		exit = new JMenuItem("Exit");
		
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
		
		frame = new JFrame("TravelBot Chat Agency");
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(660, 650);
		frame.setResizable(false);
		frame.setJMenuBar(menu);
		
		menu.setBackground(new Color(244,244,244));
		menu.add(file);
		file.add(exit);
				
		chat.setEditable(false);
		chat.setContentType("text/html");
		chat.setBorder(b);
		chat.setBackground(new Color(252,252,252));
		font = font.deriveFont(Font.PLAIN,10);
		chat.setFont(font);

		scrollChat.setBounds(500, 250, 150, 250);
		scrollChat.setBackground(new Color(252,252,252));
		scrollChat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Chat History:"));
		
		moveLog.setEditable(false);
		moveLog.setContentType("text/html");
		moveLog.setBorder(b);
		moveLog.setBackground(new Color(252,252,252));
		font = font.deriveFont(Font.PLAIN,12);
		moveLog.setFont(font);
		
		scrollLog.setBounds(500, 0, 150, 250);
		scrollLog.setBackground(new Color(252,252,252));
		scrollLog.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Move History:"));
		
		input.setLineWrap(true);
		input.setBorder(b);
		font = font.deriveFont(Font.PLAIN,12);
		input.setFont(font);
		input.setBounds(0, 500, 500, 150);
		input.setBackground(new Color(252,252,252));
		input.addKeyListener(new TextListener());
		
		chatTextarea = chat.getStyledDocument();
		moveTextarea = moveLog.getStyledDocument();
		
		send.setBounds(500, 500, 150, 50);
		send.setBorder(b);
		send.setBackground(new Color(250,250,250));
		send.setFocusPainted(false);
		clear.setBounds(500, 550, 150, 50);
		clear.setBorder(b);
		clear.setBackground(new Color(250,250,250));
		clear.setFocusPainted(false);
		
		c = frame.getContentPane();
		c.add(scrollChat);
		c.add(scrollLog);
		c.add(input);
		c.add(send);
		c.add(clear);
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		input.requestFocus();
		
	}
	
	private void drawBoard(){
				
		Color c1 = new Color(219, 169, 1);
		Color c2 = new Color(255, 229, 204);	
		
        for (int j = 0; j < columns; j++) {
           
        	for (int i = 0; i < rows; i++) {
            	
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
            	
            	cell.setBounds(i * 50, j * 50, 50, 50);
            	cell.setBorder(new LineBorder(Color.LIGHT_GRAY,1,true));
            	cell.setXY(i, j);
            	guiBoard[i][j] = cell;
            	frame.add(cell);
            }
        }
        
        frame.repaint();
		
	}
	
	public void initializePositions(){
		
		guiBoard[0][3].setWQueen();
		guiBoard[3][0].setWQueen();
		guiBoard[3][9].setWQueen();
		guiBoard[0][6].setWQueen();
		
		guiBoard[6][0].setBQueen();
		guiBoard[9][3].setBQueen();
		guiBoard[9][6].setBQueen();
		guiBoard[6][9].setBQueen();	
		
	}
	
	public void runTwoPlayer(){
		
		
		
		boolean finished = false;
		
		while (!finished){
			
			player1Turn = true;
			
			while(player1Turn){	}
			
			player2Turn = true;
			
			while(player2Turn){	}
						
			if(isFinished()){
				determineWinner();
				finished = true;
			}
			
		}
		
	}
	
	// Count the amount of available space for each player, largest area = winner
	private void determineWinner() {
		
	}

	// Need to check if each amazon is contained within a specific region
	private boolean isFinished() {

		return false;
	}

	public void runGame(){
		
		//boolean finished = false;
		//Agent agent = new Agent();
		
	}
	
	public void startGame(){
		if (useAI){
			runGame();
		} else {
			runTwoPlayer();
		}
	}
	
	public SinglePlayer(int row, int col){

		board = new Board(row,col);
		guiBoard = new Cells[row][col];
		
		rows = row;
		columns = col;
		
		createFrame();
		drawBoard();
		initializePositions();		
	}

	
	
	public static void main(String[] args){
		
		SinglePlayer sp = new SinglePlayer(10,10);
		
		sp.startGame();
		
	}
	
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
				}

				input.setText("");
				b.setBackground(new Color(250,250,250));
			}
			
			input.requestFocus();
		}
		
		private void handleMove(){
			
			String from;
			String to;
			String arrow;
			
			String[] splitIn = in.split("-");
			from = splitIn[0];
			
			splitIn = splitIn[1].split("/");
			
			to = splitIn[0];
			arrow = splitIn[1];
			
			// Get information about source of move
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
				if (!board.isMarked(fRow,fCol)){
					JOptionPane.showMessageDialog(frame,"Must start with your own piece.", "Invalid", JOptionPane.WARNING_MESSAGE);
					return;
				} else {
					// One is numeric rep of White Queen, make sure we start from a white queen
					if (board.getPiece(fRow, fCol) != 1){
						JOptionPane.showMessageDialog(frame,"Must start with your own piece.", "Invalid", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				
				// Verify move is valid (straight/diagonal, no obstructions)
				if (!moveIsValid(fRow,fCol,tRow,tCol)){
					return;
				}

				// Use same logic to validate arrow throws.
				if (!moveIsValid(tRow,tCol,aRow,aCol)){
					return;
				}
				
			
				board.freeSquare(fRow,fCol);
				board.placeMarker(tRow,tCol, 1);
				board.placeMarker(aRow, aCol, 3);
				
				guiBoard[fRow][fCol].setFree();
				guiBoard[tRow][tCol].setWQueen();
				guiBoard[aRow][aCol].setArrow();
							
				
				updateMoveLog(in,1);
				
				
				frame.repaint();
				
				player1Turn = false;
			
			} else {
				
				// Check starting from an owned piece
				if (!board.isMarked(fRow,fCol)){
					JOptionPane.showMessageDialog(frame,"Must start with your own piece.", "Invalid", JOptionPane.WARNING_MESSAGE);
					return;
				} else {
					// One is numeric rep of White Queen, make sure we start from a white queen
					if (board.getPiece(fRow, fCol) != 2){
						JOptionPane.showMessageDialog(frame,"Must start with your own piece.", "Invalid", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				
				// Verify move is valid (straight/diagonal, no obstructions)
				if (!moveIsValid(fRow,fCol,tRow,tCol)){
					return;
				}

				// Use same logic to validate arrow throws.
				if (!moveIsValid(tRow,tCol,aRow,aCol)){
					return;
				}
				
				board.freeSquare(fRow,fCol);
				board.placeMarker(tRow,tCol, 2);
				board.placeMarker(aRow, aCol, 3);
				
				guiBoard[fRow][fCol].setFree();
				guiBoard[tRow][tCol].setBQueen();
				guiBoard[aRow][aCol].setArrow();

				updateMoveLog(in,2);
				
				frame.repaint();
				
				player2Turn = false;
				
			}
			
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

		private boolean moveIsValid(int sX, int sY, int dX, int dY){
			
			if ((sX != dX) && (sY != dY)){
				if (Math.abs(sX - dX) != Math.abs(sY - dY)){
					JOptionPane.showMessageDialog(frame,"Illegal move.", "Invalid", JOptionPane.WARNING_MESSAGE);
					return false;
				}
			}
			
			// Check horizontal move, make sure no obstacles
			if (sX == dX){
				// No move, just throw arrow
				if (sY == dY){
					return true;
				}
				
				// Quick swap
				if (sX > dX){
					int temp = sX;
					sX = dX;
					dX = temp;				
				}			
				// Check
				for (int i = sX; i <= dX; i++){
					if (board.isMarked(i, dY)){
						JOptionPane.showMessageDialog(frame,"Illegal move.", "Invalid", JOptionPane.WARNING_MESSAGE);
						return false;
					}
				}
				return true;
			}
			
			// Check vertical move, same thing
			if (sY == dY){
				// Quick swap
				if (sY > dY){
					int temp = sY;
					sY = dY;
					dY = temp;				
				}			
				// Check
				for (int i = sY; i <= dY; i++){
					if (board.isMarked(dX, i)){
						JOptionPane.showMessageDialog(frame,"Illegal move.", "Invalid", JOptionPane.WARNING_MESSAGE);
						return false;
					}
				}
				return true;
			}
			
			// Check diagonal
			
			if (sX > dX){
				int temp = sX;
				sX = dX;
				dX = temp;	
			}
			if (sY > dY){
				int temp = sY;
				sY = dY;
				dY = temp;				
			}
			
			while (sX != dX || sY != dY){
				sX++;
				sY++;
				if (board.isMarked(sX, sY)){
					JOptionPane.showMessageDialog(frame,"Illegal move.", "Invalid", JOptionPane.WARNING_MESSAGE);
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
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {	
		}
	}
}
