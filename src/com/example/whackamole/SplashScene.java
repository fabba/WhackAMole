package com.example.whackamole;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.example.whackamole.BaseScene;
import com.example.whackamole.SceneManager.SceneType;

/**
 * @author Mateusz Mysliwiec
 * @author www.matim-dev.com
 * @version 1.0
 */
public class SplashScene extends BaseScene {
	
	private Sprite splash;
	
    @Override
    /*
     * Creates a splashscreen which will appear between the main menu and the game scene
     */
    public void createScene() {
    	splash = new Sprite(0, 0, resourcesManager.splash_region, vbom) {
    	    @Override
    	    protected void preDraw(GLState pGLState, Camera pCamera) {
    	       super.preDraw(pGLState, pCamera);
    	       pGLState.enableDither();
    	    }
    	};
    	        
    	splash.setScale(1.5f);
    	splash.setPosition(150, 260);
    	attachChild(splash);
    }

    @Override
    public void onBackKeyPressed() {
    }

    @Override
    public SceneType getSceneType() {
    	return SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene() {
    	splash.detachSelf();
        splash.dispose();
        this.detachSelf();
        this.dispose();
    }
}