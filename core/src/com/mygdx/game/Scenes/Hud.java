package com.mygdx.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

import java.io.Console;

/**
 * Created by Ran on 20/12/2015.
 */
public class Hud implements Disposable{
    private float leftFloat = 60;
    public Stage stage;
    private Integer worldTimer;
    private float timeCount;
    private static Integer score;
    private Viewport viewport;
    private Skin skin;
    public TextButton left,right,up,down;
    private Label countDownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        viewport = new FitViewport(MyGdxGame.GAME_WIDTH*3/2,MyGdxGame.GAME_HEIGHT*3/2,new OrthographicCamera());


        stage = new Stage(viewport,sb);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        countDownLabel = new Label(String.format("%03d",worldTimer),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d",score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("Time",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("World",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("Mario",new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countDownLabel).expandX();
        stage.addActor(table);
        setButtons();
       //

    }

    private void setButtons(){
        //TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("at.pack"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        left = new TextButton("<",skin,"default");
        left.setPosition(30, 50);
        left.setSize(30, 20);
        left.pad(10);
        left.setWidth(30);
        left.setHeight(20);
        left.getLabel().setFontScale(2, 2);
        right = new TextButton(">",skin,"default");
        right.setPosition(140, 50);
        right.setSize(30, 20);
        right.pad(10);
        right.setWidth(30);
        right.setHeight(20);
        right.getLabel().setFontScale(2, 2);
        right.getStyle().pressedOffsetX = 30;
        right.getStyle().pressedOffsetY = 30;
        up = new TextButton("^",skin,"default");
        up.setPosition((viewport.getWorldWidth() - leftFloat), 60);
        up.padRight(20);
        up.setWidth(30);
        up.setHeight(20);
        up.getLabel().setFontScale(2, 2);
        stage.addActor(left);
        stage.addActor(right);
        stage.addActor(up);
    }


    @Override
    public void dispose(){
        stage.dispose();
    }

    public void update(float dt){
        timeCount +=dt;
        if(timeCount>=1){
            worldTimer--;
            timeCount = 0;
            countDownLabel.setText(String.format("%03d",worldTimer));
        }
    }
    public static void addScore(Integer value){
        score+=value;
        scoreLabel.setText(String.format("%06d",score));
    }
}
