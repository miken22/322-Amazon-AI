package ai;

import net.n3.nanoxml.IXMLElement;
import ubco.ai.connection.ServerMessage;

public class XMLParser{

	public XMLParser() {
		
	}
	
	public void buildMessageToServer(String message){
		System.out.println(ServerMessage.parseMessage(message));
	}
	
	public String handleXML(IXMLElement xml){
		
		String info = "";
		
		
		return info;
	}

}
