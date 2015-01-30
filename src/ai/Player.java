package ai;

import java.util.ArrayList;
import java.util.Stack;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLParserFactory;
import ai.gui.GUI;
import ai.search.Agent;
import ubco.ai.GameRoom;
import ubco.ai.connection.ServerMessage;
import ubco.ai.games.GameClient;
import ubco.ai.games.GameMessage;
import ubco.ai.games.GamePlayer;

/**
 * A version of the Amazon player that is demo-ed in the API
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

	private int whiteTiles;
	private int blackTiles;
	private int bothCanReach;

	private String userName;

	private final int ROWS = 10;
	private final int COLS = 10;

	private final int WQUEEN = 1;
	private final int BQUEEN = 2;
	private final int ARROW = 3;
	
	private int playerID;
	private int oppID;
	private String role;

	private boolean isOpponentsTurn;
	private boolean finished;
	
	private int roomNumber;

	public Player(String userName, String password) {

		this.userName = userName;

		client = new GameClient(userName, password, this);
		board = new Board(ROWS, COLS);
		gui = new GUI(board, ROWS, COLS);
		parser = new XMLParser(userName);
	
	}

	public void joinServer(){
		client.roomList = getRooms();	
		gui.init();
		
		for (GameRoom g : client.roomList) {
			try {
				client.joinGameRoom(g.roomName);
				roomNumber = g.roomID;
				break;
			} catch (Exception e) {
				continue;
			}
		}
	}

	public void joinServer(String roomName){
		client.roomList = getRooms();
		gui.init();
		
		try {
			client.joinGameRoom(roomName);
			System.out.println(roomNumber);
		} catch (Exception e) {
			e.printStackTrace();
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
		
		agent = new Agent(ROWS, COLS, playerID);
		finished = false;
		inGame();
	}

	private void inGame() {

	//	do {

			if (isOpponentsTurn) {
				// TODO: Plan ahead based on possible moves
//				waitForMove();

				isOpponentsTurn = false;
				finished = isFinished();

			} else {
				// TODO: Pick a move and send it to the server
				
				//
				
				int[] move = agent.selectMove(board);
				
				String moveMessage = parser.buildMoveForServer(roomNumber, move[0], move[1], move[2], move[3], move[4], move[5]);
				client.sendToServer(moveMessage, false);

				// GUI and logic update
				updateRepresentations(move, playerID);
				isOpponentsTurn = true;
			}

//		} while (!finished);

	}

	private void waitForMove(){
		while (isOpponentsTurn){
			//isOpponentsTurn = false;
		}
	}
	
	/**
	 * Take the move coordinates and piece colour to update the board positions and GUI layout
	 * 
	 * @param move - 6 Element array for the queen move and arrow placing
	 * @param piece - Colour of the piece moved
	 */
	private void updateRepresentations(int[] move, int piece){
		gui.updateGUI(move[0], move[1], move[2], move[3], move[4], move[5], piece);
		// Update logical representation of board.
		board.freeSquare(move[0], move[1]);
		board.placeMarker(move[2], move[3], piece);
		board.placeMarker(move[4], move[5], ARROW);	
		
		if (piece == 1){
			board.updateWhitePositions(move[0], move[1], move[2], move[3]);
		} else {
			board.updateBlackPositions(move[0], move[1], move[2], move[3]);
		}	
	}

	/**
	 * Search board to detect if we are in a goal state
	 * 
	 */
	private boolean isFinished() {

		ArrayList<Pair<Integer, Integer> > wPositions = board.getWhitePositions();
		ArrayList<Pair<Integer, Integer> > bPositions = board.getBlackPositions();

		int[][] hasChecked = new int[ROWS][COLS];

		for (Pair<Integer, Integer> pair : wPositions) {
			countReachableTiles(pair, WQUEEN, hasChecked);
		}

		for (Pair<Integer, Integer> pair : bPositions) {
			countReachableTiles(pair, BQUEEN, hasChecked);
		}

		whiteTiles = 0;
		blackTiles = 0;
		bothCanReach = 0;

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				switch (hasChecked[i][j]) {
				case (1):
					whiteTiles++;
				break;
				case (2):
					blackTiles++;
				break;
				case (3):
					bothCanReach++;
				break;
				}
			}
		}

		if (blackTiles > whiteTiles + bothCanReach) {
			return true;
		} else if (whiteTiles > blackTiles + bothCanReach) {
			return true;
		}
		return false;
	}

	/****************************************************************************************
	 * This is a stack based search of the game board, we flag each tile in the grid as either
	 * belonging to White, Black, or is Neutral. If one sides score is larger than the others,
	 * plus the neutral tiles, then that side is declared the winner.
	 * 
	 * @param source
	 *            - An integer pairing (x,y) for where the amazon piece is
	 * @param player
	 *            - 1 for White, 2 for Black
	 * @param hasChecked[][]
	 * 			  - 2D integer array for mapping which pieces can reach which tiles in the grid
	 * 
	 ***************************************************************************************/
	private void countReachableTiles(Pair<Integer, Integer> source, int player,
			int[][] hasChecked) {

		int opponent;
		switch (player) {
		case (WQUEEN):
			opponent = BQUEEN;
		break;
		default:
			opponent = WQUEEN;
		}

		Stack<Pair<Integer, Integer>> stack = new Stack<>();

		hasChecked[source.getLeft()][source.getRight()] = player;

		stack.push(source);

		while (!stack.empty()) {
			// Check 8 diagonal positions.
			Pair<Integer, Integer> top = stack.pop();
			int xPos = top.getLeft();
			int yPos = top.getRight();

			// Check boundary
			if (xPos - 1 >= 0) {
				// If it is free
				if (!board.isMarked((xPos - 1), yPos)) {
					// If we haven't looked at it yet
					if (hasChecked[xPos - 1][yPos] == 0) {
						stack.push(new Pair<>(xPos - 1, yPos));
						hasChecked[xPos - 1][yPos] = player;
					} else if (hasChecked[xPos - 1][yPos] == opponent) {
						stack.push(new Pair<>(xPos - 1, yPos));
						hasChecked[xPos - 1][yPos] = ARROW;
					}
				}
			}
			if (xPos + 1 < ROWS) {
				if (!board.isMarked((xPos + 1), yPos)) {
					if (hasChecked[xPos + 1][yPos] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos + 1, yPos));
						hasChecked[xPos + 1][yPos] = player;
					} else if (hasChecked[xPos + 1][yPos] == opponent) {
						stack.push(new Pair<>(xPos + 1, yPos));
						hasChecked[xPos + 1][yPos] = ARROW;
					}
				}
			}
			if (yPos - 1 >= 0) {
				if (!board.isMarked((xPos), yPos - 1)) {
					if (hasChecked[xPos][yPos - 1] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos, yPos - 1));
						hasChecked[xPos][yPos - 1] = player;
					} else if (hasChecked[xPos][yPos - 1] == opponent) {
						stack.push(new Pair<>(xPos, yPos - 1));
						hasChecked[xPos][yPos - 1] = ARROW;
					}
				}
			}
			if (yPos + 1 < COLS) {
				if (!board.isMarked(xPos, yPos + 1)) {
					if (hasChecked[xPos][yPos + 1] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos, yPos + 1));
						hasChecked[xPos][yPos + 1] = player;
					} else if (hasChecked[xPos][yPos + 1] == opponent) {
						stack.push(new Pair<>(xPos, yPos + 1));
						hasChecked[xPos][yPos + 1] = ARROW;
					}
				}
			}
			if ((xPos + 1 < ROWS) && (yPos + 1 < COLS)) {
				if (!board.isMarked((xPos + 1), yPos + 1)) {
					if (hasChecked[xPos + 1][yPos + 1] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos + 1, yPos + 1));
						hasChecked[xPos + 1][yPos + 1] = player;
					} else if (hasChecked[xPos + 1][yPos + 1] == opponent) {
						hasChecked[xPos + 1][yPos + 1] = ARROW;
						stack.push(new Pair<>(xPos + 1, yPos + 1));
					}
				}
			}
			if ((xPos + 1 < ROWS) && (yPos - 1 >= 0)) {
				if (!board.isMarked((xPos + 1), yPos - 1)) {
					if (hasChecked[xPos + 1][yPos - 1] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos + 1, yPos - 1));
						hasChecked[xPos + 1][yPos - 1] = player;
					} else if (hasChecked[xPos + 1][yPos - 1] == opponent) {
						stack.push(new Pair<>(xPos + 1, yPos - 1));
						hasChecked[xPos + 1][yPos - 1] = ARROW;
					}
				}
			}
			if ((xPos - 1 >= 0) && (yPos + 1 < COLS)) {
				if (!board.isMarked((xPos - 1), yPos + 1)) {
					if (hasChecked[xPos - 1][yPos + 1] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos - 1, yPos + 1));
						hasChecked[xPos - 1][yPos + 1] = player;

					} else if (hasChecked[xPos - 1][yPos + 1] == opponent) {
						stack.push(new Pair<>(xPos - 1, yPos + 1));
						hasChecked[xPos - 1][yPos + 1] = ARROW;
					}
				}
			}
			if ((xPos - 1 >= 0) && (yPos - 1 >= 0)) {
				if (!board.isMarked((xPos - 1), yPos - 1)) {
					if (hasChecked[xPos - 1][yPos - 1] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos - 1, yPos - 1));
						hasChecked[xPos - 1][yPos - 1] = player;
					} else if (hasChecked[xPos - 1][yPos - 1] == opponent) {
						stack.push(new Pair<>(xPos - 1, yPos - 1));
						hasChecked[xPos - 1][yPos - 1] = ARROW;
					}
				}
			}
		}
	}

	public ArrayList<GameRoom> getRooms() {
		ArrayList<GameRoom> rooms = client.getRoomLists();
//		for (GameRoom g : rooms) {
//			System.out.println(g.roomID + " " + g.roomName);
//		}
		return rooms;
	}

	@Override
	public boolean handleMessage(String message) throws Exception {
		gui.addServerMessage("Server message: ", message);
		return false;
	}

	@Override
	public boolean handleMessage(GameMessage message) throws Exception {

		gui.addServerMessage("Server other message: ", message.toString());

		IXMLParser iParser = XMLParserFactory.createDefaultXMLParser();
		IXMLReader reader = StdXMLReader.stringReader(message.toString());
		iParser.setReader(reader);
		IXMLElement xml = (IXMLElement) iParser.parse();

		String answer = parser.handleXML(xml);
		
		if (answer.equals(GameMessage.ACTION_GAME_START)){
			this.role = parser.getUserInfo(xml);
			if (!role.equals("W") && !role.equals("B")){
				System.out.println("Spectator of match.");
				return false;
			}
			System.out.println("Starting match.");
			startGame();
			
		} else if (answer.equals(GameMessage.ACTION_MOVE)){
			System.out.println("Opponent move recieved.");
			
			// Get the queen move and arrow marker.
			int[] move = parser.getOpponentMove(xml);		
			// Update GUI
			updateRepresentations(move, oppID);
		}

		return true;
	}
	
	public static void main(String[] args) {
		Player player = new Player("Bot-1.0001", "54321");
		
		if (args.length == 0){
			player.joinServer();
		} else {
			player.joinServer(args[0] + " " + args[1]);
		}
		
//		Player p2 = new Player("Bot-3-0001", "54321");
//		p2.joinServer("Jackpine lake");
		
	}
}
