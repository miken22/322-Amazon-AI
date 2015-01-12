import java.lang.reflect.Array;
import java.util.ArrayList;

import ubco.ai.GameRoom;
import ubco.ai.connection.ServerMessage;
import ubco.ai.games.GameClient;
import ubco.ai.games.GameMessage;
import ubco.ai.games.GamePlayer;


public class Player implements GamePlayer {

	GameClient client;
	
	public Player(String userName, String password){
		
		client = new GameClient(userName, password, this);		
		
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
		
		return false;
	}

	@Override
	public boolean handleMessage(GameMessage arg0) throws Exception {
		System.out.println("The server says: " + arg0);
		return false;
	}
	
	public void sendToServer(String msg, int roomID){
		
		String actionMsg = "Try this";

		String message = ServerMessage.compileGameMessage(msg, roomID, actionMsg);
		
	}

}
