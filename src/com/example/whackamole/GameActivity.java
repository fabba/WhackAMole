package com.example.whackamole;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;


public class GameActivity extends BaseGameActivity
{
	private Camera camera;
	private ResourcesManager resourcesManager;
	private static SharedPreferences staticSetting = null;
	private static Context context;
	
    public EngineOptions onCreateEngineOptions() {
    	camera = new Camera(0, 0, 720, 1280);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(720, 1280), this.camera);
        // TODO remove
        //engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return engineOptions;
    }

    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException {
    	ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
        resourcesManager = ResourcesManager.getInstance();
        pOnCreateResourcesCallback.onCreateResourcesFinished();
        staticSetting = this.getPreferences("Setting", MODE_PRIVATE);
        context = this;
    }
    
    private SharedPreferences getPreferences(String string, int modePrivate) {
		return getSharedPreferences(string,modePrivate);
	}
    
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException {
    	SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
    }

    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
    	 mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
    	    {
    	        public void onTimePassed(final TimerHandler pTimerHandler) 
    	        {
    	            mEngine.unregisterUpdateHandler(pTimerHandler);
    	            SceneManager.getInstance().loadGameScene(mEngine);
    	        }
    	    }
    	 ));
    	 pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
    
    public ResourcesManager getResourcesManager() {
    	return this.resourcesManager;
    }
    
    public static String getName() {
		return staticSetting.getString("Name", "Player");
	}
	
	public static int getStartLevel() {
		return staticSetting.getInt("Startlevel", 1);
	}
	
	public static int getStartRound() {
		return staticSetting.getInt("Startround", 1);
	}
	
	/**
	 * Start the score activity.
	 */
	public static void goToScore() {
    	Intent intent = new Intent(context, ScoreActivity.class);
    	context.startActivity(intent);
    }
    
	/**
	 * Start the main activity.
	 */
    public static void goToMain() {
    	Intent intent = new Intent(context, MainActivity.class);
    	context.startActivity(intent);
    }
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
        }
        return false; 
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
        System.exit(0);	
    }
    
    @Override
    protected void onPause() {
    	goToMain();
    	super.onPause();
    }
}
