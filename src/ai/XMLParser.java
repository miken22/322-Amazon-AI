package ai;

import java.util.Enumeration;
import java.util.Properties;

import net.n3.nanoxml.IXMLElement;
import ubco.ai.connection.ServerMessage;
import ubco.ai.games.GameMessage;

public class XMLParser{

	private String userName;

	public XMLParser(String userName) {
		this.userName = userName;
	}

	public void buildMessageToServer(String message){
		System.out.println(ServerMessage.parseMessage(message));
	}

	public String handleXML(IXMLElement xml){

		// Need "type" to see which type of game message we have. Need to build cases for the other message types
		if (xml.hasAttribute("type")){
			if (xml.getAttribute("type", GameMessage.ACTION_GAME_START).contains(GameMessage.ACTION_GAME_START)){
				return GameMessage.ACTION_GAME_START;
			}
		}

		return "";
		// TODO: Third level of game message has userName and role information.
	}

	/**
	 * This method correctly looks through the "game-start" server message to identify our agents role
	 * 
	 * @param xml - Server message parsed into XML format
	 * @return - The string representing the agents role ("W" or "B"), or null if we cannot find our agent.
	 */
	@SuppressWarnings("rawtypes")
	public String getUserInfo(IXMLElement xml){
		// Get user list "enumeration"
		Enumeration childList = xml.enumerateChildren();
		while (childList.hasMoreElements()){
			// Actual XML List of users
			IXMLElement userList = (IXMLElement) childList.nextElement();
			// Need "enumeration" of all children to get user info
			Enumeration userId = userList.enumerateChildren();
			// Cycle through the user list, stop when we find our agent
			while (userId.hasMoreElements()){
				// Get the XML element, and get all the attributes of the child
				IXMLElement user = (IXMLElement) userId.nextElement();
				Properties userAttributes = user.getAttributes();
				// Get the player role and user name
				String role = userAttributes.getProperty("role");
				String uName = userAttributes.getProperty("name");		
				// If it is our bot return the role
				if (uName.equals(userName)){
					return role;
				}
			}
		}
		return null;
	}
}
