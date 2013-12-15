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
    
    private boolean isFrozen;
    private boolean isSmogged;
    private boolean isBurned;
    
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
    	 UserAdapter userAdapter = new UserAdapter();
    	 userAdapter.open();
    	 this.user = userAdapter.getUser(GameActivity.getName());
    	 userAdapter.close();
    	 loadLevel(GameActivity.getStartLevel(), GameActivity.getStartRound());
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
        
        allFore = new TiledSprite(0,0,ResourcesManager.getInstance().allFore,vbom);
        allFore.setCurrentTileIndex(2);
        allFore.setZIndex(4);
        gameHUD.attachChild(allFore);
        
        // CREATE SCORE TEXT
        scoreText = new Text(20, 20, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setSkewCenter(0, 0);    
        scoreText.setText("Score: 0");
        gameHUD.attachChild(scoreText);
            
        lifeText = new Text(510, 20, resourcesManager.font, "0123456789 *", new TextOptions(HorizontalAlign.LEFT), vbom);
        lifeText.setSkewCenter(0, 0);
        lifeText.setText("");
        
        spriteLives = new ArrayList<TiledSprite>();
        
        for (int i = 0 ; i < 5 ; i++) {
        	spriteLives.add(new TiledSprite(600 - 80 * i,20,resourcesManager.life, vbom));
        	gameHUD.attachChild(spriteLives.get(i));
        }
        
        gameHUD.attachChild(lifeText);
        gameHUD.sortChildren();
        camera.setHUD(gameHUD);
      
    }
    
    public void loseGame() {
    	 resetFore();
	     ScoreAdapter db = new ScoreAdapter();
		 db.open();
		 db.addScore(currentLevel.getScore(), user, currentLevel);
		 db.close();
	     GameActivity.gotToscore();
    }

    public void onGameLost() {
    	resetFore();
	    ScoreAdapter db = new ScoreAdapter();
		db.open();
		db.addScore(currentLevel.getScore(), user, currentLevel);
		db.close();
	    
		GameActivity.gotToscore();
    }
    
    public void onLivesUpdated(int lives) {
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
    
    public void onMoleDeath(MoleModel mole) {
    	this.currentLevel.onMoleDeath(mole);
    }
    
    public void onRoundEnd(LevelModel level) {
    	 resetFore();
    	 ScoreAdapter db = new ScoreAdapter();
		 db.open();
		 db.addScore(level.getScore(), user, level);
		 db.printAll();
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
    
    public void setFore() {
    	if (this.isSmogged) {
    		allFore.setCurrentTileIndex(0);
        	allFore.setZIndex(1);
    	}
    	else if (this.isFrozen) {
    		allFore.setCurrentTileIndex(1);
    	}
    	else if (this.isBurned) {
    		allFore.setCurrentTileIndex(3);
    	}
    	else {
    		allFore.setCurrentTileIndex(2);
    	}
    }
    
    private void resetFore() {
    	this.isFrozen = false;
    	this.isSmogged = false;
    	this.isBurned = false;
    	setFore();
    }
    
    public void onFreeze() {
    	this.isFrozen = true;
    	this.setFore();
    }
    
    public void onUnfreeze() {
    	this.isFrozen = false;
    	this.setFore();
    }
    
    public void onSmog() {
    	this.isSmogged = true;
    	this.setFore();
    }
    
    public void onUnsmog() {
    	this.isSmogged = false;
    	this.setFore();
    }
    
    public void onBurn() {
    	this.isBurned = true;
    	this.setFore();
    }
    
    public void onUnburn() {
    	this.isBurned = false;
    	this.setFore();
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
        
        // play a round
        currentLevel.playRound();
        
    	System.out.println("Loading level: finished");
    	
    	for (LocationModel location : currentLevel.getLocations()) {
    		System.out.print("Moles in location " + location.getX() + " , " + location.getY() + " ");
    		for (MoleModel mole : location.getMoles()) {
    			System.out.print(mole.getClass() + ", ");
    		}
    		System.out.println(".");
    	}
    
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
}