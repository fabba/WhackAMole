package com.example.whackamole;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.example.whackamole.BaseScene;
import com.example.whackamole.SceneManager.SceneType;

/**
 * Scene containing only a loading screen.
 */
public class LoadingScene extends BaseScene
{
	@Override
	public void createScene() {
	    setBackground(new Background(Color.WHITE));
	    attachChild(new Text(400, 240, resourcesManager.font, "Loading...", vbom));
	}
	
    @Override
    public void onBackKeyPressed() {
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene() {
    }
}
