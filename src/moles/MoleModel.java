package moles;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.whackamole.GameScene;



public abstract class MoleModel extends Sprite implements MoleInterface
{
    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------
    private float beginY;
    private float speed;
    private VertexBufferObjectManager vbo;
    private Camera camera;
    private PhysicsWorld physicsWorld;
    protected GameScene gameScene;
    
    public MoleModel(float pX, float pY, float beginY, float speed,
    		ITiledTextureRegion moleSprite, GameScene scene)
    {
    	super(pX, pY, moleSprite, scene.getVbom());
    	this.speed = speed;
    	this.beginY = beginY;
        
    	this.gameScene = scene;
        this.vbo = this.gameScene.getVbom();
        this.camera = this.gameScene.getCamera();
        this.physicsWorld = this.gameScene.getPhysicsWorld();
        createPhysics(this.camera, this.physicsWorld);
        this.camera.setChaseEntity(this);
    }

	 // ---------------------------------------------
	 // VARIABLES
	 // ---------------------------------------------
     
    private Body body;
	
    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
    {        
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        body.setFixedRotation(true);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
        {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);
                if (getY() > beginY)
                {                    
                    onDie();
                }
                
                if (getY() < ( beginY - 150))
                {    
                	body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, speed)); 
                }
            }
        });
    }
    
    public float getSpeed(){	
    	return this.speed;
    }
    
    public float getStartY(){
    	return this.beginY;
    }

    public void jump() {
        body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, -speed));
    }
    
    public VertexBufferObjectManager getVbo(){
    	return this.vbo;
    }
    
    public Camera getCamera(){
    	return this.camera;
    }
    
    public PhysicsWorld getPhysicsWorld(){
    	return this.physicsWorld;
    }
}
