package com.jonatas.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {
    Texture fundoExemplo;
    Texture personagemPrincipal;
    Texture disco;

    // ----- Lógica disco
    Array<Sprite> vetorDiscos;
    Array<Integer> esteira;
    float discoTimer;
    Rectangle discoRectangle;
    Rectangle PersonagemRectangle;

    Sound discoAcertoSound;
    Music musicaFase1;

    Sprite personagemPrincipalSprite;
    Sprite fundoExemploSprite; 

    SpriteBatch spriteBatch;
    FitViewport viewport;

    boolean inverterPosicao = false;

    Vector2 touchPos;

    //Animação
    Animation<TextureRegion> animacaoIdle;
    TextureRegion frameAtual;
    float posY, posX;
    float stateTimeIdle;

    @Override
    public void create() {
        // ------ Carregando imagens
        fundoExemplo = new Texture("fundo_ex.jpg");
        personagemPrincipal = new Texture("treecko.png");
        disco = new Texture("disco.jpg");
    
        // ------ Definindo Sprites
        personagemPrincipalSprite = new Sprite(personagemPrincipal);
        personagemPrincipalSprite.setSize(2, 2);
        
        // ------ Iniciando SpriteBatch
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(12, 8);

        // ------ Animações
        TextureRegion[] framesIdle = new TextureRegion[4];
        for(int i=0; i< 4;i++){
            Texture t = new Texture("player-idle-"+(i+1)+".png");
            framesIdle[i] = new TextureRegion(t);
        }
        animacaoIdle = new Animation<>(0.1f, framesIdle);
        frameAtual = framesIdle[0];
        posX = 0;
        posY = 0;
        float stateTime = 0f;

        //Capturando informações de click
        touchPos = new Vector2();

        //Lógica para os discos
        vetorDiscos = new Array<>();
        esteira = new Array<>();
        esteira.add(1);
        esteira.add(4);
        discoRectangle = new Rectangle();
        PersonagemRectangle = new Rectangle();

        discoAcertoSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
        musicaFase1 = Gdx.audio.newMusic(Gdx.files.internal("bankai.mp3"));

        musicaFase1.setVolume(.5f);
        musicaFase1.play();
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

    private void criarDiscos(){
      
        float discoWidth = 2;
        float discoHeight = 2;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite discoSprite = new Sprite(disco);
        discoSprite.setSize(discoWidth, discoHeight);
        
        //Escolhendo a esteira que o disco vem
        int laneEscolhida = MathUtils.random(0, 1);
       
        discoSprite.setY(esteira.get(laneEscolhida));
        discoSprite.setX(worldWidth - discoWidth);
        vetorDiscos.add(discoSprite);

       
        
    }

    public void input(){
        float velocidade = 3f;
        float delta = Gdx.graphics.getDeltaTime(); //rodar em qualquer lugar


        if(Gdx.input.justTouched()){
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); //Pegando informações de onde foi clicado>>>>>
            if (inverterPosicao) {
                personagemPrincipalSprite.translateY(velocidade);
            }else{
                personagemPrincipalSprite.translateY(-velocidade);
            }
            inverterPosicao = !inverterPosicao;
        }
     
    }

    public void logic(){
        //Delta só é bom pra objeto continuos
        float delta = Gdx.graphics.getDeltaTime();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float personagemPrincipalWidth = personagemPrincipalSprite.getWidth();
        float personagemPrincipalHeight = personagemPrincipalSprite.getHeight();
        personagemPrincipalSprite.setY(MathUtils.clamp(personagemPrincipalSprite.getY(), 1, worldHeight - personagemPrincipalHeight));

        // Hitbox Personagem Principal
        PersonagemRectangle.set(personagemPrincipalSprite.getX(), personagemPrincipalSprite.getY(), personagemPrincipalWidth, personagemPrincipalHeight);

        for (int i = vetorDiscos.size - 1; i >= 0; i--){
            Sprite disco = vetorDiscos.get(i);
            float discoWidth = disco.getWidth();
            float discoHeight = disco.getHeight();

            disco.translateX(-4f * delta);
            discoRectangle.set(disco.getX(), disco.getY(), discoWidth, discoHeight);

            if(disco.getX() < -discoWidth){
                vetorDiscos.removeIndex(i);
            }else if(discoRectangle.overlaps(PersonagemRectangle)){
                vetorDiscos.removeIndex(i);
                discoAcertoSound.play();
            }
        }

        stateTimeIdle += delta;
        frameAtual = animacaoIdle.getKeyFrame(stateTimeIdle, true);
        float bpm = 138f; //Cada batida  é uma nota inteira Seminima
        float segundosporbatida = 60f / bpm;
        float clicksporbatida = segundosporbatida / 2f;      //Colcheia 2 clcks
        // float clicksporbatida = segundosporbatida / 4f;      //Semicolcheia 4 clicks
        // float clicksporbatida = segundosporbatida / 8f;      //Fusa 8 clicks
        // float clicksporbatida = segundosporbatida / 16f;      //SemiFusa 16 clis
      
        discoTimer += delta;
        if (discoTimer >= clicksporbatida) {
            discoTimer -= clicksporbatida;
            criarDiscos();
        }
    
    }

    public void draw(){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        
        // ----- Desenhar as coisas é por aqui
        spriteBatch.begin();

            spriteBatch.draw(fundoExemplo, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
            personagemPrincipalSprite.draw(spriteBatch);
            for (Sprite disco : vetorDiscos){
                disco.draw(spriteBatch);
            }
            spriteBatch.draw(frameAtual,posX,posY,2,2 );


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