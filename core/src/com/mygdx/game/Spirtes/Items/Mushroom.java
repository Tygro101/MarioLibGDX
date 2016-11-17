package com.mygdx.game.Spirtes.Items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Spirtes.Mario;
import com.mygdx.game.Tools.Singleton;

import java.util.Random;

/**
 * Created by Ran on 10/01/2016.
 */
public class Mushroom extends Item {
    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        Random rand = new Random();
        velocity = new Vector2(0.7f ,0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7/ MyGdxGame.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = MyGdxGame.ITEM_BIT; // What is this fixture
        fdef.filter.maskBits = MyGdxGame.MARIO_BIT | MyGdxGame.OBJECT_BIT | MyGdxGame.GROUND_BIT | MyGdxGame.COIN_BIT | MyGdxGame.BRICK_BIT;
        body.createFixture(fdef).setUserData(this);// the body
    }

    @Override
    public void use(Mario mario) {
        destroy();
        instance.getAssetManager().get("audio/sounds/powerup.wav", Sound.class).play();
        //mario.graw();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);

    }
}
