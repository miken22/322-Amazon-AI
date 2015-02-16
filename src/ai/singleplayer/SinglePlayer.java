package ai.singleplayer;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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

import ai.Board;
import ai.Pair;
import ai.Utility;
import ai.gui.Cells;
import ai.search.Agent;
import ai.search.CountReachableTilesHeuristic;
import ai.search.MinDistanceHeuristic;

/**
 * Two bots playing game of amazons
 * 
 * 
 * @author Mike Nowicki
 *
 */
public class SinglePlayer {

	private JFrame frame;

	private JScrollPane scrollChat;
	private JScrollPane scrollLog;

	private JTextPane chat;
	private JTextPane moveLog;
	private JTextArea input;

	private Style userStyle;
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
	
	private int whiteTiles;
	private int blackTiles;
	private int bothCanReach;
	
	private Board board;
	private Cells[][] guiBoard;
	
	private boolean playerTurn = false;
	private boolean finished = false;
	
	private Agent agent = new Agent(1);
	private Agent agent2 = new Agent(2);
	
	/**
	 * 2 element array, index 0 for white score, 1 for black score
	 */
	public final int WQUEEN = 1;
	public final int BQUEEN = 2;
	public final int ARROW = 3;
	public final int FREE = -1;	

	public static void main(String[] args){
		SinglePlayer sp = new SinglePlayer(10,10);
		sp.init();	
	}

					/***************
					 * Main program*
					 ***************/
	
	public SinglePlayer(int row, int col){
		board = new Board(row,col);
		guiBoard = new Cells[row][col];		
		rows = row;
		columns = col;
		board.initialize();
	}

	public void init(){
		createFrame();
		drawBoard();
		initializePositions();
	}	

	private void createFrame(){
		
		Border b = new LineBorder(Color.LIGHT_GRAY,1,true);
		Container c;

		frame = new JFrame("Game of Amazons");
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
		chat.addStyle("agentstyle",null);

		try {
			font = Font.createFont(0,this.getClass().getResourceAsStream("/Trebuchet MS.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		font = font.deriveFont(Font.BOLD,15);

		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(725, 675);
		frame.setResizable(false);
		frame.setJMenuBar(menu);

		menu.setBackground(new Color(244,244,244));
		menu.add(file);
		file.add(exit);

		chat.setEditable(false);
		chat.setContentType("text/html");
		chat.setBorder(b);
		chat.setBackground(new Color(252,252,252));
		chat.setFont(font);

		scrollChat.setBounds(550, 250, 170, 300);
		scrollChat.setBackground(new Color(252,252,252));
		scrollChat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Chat History:"));

		moveLog.setEditable(false);
		moveLog.setContentType("text/html");
		moveLog.setBorder(b);
		moveLog.setBackground(new Color(252,252,252));
		moveLog.setFont(font);

		scrollLog.setBounds(550, 0, 170, 250);
		scrollLog.setBackground(new Color(252,252,252));
		scrollLog.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Move History:"));

		input.setLineWrap(true);
		input.setBorder(b);
		input.setFont(font);
		input.setBounds(0, 550, 550, 100);
		input.setBackground(new Color(252,252,252));
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
		c.add(scrollChat);
		c.add(scrollLog);
		c.add(input);
		c.add(send);
		c.add(clear);
		
	}

	private void drawBoard(){

		Color light = new Color(219, 169, 1);
		Color dark = new Color(255, 229, 204);	

		for (int i = rows-1; i >= 0; i--) {

			for (int j = columns-1; j >= 0; j--) {

				Cells cell;

				if (i % 2 == 0){
					if (j % 2 == 0){
						cell = new Cells(dark);
					} else {
						cell = new Cells(light);
					}
				} else {
					if (j % 2 == 0){
						cell = new Cells(light);
					} else {
						cell = new Cells(dark);
					}
				}

				cell.setBounds(j * 50 + 50, 500 - i * 50, 50, 50);
				cell.setBorder(new LineBorder(Color.LIGHT_GRAY,1,true));
				cell.setXY(i,j);	// This is weird but it has to be backwards because the gui is tilted, is only for reference anyways never used.
				guiBoard[i][j] = cell;
				frame.add(cell);
			}
		}

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
		
		if (!playerTurn){
			playGame();
		}
	}
	
	/**
	 * Have two agents play against each other
	 */
	private void playGame(){

		boolean whiteTurn = true;
		
		agent.setupHeuristic(new MinDistanceHeuristic(1));
		agent2.setupHeuristic(new MinDistanceHeuristic(2));
		
//		agent.setupHeuristic(new BlindFunction(1));
//		agent2.setupHeuristic(new BlindFunction(2));
		
		while (true){
			if (whiteTurn){
				try{
					int[] move = agent.selectMove(board);
					makeMove(board, move, 1);	
					whiteTurn = false;

					String action = Utility.getColumnLetter(move[1]) + "" + move[0] + "-" + Utility.getColumnLetter(move[3]) + move[2] + "-" + Utility.getColumnLetter(move[5]) + move[4];
					System.out.println("White: " + action);
					updateMoveLog(action, 1);
					
					agent.checkIfFinished();
					
				} catch (NullPointerException e){
					finished = true;
					break;
				}
			} else {

				try{
					int[] move = agent2.selectMove(board);
					makeMove(board, move, 2);
					whiteTurn = true;

					String action = Utility.getColumnLetter(move[1]) + "" + move[0] + "-" + Utility.getColumnLetter(move[3]) + "" + move[2] + "-" + Utility.getColumnLetter(move[5]) + "" + move[4];
					System.out.println("Black: " + action);
					updateMoveLog(action, 2);
					
					agent2.checkIfFinished();
					
				} catch (NullPointerException e){
					finished = true;
					break;
				}
			}
			finished = isFinished();
		}
		endGame();
		
	}
	
	private void makeMove(Board board, int[] move, int player){
		board.freeSquare(move[0], move[1]);
		board.placeMarker(move[2], move[3], player);
		board.placeMarker(move[4], move[5], ARROW);	

		guiBoard[move[0]][move[1]].setFree();
		guiBoard[move[4]][move[5]].setArrow();

		
		if (player == 1){
			board.updateWhitePositions(move[0], move[1], move[2], move[3]);
			guiBoard[move[2]][move[3]].setWQueen();
		} else {
			board.updateBlackPositions(move[0], move[1], move[2], move[3]);
			guiBoard[move[2]][move[3]].setBQueen();
		}
		

	}
	
	private void endGame(){
		System.out.println("Game Over!");
		addText("Game over!");
//		frame.dispose();
//		System.exit(0);
	}

	private void addText(String message){
		Document doc = chat.getDocument();		
		StyleConstants.setForeground(userStyle, Color.red);
		try { 
			chatTextarea.insertString(chatTextarea.getLength(), "\r\nSystem: ",userStyle); 
		} catch (BadLocationException e1){}

		StyleConstants.setForeground(userStyle, Color.black);
		try { 
			chatTextarea.insertString(chatTextarea.getLength(), message, userStyle); 
		} catch (BadLocationException e1){}
		chat.select(doc.getLength(), doc.getLength());
	}
	
	/**
	 * Search board starting from each of the players pieces. If there is no path to any opposing colour then the game
	 * is over.
	 * 
	 * @param player - Which players pieces to start from to look for the goal.
	 */
	private boolean isFinished() {	

		ArrayList<Pair<Integer, Integer> > wPositions = board.getWhitePositions();
		ArrayList<Pair<Integer, Integer> > bPositions = board.getBlackPositions();

		int[][] hasChecked = new int[rows][columns];
	
		for (Pair<Integer, Integer> pair : wPositions){
			// Reach opposing amazon using legal moves then the game is not over.
			hasChecked = Utility.countReachableTiles(board, pair, WQUEEN, hasChecked);		}
		
		for (Pair<Integer, Integer> pair : bPositions){
			// Reach opposing amazon using legal moves then the game is not over.
			hasChecked = Utility.countReachableTiles(board, pair, BQUEEN, hasChecked);
		}
		
		whiteTiles = 4;
		blackTiles = 4; 
		bothCanReach = 0;
		
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				switch(hasChecked[i][j]){
					case(1):
						whiteTiles++;
						break;
					case(2):
						blackTiles++;
						break;
					case(3):
						bothCanReach++;
						break;
				}
			}
		}
		
		System.out.println("Black: " + blackTiles + " White: " + whiteTiles + " Both: " + bothCanReach);
		
		if (blackTiles > whiteTiles + bothCanReach){
			System.out.println("Black wins");
			System.out.println("Black: " + blackTiles + " White: " + (whiteTiles + bothCanReach));
			return true;
		} else if (whiteTiles > blackTiles + bothCanReach){
			System.out.println("White wins!");
			System.out.println("Black: " + (blackTiles  + bothCanReach) + " White: " + (whiteTiles));
			return true;
		} 
		
		// If we reach this point we have examined every component
		return false;
	}
	
	private void updateMoveLog(String move, int pid) {
		Document doc = moveLog.getDocument();

		StyleConstants.setForeground(userStyle, Color.red);
		try { 
			moveTextarea.insertString(moveTextarea.getLength(), "\r\nPlayer " + pid + ": ",userStyle); 
		} catch (BadLocationException e1){}

		StyleConstants.setForeground(userStyle, Color.black);
		try { 
			moveTextarea.insertString(moveTextarea.getLength(), move, userStyle); 
		} catch (BadLocationException e1){}

		moveLog.select(doc.getLength(), doc.getLength());
	}
	
}
