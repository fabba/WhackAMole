package com.example.whackamole;


import java.util.ArrayList;

import models.levels.LevelModel;
import models.levels.LocationModel;
import models.moles.MoleModel;
import models.users.UserModel;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import com.badlogic.gdx.math.Vector2;
import com.example.whackamole.BaseScene;
import com.example.whackamole.SceneManager.SceneType;

import databaseadapter.ScoreAdapter;
import databaseadapter.UserAdapter;

public class GameScene extends BaseScene
{
    private HUD gameHUD;

    private Text scoreText;
    private Text lifeText;
    private PhysicsWorld physicsWorld;
    private int lives;
    public TiledSprite allFore;
    private ArrayList<TiledSprite> spriteLives;
    public LevelModel currentLevel;
    private boolean endRound;
    private boolean endGame;
    private LevelModel levelComplete;
    private Text clickText;
    private Text finishText;
    private Text finish2Text;
    private Text finish3Text;
    private UserModel user;
    
	@Override
    public void createScene() {
		endRound = false;
		endGame  = false;
		this.setOnSceneTouchListener(new IOnSceneTouchListener() {

            @Override
            public boolean onSceneTouchEvent(Scene pScene,TouchEvent pSceneTouchEvent) {
            	 if(pSceneTouchEvent.isActionDown()) {
            		 if( endRound ){
            			 if (levelComplete.nextRound()) {
            		    		levelComplete.playRound();
            		    	} 
            			 else {
            		    		loadLevel(levelComplete.getNumLevel() + 1,1);
            		    	}
            			 gameHUD.detachChild(clickText);
            			 gameHUD.detachChild(finishText);
            			 gameHUD.detachChild(finish2Text);
            			 gameHUD.detachChild(finish3Text);
            			 endRound = false;
            			 return true;
            		 }
            		
            	 }
               return false;
            }
        });
    	 createBackground();
    	 createHUD();
    	 createPhysics();
    	 UserAdapter user = new UserAdapter();
    	 user.open();
    	 this.user = user.getUser(GameActivity.getName());
    	 user.close();
    	 loadLevel(GameActivity.getStartLevel(),GameActivity.getStartRound());
    }

    @Override
    public void onBackKeyPressed() {
    	GameActivity.gotTomain();
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {
    	 camera.setHUD(null);
    	 camera.setCenter(400, 240);
    }
    
    private void createBackground() {
    	ParallaxBackground background = new ParallaxBackground(0, 0, 0);
		background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0, 0, resourcesManager.background_region, vbom)));
        setBackground(background);
    }
    
    public void onScoreUpdated() {
        scoreText.setText("Score: " + this.currentLevel.getScore());
    }

    private void createPhysics() {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false); 
        registerUpdateHandler(physicsWorld);
    }
    
    private void createHUD() {
        gameHUD = new HUD();
        
        //allFore = new TiledSprite(0,0,ResourcesManager.getInstance().allFore,vbom);
        // CREATE SCORE TEXT
        scoreText = new Text(20, 20, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setSkewCenter(0, 0);    
        scoreText.setText("Score: 0");
        gameHUD.attachChild(scoreText);
        
        lives = 6;
        lifeText = new Text(510, 20, resourcesManager.font, "0123456789 *", new TextOptions(HorizontalAlign.LEFT), vbom);
        lifeText.setSkewCenter(0, 0);
        lifeText.setText("");
        
        spriteLives = new ArrayList<TiledSprite>();
        for(int i = 0 ; i < 5 ; i++){
        	spriteLives.add(new TiledSprite(600 - 80 * i,20,resourcesManager.life, vbom));
        	gameHUD.attachChild(spriteLives.get(i));
        }
        if(lives > 5){
        	for(int i = 1 ; i < 5  ; i++){
        		spriteLives.get(i).setCurrentTileIndex(1);
        		lifeText.setText(Integer.toString(lives) + " * ");
        	}
        }
        gameHUD.attachChild(lifeText);
        
        //gameHUD.attachChild(allFore);
        //allFore.setCurrentTileIndex(2);
        camera.setHUD(gameHUD);
      
    }
    
    public void loseGame() {
    	 finishText = new Text(30, 300, resourcesManager.font, "Too bad, ", new TextOptions(HorizontalAlign.LEFT), vbom);
   	     finish2Text = new Text(30, 360, resourcesManager.font, "you made it until, ", new TextOptions(HorizontalAlign.LEFT), vbom);
   	     finish3Text = new Text(30, 420, resourcesManager.font, "level : " + currentLevel.getNumLevel() + " and round : " + currentLevel.getNumRound(), new TextOptions(HorizontalAlign.LEFT), vbom);
	   	 finishText.setSkewCenter(0, 0);    
	     gameHUD.attachChild(finishText);
	     finish2Text.setSkewCenter(0, 0);    
	     gameHUD.attachChild(finish2Text);
	     finish2Text.setSkewCenter(0, 0);    
	     gameHUD.attachChild(finish3Text);
	     try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     ScoreAdapter db = new ScoreAdapter();
		 db.open();
		 db.addScore(currentLevel.getScore(), user, currentLevel);
		 db.close();
	     GameActivity.gotToscore();
		 gameHUD.detachChild(clickText);
		 gameHUD.detachChild(finishText);
		 gameHUD.detachChild(finish2Text);
		 gameHUD.detachChild(finish3Text);
		 endRound = false;

	     
    }
    
    public void addLife(int addLives) {
    	lives += addLives;
    	if (lives > 5) {
    		for (int i = 1 ; i < 5  ; i++) {
        		spriteLives.get(i).setCurrentTileIndex(1);
        	}
    		lifeText.setText(Integer.toString(lives) + " * ");
    	} else {
    		for (int i = 1 ; i < lives  ; i++) {
        		spriteLives.get(i).setCurrentTileIndex(0);
        	}
    	}
    }
    
    public void loseLife()  {	
    	lives -= 1;
    	if (lives <= 0) {
    		loseGame();
    	}
    	else if (lives > 5) {
    		lifeText.setText(Integer.toString(lives) + " * ");
    	}
    	else if (lives == 5) {
    		for (int i = 1 ; i < 5  ; i++) {
        		spriteLives.get(i).setCurrentTileIndex(0);
        		lifeText.setText("");
        	}
    	} else {
    		
    		spriteLives.get(lives).setCurrentTileIndex(1);	
    	}	
    }
    
    public void onMoleDeath(MoleModel mole) {
    	this.currentLevel.onMoleDeath(mole);
    }
    
    public void onRoundEnd(LevelModel level) {
    	 ScoreAdapter db = new ScoreAdapter();
		 db.open();
		 db.addScore(level.getScore(), user, level);
		 db.printAll();
		 db.close();
		 
    	 finishText = new Text(30, 300, resourcesManager.font, "Congratulations, ", new TextOptions(HorizontalAlign.LEFT), vbom);
   	     finish2Text = new Text(30, 360, resourcesManager.font, "You have finished, ", new TextOptions(HorizontalAlign.LEFT), vbom);
   	     finish3Text = new Text(30, 420, resourcesManager.font, "level : " + level.getNumLevel() + " and round : " + level.getNumRound(), new TextOptions(HorizontalAlign.LEFT), vbom);
  
    	 finishText.setSkewCenter(0, 0);    
         gameHUD.attachChild(finishText);
         finish2Text.setSkewCenter(0, 0);    
         gameHUD.attachChild(finish2Text);
         finish2Text.setSkewCenter(0, 0);    
         gameHUD.attachChild(finish3Text);

         levelComplete = level;
	     endRound = true;
    }
    
    
    public void onBurn() {
    	// TODO some effect?
    }
    
    public void onFreeze() {
    	// TODO some effect?
    }
    
    public void onSmog() {
    	// TODO some effect?
    }
    
    public void blur() {
    }
    
    public Camera getCamera() {
    	return camera;
    }
    
    public PhysicsWorld getPhysicsWorld() {
    	return physicsWorld;
    }
    
    public HUD getGameHUD() {
    	return gameHUD;
    }
   
    public VertexBufferObjectManager getVbom() {
		return vbom;
	}
    
    public ResourcesManager getResourcesManager() {
    	return resourcesManager;
    }
    
    private void loadLevel(int level, int round) {
    	System.out.println("Level loading!");
        
        // get a level from the database
        currentLevel = LevelModel.loadLevel(level, round, this);
        // load the next round
        // System.out.println("Switching round : " + currentLevel.nextRound());
        
        System.out.println("Current round: " + currentLevel.getCurrentRound().getNumRound());
        System.out.println("Moles in current round: " + currentLevel.getCurrentRound().getMoles());
        
        // make the moles jump
        currentLevel.playRound();
        
    	System.out.println("Loading level: finished");
    	
    	for (LocationModel location : currentLevel.getLocations()) {
    		System.out.print("Moles in location " + location.getX() + " , " + location.getY() + " ");
    		for (MoleModel mole : location.getMoles()) {
    			System.out.print(mole.getClass() + ", ");
    		}
    		System.out.println(".");
    	}
    	
    	UserModel user = new UserModel(1, "jelle");
    	
    	System.out.println("Loading score!");
    	ScoreAdapter scoreAdapter = new ScoreAdapter();
    	scoreAdapter.open();
    	int score = scoreAdapter.getScore(user, currentLevel);
    	System.out.println("Loading score: finished");
    	
    	System.out.println("Loaded score = " + score);
    	
    	System.out.println("Adding new score!");
    	boolean succesScore = scoreAdapter.addScore(score + 1, user, currentLevel);
    	System.out.println("Finished adding new score!" + succesScore);
    	
    	System.out.println("New score = " + scoreAdapter.getScore(user, currentLevel));
    	scoreAdapter.close();
    }
   
    /*
    private void createNewMole(Timer t, int delay, final int listLevel){
    	t.schedule(new TimerTask() {

            @Override
            public void run() {
            	int[] coordinates = getRandomPosition();
            	MoleModel moleNormy = getRandomMole(listLevel, coordinates);
        		moleNormy.jump();
        	}
        }, delay);
    }
    private int randInt(int min, int max){

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    private MoleModel getRandomMole(int moleCode,int[] coordinates){
    	float speed = 1;
    	MoleModel newMole = null;
    	switch(moleCode){
    	case 1:
    		newMole = createMoleNormy(coordinates[0], coordinates[1] , coordinates[1], speed);
    		break;
    	case 2:
    		newMole = createMoleHatty(coordinates[0], coordinates[1] , coordinates[1], speed);
    		break;	
    	case 3:
    		newMole = createMoleTanky(coordinates[0], coordinates[1] , coordinates[1], speed);
    		break;
    	case 4:
    		newMole = createMoleGoldy(coordinates[0], coordinates[1] , coordinates[1], speed);
    		break;
    	case 5:
    		newMole = createMoleSpeedy(coordinates[0], coordinates[1] , coordinates[1], speed);
    		break;
    	case 6:
    		newMole = createMoleSniffy(coordinates[0], coordinates[1] , coordinates[1], speed);
    		break;
    	case 7:
    		newMole = createMoleIcy(coordinates[0], coordinates[1] , coordinates[1], speed);
    		break;
    	case 8:
    		newMole = createMoleSmogy(coordinates[0], coordinates[1] , coordinates[1], speed);
    		break;
    	case 9:
    		newMole = createMoleBurny(coordinates[0], coordinates[1] , coordinates[1], speed);
    		break;
    	}
    	return newMole;
    }

    private int[] getRandomPosition() {
        int[] xCoordinates = new int[] {43,297,546};
        int[] yCoordinates = new int[] {250,649,1071 };
        return new int[] {xCoordinates[randInt(0,2)],yCoordinates[randInt(0,2)]};
	}
	*/
}