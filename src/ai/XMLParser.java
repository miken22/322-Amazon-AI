package ai;

import java.util.Enumeration;

import net.n3.nanoxml.IXMLElement;
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

	public String[] getUserInfo(IXMLElement xml){
		
		String[] userInfo = new String[3];
		
		if (xml.hasAttribute("type")){
			xml.removeAttribute("type");
			Enumeration children = xml.enumerateChildren();
			while (children.hasMoreElements()){
				IXMLElement child = (IXMLElement) children.nextElement();
				if (child.hasAttribute("usrlist")){
					System.out.println("Found user list!!");
				}
			}
		}
		
		
		return userInfo;
	}
	
}
