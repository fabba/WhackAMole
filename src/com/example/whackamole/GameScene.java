package com.example.whackamole;

import java.util.Random;

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
import com.example.whackamole.Mole;
import com.example.whackamole.SceneManager.SceneType;

public class GameScene extends BaseScene
{
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
    
    private int score = 0;

    private void addToScore(int i)
    {
        score += i;
        scoreText.setText("Score: " + score);
    }
    
    private HUD gameHUD;
    private Text scoreText;

    private PhysicsWorld physicsWorld;

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
   
    private float horzLeft = 43 ;
    private float horzMid = 297 ;
    private float horzRight = 546 ;
    private float vertUp = 250 ;
    private float vertMid = 649 ;
    private float vertDown = 1071 ;
    
    
    private Mole createMoleNormy(float pX, float pY , float beginY,  float speed, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld){
    	Mole moleNormy;
    	moleNormy = new Mole(pX + 2, pY,beginY,speed,ResourcesManager.getInstance().mole_normy, vbo, camera, physicsWorld)
    	{

			@Override
			public void onDie() {
				gameHUD.detachChild(this);
				gameHUD.unregisterTouchArea(this);
				
			}

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				addToScore(1);
				gameHUD.detachChild(this);
		    	gameHUD.unregisterTouchArea(this);	
				return true;
			}};
    	return moleNormy;
    }
    
    private Mole createMoleHatty(float pX, float pY , float beginY,  float speed, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld){
    	Mole moleHatty;
    	moleHatty = new Mole(pX + 2, pY,beginY,speed,ResourcesManager.getInstance().mole_hatty, vbo, camera, physicsWorld)
    	{

			@Override
			public void onDie() {
				gameHUD.detachChild(this);
				gameHUD.unregisterTouchArea(this);
			}

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				
				gameHUD.detachChild(this);
		    	gameHUD.unregisterTouchArea(this);
		    	addToScore(1);
				return true;
			}};
    	return moleHatty;
    }
    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
   
	
    private Mole getRandomMole(int[] level1, int[] coordinates) {
    	Mole newMole = null;
		switch(level1[randInt(0,level1.length-1)]){
		case 1:
			newMole = createMoleNormy(coordinates[0], coordinates[1],coordinates[1],1, vbom, camera, physicsWorld);
			break;
		case 2:
			newMole = createMoleHatty(coordinates[0], coordinates[1],coordinates[1],1, vbom, camera, physicsWorld);
			break;
		}
		return newMole;
	}
    private int[] getRandomPosition() {
        int[] xCoordinates = new int[] {43,297,546};
        int[] yCoordinates = new int[] {250,649,1071 };
        return new int[] {xCoordinates[randInt(0,2)],yCoordinates[randInt(0,2)]};
	}
   
	private void loadLevel(int levelID)
    {
		int[] level1 = new int[] {1,2,1,1,1,2,1,1};
		int[] coordinates = getRandomPosition();
		Mole newMole = getRandomMole(level1, coordinates);
		newMole.jump();
		gameHUD.attachChild(newMole);
		gameHUD.registerTouchArea(newMole);
		gameHUD.attachChild( new Sprite(coordinates[0], coordinates[1], resourcesManager.back, vbom));
	
    }


}