# Tanks
Tanks is a reimagining of an old flash 2-dimensional game where players could battle to be the last man standing.
Tanks uses JAMA 1.0.3 and MigLayout 4.0:
* http://www.miglayout.com
* http://math.nist.gov/javanumerics/jama/

System Requirements: Java 8

## Compiling

To build the game, clone the repository and run `ant` from the root of the project. Assuming all of the necessary libraries are present and can be found, this will produce a runnable game.

## Running

To run the game from the root of the project, run the command `java -cp "out/production/Tanks:lib/*" Main.Main`

## Playing

When the game starts, the main menu will appear. From this menu, the "Help" button will open a list of controls and various other useful bits. The "Exit" button is self-explanatory.

The "Play" button will open the "Game Options" screen. From this screen, you may select what type of terrain to play on and the number of players. Human players' names can be customized.

Once in the game, players take turns aiming and firing. For controls, view the "Help" menu available from the main menu. All humans take their turns before the AI players, in order of number (e.g. Player 1, followed by Player 2, etc.).
