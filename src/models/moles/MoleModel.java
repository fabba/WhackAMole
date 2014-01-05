package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.example.whackamole.GameScene;

/**
 * The mole model, specifies its behaviour on death and when touched and
 * its movement.
 */
public abstract class MoleModel extends TiledSprite {
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
    
    /**
     * Called when the mole has died.
     * Notifies the level and by default substracts 1 life.
     */
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
    
    protected synchronized void destroyMole() {
		HUD gameHUD = gameScene.getGameHUD();
		gameHUD.detachChild(this);
		gameHUD.unregisterTouchArea(this);
		this.dispose();
	}
    
    /**
     * Go from location from to location to in time.
     * @param from
     * @param to
     * @param time
     */
    private void goFromTo(float from, float to, float time) {
    	if (from > to) {
    		goUp(from, to, time);
    	} else {
    		goDown(from, to, time);
    	}
    }
    
    /**
     * Move upwards (negative direction) by 150px from location's y.
     */
    private void goUp() {
    	goUp(location.getY(), location.getY() - 150, appearanceTime / 2);
    }
    
    /**
     * Move upwards (negative direction) from location from to location to in time.
     */
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
    
    /**
     * Move downwards (positive direction) by 150px from location's y-150.
     */
    private void goDown() {
    	goDown(location.getY() - 150, location.getY(), appearanceTime / 2);
    }
    
    /**
     * Move downwards (positive direction) from location from to location to in time.
     */
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
    
    public float getStartY() {
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

    /**
     * Freezes the mole and its movement.
     */
    public void freeze() {
    	if (moveY != null) {
    		this.from = this.getY();
    		this.to = moveY.getToValue();
    		this.pausedTime = moveY.getSecondsElapsed();
    		this.unregisterEntityModifier(moveY);
    		moveY = null;
    	}
    }
    
    /**
     * Unfreezes the mole and continues its movement.
     */
    public void unfreeze() {
    	if (moveY == null) {
	    	this.goFromTo(this.from, this.to, (this.appearanceTime / 2) - this.pausedTime);
	    	this.from = -1;
	    	this.to = -1;
	    	this.pausedTime = -1;
    	}
    }
    
    /**
     * Makes the mole jump from its location.
     */
    public void jump() {
    	if (!isJumping) {
    		goUp();
    		
	        HUD gameHUD = this.gameScene.getGameHUD();
	        
	        gameHUD.attachChild(this);
	    	gameHUD.registerTouchArea(this);
	    	this.setZIndex(1);
	    	
	    	gameHUD.sortChildren();
	    	
	    	isJumping = true;
    	}
    }
    
    /**
     * Called when the mole is unavoidably touched, thus the user should
     * not be punished! By default calls touched.
     */
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
    
    /**
     * This function should implement the moles behavior if it is touched.
     */
    public abstract void touched();
}