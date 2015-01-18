package ai;

import java.util.ArrayList;
import java.util.Stack;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLParserFactory;
import net.n3.nanoxml.XMLWriter;
import ai.gui.GUI;
import ai.search.Agent;
import ubco.ai.GameRoom;
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
	
	private boolean isOpponentsTurn;
	private boolean finished;

	public Player(String userName, String password) {
		
		this.userName = userName;

		client = new GameClient(userName, password, this);
		client.roomList = getRooms();
		client.getUserID();

		board = new Board(ROWS, COLS);
		
		gui = new GUI(board, ROWS, COLS);
		gui.init();

		parser = new XMLParser();

		for (GameRoom g : client.roomList) {
			try {
				client.joinGameRoom(g.roomName);
				break;
			} catch (Exception e) {
				continue;
			}
		}

		if (client.isRunning()){
			startGame(1);
		}
		
		// if game starts

		// TODO: Build GUI, pass board to constructor
	}

	public void startGame(int playerNumber) {

		agent = new Agent(board, ROWS, COLS, playerNumber);
		
		if (playerNumber == 1) {
			isOpponentsTurn = false;
		} else {
			isOpponentsTurn = true;
		}

		finished = false;

		inGame();

	}

	private void inGame() {

		do {

			if (isOpponentsTurn) {
				// TODO: Plan ahead based on possible moves
				finished = isFinished();
				
				waitForMove();
				
				
			} else {
				// TODO: Pick a move and send it to the server
				agent.selectMove();
			}

		} while (!finished);

	}
	
	private void waitForMove(){
		
		while (isOpponentsTurn){
			
		}
		
	}

	/**
	 * Search board starting from each of the players pieces. If there is no
	 * path to any opposing colour then the game is over.
	 * 
	 * @param player
	 *            - Which players pieces to start from to look for the goal.
	 */
	private boolean isFinished() {

		ArrayList<Pair<Integer, Integer>> wPositions = board
				.getWhitePositions();
		ArrayList<Pair<Integer, Integer>> bPositions = board
				.getBlackPositions();

		int[][] hasChecked = new int[ROWS][COLS];

		for (Pair<Integer, Integer> pair : wPositions) {
			// Reach opposing amazon using legal moves then the game is not
			// over.
			countReachableTiles(pair, WQUEEN, hasChecked);
		}

		for (Pair<Integer, Integer> pair : bPositions) {
			// Reach opposing amazon using legal moves then the game is not
			// over.
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

		// If we reach this point we have examined every component
		return false;
	}

	/****************************************************************************************
	 * This is a stack based search of the game board, if we detect an amazon
	 * piece of the oppsing player then the game should not finish as there are
	 * legal moves to reach other pieces. If every amazon is isolated from the
	 * opposite colour we declare the game over.
	 * 
	 * The scoring system does not work, it finds the right winner (in the runs
	 * i've done) but does not return the expected value. Will have to see
	 * proper scoring later I guess.
	 * 
	 * @param source
	 *            - An integer pairing (x,y) for where the amazon piece is
	 * @param player
	 *            - 1 for White, 2 for Black
	 * @return - True if we reach an opponent, false otherwise.
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
		for (GameRoom g : rooms) {
			System.out.println(g.roomID + " " + g.roomName);
		}
		return rooms;
	}

	@Override
	public boolean handleMessage(String message) throws Exception {
		gui.addServerMessage("Server message: ", message);
		return false;
	}

	@Override
	public boolean handleMessage(GameMessage message) throws Exception {
		
		/**
		 * These are the NanoXML classes we need to convert the message to XML and such, need to
		 * figure out the message header
		 */
		
		IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
		IXMLReader reader = StdXMLReader.stringReader(message.toString());
		parser.setReader(reader);
		IXMLElement xml = (IXMLElement) parser.parse();
		
		// Handle the different types of messages that we recieve.
		
		if (xml.hasAttribute(GameMessage.ACTION_MOVE)){
			
		} else if (xml.hasAttribute(GameMessage.ACTION_GAME_START)){
			
		} else if (xml.hasAttribute(GameMessage.ACTION_POS_MARKED)){
			
		} else if (xml.hasAttribute(GameMessage.ACTION_ROOM_JOINED)){
			
		} else if (xml.hasAttribute(GameMessage.MSG_GAME)){
			
		} else if (xml.hasAttribute(GameMessage.MSG_CHAT)){
			
		} else if (xml.hasAttribute(GameMessage.MSG_GENERAL)){
			
		} else if (xml.hasAttribute(GameMessage.MSG_JOIN_ROOM)) {
			
		}
    
		gui.addServerMessage("Server game message: ", message.toString());
		return false;
	}

	/**
	 * Main method to pass agent actions to the server
	 * 
	 * @param msg - What to say to the server
	 * @param roomID - Which room to direct it to.
	 */
	public void sendToServer(String msg, int roomID) {

	}

	public static void main(String[] args) {
		Player player = new Player("Berate-A-Bot-2.0001", "54321");
	}
}
