package ai;
import java.util.ArrayList;

import javax.swing.JFrame;

import ubco.ai.GameRoom;
import ubco.ai.connection.ServerMessage;
import ubco.ai.games.GameClient;
import ubco.ai.games.GameMessage;
import ubco.ai.games.GamePlayer;

/**
 * A version of the Amazon player that is demo-ed in the API
 * 
 * 
 * @author Mike Nowicki
 *
 */

public class Player extends JFrame implements GamePlayer {

	private static final long serialVersionUID = 348710967198419938L;

	private GameClient client;
	private Board board;
	
	public Player(String userName, String password){
		
		client = new GameClient(userName, password, this);		
		
		client.roomList = getRooms();
		
		client.getUserID();
		
		board = new Board(10,10);
		
		for(GameRoom g : client.roomList){
			try{
				client.joinGameRoom(g.roomName);
				break;
			} catch (Exception e){
				continue;
			}
		}
	}
	
	
	public ArrayList<GameRoom> getRooms(){

		ArrayList<GameRoom> rooms = client.getRoomLists();
		
		for(GameRoom g : rooms){
			System.out.println(g.roomID);
			System.out.println(g.roomName);	
		}
		return rooms;
	}
	
	@Override
	public boolean handleMessage(String arg0) throws Exception {
		System.out.println("Server message: " + arg0);
		return false;
	}

	@Override
	public boolean handleMessage(GameMessage arg0) throws Exception {
		System.out.println("The server says: " + arg0);
		return false;
	}
	
	public void sendToServer(String msg, int roomID){
		
		
	}



	public static void main(String[] args){
	
		Player player = new Player("","");
		
		
	}
	

}
