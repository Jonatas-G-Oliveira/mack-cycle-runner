package com.jonatas.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jonatas.game.MyGame;

public class GameOverScreen implements Screen {

    private MyGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private int score;

    public GameOverScreen(MyGame game, int score){
        this.game = game;
        this.score = score;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(2f);
    }

    @Override
    public void render(float delta) {
        batch.begin();

        font.draw(batch, "GAME OVER", 350, 350);
        font.draw(batch, "Pontuação final: " + score, 350, 300);
        font.draw(batch, "Pressione ENTER para reiniciar", 300, 220);
        font.draw(batch, "Pressione ESC para voltar ao menu", 300, 180);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new Fase1(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { batch.dispose(); }
}