package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.whackamole.GameScene;
import com.example.whackamole.ResourcesManager;

public abstract class MoleModel extends TiledSprite implements MoleInterface
{
    private float time, speed, appearanceTime;
    protected GameScene gameScene;
    private LocationModel location;
    private Body body;
	private boolean isJumping;
	PhysicsConnector physicsConnector;
    protected boolean isTouched;
    LevelModel level;
	
    public MoleModel(LocationModel location, float time, float appearanceTime,
    		ITiledTextureRegion moleSprite, LevelModel level) {
    	super(location.getX(), location.getY(), moleSprite, level.getGameScene().getVbom());
    	this.speed = -1; // TODO calculate from time.
    	this.location = location;
    	
    	this.time = time;
    	this.appearanceTime = appearanceTime;
        this.isJumping = false;
    	
        this.level = level;
    	this.gameScene = level.getGameScene();
    }

    public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		if (pSceneTouchEvent.isActionDown()) {
			touched();
			return true;
		}
		return false;
	}
    
    public void onDie() {
		if (!isTouched) {
			gameScene.loseLife();
		}
		
		this.destroyMole();
		
		this.level.onMoleDeath(this);
	}
    
    protected void destroyMole() {
    	HUD gameHUD = gameScene.getGameHUD();
		gameHUD.detachChild(this);
		gameHUD.unregisterTouchArea(this);
		this.dispose();
		destroy(physicsConnector);
    }
    
    private void createPhysics(final Camera camera, final PhysicsWorld physicsWorld) {
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        body.setFixedRotation(true);
        
        this.physicsConnector = new PhysicsConnector(this, body, true, false) {
            
        	@Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);

                if (getY() > location.getBeginY()) {
                    onDie();
                }
                
                else if (getY() < (location.getBeginY() - 150)) {    
                	speed = -speed;
                	body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, speed)); 
                }
            }
        };
        
        physicsWorld.registerPhysicsConnector(physicsConnector);
    }
    
    public void destroy(PhysicsConnector tPhysicsConnector){
    	PhysicsWorld physicsWorld = this.gameScene.getPhysicsWorld();
    	physicsWorld.unregisterPhysicsConnector(tPhysicsConnector);
    	physicsWorld.destroyBody(tPhysicsConnector.getBody());
    }
    
    public float getSpeed(){	
    	return this.speed;
    }
    
    public float getStartY(){
    	return this.location.getBeginY();
    }
    
    public float getTime() {
    	return this.time;
    }
    
    public float getAppearanceTime() {
    	return this.appearanceTime;
    }
    
    public LocationModel getLocation() {
    	return this.location;
    }

    public void freeze() {
    	body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 0));
    }
    
    public void unfreeze() {
    	body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, this.getSpeed()));
    }
    
    public void jump() {
    	if (!isJumping) {
    		createPhysics(this.gameScene.getCamera(), this.gameScene.getPhysicsWorld());
            this.gameScene.getCamera().setChaseEntity(this);
    		
	        body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, speed));
	        
	        HUD gameHUD = this.gameScene.getGameHUD();
	        ResourcesManager resourcesManager = this.gameScene.getResourcesManager();
	        
	        gameHUD.attachChild(this);
	    	gameHUD.registerTouchArea(this);
	    	gameHUD.attachChild( new Sprite(location.getX(), location.getY(),
	    			resourcesManager.back, gameScene.getVbom()));
	    	
	    	isJumping = true;
    	}
    }
    
    public void unavoidableTouched() {
		touched();
	}
    
    public VertexBufferObjectManager getVbo() {
    	return this.gameScene.getVbom();
    }
    
    public Camera getCamera() {
    	return this.gameScene.getCamera();
    }
    
    public PhysicsWorld getPhysicsWorld() {
    	return this.gameScene.getPhysicsWorld();
    }
}