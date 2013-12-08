package com.example.whackamole;

import org.andengine.engine.camera.Camera;
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



public abstract class Mole extends Sprite 
{
    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------
    private float beginY;
    private float speed;
    private VertexBufferObjectManager vbo;
    private Camera camera;
    private PhysicsWorld physicsWorld;
    
    public Mole(float pX, float pY , float beginY,  float speed, ITiledTextureRegion moleSprite, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
    {
    	
    	super(pX, pY, moleSprite, vbo);
    	this.speed = speed;
    	this.beginY = beginY;
        
        this.vbo = vbo;
        this.camera = camera;
        this.physicsWorld = physicsWorld;
        createPhysics(this.camera, this.physicsWorld);
        this.camera.setChaseEntity(this);
    }
    
    @Override
    public abstract boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY);    	


	 // ---------------------------------------------
	 // VARIABLES
	 // ---------------------------------------------
     
    private Body body;
    
    public abstract void onDie();
    
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

    public void jump()
    {
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
