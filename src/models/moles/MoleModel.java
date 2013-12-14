package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.example.whackamole.GameScene;
import com.example.whackamole.ResourcesManager;

public abstract class MoleModel extends TiledSprite implements MoleInterface
{
    private float time, appearanceTime;
    protected GameScene gameScene;
    private LocationModel location;
    private boolean isJumping;
	protected boolean isDead;
	PhysicsConnector physicsConnector;
    protected boolean isTouched;
    LevelModel level;
	
    MoveYModifier moveY;
	private float from, to, pausedTime;
	
    public MoleModel(LocationModel location, float time, float appearanceTime,
    		ITiledTextureRegion moleSprite, LevelModel level) {
    	super(location.getX(), location.getY(), moleSprite, level.getGameScene().getVbom());
    	this.location = location;
    	
    	this.time = time;
    	this.appearanceTime = appearanceTime;
        this.isJumping = false;
    	this.isDead = false;
    	
        this.level = level;
    	this.gameScene = level.getGameScene();
    	
    	this.moveY = null;
    	this.from = -1;
    	this.to = -1;
    	this.pausedTime = -1;
    }

    public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		if (pSceneTouchEvent.isActionDown()) {
			touched();
			return true;
		}
		return false;
	}
    
    public synchronized void onDie() {
    	if (!this.isDead) {
			this.isDead = true;
    		
			if (!isTouched) {
				level.loseLives(1);
			}
			
			this.destroyMole();
			
			this.level.onMoleDeath(this);
    	}
	}
    
    protected void destroyMole() {
		HUD gameHUD = gameScene.getGameHUD();
		gameHUD.detachChild(this);
		gameHUD.unregisterTouchArea(this);
		this.dispose();
	}
    
    private void goFromTo(float from, float to, float time) {
    	if (from > to) {
    		goUp(from, to, time);
    	} else {
    		goDown(from, to, time);
    	}
    }
    
    private void goUp() {
    	goUp(location.getY(), location.getY() - 150, appearanceTime / 2);
    }
    
    private void goUp(float from, float to, float time) {
    	this.moveY = new MoveYModifier(time, from, to){
        	@Override
        	protected void onModifierFinished(IEntity pItem) {
                super.onModifierFinished(pItem);
                goDown();
        	}
        };
        moveY.setAutoUnregisterWhenFinished(true);
        this.registerEntityModifier(moveY);
    }
    
    private void goDown() {
    	goDown(location.getY() - 150, location.getY(), appearanceTime / 2);
    }
    
    private void goDown(float from, float to, float time) {
    	this.moveY = new MoveYModifier(time, from, to) {
         	@Override
         	protected void onModifierFinished(IEntity pItem) {
         		gameScene.getEngine().runOnUpdateThread(new Runnable() {
                    @Override
                    public void run() {
                        onDie();
                        moveY = null;
                    }
                });
         	}
         };
         moveY.setAutoUnregisterWhenFinished(true); 
         registerEntityModifier(moveY);
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
    	if (moveY != null) {
    		this.from = this.getY();
    		this.to = moveY.getToValue();
    		this.pausedTime = moveY.getSecondsElapsed();
    		this.unregisterEntityModifier(moveY);
    		moveY = null;
    	}
    	System.out.println("Mole: " + this + " is freezing!!!!!");
    }
    
    public void unfreeze() {
    	if (moveY == null) {
	    	this.goFromTo(this.from, this.to, (this.appearanceTime / 2) - this.pausedTime);
	    	this.from = -1;
	    	this.to = -1;
	    	this.pausedTime = -1;
    	}
    }
    
    public void jump() {
    	if (!isJumping) {
    		goUp();
    		
	        HUD gameHUD = this.gameScene.getGameHUD();
	        
	        gameHUD.attachChild(this);
	    	gameHUD.registerTouchArea(this);
	    	this.setZIndex(2);
	    	
	    	ResourcesManager resourcesManager = this.gameScene.getResourcesManager();
	        Sprite back = new Sprite(location.getX(), location.getY(),
	    			resourcesManager.back, gameScene.getVbom());
	    	back.setZIndex(3);
	    	gameHUD.attachChild(back);
	    	
	    	gameHUD.sortChildren();
	    	isJumping = true;
    	}
    }
    
    public synchronized void unavoidableTouched() {
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
    
    public boolean isDead() {
    	return this.isDead;
    }
}