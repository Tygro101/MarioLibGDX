package com.mygdx.game.Spirtes.Enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Created by Ran on 05/01/2016.
 */
public class Goomba extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean Destroyed;
    private PlayScreen screen;
    public Goomba(PlayScreen screen,float x, float y) {
        super(screen,x,y);
        this.screen = screen;
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"),i*16,0,16,16));
        }
        walkAnimation = new Animation(0.4f,frames);
        stateTime = 0;
        setBounds(getX(),getY(),16/MyGdxGame.PPM,16/MyGdxGame.PPM);
        setToDestroy = false;
        Destroyed = false;
    }

    public void update(float dt){
        stateTime+=dt;
        if(setToDestroy&&!Destroyed) {
            world.destroyBody(b2body);
            Destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"),32,0,16,16));
            stateTime = 0; // to know how long goomba is dead
        }else if(!Destroyed){
            b2body.setLinearVelocity(velocity);// velocity from the super
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight()/2);
            setRegion(walkAnimation.getKeyFrame(stateTime,true));

        }
    }

    @Override
    public void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7/ MyGdxGame.PPM);
        fdef.filter.categoryBits = MyGdxGame.ENEMY_BIT;

        fdef.filter.maskBits = MyGdxGame.BRICK_BIT | MyGdxGame.COIN_BIT | MyGdxGame.GROUND_BIT|
        MyGdxGame.MARIO_BIT|MyGdxGame.OBJECT_BIT| MyGdxGame.MARIO_BIT | MyGdxGame.ENEMY_BIT;

        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);// the body

        PolygonShape Head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5,9).scl(1/MyGdxGame.PPM);
        vertice[1] = new Vector2(5,9).scl(1/MyGdxGame.PPM);
        vertice[2] = new Vector2(-3,3).scl(1/MyGdxGame.PPM);
        vertice[3] = new Vector2(3,3).scl(1/MyGdxGame.PPM);

        Head.set(vertice);
        //// the first is for mario settings and the second for the brick so -2 it means that little to left in axes coordinates and 5 in y
        fdef.shape = Head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = MyGdxGame.ENEMY_HEAD_BIT;
        //fdef.isSensor = true; // have no colision now with box2d world
        b2body.createFixture(fdef).setUserData(this);// the head
    }

    public void draw(Batch batch){
        if(!Destroyed || stateTime < 1)// the state time is up for drowing the goomba after he is dead
            super.draw(batch);

    }
    @Override
    public void hitOnHead() {
        setToDestroy = true;
    }
}
