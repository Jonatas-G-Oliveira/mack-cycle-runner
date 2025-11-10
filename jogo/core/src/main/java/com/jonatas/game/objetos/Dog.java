package com.jonatas.game.objetos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

//As animações tinham que ficar aqui também,ver isso
public class Dog extends GameObject{
    private Vector2 touchPos;
    private Viewport viewport;
    private boolean inverterPosicao;
    private float posX, posY;

    //Animação
    Animation<TextureRegion> animacaoIdle;
    TextureRegion frameAtual;
    float stateTimeIdle;

    //Usando o construtor dinãmico
    public Dog(float x, float y, float largura, float altura, Viewport viewport){
        super(x, y, largura, altura);
        this.viewport = viewport;
        this.touchPos = new Vector2();
        this.inverterPosicao = false;
        this.posY = y;
        this.posX = x;
        carregarAnimacoes();
    }

    private void carregarAnimacoes(){
        TextureRegion[] framesIdle = new TextureRegion[4];
        for(int i=0; i< 4;i++){
            Texture t = new Texture("player-idle-"+(i+1)+".png");
            framesIdle[i] = new TextureRegion(t);
        }
        animacaoIdle = new Animation<>(0.1f, framesIdle);
        frameAtual = framesIdle[0];
        float stateTimeIdle = 0f;
    }


    @Override
    public void update(float delta){
        float velocidade = 3f;
         //rodar em qualquer lugar


        if(Gdx.input.justTouched()){
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); //Pegando informações de onde foi clicado>>>>>
            if (inverterPosicao) {
                sprite.translateY(velocidade);
                posY = 4;
            }else{
                posY= 1;
                sprite.translateY(-velocidade);
            }
            inverterPosicao = !inverterPosicao;
        }
        stateTimeIdle += delta;
        frameAtual = animacaoIdle.getKeyFrame(stateTimeIdle, true);
        // Garante que o jogador não saia da tela
        sprite.setY(MathUtils.clamp(sprite.getY(), 1, 12 - 2));
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(frameAtual, posX, posY, sprite.getWidth(), sprite.getHeight());
    }

}