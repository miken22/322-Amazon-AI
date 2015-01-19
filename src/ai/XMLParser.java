package ai;

import java.io.IOException;
import java.util.Enumeration;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLWriter;
import ubco.ai.connection.ServerMessage;
import ubco.ai.games.GameMessage;

public class XMLParser{

		
	public XMLParser() {
		
	}
	
	public void buildMessageToServer(String message){
		System.out.println(ServerMessage.parseMessage(message));
	}
	
	public String handleXML(IXMLElement xml){
			
		// Need "type" to see which type of game message we have. Need to build cases for the others, especially to determine which
		// role our AI has.
		if (xml.hasAttribute("type")){
			if (xml.getAttribute("type", GameMessage.ACTION_GAME_START).contains(GameMessage.ACTION_GAME_START)){
				return GameMessage.ACTION_GAME_START;
			}
		}
		
		return "";
		// TODO: Third level of game message has userName and role information.
	}

}
