<h1 align="center">Artificial Intelligence</h1> 
<h2 align="center">Controlling Game Characters with a Neural Network and Fuzzy Logic</h2> 


### Project Overview
This project is a maze game where a player uses the arrow keys to navigate through the maze. The player can encounter a variety of spider enemies in the maze. The player has stats such as health and spiders have a status health and anger. The maze contains sprites which the player can pick up such as a sword, bombs and health however they aren’t implemented in my game.

### Project Requirements
Create an AI controlled maze game containing the set of features using a suite of stubs. The objective of the game should be to escape
from a maze and either avoid or fight off the enemy characters that move around in the game environment. The provided stubs already create a basic maze game, with a key-controlled player and (immobile) sprite enemies. Several game objects, such as swords, help points and
bombs, have already been added to the game. The purpose is to design a fuzzy logic system and neural network(s) that can control the enemy characters and make them act in a pseudointelligent manner. As such, much of the programming will be declarative (fuzzy control
language) or secondary to AI design considerations, e.g. neural network topology and training data.

### Relevant features
There are four spiders in the game black, blue, brown and green. They start to move randomly when the player makes the first move. Black and blue spiders using A* Heuristic search to follow the player. A* with Admissible Heuristic Guarantees Optimal Path. There are finitely many acyclic paths in the search tree. A* only ever considers acyclic paths.  On each iteration of A* a new acyclic path is generated because when a node is added the first time, a new path exists. If a node is “promoted”, a new path to that node exists. It must be new because it’s shorter. So, the very most work it could do is to look at every acyclic path in the graph. So, it terminates. A* downside: can use lots of memory. In principle: O(number of states). For big search spaces, A* will run out of memory. This happens in my game, runs slower then comparing with other searches that I tried such as: BestFirstSearch or RecursiveDFSTraversator but I think it was the most accurate. Spiders stats following the player when it’s ten fields away from them. The spiders are fully threaded. Spiders have certain attributes - health, anger level, position in the maze, traversal algorithm and AI type. AI type is either fuzzy logic or neural networks - this control how the spider fights. Neural Network is implemented for a black spider which checks the player health and spider anger. Based on this, he makes decisions about attack, panic or escape. Fuzzy logic is used for a battle between player and spiders. Based on spider anger amount player health decreases. If the player is in the same position as the spider and has no weapons player health decreases by the amount of enemy’s anger value.

### Used Technologies
Java 9 </br>
Eclipse </br>

