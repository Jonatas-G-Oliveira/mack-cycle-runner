package com.jonatas.game.objetos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Inimigo extends GameObject {
    private Viewport viewport;
    //Animação
    Animation<TextureRegion> animacaoIdle;
    Animation<TextureRegion> animacaoAtaque;
    TextureRegion frameAtual;
    float stateTime;

    // --- Controle de tempo e estado ---
    private float tempoTroca; // acumula o tempo desde a última troca
    private float intervaloTroca = 3f; // a cada 5 segundos muda de animação
    private boolean atacando = false;

    public Inimigo(float x, float y, float largura, float altura, Viewport viewport){
        super(x, y, largura, altura);
        this.viewport = viewport;
        carregarAnimacoes();
    }
    
    private void carregarAnimacoes(){
        // ----- IDLE 
        TextureRegion[] framesIdle = new TextureRegion[4];
        for(int i=0; i< 4;i++){
            Texture t = new Texture("enemie-idle-"+(i+1)+".png");
            TextureRegion sprite_flipada = new TextureRegion(t);
            sprite_flipada.flip(true, false);
            framesIdle[i] = new TextureRegion(sprite_flipada);
        }
        animacaoIdle = new Animation<>(0.1f, framesIdle);


        // ----- ATAQUE
        TextureRegion[] framesAtaque = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            Texture t = new Texture("enemie-attack-" + (i + 1) + ".png");
            TextureRegion sprite_flipada =  new TextureRegion(t);
            sprite_flipada.flip(true, false);
            framesAtaque[i] = new TextureRegion(sprite_flipada);
        }
        animacaoAtaque = new Animation<>(0.2f, framesAtaque);
        
        // Começa no idle
        frameAtual = framesIdle[0];
        stateTime = 0f;
        tempoTroca = 0f;
    }


    @Override
    public void update(float delta){
        stateTime += delta;
        tempoTroca += delta;
        frameAtual = animacaoIdle.getKeyFrame(stateTime, true);
        
        if (!atacando && tempoTroca >= intervaloTroca) {
            atacando = true;
            tempoTroca = 0f;
            stateTime = 0f;
        }

          // Atualiza o frame da animação atual
        if (atacando) {
            frameAtual = animacaoAtaque.getKeyFrame(stateTime, false);
            // Se terminou a animação de ataque, volta para idle
            if (animacaoAtaque.isAnimationFinished(stateTime)) {
                atacando = false;
                stateTime = 0f;
            }
        } else {
            frameAtual = animacaoIdle.getKeyFrame(stateTime, true);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(frameAtual, sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }
}
