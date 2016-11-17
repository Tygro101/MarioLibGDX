package com.mygdx.game.Spirtes.TileObject;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.Singleton;

/**
 * Created by Ran on 23/12/2015.
 */
public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected Body body;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Fixture fixture;
    protected Singleton instance;
    protected PlayScreen screen;
    protected MapObject object;
    public InteractiveTileObject(PlayScreen screen,MapObject object){
        this.object = object;
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();;
        if(instance == null)
            instance = Singleton.getInstance();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / MyGdxGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / MyGdxGame.PPM);
        body = world.createBody(bdef);
        shape.setAsBox(bounds.getWidth() / 2 / MyGdxGame.PPM, bounds.getHeight() / 2 / MyGdxGame.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = MyGdxGame.OBJECT_BIT;
        fixture = body.createFixture(fdef);

    }

    public abstract void onHeadHit();
    public void setCatigoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(int Layer){
        TiledMapTileLayer layer = ((TiledMapTileLayer)map.getLayers().get(Layer));
        return layer.getCell((int)(body.getPosition().x*MyGdxGame.PPM / 16),(int)(body.getPosition().y*MyGdxGame.PPM/16));
    }
}
