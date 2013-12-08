package com.example.whackamole;

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
    
    
    private void loadLevel(int levelID)
    {
    	float horzLeft = 43 ;
        float horzMid = 297 ;
        float horzRight = 546 ;
        float vertUp = 250 ;
        float vertMid = 649 ;
        float vertDown = 1071 ;
		
		Mole moleNormy = createMoleHatty(horzMid, vertMid,vertMid,1, vbom, camera, physicsWorld);
		moleNormy.jump();
		gameHUD.attachChild(moleNormy);
    	gameHUD.registerTouchArea(moleNormy);
    	
		Mole moleHatty = createMoleHatty(horzLeft, vertUp,vertUp,1, vbom, camera, physicsWorld);
		moleHatty.jump();
    	gameHUD.attachChild(moleHatty);
    	gameHUD.registerTouchArea(moleHatty);
    	
    	gameHUD.attachChild( new Sprite(horzLeft, vertUp, resourcesManager.back, vbom));
    	gameHUD.attachChild( new Sprite(horzMid, vertMid, resourcesManager.back, vbom));
    	/* moleNormy1 = new MoleNormy(300, 250,1, vbom, camera, physicsWorld){

			@Override
			public void onDie() {
				// TODO Auto-generated method stub
	
				
			}};
    	gameHUD.attachChild(moleNormy1);
    	gameHUD.attachChild( new Sprite(297, 250, resourcesManager.back, vbom));
    	moleNormy2 = new MoleNormy(549, 256,1, vbom, camera, physicsWorld){

			@Override
			public void onDie() {
				// TODO Auto-generated method stub
				
			}};
    	gameHUD.attachChild(moleNormy2);
    	gameHUD.attachChild( new Sprite(546, 256, resourcesManager.back, vbom));
    	moleNormy3 = new MoleNormy(46, 649,1, vbom, camera, physicsWorld){

			@Override
			public void onDie() {
				// TODO Auto-generated method stub
				
			}};
    	gameHUD.attachChild(moleNormy3);
    	gameHUD.attachChild( new Sprite(43, 649, resourcesManager.back, vbom));
    	moleNormy4 = new MoleNormy(300, 649,1, vbom, camera, physicsWorld){

			@Override
			public void onDie() {
				// TODO Auto-generated method stub
				
			}};
    	gameHUD.attachChild(moleNormy4);
    	gameHUD.attachChild( new Sprite(297, 649, resourcesManager.back, vbom));
    	moleNormy5 = new MoleNormy(549, 655,1, vbom, camera, physicsWorld){

			@Override
			public void onDie() {
				// TODO Auto-generated method stub
				
			}};
    	gameHUD.attachChild(moleNormy5);
    	gameHUD.attachChild( new Sprite(546, 655, resourcesManager.back, vbom));
    	moleNormy6 = new MoleNormy(45, 1071,1, vbom, camera, physicsWorld){

			@Override
			public void onDie() {
				// TODO Auto-generated method stub
				
			}};
    	gameHUD.attachChild(moleNormy6);
    	gameHUD.attachChild( new Sprite(42, 1071, resourcesManager.back, vbom));
    	moleNormy7 = new MoleNormy(297, 1071,1, vbom, camera, physicsWorld){

			@Override
			public void onDie() {
				// TODO Auto-generated method stub
				
			}};
    	gameHUD.attachChild(moleNormy7);
    	gameHUD.attachChild( new Sprite(294, 1071, resourcesManager.back, vbom));
    	moleNormy8 = new MoleNormy(546, 1071,1, vbom, camera, physicsWorld){

			@Override
			public void onDie() {
				// TODO Auto-generated method stub
				
			}};
    	gameHUD.attachChild(moleNormy8);
    	gameHUD.attachChild( new Sprite(543, 1071, resourcesManager.back, vbom)); */
    	
    }

}