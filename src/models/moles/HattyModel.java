package models.moles;

import models.levels.LocationModel;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.example.whackamole.GameScene;
import com.example.whackamole.ResourcesManager;

public class HattyModel extends MoleModel {
	
	private boolean touched;
	public HattyModel(LocationModel location, float speed, float time,
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
		HUD gameHUD = gameScene.getGameHUD();
		if(pSceneTouchEvent.isActionDown()){
			if(getCurrentTileIndex() == 0){
				setCurrentTileIndex(1);
				gameScene.addToScore(1);
				return true;
				
			}
			else{
				gameHUD.detachChild(this);
		    	gameHUD.unregisterTouchArea(this);
		    	gameScene.addToScore(1);
		    	this.dispose();
		    	touched = true;
				return true;
			}
	    	
		}
		return false;
	}
}

