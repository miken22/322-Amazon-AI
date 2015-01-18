package ai;

import ubco.ai.connection.ServerMessage;

public class XMLParser{

	public XMLParser() {
		
	}
	
	public void buildMessageToServer(String message){
		System.out.println(ServerMessage.parseMessage(message));
	}

}
