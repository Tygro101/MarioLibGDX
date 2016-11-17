package com.mygdx.game.Spirtes.TileObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Created by Ran on 22/12/2015.
 */
public class Brick extends InteractiveTileObject{
    public Brick(PlayScreen screen,MapObject object)
    {
        super(screen,object);
        fixture.setUserData(this);
        setCatigoryFilter(MyGdxGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Brick","Col");
        setCatigoryFilter(MyGdxGame.DESTROYED_BIT);
        getCell(1).setTile(null);
        Hud.addScore(200);
        instance.getAssetManager().get("audio/sounds/breakblock.wav", Sound.class).play();

    }
}
