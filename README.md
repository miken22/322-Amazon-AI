# 322-Amazon-AI
Java implementation of a server based board game Amazons

The main class for playing a game on the server is the Player class. It creates an instance of the GameClient and controls the search agent. The Agent class is an implementation of a game player, and is created with the type of heuristic to be used. Heuristics used must extend the EvaluationFunction class. The Agent also stores an instance of the HMinimaxSearch class which performs an Alpha-Beta minimax search through the game tree. The Agent controls an instance of the XMLParser class to read and create messages to send to the server.

When the Alpha-Beta search begins it checks which move we are at, skipping any search depths beyond the first if the game is in the early stages (currently, trying to find alternative atm). The minimax search is a timed iterative deepening search of the game tree.

The main evaluation function used in the MinDistanceHeuristic, which determines which side can get to each tile in the fewest moves, and returns the value for the appropriate player the agent represents.

The game board is represented as a 10x10 matrix of bytes, where each value can be 0 for free, 1 for white, 2 for black, and 3 for an arrow. The board also maintains a record of where each piece is for quick look ups.

To generate successors there is an Action class which stores all possible moves that can be made from a position. The successor generator determines which moves of the Action class are relavent to the current state before generating each successor for the Minimax search. The SuccessorGenerator implements an instance of the GameTreeSearch class, which is able to determine which moves are valid. Moves are represented as a 6 element byte arrow, where the piece started, where the piece is going, and where the arrow is thrown.

Finally, the Utility class is used for quick conversions from integer representations to their equivalent characters for message passing and handling, and also includes a method for determining if the board is in an end-game state if a change of heuristics is desired.
