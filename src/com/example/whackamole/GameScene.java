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
import org.andengine.entity.scene.background.SpriteBackground;
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

/**
 * Scene containing the game whack-a-mole. 
 */
public class GameScene extends BaseScene {
	
	// Flag indicating whether the scene on create should continue
	// where the user left off.
	private static boolean shouldResume = false;
	
    private HUD gameHUD;

    private Text scoreText;
    private Text lifeText;
    private PhysicsWorld physicsWorld;
    public TiledSprite allFore;
    private ArrayList<TiledSprite> spriteLives;
    public LevelModel currentLevel;
    private boolean endRound;
    private LevelModel levelComplete;
    private Text clickText;
    private Text finishText;
    private Text finish2Text;
    private Text finish3Text;
    private UserModel user;
    
    // Flags indicating whether the game is currently frozen, smogged or burned.
    private boolean isFrozen;
    private boolean isSmogged;
    private boolean isBurned;
    
	@Override
    public void createScene() {
		endRound = false;
		
		this.setOnSceneTouchListener(new IOnSceneTouchListener() {
			
            @Override
            public boolean onSceneTouchEvent(Scene pScene,TouchEvent pSceneTouchEvent) {
            	if(pSceneTouchEvent.isActionDown() && endRound) {
        			if (levelComplete.nextRound()) {
    		    		levelComplete.playRound();
    		    	} 
        			else if (loadLevel(levelComplete.getNumLevel() + 1, 1)) {
        		        currentLevel.playRound();
        		    }
        			else {
        				// TODO Congratulate user first!
        				GameActivity.goToMain();
        			}
        			
        			gameHUD.detachChild(clickText);
        			gameHUD.detachChild(finishText);
        			gameHUD.detachChild(finish2Text);
        			gameHUD.detachChild(finish3Text);
        			endRound = false;
        			return true;
            	}
            	return false;
            }
        });
		
    	createBackground();
    	createHUD();
    	createPhysics();
    	
    	UserAdapter userAdapter = new UserAdapter();
    	userAdapter.open();
    	this.user = userAdapter.getUser(GameActivity.getName());
    	
    	// if this game should resume from the last completed round, resume.
    	// else start a new fresh game.
    	if (shouldResume) {
    		int[] levelAndRound = userAdapter.getLastLevelAndRound(user);
    		
    		// TODO remove on final
        	System.out.println("Last Level: " + levelAndRound[0] + " last round: " +
        						levelAndRound[1]);
        	
    		if (levelAndRound[0] == -1 || levelAndRound[1] == -1) {
	    			loadLevel(1, 1);
			}	
	    	else if (loadLevel(levelAndRound[0], levelAndRound[1] + 1));
	    	else if (loadLevel(levelAndRound[0] + 1, 1));
	    	else loadLevel(1, 1);
    	} else {
    		loadLevel(GameActivity.getStartLevel(), GameActivity.getStartRound());
    	}
    	userAdapter.close();
    	
    	// add a small overlapping patch on the background, between this patch
    	// and the background, the moles will appear.
    	for (LocationModel location : this.currentLevel.getLocations()) {
	    	ResourcesManager resourcesManager = this.getResourcesManager();
	        Sprite back = new Sprite(location.getX(), location.getY(),
	    			resourcesManager.back, this.vbom);
	    	back.setZIndex(2);
	    	gameHUD.attachChild(back);
    	}
    	
    	gameHUD.sortChildren();
    	
    	// play a round
        currentLevel.playRound();
    }

    @Override
    public void onBackKeyPressed() {
    	GameActivity.goToMain();
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {
    	 camera.setHUD(null);
    	 camera.setCenter(400, 240);
    	 this.dispose();
    }
    
    private void createBackground() {
        this.setBackground(new SpriteBackground(new Sprite(0, 0, resourcesManager.background_region, vbom)));
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
        
        allFore = new TiledSprite(0,0,ResourcesManager.getInstance().allFore,vbom);
        allFore.setCurrentTileIndex(2);
        allFore.setZIndex(4);
        gameHUD.attachChild(allFore);
        
        // create score text
        scoreText = new Text(20, 20, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setSkewCenter(0, 0);    
        scoreText.setText("Score: 0");
        gameHUD.attachChild(scoreText);
            
        lifeText = new Text(510, 20, resourcesManager.font, "0123456789 *", new TextOptions(HorizontalAlign.LEFT), vbom);
        lifeText.setSkewCenter(0, 0);
        lifeText.setText("");
        
        spriteLives = new ArrayList<TiledSprite>();
        
        for (int i = 0 ; i < 5 ; i++) {
        	spriteLives.add(new TiledSprite(600 - 80 * i, 20, resourcesManager.life, vbom));
        	gameHUD.attachChild(spriteLives.get(i));
        }
        
        gameHUD.attachChild(lifeText);
        gameHUD.sortChildren();
        camera.setHUD(gameHUD); 
    }

    /**
     * Called on game lost, submits the score and starts the score activity.
     */
    public void onGameLost() {
    	resetFore();
    	
	    ScoreAdapter db = new ScoreAdapter();
		db.open();
		db.addScore(currentLevel.getScore(), user, currentLevel);
		db.close();
	    disposeScene();
		GameActivity.goToScore();
	}
    
    /**
     * Called when lives are lost or gained, updates the visual representation
     * of the lives accordingly.
     * @param lives number of lives remaining.
     */
    public void onLivesUpdated(int lives) {
    	
    	// TODO remove
    	System.out.println("MOLE DIED ON SECONDS ELAPSED: " + this.getEngine().getSecondsElapsedTotal());
    	
    	if (lives > 5) {
    		for (int i = 0; i < 5; i++) {
        		spriteLives.get(i).setCurrentTileIndex(i > 0 ? 1 : 0);
        	}
    		
    		lifeText.setText(lives + " * ");
    	} else {
    		for (int i = 0; i < 5; i++) {
        		spriteLives.get(i).setCurrentTileIndex(i < lives ? 0 : 1);
        	}
    		
    		lifeText.setText("");
    	}
    }
    
    /**
     * Called when a round has ended. Congratulates the user and
     * shows the score on screen.
     * @param level the level of which a round has ended.
     */
    public void onRoundEnd(LevelModel level) {
    	 resetFore();
    	 ScoreAdapter db = new ScoreAdapter();
		 db.open();
		 db.addScore(level.getScore(), user, level);
		 db.printAll(); // TODO remove????
		 db.close();
		 
    	 finishText = new Text(30, 300, resourcesManager.font, "Congratulations, ",
    			 new TextOptions(HorizontalAlign.LEFT), vbom);
   	     finish2Text = new Text(30, 360, resourcesManager.font, "You have finished, ",
   	    		 new TextOptions(HorizontalAlign.LEFT), vbom);
   	     finish3Text = new Text(30, 420, resourcesManager.font, "level : " +
   	    		 level.getNumLevel() + " and round : " + level.getNumRound(),
   	    		 new TextOptions(HorizontalAlign.LEFT), vbom);
  
    	 finishText.setSkewCenter(0, 0);    
         gameHUD.attachChild(finishText);
         finish2Text.setSkewCenter(0, 0);    
         gameHUD.attachChild(finish2Text);
         finish3Text.setSkewCenter(0, 0);    
         gameHUD.attachChild(finish3Text);

         levelComplete = level;
	     endRound = true;
    }
    
    /**
     * Sets the foreground of the game according to the state.
     * There is an ordering in states as multiple might occur at the same time:
     * smogged > frozen > burned > nothing.
     */
    public void setFore() {
    	if (this.isSmogged) {
    		allFore.setCurrentTileIndex(0);
        	allFore.setZIndex(3);
    	}
    	else if (this.isFrozen) {
    		allFore.setCurrentTileIndex(1);
    		allFore.setZIndex(3);
    	}
    	else if (this.isBurned) {
    		allFore.setCurrentTileIndex(3);
    		allFore.setZIndex(3);
    	}
    	else {
    		allFore.setCurrentTileIndex(2);
    		allFore.setZIndex(3);
    	}
    }
    
    /**
     * Reset the foreground and reset all states.
     */
    private void resetFore() {
    	this.isFrozen = false;
    	this.isSmogged = false;
    	this.isBurned = false;
    	setFore();
    }
    
    /**
     * Called when the game is frozen, also sets the foreground.
     */
    public void onFreeze() {
    	this.isFrozen = true;
    	this.setFore();
    }
    
    /**
     * Called when the game is unfrozen, also sets the foreground.
     */
    public void onUnfreeze() {
    	this.isFrozen = false;
    	this.setFore();
    }
    
    /**
     * Called when the game is smogged, also sets the foreground.
     */
    public void onSmog() {
    	this.isSmogged = true;
    	this.setFore();
    }
    
    /**
     * Called when the game is 'unsmogged', also sets the foreground.
     */
    public void onUnsmog() {
    	this.isSmogged = false;
    	this.setFore();
    }
    
    /**
     * Called when the game is burned, also sets the foreground.
     */
    public void onBurn() {
    	this.isBurned = true;
    	this.setFore();
    }
    
    /**
     * Called when the game is 'unburned', also sets the foreground.
     */
    public void onUnburn() {
    	this.isBurned = false;
    	this.setFore();
    }
    
    public float getSecondsElapsedTotal() {
    	return getEngine().getSecondsElapsedTotal();
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
    
    /**
     * Load level level with round round from database. 
     * @param level number of level to load.
     * @param round number of round to load.
     * @return true if succes, false otherwise.
     */
    private boolean loadLevel(int level, int round) {
    	System.out.println("Level loading!");
        
        // get a level from the database
        currentLevel = LevelModel.loadLevel(level, round, this);
        
        System.out.println("Loading level: finished");
        
        // TODO remove on final (debug info).
        if (currentLevel != null) {
	        System.out.println("Current round: " + currentLevel.getCurrentRound().getNumRound());
	        System.out.println("Moles in current round: " + currentLevel.getCurrentRound().getMoles());
	        
	    	for (LocationModel location : currentLevel.getLocations()) {
	    		System.out.print("Moles in location " + location.getX() + " , " + location.getY() + " ");
	    		for (MoleModel mole : location.getMoles()) {
	    			System.out.print(mole.getClass() + ", ");
	    		}
	    		System.out.println(".");
	    	}
        }
    	
    	return currentLevel != null;
    }
    
    /**
     * If set to true, the scene will resume with the next level and round where the user
     * does not a score for.
     * @param shouldResume true if should resume where the user left off, false otherwise.
     */
    public static void shouldResume(boolean shouldResume) {
    	GameScene.shouldResume = shouldResume;
    }
}