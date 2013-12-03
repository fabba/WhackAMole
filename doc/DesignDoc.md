DesignDocument

Database in SQL(ite). We store the current game state in a table called game, from which we should be able to reconstruct that specific game when a player presses resume.This game will be constructed at the beginning of each level. Each round consists of a set of moles which will be execute in order at specific points in time, the place where the mole will appear will be randomly chosen.


Database tables:

Table: game

- id (int) | username | score (int) | level(int) | round ( int )

Table: score

- id(int) | username (string) | score (int) 

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
		Set prefence setting at which round to start

Maipanel:

	private moleNormy Normy; ( for each mole)
	private level1 [ normy, tanky , hatty, normy,normy,normy]  ( for each level which moles will be available
	private Positions [right,left,Rightdown... etc]
	private mainThread thread; ( The thread of the game )

	private score = 0;
	
	public Class pickMole
		pick a random Mole from the array level1
		
	public Class pickPostition
		pick a random position form the array positions
		
	public MainPanel(Context context)
		constructors and create mainThread
	
	public void surfaceCreated
		Set the background and start the Thread

	 public void surfaceDestroyed
		If the surface is destroyed safe the game state
	
	public boolean onTouchEvent
		Checks which mole was touched and execute the moles ability when touched
		Update score
	

	 protected void onDraw
	 	Draw the score on the surface and draw the background
	 		if moleAppearance time > elapsedTime
	 		Destroy mole
	 		set New Mole
	 	
		draw the new mole at the new position

	
MainThread:

	
	MainThread
		Set the surface and the mainpanel in the mainthread
	public void run
		Will run the application and will update the draws in main panel
	
	
		
	


MoleNormy ( for each mole ):
	
	private bitmap = get bitmap drawable spirte of mole
	private appearanceTime;
	
	public MoleNormy
		set the appearance time
	
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

PositionRightDown ( for each posistion ):
	
	private startX
	private startY
	private AppearanceTime
	
	public PositionRightDown ( class mole )
		set the appearancetime
		
	public draw
		draws the mole at the position and moves it equal to the width of the bitmap
	
	private getAppearanceTime
		
		
	
	
		
Styleguide:
- CamelCase.
- Max 120 chars per line.
- Use two spaces per indentation level. No hard tabs.
- No single line methods.
- Spaces around operators, after commas, semicolons.
- No spaces after (,[ or before ],). 
- Everything in English, no Dutch variable/funtion/class names.
