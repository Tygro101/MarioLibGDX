package com.mygdx.game.Tools;

import com.badlogic.gdx.assets.AssetManager;

/**
 * Created by Ran on 04/01/2016.
 */
public class Singleton {
    public static Singleton instance;
    private AssetManager manager;
    private Singleton(){
    }
    public static Singleton getInstance(){
        if(instance==null)
            instance = new Singleton();
        return instance;
    }

    public void setAssetManager(AssetManager manager) {
        this.manager = manager;
    }

    public AssetManager getAssetManager(){
        return manager;
    }
}
