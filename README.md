# word-processing-game

This is a Wordle-like program
- The player guesses a word and receives feedback on how close their guess was to the solution word
- For each letter in the solution word, the game tells the player if the letter is *in the correct location*, if the letter exists in the word but is *not in the correct location*, or if the letter does *not exist in the word*.
- The player gets 6 attempts to figure out the word

If you’ve never played the official version before, you can try it [here](https://www.nytimes.com/games/wordle/index.html)

## Design
- get the list of the solution word and store it
- randomly select a solution word
- prompt the user to guess the word at maximum 6 times
- ask the user to play again or quit
- write the game history out to a file

## Display
Below is the display of the game.
![display screen](display.png)
