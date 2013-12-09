package moles;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.example.whackamole.GameScene;
import com.example.whackamole.ResourcesManager;

public class SmogyModel extends MoleModel {

	public SmogyModel(float pX, float pY, float beginY, float speed,
			ITiledTextureRegion moleSprite, GameScene scene) {
		super(pX, pY, beginY, speed * 2, moleSprite, scene);
	}
	
	public void onDie() {
		HUD gameHUD = gameScene.getGameHUD();

		gameHUD.detachChild(this);
		gameHUD.unregisterTouchArea(this);
	}

	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		HUD gameHUD = gameScene.getGameHUD();
		if(pSceneTouchEvent.isActionDown()){
			gameScene.addToScore(2);
			
			gameHUD.detachChild(this);
			gameHUD.unregisterTouchArea(this);
			return true;
		}
		return false;
	}
}



