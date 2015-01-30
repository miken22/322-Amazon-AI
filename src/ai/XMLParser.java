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

			if (xml.getAttribute("type", GameMessage.ACTION_MOVE).contains(GameMessage.ACTION_MOVE)){
				return GameMessage.ACTION_MOVE;
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

	/**
	 * Create the server message for a move in the correct format
	 * 
	 * @param roomID - Room to send message to.
	 * @param fX - X location where queen leaves from
	 * @param fY - Y location where queen leaves from
	 * @param tX - X location where queen goes.
	 * @param tY - Y location where queen goes.
	 * @param arow - X location where arrow goes.
	 * @param acol - Y location where arrow goes.
	 * @return - String of the message for the server.
	 */
	public String buildMoveForServer(int roomID, int fX, int fY, int tX, int tY,  int arow, int acol){

		String actionMsg = "<action type='" +  GameMessage.ACTION_MOVE + "'>";
		char c = Utility.getColumn(fY);
		actionMsg = actionMsg + "<queen move='" + c + String.valueOf(fX) + "-";  
		c = Utility.getColumn(tY);
		actionMsg = actionMsg + c + String.valueOf(tX) + "'>" + "</queen> ";
		c = Utility.getColumn(acol);
		actionMsg = actionMsg + "<arrow move='" + c + String.valueOf(arow) + "'>" + "</arrow></action>";

		String msg = ServerMessage.compileGameMessage(GameMessage.MSG_GAME, roomID, actionMsg);
		return msg;
	}

	public int[] getOpponentMove(IXMLElement xml) {

		int[] move = new int[6];

		IXMLElement queen = xml.getFirstChildNamed("queen");
		String qmove = queen.getAttribute("move", "default");

		IXMLElement arrow = xml.getFirstChildNamed("arrow");
		String amove = arrow.getAttribute("move", "defalut");
				
		char c = qmove.charAt(0);
		move[1] = c - 97;
		move[0] = Integer.parseInt(qmove.substring(1, 2));
		
		c = qmove.charAt(3);
		move[3] = c - 97; 
		move[2] = Integer.parseInt(qmove.substring(4,5));
		
		c = amove.charAt(0);
		move[5] = c - 97;
		move[4] = Integer.parseInt(amove.substring(1, amove.length()));

		return move;
	}
}