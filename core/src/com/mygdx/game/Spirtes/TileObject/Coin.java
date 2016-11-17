package com.mygdx.game.Spirtes.TileObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Spirtes.Items.ItemDef;
import com.mygdx.game.Spirtes.Items.Mushroom;

/**
 * Created by Ran on 23/12/2015.
 */
public class Coin extends InteractiveTileObject{
    private static TiledMapTileSet tileSat; // Tile map start count from zero index but TileMapSet(lib gdx) starts form 1 index so we need the int below
    private final int BRICK_COIN = 28;
    public Coin(PlayScreen screen,MapObject object){
        super(screen,object);
        tileSat = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCatigoryFilter(MyGdxGame.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Col");
        if(getCell(1).getTile().getId()==BRICK_COIN)
            instance.getAssetManager().get("audio/sounds/bump.wav", Sound.class).play();
        else {

            getCell(1).setTile(tileSat.getTile(BRICK_COIN));
            if(object.getProperties().containsKey("mushroom")) {
                screen.spwanItem(new ItemDef(new Vector2(body.getPosition().x,body.getPosition().y + 16 / MyGdxGame.PPM), Mushroom.class));
                instance.getAssetManager().get("audio/sounds/powerup_appears.wav", Sound.class).play();
            }else {
                instance.getAssetManager().get("audio/sounds/coin.wav", Sound.class).play();
                Hud.addScore(100);
            }
        }
    }


}
