
package com.jonatas.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jonatas.game.MyGame;

public class MenuScreen implements Screen{
    
    private MyGame game;
    private Stage stage;
    private Skin skin;

    public MenuScreen(MyGame game){
        this.game = game;
        stage = new Stage(new FitViewport(1280, 720));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.absolute("C:/Users/jonat/Documents/MACKENZIE/8_Semestre/mack-cycle-runner/jogo/assets/uiskin.json"));


        // Botão Fase 1
        TextButton btnFase1 = new TextButton("Fase 1 - Behind The Clouds", skin);
        btnFase1.setSize(300, 80);
        btnFase1.setPosition(490, 500);

        btnFase1.addListener(e -> {
            if (btnFase1.isPressed()) {
                game.setScreen(new Fase1(game));
            }
            return true;
        });

         // Botão Fase 2
        TextButton btnFase2 = new TextButton("Fase 2 - Life is A Highway", skin);
        btnFase2.setSize(300, 80);
        btnFase2.setPosition(490, 380);

        btnFase2.addListener(e -> {
            if (btnFase2.isPressed()) {
                game.setScreen(new Fase2(game));
            }
            return true;
        });


        // Botão Fase 3
        TextButton btnFase3 = new TextButton("Fase 3 - REAL GONE", skin);
        btnFase3.setSize(300, 80);
        btnFase3.setPosition(490, 260);

        btnFase3.addListener(e -> {
            if (btnFase3.isPressed()) {
                game.setScreen(new Fase3(game));
            }
            return true;
        });
           // Adiciona ao palco
        stage.addActor(btnFase1);
        stage.addActor(btnFase2);
        stage.addActor(btnFase3);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

}