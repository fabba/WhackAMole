package com.example.whackamole;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import models.levels.LocationModel;
import models.levels.RoundModel;
import models.moles.HattyModel;
import models.moles.MoleModel;
import models.moles.NormyModel;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.example.whackamole.BaseScene;
import com.example.whackamole.SceneManager.SceneType;

import databaseadapter.RoundAdapter;

public class GameScene extends BaseScene
{
    private int score = 0;
    private HUD gameHUD;
    private Text scoreText;
    private PhysicsWorld physicsWorld;
    
    private ArrayList<LocationModel> locations;
    
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
    	SceneManager.getInstance().loadMenuScene(engine);
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
        addToScore(1);
        gameHUD.attachChild(scoreText);
        
        camera.setHUD(gameHUD);
    }
   
    public MoleModel createMoleNormy(LocationModel location,
    		float speed, float time, float appearanceTime) {
    	return new NormyModel(location, speed, time, appearanceTime,
    			ResourcesManager.getInstance().mole_normy, this);
    }
    
    public MoleModel createMoleHatty(LocationModel location,
    		float speed, float time, float appearanceTime) {
    	return new HattyModel(location, speed, time, appearanceTime,
    			ResourcesManager.getInstance().mole_hatty, this);
    }
    
    public MoleModel createMole(Class<?> moleClass, LocationModel location,
    		float speed, float time, float appearanceTime){
    	
    	if (moleClass.equals(NormyModel.class)) {
    		return createMoleNormy(location, speed, time, appearanceTime);
    	}
    	else if (moleClass.equals(HattyModel.class)) {
    		return createMoleHatty(location, speed, time, appearanceTime);
    	}
    	else {
    		return null;
    	}
    }
    
    public ArrayList<MoleModel> createMoles(ArrayList<Class<?>> moleClasses,
    		ArrayList<Float> times, ArrayList<Float> appearanceTimes) {
    	
    	ArrayList<MoleModel> moles = new ArrayList<MoleModel>();
    	float speed = 1;
    	int size = moleClasses.size();
    	
    	for (int i = 0; i < size; i++) {
    		float time = times.get(i);
    		float appearanceTime = appearanceTimes.get(i);
    		Class<?> moleClass = moleClasses.get(i);
    		
	    	Random random = new Random();
	    	
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
    
    private void loadLevel(int levelID)
    {
    	float horzLeft = 43 ;
        float horzMid = 297 ;
        float horzRight = 546 ;
        float vertUp = 250 ;
        float vertMid = 649 ;
        float vertDown = 1071 ;
		
        float[] horz = {43, 297, 546};
        float[] vert = {250, 649, 1071};
        
        this.locations = new ArrayList<LocationModel>();
        for (float hor : horz) {
        	for (float ver : vert) {
        		this.locations.add(new LocationModel(hor, ver, ver));
        	}
        }
        
        RoundAdapter roundAdapter = new RoundAdapter();
        roundAdapter.open();
        RoundModel round = roundAdapter.getRound(1, this);
        roundAdapter.close();
        
        ArrayList<MoleModel> moles = round.getMoles();
        moles.get(0).jump();
        gameHUD.attachChild(moles.get(0));
        gameHUD.registerTouchArea(moles.get(0));
        
        /*
		MoleModel moleNormy = createMoleNormy(locations.get(0), 1, 0, 3);
		moleNormy.jump();
		gameHUD.attachChild(moleNormy);
    	gameHUD.registerTouchArea(moleNormy);
    	*/
    	
    	/*
		MoleModel moleHatty = createMoleHatty(locations.get(1), 1, 0, 3);
		moleHatty.jump();
    	gameHUD.attachChild(moleHatty);
    	gameHUD.registerTouchArea(moleHatty);
    	*/
    	
    	gameHUD.attachChild( new Sprite(horzLeft, vertUp, resourcesManager.back, vbom));
    	gameHUD.attachChild( new Sprite(horzMid, vertMid, resourcesManager.back, vbom));

    	
    }
    private int randInt(int min, int max){

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
   

    private int[] getRandomPosition() {
        int[] xCoordinates = new int[] {43,297,546};
        int[] yCoordinates = new int[] {250,649,1071 };
        return new int[] {xCoordinates[randInt(0,2)],yCoordinates[randInt(0,2)]};
	}

	public VertexBufferObjectManager getVbom() {
		return vbom;
	}
  


}