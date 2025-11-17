package com.jonatas.game.objetos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private boolean pulando = false;
    private boolean descendo = false;

    //Animação
    private Animation<TextureRegion> animacaoAtual;
    Animation<TextureRegion> animacaoIdle;
    Animation<TextureRegion> animacaoCima;
    Animation<TextureRegion> animacaoBaixo;
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
        // ---------- IDLE ----------
        TextureRegion[] framesIdle = new TextureRegion[4];
        for(int i=0; i< 4;i++){
            Texture t = new Texture("player-idle-"+(i+1)+".png");
            framesIdle[i] = new TextureRegion(t);
        }
        animacaoIdle = new Animation<>(0.1f, framesIdle);

        // ---------- ANIMAÇÃO CIMA ----------
        TextureRegion[] framesCima = new TextureRegion[1];
        framesCima[0] = new TextureRegion(new Texture("player-jump-1.png"));
        animacaoCima = new Animation<>(0.2f, framesCima);

        // ---------- ANIMAÇÃO BAIXO ----------
        TextureRegion[] framesBaixo = new TextureRegion[1];
        framesBaixo[0] = new TextureRegion(new Texture("player-jump-2.png"));
        animacaoBaixo = new Animation<>(0.4f, framesBaixo);

        
        animacaoAtual = animacaoIdle;
        frameAtual = framesIdle[0];
        stateTimeIdle = 0f;
    }


    @Override
    public void update(float delta){
    // processa input e inicia animação, se aplicável
    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !pulando) {
        // pular (ir pra cima)
        pulando = true;
        descendo = false;
        animacaoAtual = animacaoCima;
        stateTimeIdle = 0f;      // reset pra começar a animação do início
        posY = 4;
        sprite.setY(posY);
    }

    if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && !descendo) {
        // descer (ir pra baixo)
        descendo = true;
        pulando = false;
        animacaoAtual = animacaoBaixo;
        stateTimeIdle = 0f;
        posY = 1;
        sprite.setY(posY);
    }

    // avança tempo da animação atual
    stateTimeIdle += delta;

    // atualiza frame de acordo com o estado atual (prioridade: pulando -> descendo -> idle)
    if (pulando) {
        frameAtual = animacaoCima.getKeyFrame(stateTimeIdle, false);
        if (animacaoCima.isAnimationFinished(stateTimeIdle)) {
            pulando = false;
            animacaoAtual = animacaoIdle;
            stateTimeIdle = 0f;
            frameAtual = animacaoIdle.getKeyFrame(0f, true);
        }
    } else if (descendo) {
        frameAtual = animacaoBaixo.getKeyFrame(stateTimeIdle, false);
        if (animacaoBaixo.isAnimationFinished(stateTimeIdle)) {
            descendo = false;
            animacaoAtual = animacaoIdle;
            stateTimeIdle = 0f;
            frameAtual = animacaoIdle.getKeyFrame(0f, true);
        }
    } else {
        // idle (looping)
        frameAtual = animacaoIdle.getKeyFrame(stateTimeIdle, true);
    }

    // garante que o jogador não saia da tela
    sprite.setY(MathUtils.clamp(sprite.getY(), 1, 12 - 2));
}

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(frameAtual, posX, posY, sprite.getWidth(), sprite.getHeight());
    }

}