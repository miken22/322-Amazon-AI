package ai.singleplayer;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ai.Board;
import ai.Utility;
import ai.gui.Cells;
import ai.search.Agent;
import ai.search.GameTreeSearch;
import ai.search.MinDistanceHeuristic;
import javafx.scene.input.KeyCode;

/**
 * Two bots playing game of amazons, ugly code as it's
 * mostly written in one file. For testing only
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
	
	private Board board;
	private Cells[][] guiBoard;

	private boolean waiting = true;
	
	private boolean playerTurn = false;
//	private boolean finished = false;
	
	private Agent agent = new Agent((byte)2);

	private GameTreeSearch gts = new GameTreeSearch();
	
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
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				input.requestFocus();
				handleMove();
			}
		});
		
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
		guiBoard[0][3].setWhitePiece();
		guiBoard[0][6].setWhitePiece();
		guiBoard[3][0].setWhitePiece();
		guiBoard[3][9].setWhitePiece();

		guiBoard[6][0].setBlackPiece();
		guiBoard[6][9].setBlackPiece();
		guiBoard[9][3].setBlackPiece();
		guiBoard[9][6].setBlackPiece();	
	
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		input.requestFocus();

		agent.setupHeuristic(new MinDistanceHeuristic((byte)2));
		
		if (!playerTurn){
			playGame(true);
		}
	}
	
	/**
	 * Have user play against the agent
	 */
	private void playGame(boolean turn) {

		playerTurn = turn;

		if (turn) {

		} else {

			System.out.println("made it");

			byte[] move = agent.selectMove(board);

			// This signals no valid moves
			if (move[0] == move[2] &&  move[1] == move[3]) {
				endGame();
			}

			String action = Utility.getColumnLetter(move[1]) + "" + move[0] + "-" + Utility.getColumnLetter(move[3]) + "" + move[2] + "-" + Utility.getColumnLetter(move[5]) + "" + move[4];
			System.out.println(action);
			updateMoveLog(action, 2);

			makeMove(board, move, 2);
			playerTurn = true;
		}

	}

	private void endGame() {
		addText("Game over, computer ran out of moves!");
	}

	private void makeMove(Board board, byte[] move, int player){
		board.freeSquare(move[0], move[1]);
		board.placeMarker(move[2], move[3], (byte) player);
		board.placeMarker(move[4], move[5], (byte) ARROW);	

		guiBoard[move[0]][move[1]].setFree();
		guiBoard[move[4]][move[5]].setArrow();
		
		if (player == 1){
			board.updateWhitePositions(move[0], move[1], move[2], move[3]);
			guiBoard[move[2]][move[3]].setWhitePiece();
		} else {
			board.updateBlackPositions(move[0], move[1], move[2], move[3]);
			guiBoard[move[2]][move[3]].setBlackPiece();
		}
		

	}

	private void handleMove() {
		String in = input.getText();

		String from;
		String to;
		String arrow;

		String[] splitIn = in.split("-");
		from = splitIn[0];
		to = splitIn[1];
		arrow = splitIn[2];

		// Get information about source of move
		char fromRow = from.toLowerCase().charAt(0);
		int fCol = Utility.getColumn(fromRow);
		int fRow = from.charAt(1)-48;

		char toRow = to.toLowerCase().charAt(0);
		int tCol = Utility.getColumn(toRow);
		int tRow = to.charAt(1)-48;

		char arrRow = arrow.toLowerCase().charAt(0);
		int aCol = Utility.getColumn(arrRow);
		int aRow = arrow.charAt(1)-48;

		byte[] move = new byte[6];
		move[0] = (byte)fRow;
		move[1] = (byte)fCol;
		move[2] = (byte)tRow;
		move[3] = (byte)tCol;
		move[4] = (byte)aRow;
		move[5] = (byte)aCol;

		if (board.getPiece(fRow, fCol) != WQUEEN){
			JOptionPane.showMessageDialog(frame,"Must start with your own piece.", "Invalid", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (!gts.moveIsValid(board, move[0], move[1], move[2], move[3], 1, false)) {
			JOptionPane.showMessageDialog(null, "Invalid move chosen");
			return;
		}

		if (!gts.moveIsValid(board, move[2], move[3], move[4], move[5], 1, true)) {
			JOptionPane.showMessageDialog(null, "Invalid move chosen");
			return;
		}

		makeMove(board, move, 1);

		String action = Utility.getColumnLetter(move[1]) + "" + move[0] + "-" + Utility.getColumnLetter(move[3]) + move[2] + "-" + Utility.getColumnLetter(move[5]) + move[4];
		System.out.println("White: " + action);
		updateMoveLog(action, 1);

		input.setText("");
		playGame(false);
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
