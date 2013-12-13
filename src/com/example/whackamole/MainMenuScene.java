package com.example.whackamole;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.andengine.engine.camera.Camera;

import com.example.whackamole.SceneManager.SceneType;



public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener {

	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
	
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		
		
	}
	
	private void createBackground()
	{
	    attachChild(new Sprite(0, 0, resourcesManager.menu_background_region, vbom)
	    {
	        @Override
	        protected void preDraw(GLState pGLState, Camera pCamera) 
	        {
	            super.preDraw(pGLState, pCamera);
	            pGLState.enableDither();
	        }
	    });
	}
	
	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private final int MENU_RESUME = 1;
	private final int MENU_SETTING = 1;
	private final int MENU_SCORE = 1;
	private final int MENU_MANUAL = 1;

	private void createMenuChildScene()
	{
	    menuChildScene = new MenuScene(camera);
	    menuChildScene.setPosition(0, 0);
	    
	    final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
	    final IMenuItem settingsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_RESUME, resourcesManager.resume_region, vbom), 1.2f, 1);
	    final IMenuItem resumeMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SCORE, resourcesManager.score_region, vbom), 1.2f, 1);
	    final IMenuItem scoreMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_SETTING, resourcesManager.settings_region, vbom), 1.2f, 1);
	    final IMenuItem manualMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_MANUAL, resourcesManager.manual_region, vbom), 1.2f, 1);
	
	    menuChildScene.addMenuItem(playMenuItem);
	    menuChildScene.addMenuItem(settingsMenuItem);
	    menuChildScene.addMenuItem(resumeMenuItem);
	    menuChildScene.addMenuItem(scoreMenuItem);
	    menuChildScene.addMenuItem(manualMenuItem);
	    
	    menuChildScene.buildAnimations();
	    menuChildScene.setBackgroundEnabled(false);
	    
	    playMenuItem.setPosition(350, 300);
	    resumeMenuItem.setPosition(350, 400);
	    settingsMenuItem.setPosition(350, 500);
	    scoreMenuItem.setPosition(350, 600);
	    manualMenuItem.setPosition(350, 700);
	  
	    
	    menuChildScene.setOnMenuItemClickListener(this);
	    
	    setChildScene(menuChildScene);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID())
        {
        case MENU_PLAY:
        	SceneManager.getInstance().loadGameScene(engine);
            return true;
        case MENU_SETTING:
            return true;
        default:
            return false;
    }
	}

}
