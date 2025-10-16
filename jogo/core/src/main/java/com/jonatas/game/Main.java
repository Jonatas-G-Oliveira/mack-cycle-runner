package com.jonatas.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {
    Texture fundoExemplo;
    Texture personagemPrincipal;

    Sprite personagemPrincipalSprite;
    Sprite fundoExemploSprite; 

    SpriteBatch spriteBatch;
    FitViewport viewport;

    boolean inverterPosicao = false;

    Vector2 touchPos;

    @Override
    public void create() {
        // ------ Carregando imagens
        fundoExemplo = new Texture("fundo_ex.jpg");
        personagemPrincipal = new Texture("treecko.png");

        // ------ Definindo Sprites
        personagemPrincipalSprite = new Sprite(personagemPrincipal);
        personagemPrincipalSprite.setSize(1, 1);

        // ------ Iniciando SpriteBatch
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        //Capturando informações de click
        touchPos = new Vector2();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height,true);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    //Tudo que é de clicar,de entrada a gente ve por aqui
    public void input(){
        float velocidade = .5f;
        float delta = Gdx.graphics.getDeltaTime(); //rodar em qualquer lugar


        if(Gdx.input.justTouched()){
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); //Pegando informações de onde foi clicado>>>>>
            if (inverterPosicao) {
                personagemPrincipalSprite.translateY(velocidade * delta * 150);
            }else{
                personagemPrincipalSprite.translateY(-velocidade * delta* 100);
            }
            inverterPosicao = !inverterPosicao;
        }
     
    }

    //Tudo que foi
    public void logic(){
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        float personagemPrincipalWidth = personagemPrincipalSprite.getWidth();
        float personagemPrincipalHeight = personagemPrincipalSprite.getHeight();

        //Vamos impedir que o personagem sai da tela para valores entre 0 e a altura do mundo
        personagemPrincipalSprite.setY(MathUtils.clamp(personagemPrincipalSprite.getY(), 0, worldHeight - personagemPrincipalHeight));
    }

    public void draw(){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        //Desenhar as coisas é por aqui
        spriteBatch.begin();

        spriteBatch.draw(fundoExemplo, 0, 0, viewport.getWorldHeight(), viewport.getWorldWidth());
        personagemPrincipalSprite.draw(spriteBatch);

        spriteBatch.end();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {

    }
}