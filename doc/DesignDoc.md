DesignDocument

Database in SQL. We store the current game state in a table called game, from which we should be able to reconstruct that specific game when a player presses resume. The implementation of hangman is pretty straight forward. Pick a word at random matching the settings set in the game. Prompt the user for letters untill the word is guessed or the man is hung. For efficiency we first pick at random a word length and then we pick all words from the database with that length, this way we do not store a gigantic list of words in memory. After we have picked our list of words, we pick a random word from it.

For evil we keep track of a list of possible worlds, which at first with no guesses should be the entire dictionary of words. We pick one random word to start with just like in ordinary hangman. When a user guesses a letter we construct a Hashtable with as key a string representing the location of the letters and as value a list of words with matches that location. For instance if we have a two letter word and the the user has guessed the letter u. We get the following possible scenarios: --, u-,-u,uu. These scenarios are the keys for our Hashtable. By scanning through the list of possible words we select each word matching one of these scenarios and add them to the list of that scenario. After we have scanned the list of possible words, we select the scenario which has the most possible words attached to it, thus the longest list of words. We set this list as our list of possible words and the game continues for the next round.

Score calculation for use in highscore ranking is done by the equation below. This gives a score which can be interpreted as a % of correct guesses ranging from 0-100.
- (((count of different letters in word) - (players guesses))/(players guesses)*100)+100


Database tables:

Table: game

- id (int) | username | score (int) | letters (string) | word (int) | guesses (int) |evil ( boolean )

Table: score

- id(int) | username (string) | score (int) | evil (bool)

Table: user

- id(int) | username (string) | password (string)

Model Classes:

Main activiy:

	onCreate(Bundle savedInstanceState)
		Set the view to activity_main.xml

	OnClick(View view):
		view => properties of the clicked button
		Each button in the lay-out of the main activity have a onClick function to the activities ( Game,Setting and Score )

Score Activity:
	onCreate(Bundle savedInstanceState)
		Set the view to activity_score.xml

	viewDatabase():
		Get all the elements of the database and put them in a listview in activity_score.xml

Setting Activity:
	onCreate(Bundle savedInstanceState)
		Set the view to activity_setting.xml

	
	OnClick(View view):
		view => properties of the clicked button
		Checks if the value of the input is between the given range otherwise error was deployed.
		If the input is correct go to the main_activity

Game Activity:

	Arraylist<String> ALL_LETTERS = All the letters of the alphabet
	Arraylist<String> word = The word used for hangman
	Arraylist<String> empty = The word but all letter are replaced with 
	int turn 

	onCreate(Bundle savedInstanceState)
		Set the view to activity_game.xml
	
	gameStart()
		Starts a new game of hangman. Finds all possible variables ( acceptable_turns, acceptable_wordlength, evilMode) of the setting page

	getWord() 
		Pick a random word from the xml. The xml file will first be sorted at which words will be in the acceptable_wordlength
		Sets all possible words in wordList
	
	changeWord()
		Checks if evilMode is on.
		Checks if the current word can be replaced with another word in the tree

	wordMask()
		Replace all letters within the word with -
	
	onKey(View v, int keyCode, KeyEvent event) 
		view => properties of the pressed key
		keyCode => code of the pressed key
		event => What type of press (down,up,doubleclick,etc.)
		Checks if there is a key press and checks if the keycode is a valid input and checks if the keycode is in the word.
		If the word is complete finish the game
		If the amount of turn is greater then acceptable turns GAME OVER
	
	integer claculateScore()
		will calculate the score = (((count of different letters in word) - (players guesses))/(players guesses)*100)+100
		returns the score

	finishedGame()
		Checks if the player name have already been filled in the setting and shows the score of the player
		After the show the player can decide if the score will be submitted to the topScore
		
	OnClick(View view):
		view => properties of the clicked button
		Checks if the value of the input is between the given range otherwise error was deployed.
		If the input is correct go to the main_activity

ScoresTable:
	
	onCreate(SQLiteDatabase db)
		Creates score database with four collumns ( int id,string name,int points,String word,int guesses,boolean evil )
		boolean evil = If the game was played in evilMode or not
	
	addScore(String name,int points,String word,int guesses)
		Insert a new row with a name and points to the database
	
	List<Map<String, String>> getAllPoints()
		Gets all de names mapped up with points from the database
		Returns the mapped names and points
		
Styleguide:
- CamelCase.
- Max 120 chars per line.
- Use two spaces per indentation level. No hard tabs.
- No single line methods.
- Spaces around operators, after commas, semicolons.
- No spaces after (,[ or before ],). 
- Everything in English, no Dutch variable/funtion/class names.
