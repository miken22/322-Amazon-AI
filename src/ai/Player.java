package ai;

import java.util.ArrayList;
import java.util.Scanner;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLParserFactory;
import ai.gui.GUI;
import ai.search.Agent;
import ai.search.MinDistanceHeuristic;
import ubco.ai.GameRoom;
import ubco.ai.games.GameClient;
import ubco.ai.games.GameMessage;
import ubco.ai.games.GamePlayer;

/**
 * An implementation of an Amazons player
 * 
 * @author Mike Nowicki
 *
 */

public class Player implements GamePlayer {

	private GameClient client;
	private Agent agent;
	private Board board;
	private GUI gui;
	private XMLParser parser;

	private final int ROWS = 10;
	private final int COLS = 10;
	private final byte ARROW = 3;

	private byte playerID;
	private byte oppID;
	private String role;

	private boolean isOpponentsTurn;

	private int roomNumber;

	public Player(String userName, String password) {

		client = new GameClient(userName, password, this);
		board = new Board(ROWS, COLS);
		gui = new GUI(ROWS, COLS);
		parser = new XMLParser(userName);
		
		board.initialize();

	}
	
	/**
	 * Simple method that displays the rooms, their array index, and number
	 * of users. Select the room to join by typing the index to join, launches
	 * GUI after.
	 *  
	 */
	public void joinServer(){
		
		gui.init();
		
		client.roomList = getRooms();	

		int index = 0;
		for (GameRoom g : client.roomList) {
			System.out.print(g.roomName + " [" + index++ + "] ");
			System.out.println("Number of users: " + g.userCount);
		}
		System.out.print("Pick a room number to join: ");
		
		Scanner in = new Scanner(System.in);
		int number = in.nextInt();
		in.close();
		
		try {
			GameRoom room = client.roomList.get(number);
			this.roomNumber = room.roomID;
			client.joinGameRoom(room.roomName);
		} catch (Exception e) {
			System.out.print("Invalid room number, exiting!");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void startGame() {

		System.out.println("Game started");

		if (role.equals("W")) {
			isOpponentsTurn = false;
			playerID = 1;
			oppID = 2;
		} else if (role.equals("B")) {
			isOpponentsTurn = true;
			oppID = 1;
			playerID = 2;
		} else {
			System.out.println("Something went wrong detecting our role");
		}

		agent = new Agent(playerID);
		agent.setupHeuristic(new MinDistanceHeuristic(playerID));

		if (playerID == 1) {
			pickMove();
		}
	}

	private void pickMove() {

		
		if (!isOpponentsTurn) {
			System.out.println("Agents move:");

			try{
				
				byte[] move = agent.selectMove(board);

				String moveMessage = parser.buildMoveForServer(roomNumber, move[0], move[1], move[2], move[3], move[4], move[5]);
				client.sendToServer(moveMessage, true);

				// GUI and logic update
				String action = Utility.getColumnLetter(move[1]) + "" + move[0] + "-" + Utility.getColumnLetter(move[3]) + "" + move[2] + "-" + Utility.getColumnLetter(move[5]) + "" + move[4];

				updateRepresentations(move, playerID);
				gui.updateMoveLog("Agent: ", action);
				isOpponentsTurn = true;

				//agent.checkIfFinished();
				
				
			} catch (NullPointerException e){
				endGame();
			}	
		}
		return;
		
	}

	/**
	 * Method to handle goal state
	 */
	private void endGame(){
		System.out.println("Game over.");
	}

	/**
	 * Take the move coordinates and piece colour to update the board positions and GUI layout
	 * 
	 * @param move - 6 Element array for the queen move and arrow placing
	 * @param piece - Colour of the piece moved
	 */
	private void updateRepresentations(byte[] move, int piece){
		
		gui.updateGUI(move[0], move[1], move[2], move[3], move[4], move[5], piece);
		// Update logical representation of board.
		board.freeSquare(move[0], move[1]);
		board.placeMarker(move[2], move[3], (byte) piece);
		board.placeMarker(move[4], move[5], (byte) ARROW);	

		if (piece == 1){
			board.updateWhitePositions(move[0], move[1], move[2], move[3]);
		} else {
			board.updateBlackPositions(move[0], move[1], move[2], move[3]);
		}	
	}

	public ArrayList<GameRoom> getRooms() {
		ArrayList<GameRoom> rooms = client.getRoomLists();
		return rooms;
	}

	@Override
	public boolean handleMessage(String message) throws Exception {
		System.out.println("Server message: " + message);
		return false;
	}

	@Override
	public boolean handleMessage(GameMessage message) throws Exception {

		IXMLParser iParser = XMLParserFactory.createDefaultXMLParser();
		IXMLReader reader = StdXMLReader.stringReader(message.toString());
		iParser.setReader(reader);
		IXMLElement xml = (IXMLElement) iParser.parse();

		String answer = parser.handleXML(xml);

		if (answer.equals(GameMessage.ACTION_GAME_START)){
			
			System.out.println(message.toString());
			
			this.role = parser.getUserInfo(xml);
			if (!role.equals("W") && !role.equals("B")){
				System.out.println("Spectator of match.");
				return false;
			}
			System.out.println("Starting match.");
			this.startGame();

		} else if (answer.equals(GameMessage.ACTION_MOVE)){

			System.out.println("Opponent move recieved.");
			System.out.println(message.toString());
			// Get the queen move and arrow marker.
			byte[] move = parser.getOpponentMove(xml);	
			isOpponentsTurn = false;	
			
			// Update GUI
			String action = parser.formatMove(xml);
			updateRepresentations(move, oppID);
			gui.updateMoveLog("Opponent: ", action);
			// Start game play method
			pickMove();
			
		}

		return true;
	}

	public static void main(String[] args) {
		Player player = new Player("Bot-2.0001", "54321");
		player.joinServer();
	}
}
