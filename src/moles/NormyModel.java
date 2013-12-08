package moles;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.example.whackamole.GameScene;

public class NormyModel extends MoleModel {
	public NormyModel(float pX, float pY, float beginY, float speed,
			ITiledTextureRegion moleSprite, GameScene scene) {
		super(pX, pY, beginY, speed, moleSprite, scene);
	}

	public void onDie() {
		HUD gameHUD = gameScene.getGameHUD();
		gameHUD.detachChild(this);
		gameHUD.unregisterTouchArea(this);
	}

	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		HUD gameHUD = gameScene.getGameHUD();
		gameHUD.detachChild(this);
    	gameHUD.unregisterTouchArea(this);
    	
    	gameScene.addToScore(1);
		return true;
	}
}
