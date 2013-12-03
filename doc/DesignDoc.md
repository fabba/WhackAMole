DesignDocument

Database in SQL(ite). We store the current game state in a table called game, from which we should be able to reconstruct that specific game when a player presses resume.This game will be constructed at the beginning of each level. Each round consists of a set of moles which will appear in order at specific points in time, the location where the mole will appear will be randomly chosen. For the graphics we will make use of OpenGl (http://developer.android.com/guide/topics/graphics/opengl.html).


Database tables:

Table: gameLog

- id (int) | userId (int) | scoreId (int) | level(int) | round ( int )

Table: score

- id (int) | userId (int) | score (int) 

Table: user

- id (int) | username (string) | password (string)

Table: level

- id (int) | spriteLocation (string)

Table: location:

- id (int) | levelId(int) | x (int) | y (int)

Table: round

- id (int) | levelId (int) | moleId (int) | time (int) | appearanceTime (int)

Table: mole

- id (int) | type (int) | clicks (int) | points (int) | spriteLocation (string)

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
		Set prefence setting at which round to start

Maipanel:

	private Mole[] moles # moles in order of appearance
	private Level # current level
	private mainThread thread; ( The thread of the game )

	private score = 0;
		
	public Class pickPosition
		pick a random position form the array positions
		
	public MainPanel(Context context)
		constructors and create mainThread
		load current level(/round) for user
		get all moles belonging to that round
		divide moles among known positions.
	
	public void surfaceCreated
		Set the background and start the Thread

	 public void surfaceDestroyed
		If the surface is destroyed safe the game state
	
	public boolean onTouchEvent
		Checks which position was touched and call the position's onTouch method
		Update score

	 protected void onDraw
	 	Draw the score on the surface and draw the background
	 	Call the draw method of all locations
	
MainThread:

	MainThread
		Set the surface and the mainpanel in the mainthread
	public void run
		Will run the application and will update the draws in main panel
	
	
Position: 

	private startX
	private startY
	Mole currentMole
	Mole[] moles # moles in order of appearance
	
	public PositionRightDown (x, y, moles)
	    instantiate position
	
	public OnTouch(time)
	   if the time matches or passed mole appear time
	   	call draw
	   	load next mole from moles.
	   return score
	
	public draw(time)
		if appear time >= time > appear time + appearance time
		    draws the current mole at the position ()
		    and moves it equal to the width of the bitmap
		else if time > appear time + appearance time
		    load next mole from moles.


MoleNormy ( for each mole ): # should inherit from some abstract class + implement some interface.
	
	private bitmap = get bitmap drawable sprite of mole
	private appearanceTime;
	
	public MoleNormy
		construct self from database.
	
	public boolean isTouched
		return if touched and executes the ability
		
	public void setTouched
	
	public void setX
	
	public void setY
	
	public void getX
	
	public void getY
	
	public void getAppearanceTime
	
	public void bitmap
		
	public void handleActionDown
		checks if the mole was touched and set touched
		
Level:

      Round[] rounds
      private bitmap = get bitmap drawable sprite of the level.
      Round currentRound
      
      Level():
         construct all rounds from database
         
      getMoles():
         returns mole in order of appearance
         
      nextRound()
      
Round: 
      
      Mole[] moles # moles in order of appearance
      
      Round():
         construct all moles from database
		
     getMoles():
        returns moles in order of appearance
        
Styleguide:
- CamelCase.
- Max 120 chars per line.
- Use four spaces per indentation level. No hard tabs.
- No single line methods.
- Spaces around operators, after commas, semicolons.
- No spaces after (,[ or before ],). 
- Everything in English, no Dutch variable/funtion/class names.
