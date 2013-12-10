package models.moles;

import models.levels.LocationModel;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.whackamole.GameScene;
import com.example.whackamole.ResourcesManager;

public abstract class MoleModel extends TiledSprite implements MoleInterface
{
    private float time, speed, appearanceTime;
    protected GameScene gameScene;
    private LocationModel location;
    private Body body;
	
    public MoleModel(LocationModel location, float speed, float time, float appearanceTime,
    		ITiledTextureRegion moleSprite, GameScene scene)
    {
    	super(location.getX(), location.getY(), moleSprite, scene.getVbom());
    	this.speed = speed;
    	this.location = location;
    	
    	this.time = time;
    	this.appearanceTime = appearanceTime;
        
    	this.gameScene = scene;
        //this.vbo = this.gameScene.getVbom();
        //this.camera = this.gameScene.getCamera();
        //this.physicsWorld = this.gameScene.getPhysicsWorld();
        createPhysics(this.gameScene.getCamera(), this.gameScene.getPhysicsWorld());
        this.gameScene.getCamera().setChaseEntity(this);
    }

    private void createPhysics(final Camera camera, final PhysicsWorld physicsWorld)
    {        
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        body.setFixedRotation(true);
        
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {
            
        	@Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);

                if (getY() > location.getBeginY()) {
                    onDie();
                    destroy(this);
                    
                }
                
                else if (getY() < (location.getBeginY() - 150)) {    
                	body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, speed)); 
                }
            }
        });
    }
    
    public void destroy(PhysicsConnector tPhysicsConnector){
    	PhysicsWorld physicsWorld = this.gameScene.getPhysicsWorld();
    	physicsWorld.unregisterPhysicsConnector(tPhysicsConnector);
    	physicsWorld.destroyBody(tPhysicsConnector.getBody());
    }
    public float getSpeed(){	
    	return this.speed;
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

    public void jump() {
        body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, -speed));
        
        HUD gameHUD = this.gameScene.getGameHUD();
        ResourcesManager resourcesManager = this.gameScene.getResourcesManager();
        
        gameHUD.attachChild(this);
    	gameHUD.registerTouchArea(this);
    	gameHUD.attachChild( new Sprite(location.getX(), location.getY(),
    			resourcesManager.back, gameScene.getVbom()));
    }
    
    public VertexBufferObjectManager getVbo(){
    	return this.gameScene.getVbom();
    }
    
    public Camera getCamera(){
    	return this.gameScene.getCamera();
    }
    
    public PhysicsWorld getPhysicsWorld(){
    	return this.gameScene.getPhysicsWorld();
    }
}