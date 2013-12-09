package com.example.whackamole;


import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import moles.BurnyModel;
import moles.GoldyModel;
import moles.HattyModel;
import moles.IcyModel;
import moles.MoleModel;
import moles.NormyModel;
import moles.SmogyModel;
import moles.SniffyModel;
import moles.SpeedyModel;
import moles.TankyModel;

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



import android.content.Intent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.example.whackamole.BaseScene;
import com.example.whackamole.SceneManager.SceneType;

public class GameScene extends BaseScene
{
    private int score = 0;
    private HUD gameHUD;
  
    private Text scoreText;
    private PhysicsWorld physicsWorld;
    
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
        addToScore(1);
        gameHUD.attachChild(scoreText);
        
        camera.setHUD(gameHUD);
    }
   
    private MoleModel createMoleNormy(float pX, float pY , float beginY, float speed) {
    	return new NormyModel(pX + 2, pY,beginY,speed,
    			ResourcesManager.getInstance().mole_normy, this);
    }
    
    private MoleModel createMoleHatty(float pX, float pY , float beginY,  float speed) {
    	return new HattyModel(pX + 2, pY,beginY,speed,
    			ResourcesManager.getInstance().mole_hatty, this);
    }
    private MoleModel createMoleTanky(float pX, float pY , float beginY, float speed) {
    	return new TankyModel(pX + 2, pY,beginY,speed,
    			ResourcesManager.getInstance().mole_tanky, this);
    }
    
    private MoleModel createMoleSpeedy(float pX, float pY , float beginY,  float speed) {
    	return new SpeedyModel(pX + 2, pY,beginY,speed * 2,
    			ResourcesManager.getInstance().mole_speedy, this);
    }
    
    private MoleModel createMoleGoldy(float pX, float pY , float beginY, float speed) {
    	return new GoldyModel(pX + 2, pY,beginY,speed,
    			ResourcesManager.getInstance().mole_goldy, this);
    }
    
    private MoleModel createMoleIcy(float pX, float pY , float beginY,  float speed) {
    	return new IcyModel(pX + 2, pY,beginY,speed,
    			ResourcesManager.getInstance().mole_icy, this);
    }
    
    private MoleModel createMoleBurny(float pX, float pY , float beginY, float speed) {
    	return new BurnyModel(pX + 2, pY,beginY,speed,
    			ResourcesManager.getInstance().mole_burny, this);
    }
    
    private MoleModel createMoleSniffy(float pX, float pY , float beginY,  float speed) {
    	return new SniffyModel(pX + 2, pY,beginY,speed,
    			ResourcesManager.getInstance().mole_sniffy, this);
    }
    
    private MoleModel createMoleSmogy(float pX, float pY , float beginY,  float speed) {
    	return new SmogyModel(pX + 2, pY,beginY,speed,
    			ResourcesManager.getInstance().mole_smogy, this);
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
    	ArrayList<Integer> level1 = new ArrayList<Integer>();
    	level1.add(1);
    	level1.add(1);
    	level1.add(2);
    	level1.add(3);
    	level1.add(2);
    	level1.add(4);
    	level1.add(3);
    	level1.add(1);
    	level1.add(1);
    	level1.add(2);
    	level1.add(2);
    	level1.add(1);
    	Timer t = new Timer();
    	int delay = 0;
    	while(!level1.isEmpty()){
    		int pickRandomIndex = randInt(0,level1.size()-1);
    		int moleCode = level1.get(pickRandomIndex);
    		level1.remove(pickRandomIndex);
    		createNewMole(t,delay,moleCode);
    		delay += 4000;
    	}	

    	
    }
    private void createNewMole(Timer t, int delay, final int listLevel){
    	t.schedule(new TimerTask() {

            @Override
            public void run() {
            	int[] coordinates = getRandomPosition();
            	MoleModel moleNormy = getRandomMole(listLevel, coordinates);
        		moleNormy.jump();
        		gameHUD.attachChild(moleNormy);
            	gameHUD.registerTouchArea(moleNormy);
            	gameHUD.attachChild( new Sprite(coordinates[0], coordinates[1], resourcesManager.back, vbom));
            	

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

	public VertexBufferObjectManager getVbom() {
		return vbom;
	}
  


}