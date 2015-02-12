package ai;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.IXMLParser;
import net.n3.nanoxml.IXMLReader;
import net.n3.nanoxml.StdXMLReader;
import net.n3.nanoxml.XMLParserFactory;
import ai.gui.GUI;
import ai.search.Agent;
import ai.search.HMinimaxSearch;
import ai.search.TrivialFunction;
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
	private final int ARROW = 3;

	private int playerID;
	private int oppID;
	private String role;

	private boolean isOpponentsTurn;

	private int roomNumber;
	
	private int nOfProcessors;
	private ExecutorService eService;
	private ArrayList<Future<int[]>> futureList;

	public Player(String userName, String password) {

		client = new GameClient(userName, password, this);
		board = new Board(ROWS, COLS);
		gui = new GUI(ROWS, COLS);
		parser = new XMLParser(userName);

		nOfProcessors = Runtime.getRuntime().availableProcessors();
		eService = Executors.newFixedThreadPool(1);
		futureList = new ArrayList<>();
		
		board.initialize();

	}

	public void joinServer(){
		client.roomList = getRooms();	
		gui.init();

		for (GameRoom g : client.roomList) {
			try {
//				if (g.userCount == 1){
					client.joinGameRoom(g.roomName);
					roomNumber = g.roomID;
					break;
//				}
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

		// TODO: Handle arguments to set properties such as heuristic choice, search depth
		agent = new Agent(playerID);

		agent.setupHeuristic(new TrivialFunction(playerID));

		
		if (!isOpponentsTurn){
			pickMove();
		}
		
	}

	private void pickMove() {

		if (isOpponentsTurn) {
			// TODO: Plan ahead based on possible moves

			System.out.println("Opponents turn:");

			waitForMove();

		} else {

			agent.startTimer();
			
			System.out.println("Agents move:");

			try{
				
				
				int[] move = agent.selectMove(board);
				
				String moveMessage = parser.buildMoveForServer(roomNumber, move[0], move[1], move[2], move[3], move[4], move[5]);
				client.sendToServer(moveMessage, false);
				
				// GUI and logic update
				String action = Utility.getColumnLetter(move[1]) + "" + move[0] + "-" + Utility.getColumnLetter(move[3]) + "" + move[2] + "-" + Utility.getColumnLetter(move[5]) + "" + move[4];
				
				updateRepresentations(move, playerID);
				gui.updateMoveLog("Agent: ", action);
				isOpponentsTurn = true;
				
			} catch (NullPointerException e){
				endGame();
			}
			
		}

	}

	private void waitForMove(){
		while (isOpponentsTurn){

		}
	}
	
	/**
	 * Method to handle goal state
	 */
	private void endGame(){
		System.out.println("Game over.");
		gui.destroy();
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
			this.startGame();

		} else if (answer.equals(GameMessage.ACTION_MOVE)){

			System.out.println("Opponent move recieved.");
			System.out.println(message.toString());
			// Get the queen move and arrow marker.
			int[] move = parser.getOpponentMove(xml);	
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
		if (args.length == 0){
			player.joinServer();
		} else {
			player.joinServer(args[0] + " " + args[1]);
		}
	}
}
