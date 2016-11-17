package com.mygdx.game.Spirtes.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Spirtes.Mario;
import com.mygdx.game.Tools.Singleton;

import java.util.Base64;

/**
 * Created by Ran on 10/01/2016.
 */
public abstract class Item extends Sprite{
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean Destroyed;
    protected Body body;
    protected Singleton instance;
    public Item(PlayScreen screen,float x,float y){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), 16 / MyGdxGame.PPM, 16 / MyGdxGame.PPM);// picture size itself
        defineItem();
        toDestroy = false;
        Destroyed = false;
        instance = Singleton.getInstance();
    }
    public abstract void defineItem();
    public abstract void use(Mario mario);
    public void update(float dt){
        if(toDestroy && !Destroyed){
            world.destroyBody(body);
            Destroyed = true;
        }
    }
    public void draw(Batch batch){
        if (!Destroyed)
            super.draw(batch);
    }
    public void destroy(){
        toDestroy = true;
    }


    public void reverceVelocity(boolean x,boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
}
