package com.mygdx.game.Spirtes;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

import javax.xml.soap.Text;

/**
 * Created by Ran on 22/12/2015.
 */
public class Mario extends Sprite {
    // Animation Running
    public enum State {FALLING , RUINING, JUMPING, STANDING};
    public State currentState;
    public State previousState;
    private float stateTimer;
    private boolean runningRight;
    private Animation marioRun;
    private Animation marioJump;
    private PlayScreen screen;


    public World world;
    public Body b2body;
    private TextureRegion marioStand;


    public  Mario(PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();
        // new
        this.screen = screen;
        //


        // Animation
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1 ; i<4 ; i++)
            frames.add(new TextureRegion(getTexture(),i*16,10,16,18));
        marioRun = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 4 ; i<6 ; i++)
            frames.add(new TextureRegion(getTexture(),i*16,10,16,18));
        marioJump = new Animation(0.1f,frames);
        frames.clear();

        initialMario();
        marioStand = new TextureRegion(getTexture(),0,10,16,18);
        setBounds(0,0,16/MyGdxGame.PPM,16/MyGdxGame.PPM);
        setRegion(marioStand);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));
    }

    public void Graw(){
        //super(screen.getAtlas().findRegion("little_mario"));
    }

    private TextureRegion
            (float dt){
        currentState = getState();
        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUINING:
                region = marioRun.getKeyFrame(stateTimer,true); // true loop animation
                break;
            case FALLING:

            case STANDING:
            default:
                region = marioStand;
        }
        if((b2body.getLinearVelocity().x<0 || !runningRight) && !region.isFlipX()){
            region.flip(true,false);
            runningRight = false;
        }else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return  region;
    }

    private State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y<0 && previousState == State.JUMPING))
            return State.JUMPING;
        if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        if(b2body.getLinearVelocity().x!=0)
            return State.RUINING;
        else
            return State.STANDING;
    }
    private void initialMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(33/ MyGdxGame.PPM,33/ MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7/ MyGdxGame.PPM);
        fdef.filter.categoryBits = MyGdxGame.MARIO_BIT;

        fdef.filter.maskBits = MyGdxGame.BRICK_BIT | MyGdxGame.COIN_BIT | MyGdxGame.GROUND_BIT | MyGdxGame.OBJECT_BIT | MyGdxGame.ENEMY_BIT
                | MyGdxGame.ENEMY_HEAD_BIT | MyGdxGame.ITEM_BIT ;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape Head = new EdgeShape();
        Head.set(new Vector2(-2/MyGdxGame.PPM,7/MyGdxGame.PPM),new Vector2(2/MyGdxGame.PPM,7/MyGdxGame.PPM));
        // the first is for mario settings and the second for the brick so -2 it means that little to left in axes coordinates and 5 in y
        fdef.shape = Head;
        fdef.isSensor = true; // have no colision now with box2d world
        b2body.createFixture(fdef).setUserData("Head");
    }

}
