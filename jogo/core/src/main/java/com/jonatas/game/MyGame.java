package com.jonatas.game;


import com.badlogic.gdx.Game;
import com.jonatas.game.screens.MenuScreen;


public class MyGame extends Game {

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
}