package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Spirtes.Items.Item;
import com.mygdx.game.Spirtes.Items.ItemDef;
import com.mygdx.game.Spirtes.Items.Mushroom;
import com.mygdx.game.Spirtes.Mario;
import com.mygdx.game.Spirtes.Enemy.Enemy;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.Singleton;
import com.mygdx.game.Tools.WorldContactFilter;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * Created by Ran on 20/12/2015.
 */
public class PlayScreen implements Screen {
    // settings
    private Singleton instance;

    // Camera
    private MyGdxGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private Mario mario;

    // input handle
    private boolean isRightPressed = false;
    private boolean isLeftPressed = false;
    private boolean isUpPressed = false;

    // map files
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mRend;

    // World
    private World world;
    private Box2DDebugRenderer box2Dr;


    // Images
    private TextureAtlas Atlas;


    // Music
    private Music music;


    // Enemy
    private B2WorldCreator b2WorldCreator;


    //items
    private Array<Item> items;
    private LinkedBlockingDeque<ItemDef> itemsToSpawn;

    public PlayScreen(MyGdxGame game){
        if(instance==null)
            instance = Singleton.getInstance();
        this.game = game;
        // Hud Initialization
        gameCam = new OrthographicCamera();
        hud = new Hud(game.batch);
        gamePort = new FitViewport(MyGdxGame.GAME_WIDTH/MyGdxGame.PPM,MyGdxGame.GAME_HEIGHT/ MyGdxGame.PPM,gameCam); // game cam is the commander
        // map Initialization
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        mRend = new OrthogonalTiledMapRenderer(map,1/MyGdxGame.PPM);
        gameCam.position.set(gamePort.getWorldWidth()/2f,gamePort.getWorldHeight()/2f,0);
        // World Initialize
        world = new World(new Vector2(0,-10),true);
        box2Dr = new Box2DDebugRenderer();

        Atlas = new TextureAtlas("Mario pack/Mario_and_Enemies.pack");
        b2WorldCreator = new B2WorldCreator(this);
        mario = new Mario(this);

        world.setContactListener(new WorldContactFilter());

        music = instance.getAssetManager().get("audio/music/mario_music.ogg",Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingDeque<ItemDef>();
        //goomba = new Goomba(this,0.64f,.32f);// goomba position
        //b2WorldCreator = new B2WorldCreator(this);
        // Input handling
        hud.right.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                isRightPressed = true;
            }
        });
        hud.right.addListener(new ActorGestureListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                isRightPressed = false;
            }
        });
        hud.left.addListener(new ActorGestureListener(){
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                isLeftPressed = true;
            }
        });
        hud.left.addListener(new ActorGestureListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                isLeftPressed = false;
            }
        });
        hud.up.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                isUpPressed = true;
            }
        });
        hud.up.addListener(new ActorGestureListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                isUpPressed = false;
            }
        });

    }

    // do all updating in our game world
    public void Update(float dt){
        HundleInput(dt);
        handleSpwaninigItems();
        world.step(1 / 60f, 6, 2);
        gameCam.position.x = mario.b2body.getPosition().x;
        gameCam.update();
        mRend.setView(gameCam);  // this is what makes the screen fit the map
        mario.update(dt);
        hud.update(dt);
        for(Enemy enemy:b2WorldCreator.getGoombas()){
            enemy.update(dt);
            if(enemy.getX()<mario.getX()+224/MyGdxGame.PPM)
                enemy.getB2body().setActive(true);
        }

        for(Item item : items)
            item.update(dt);
    }


    //Atlas
    public TextureAtlas getAtlas(){
        return Atlas;
    }
    public void HundleInput(final float dt) {
        if(isRightPressed && (mario.b2body.getLinearVelocity().x)<2){
            mario.b2body.applyLinearImpulse(new Vector2(0.15f,0),mario.b2body.getWorldCenter(),true);
        }
        if(isLeftPressed && Math.abs(mario.b2body.getLinearVelocity().x)<2){
            mario.b2body.applyLinearImpulse(new Vector2(-0.15f,0),mario.b2body.getWorldCenter(),true);
        }
        if(isUpPressed && !(Math.abs(mario.b2body.getLinearVelocity().y)>0)){
            if(instance!=null)
                instance.getAssetManager().get("audio/sounds/jump_small.wav", Sound.class).play();
            mario.b2body.applyLinearImpulse(new Vector2(0,4f),mario.b2body.getWorldCenter(),true);
            isUpPressed = false;
        }

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mRend.render();
        box2Dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();

        // draw the region on mario class (the draw is in the sprite extends)
        mario.draw(game.batch);
        for(Enemy enemy:b2WorldCreator.getGoombas())
            enemy.draw(game.batch);
        for(Item item : items)
            item.draw(game.batch);
        game.batch.end();
        // the randering
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.act(Gdx.graphics.getDeltaTime());

        hud.stage.draw();


    }


    public void spwanItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }

    public void handleSpwaninigItems(){ // every render time this function is called
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll(); // why only one??
            if(idef.type  == Mushroom.class){
                items.add(new Mushroom(this,idef.position.x,idef.position.y));
            }
        }
    }
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
        map.dispose();
        mRend.dispose();
        box2Dr.dispose();
        world.dispose();
        hud.dispose();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }




}
