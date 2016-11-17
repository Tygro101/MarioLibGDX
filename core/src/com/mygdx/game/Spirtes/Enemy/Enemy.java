package com.mygdx.game.Spirtes.Enemy;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Created by Ran on 05/01/2016.
 */
public abstract class Enemy extends Sprite{
    protected World world;
    protected TiledMap map;

    public Body getB2body() {
        return b2body;
    }

    protected Body b2body;
    protected Vector2 velocity;
    public Enemy(PlayScreen screen,float x,float y){
        this.world = screen.getWorld();
        this.map = screen.getMap();
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(1,0);
        b2body.setActive(false);
    }
    public abstract void defineEnemy();
    public abstract void hitOnHead();
    public void reverceVelocity(boolean x,boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
    public abstract void update(float dt);

}
