package com.jonatas.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.jonatas.game.MyGame;

public class Fase2 implements Screen {
    
    private MyGame game;

    public Fase2(MyGame game){
        this.game = game;
    }

    @Override
    public void render(float delta) {
        // Aqui vai toda a lógica da fase 1 (dog, discos, UI…)

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                game.setScreen(new MenuScreen(game));
        }
    }

    @Override public void resize(int w, int h) {}
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {}
}