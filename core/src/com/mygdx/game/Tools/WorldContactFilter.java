package com.mygdx.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Spirtes.Enemy.Enemy;
import com.mygdx.game.Spirtes.Items.Item;
import com.mygdx.game.Spirtes.Mario;
import com.mygdx.game.Spirtes.TileObject.InteractiveTileObject;

/**
 * Created by Ran on 02/01/2016.
 */
public class WorldContactFilter implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        if(fixA.getUserData()!=null && fixB.getUserData() !=null)
            if (fixA.getUserData().equals("Head") || fixB.getUserData().equals("Head")) {
                Fixture head = fixA.getUserData() == "Head" ? fixA : fixB; // get mario object
                Fixture object = head == fixA ? fixB : fixA; // get collide object
                if (object != null && object.getUserData() instanceof InteractiveTileObject) { // check if this object is interactive tile one
                    ((InteractiveTileObject) object.getUserData()).onHeadHit();
                }
            }
        switch (cDef){
            case MyGdxGame.ENEMY_HEAD_BIT | MyGdxGame.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MyGdxGame.ENEMY_HEAD_BIT){
                    ((Enemy)fixA.getUserData()).hitOnHead();
                }//this is the enemy (need to understand way
                else if(fixB.getFilterData().categoryBits == MyGdxGame.ENEMY_HEAD_BIT){
                    ((Enemy)fixA.getUserData()).hitOnHead();
                }//this is the enemy (need to understand way
                break;
            case MyGdxGame.ENEMY_BIT | MyGdxGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MyGdxGame.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverceVelocity(true,false);
                else
                    ((Enemy)fixB.getUserData()).reverceVelocity(true,false);
                break;
            case MyGdxGame.ENEMY_BIT | MyGdxGame.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverceVelocity(true,false);
                ((Enemy)fixB.getUserData()).reverceVelocity(true,false);
                break;
            case MyGdxGame.ITEM_BIT | MyGdxGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MyGdxGame.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverceVelocity(true,false);
                else
                    ((Item)fixB.getUserData()).reverceVelocity(true,false);
                break;
            case MyGdxGame.ITEM_BIT | MyGdxGame.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MyGdxGame.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Mario)fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Mario)fixA.getUserData());
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
