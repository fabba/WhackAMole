package com.example.whackamole;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import models.levels.LevelModel;
import models.levels.LocationModel;
import models.levels.RoundModel;
import models.moles.BurnyModel;
import models.moles.GoldyModel;
import models.moles.HattyModel;
import models.moles.IcyModel;
import models.moles.MoleModel;
import models.moles.NormyModel;
import models.moles.SmogyModel;
import models.moles.SniffyModel;
import models.moles.SpeedyModel;
import models.moles.TankyModel;
import models.users.UserModel;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.SAXUtils;
import org.andengine.util.color.Color;

import android.content.Intent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.example.whackamole.BaseScene;
import com.example.whackamole.SceneManager.SceneType;

import databaseadapter.LevelAdapter;
import databaseadapter.ScoreAdapter;

public class GameScene extends BaseScene
{
    private int score = 0;
    private HUD gameHUD;
  
    private Text scoreText;
    private PhysicsWorld physicsWorld;
    private int lives;
    private ArrayList<TiledSprite> spriteLives;
    
	@Override
    public void createScene()
    {
    	 createBackground();
    	 createHUD();
    	 createPhysics();
    	 loadLevel(1);
    }

    @Override
    public void onBackKeyPressed()
    {
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
    	 camera.setHUD(null);
    	 camera.setCenter(400, 240);

    }
    
    private void createBackground()
    {
    	ParallaxBackground background = new ParallaxBackground(0, 0, 0);
		background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0, 0, resourcesManager.background_region, vbom)));
        setBackground(background);
    }
    
    public void addToScore(int i)
    {
        score += i;
        scoreText.setText("Score: " + score);
    }
    

    private void createPhysics()
    {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false); 
        registerUpdateHandler(physicsWorld);
    }
    
    private void createHUD()
    {
        gameHUD = new HUD();
    
        // CREATE SCORE TEXT
        scoreText = new Text(20, 20, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setSkewCenter(0, 0);    
        scoreText.setText("Score: 0");
        gameHUD.attachChild(scoreText);
        lives = 5;
        spriteLives = new ArrayList<TiledSprite>();
        for(int i = 0 ; i < lives ; i++){
        	spriteLives.add(new TiledSprite(600 - 80 * i,20,resourcesManager.life, vbom));
        	gameHUD.attachChild(spriteLives.get(i));
        }
        camera.setHUD(gameHUD);
    }
    
    public void loseGame(){
    }
    public void loseLife(){	
    	
    	if ( lives <= 0){
    		loseGame();
    	}
    	else{
    		spriteLives.get(lives - 1).setCurrentTileIndex(1);
        	lives -= 1;
    	}
    	
    }
    public NormyModel createMoleNormy(LocationModel location,
    		float speed, float time, float appearanceTime) {
    	return new NormyModel(location, speed, time, appearanceTime,
    			ResourcesManager.getInstance().mole_normy, this);
    }
    
    public HattyModel createMoleHatty(LocationModel location,
    		float speed, float time, float appearanceTime) {
    	return new HattyModel(location, speed, time, appearanceTime,
    			ResourcesManager.getInstance().mole_hatty, this);
    }
    
    public TankyModel createMoleTanky(LocationModel location,
    		float speed, float time, float appearanceTime) {
    	return new TankyModel(location, speed, time, appearanceTime,
    			ResourcesManager.getInstance().mole_tanky, this);
    }
    
    public SpeedyModel createMoleSpeedy(LocationModel location,
    		float speed, float time, float appearanceTime) {
    	return new SpeedyModel(location, speed, time, appearanceTime,
    			ResourcesManager.getInstance().mole_speedy, this);
    }
    
    public GoldyModel createMoleGoldy(LocationModel location,
    		float speed, float time, float appearanceTime) {
    	return new GoldyModel(location, speed, time, appearanceTime,
    			ResourcesManager.getInstance().mole_goldy, this);
    }

    public IcyModel createMoleIcy(LocationModel location,
    		float speed, float time, float appearanceTime) {
    	return new IcyModel(location, speed, time, appearanceTime,
    			ResourcesManager.getInstance().mole_icy, this);
    }
	
    public BurnyModel createMoleBurny(LocationModel location,
    		float speed, float time, float appearanceTime) {
    	return new BurnyModel(location, speed, time, appearanceTime,
    			ResourcesManager.getInstance().mole_burny, this);
    }
	
    public SniffyModel createMoleSniffy(LocationModel location,
    		float speed, float time, float appearanceTime) {
    	return new SniffyModel(location, speed, time, appearanceTime,
    			ResourcesManager.getInstance().mole_sniffy, this);
    }
	
    public SmogyModel createMoleSmogy(LocationModel location,
    		float speed, float time, float appearanceTime) {
    	return new SmogyModel(location, speed, time, appearanceTime,
    			ResourcesManager.getInstance().mole_smogy, this);
    }

    public MoleModel createMole(Class<?> moleClass, LocationModel location,
    		float speed, float time, float appearanceTime){
    	
    	if (moleClass.equals(NormyModel.class)) {
    		return createMoleNormy(location, speed, time, appearanceTime);
    	}
    	else if (moleClass.equals(HattyModel.class)) {
    		return createMoleHatty(location, speed, time, appearanceTime);
    	}
    	else if (moleClass.equals(TankyModel.class)) {
    		return createMoleTanky(location, speed, time, appearanceTime);
    	}
    	else if (moleClass.equals(SpeedyModel.class)) {
    		return createMoleSpeedy(location, speed, time, appearanceTime);
    	}
    	else if (moleClass.equals(GoldyModel.class)) {
    		return createMoleGoldy(location, speed, time, appearanceTime);
    	}
    	else if (moleClass.equals(IcyModel.class)) {
    		return createMoleIcy(location, speed, time, appearanceTime);
    	}
    	else if (moleClass.equals(BurnyModel.class)) {
    		return createMoleBurny(location, speed, time, appearanceTime);
    	}
    	else if (moleClass.equals(SniffyModel.class)) {
    		return createMoleSniffy(location, speed, time, appearanceTime);
    	}
    	else if (moleClass.equals(SmogyModel.class)) {
    		return createMoleSmogy(location, speed, time, appearanceTime);
    	}
    	else {
    		return null;
    	}
    }
	
    public ArrayList<MoleModel> createMoles(ArrayList<Class<?>> moleClasses,
    		ArrayList<Float> times, ArrayList<Float> appearanceTimes,
    		ArrayList<LocationModel> locations) {
    	
    	ArrayList<MoleModel> moles = new ArrayList<MoleModel>();
    	float speed = 1;
    	int size = moleClasses.size();
    	
    	for (int i = 0; i < size; i++) {
    		float time = times.get(i);
    		float appearanceTime = appearanceTimes.get(i);
    		Class<?> moleClass = moleClasses.get(i);
    		
	    	ArrayList<Integer> shuffledIndexes = new ArrayList<Integer>();
	    	for (int j = 0; j < locations.size(); j++) {
	    		shuffledIndexes.add(j);
	    	}
	    	Collections.shuffle(shuffledIndexes);
	    	
	    	boolean placementSucceeded = false;
	    	for (int index : shuffledIndexes) {
	    		LocationModel location = locations.get(index);
	    		
	    		System.out.println("Placement succeeded " + location.isRoomForMole(time, appearanceTime));
	    		
	    		if (location.isRoomForMole(time, appearanceTime)) {
	    			MoleModel mole = createMole(moleClass, location, speed, time, appearanceTime);
	    			
	    			if (mole != null) {
	    				location.addMole(mole);
	    				moles.add(mole);
	    			}
	    			
	    			placementSucceeded = true;
	    			break;
	    		}
	    	}
	    	
	    	if (!placementSucceeded) {
	    		// TODO throw exception
	    	}
    	}
    	
    	return moles;
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
    
    private void loadLevel(int levelID)
    {
    	// TODO load from database instead.
    	// create all locations for the moles to jump from
        System.out.println("Level loading!");
        
        // get a level from the database
        LevelModel level = LevelModel.loadLevel(1, 1, this);
        
        // load the next round
        System.out.println("Switching round : " + level.nextRound());
        
        System.out.println("Current round: " + level.getCurrentRound().getNumRound());
        System.out.println("Moles in current round: " + level.getCurrentRound().getMoles());
        
        // make the moles jump
        level.playRound();
        
    	System.out.println("Loading level: finished");
    	
    	for (LocationModel location : level.getLocations()) {
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
    	int score = scoreAdapter.getScore(user, level);
    	System.out.println("Loading score: finished");
    	
    	System.out.println("Loaded score = " + score);
    	
    	System.out.println("Adding new score!");
    	boolean succesScore = scoreAdapter.addScore(score + 1, user, level);
    	System.out.println("Finished adding new score!" + succesScore);
    	
    	System.out.println("New score = " + scoreAdapter.getScore(user, level));
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