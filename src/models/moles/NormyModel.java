package models.moles;

import models.levels.LocationModel;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.example.whackamole.GameScene;

public class NormyModel extends MoleModel {
	private boolean touched;
	public NormyModel(LocationModel location, float speed, float time,
			float appearanceTime, ITiledTextureRegion moleSprite,
			GameScene scene) {
		
		super(location, speed, time, appearanceTime, moleSprite, scene);
		touched = false;
	}

	public void onDie() {
		
		HUD gameHUD = gameScene.getGameHUD();
		if(!touched){
			gameHUD.detachChild(this);
			gameHUD.unregisterTouchArea(this);
			gameScene.loseLife();
			this.dispose();
		}
	}

	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if(pSceneTouchEvent.isActionDown()){
			touched();
			return true;
		}
		return false;
	}
	
	public void touched(){
		HUD gameHUD = gameScene.getGameHUD();
		gameHUD.detachChild(this);
    	gameHUD.unregisterTouchArea(this);
    	gameScene.addToScore(1);
    	this.dispose();
    	touched = true;
	}
}
