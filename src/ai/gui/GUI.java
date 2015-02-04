package ai.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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

public class GUI {

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

	private Cells[][] guiBoard;
	
	public final int WQUEEN = 1;
	public final int BQUEEN = 2;
	public final int ARROW = 3;
	public final int FREE = -1;	

	public GUI(Board board, int rows, int columns){
		this.rows = rows;
		this.columns = columns;
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
		agentStyle = chat.addStyle("agentstyle", null);

		try {
			font = Font.createFont(0,this.getClass().getResourceAsStream("/Trebuchet MS.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		font = font.deriveFont(Font.PLAIN,13);

		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(725, 700);
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

		scrollChat.setBounds(0, 530, 720, 120);
		scrollChat.setBackground(new Color(252,252,252));
		scrollChat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Message History:"));

		moveLog.setEditable(false);
		moveLog.setContentType("text/html");
		moveLog.setBorder(b);
		moveLog.setBackground(new Color(252,252,252));
		moveLog.setFont(font);

		scrollLog.setBounds(522, 0, 200, 530);
		scrollLog.setBackground(new Color(252,252,252));
		scrollLog.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1,true), "Move History:"));	

		chatTextarea = chat.getStyledDocument();
		moveTextarea = moveLog.getStyledDocument();

		exit.addActionListener(new MenuListener(1));
		
		
		c = frame.getContentPane();
		c.add(scrollChat);
		c.add(scrollLog);
		c.add(input);
		c.add(send);
		c.add(clear);

	}
	
	/**
	 * Place cells onto board and labels
	 */
	private void drawBoard(){

		Color light = new Color(219, 169, 1);
		Color dark = new Color(255, 229, 204);
		
		guiBoard = new Cells[rows][columns];

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

				cell.setBounds(j * 50 + 22, 475 - i * 50, 50, 50);
				cell.setBorder(new LineBorder(Color.LIGHT_GRAY,1,true));
				cell.setXY(i,j);	// This is weird but it has to be backwards because the gui is tilted, is only for reference anyways never used.
				guiBoard[i][j] = cell;
				frame.add(cell);
			}
		}

		char[] letters = {'a','b','c','d','e','f','g','h','i','j'};
		for (int i = 0; i < columns; i++){
			JLabel label = new JLabel();
			label.setBackground(Color.BLACK);
			label.setBounds(35 + i * 50,-10, 50,50);
			label.setText("" + letters[i]);
			frame.add(label);
		}

		// Columns are used as integers but map to a char value
		for (int i = rows-1; i >= 0; i--){
			JLabel label = new JLabel();
			label.setBackground(Color.BLACK);
			label.setBounds(5, 480-i*50, 50,40);
			label.setText("" + i);
			frame.add(label);
		}

		frame.getContentPane().repaint();
		frame.repaint();

	}

	/**
	 * Put amazons on their initial tiles and show the GUI
	 */
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

	/**
	 * Once a valid move has been made we update the GUI to reflect the change
	 * 
	 * @param fRow - The row the piece started at.
	 * @param fCol - The column the piece started at.
	 * @param tRow - The row the piece goes to.
	 * @param tCol - The column the piece goes to.
	 * @param aRow - The row where the arrow lands.
	 * @param aCol - The column where the arrow lands.
	 * @param pieceColour - 1 for White, 2 for Black.
	 */
	public void updateGUI(int fRow, int fCol, int tRow, int tCol, int aRow, int aCol, int pieceColour){

		guiBoard[fRow][fCol].setFree();
		guiBoard[aRow][aCol].setArrow();
		
		if (pieceColour == WQUEEN){
			guiBoard[tRow][tCol].setWQueen();
		} else {
			guiBoard[tRow][tCol].setBQueen();
		}	
	}
	
	/**
	 * Put the message from the server onto the GUI
	 * @param type - Server message type.
	 * @param message - Server message.
	 */
	public void addServerMessage(String type, String message) {
		Document doc = chat.getDocument();
		input.setText("");
		
		StyleConstants.setForeground(agentStyle, Color.GREEN);
		try { 
			chatTextarea.insertString(chatTextarea.getLength(), "\r\n" + type + " ",agentStyle); 
		} catch (BadLocationException e1){}

		StyleConstants.setForeground(agentStyle, Color.black);
		try { 
			chatTextarea.insertString(chatTextarea.getLength(), message, agentStyle); 
		} catch (BadLocationException e1){}

		chat.select(doc.getLength(), doc.getLength());

	}
	
	public void updateMoveLog(String player, String move){
		Document doc = moveLog.getDocument();

		StyleConstants.setForeground(userStyle, Color.red);
		try { 
			moveTextarea.insertString(moveTextarea.getLength(), "\r\n" + player + " ",userStyle); 
		} catch (BadLocationException e1){}

		StyleConstants.setForeground(userStyle, Color.black);
		try { 
			moveTextarea.insertString(moveTextarea.getLength(), move, userStyle); 
		} catch (BadLocationException e1){}

		chat.select(doc.getLength(), doc.getLength());
	
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

	public void destroy() {
		frame.dispose();
	}

	public void removeArrow(int i, int j) {
		
		guiBoard[i][j].setFree();
		
	}
}
