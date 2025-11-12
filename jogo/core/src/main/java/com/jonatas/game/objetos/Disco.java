package com.jonatas.game.objetos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Disco extends GameObject{
    private float velocidade;
    private Viewport viewport;

    Animation<TextureRegion> animacaoIdle;
    TextureRegion frameAtual;
    float stateTimeIdle;
    
    public Disco(float x, float y, float largura, float altura, Viewport viewport){
        super(x, y, largura, altura);
        this.viewport = viewport;
        this.velocidade = 4f;
        carregarAnimacoes();
    }

    private void carregarAnimacoes(){
        TextureRegion[] framesIdle = new TextureRegion[8];
        for(int i=0; i <= 7;i++){
            Texture t = new Texture("disco-"+(i+1)+".gif");
            framesIdle[i] = new TextureRegion(t);
        }
        animacaoIdle = new Animation<>(0.1f, framesIdle);
        frameAtual = framesIdle[0];
        stateTimeIdle = 0f;
    }

    @Override
    public void update(float dt){
        sprite.translateX(-velocidade * dt);
        hitbox.setPosition(sprite.getX(), sprite.getY()); //Talvez seja desnecesário

        stateTimeIdle += dt;
        frameAtual = animacaoIdle.getKeyFrame(stateTimeIdle, true);
    }

     @Override
    public void draw(SpriteBatch batch) {
        batch.draw(frameAtual, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

}
