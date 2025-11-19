package com.jonatas.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jonatas.game.MyGame;

public class VictoryScreen implements Screen {

    private MyGame game;
    private SpriteBatch batch;
    private FitViewport viewport;
    private Stage stage;
    private BitmapFont font;

    private Texture imgVictory;

    private int finalScore;

    public VictoryScreen(MyGame game, int finalScore) {
        this.game = game;
        this.finalScore = finalScore;

        batch = new SpriteBatch();

        imgVictory = new Texture("vitoria2.jpg");

        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.GREEN);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        // batch.draw(imgVictory, 0, 0, 1, 1);
            font.draw(batch, "VITÓRIA", 350, 350);
            font.draw(batch, "Pontuação final: " + finalScore, 350, 300);
            font.draw(batch, "Pressione ENTER para reiniciar", 300, 220);
            font.draw(batch, "Pressione ESC para voltar ao menu", 300, 180);
        batch.end();

        // DESENHA TEXTO POR CIMA

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MenuScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        // viewport.update(width, height, true);
    }

    @Override public void show() {}
    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        imgVictory.dispose();
        font.dispose();
    }
}
