package com.jonatas.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.jonatas.game.MyGame;
import com.jonatas.game.objetos.Disco;
import com.jonatas.game.objetos.Dog;
import com.jonatas.game.objetos.Inimigo;


//Deixar as musicas fora daqui
public class Fase1 implements Screen {
    
    private MyGame game;
    SpriteBatch spriteBatch;
    ExtendViewport viewport;

    //Criação dos disco
    Array<Disco> vetorDiscos;
    Texture discoTexture;
    float discoTimer;

    private Dog dog;
    private Inimigo inimigo;
    Texture fundo;
    
    // ----- Efeitos sonoros
    Sound somAcerto;
    Music musicaFase1;

    // ----- UI
    private float barraValor;
    private int score;
    private Stage uiStage;
    private Label scoreLabel;
    private BitmapFont font;
    
    // Elementos visuais
    private ShapeRenderer shapeRenderer;
    private String feedback;
    private float feedbackTimer;

    private Label feedbackLabel;
    private final float[] esteira = {1 , 4};

    public Fase1(MyGame game){
        this.game = game;

        // ------ Iniciando SpriteBatch
        spriteBatch = new SpriteBatch();
        viewport = new ExtendViewport(12, 8);
        fundo = new Texture("zootopia_fundo.jpg");

        // ------ Elementos do jogo
        dog = new Dog(1, 1, 2, 2, viewport);
        inimigo = new Inimigo(9, 4, 3, 3, viewport);
        discoTexture = new Texture("disco.jpg");
        vetorDiscos = new Array<>();
        
        
        // ----- Efeitos Sonoros
        somAcerto = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
        musicaFase1 = Gdx.audio.newMusic(Gdx.files.internal("bankai.mp3"));
        musicaFase1.setVolume(.5f);
        

        // ----- UI
        score = 0;
        uiStage = new Stage();
        font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        
        scoreLabel = new Label("Pontuação: " + score, style);
        feedbackLabel = new Label(feedback, style);

        scoreLabel.setPosition(20, 440);
        feedbackLabel.setPosition(250, 250);
        uiStage.addActor(scoreLabel);
        uiStage.addActor(feedbackLabel);

        // ------ Elementos visuais
        barraValor = 0f;
        shapeRenderer = new ShapeRenderer();
        feedback = "";
        feedbackTimer = 0f;
    }

    @Override
    public void render(float delta) {
        float dt = Gdx.graphics.getDeltaTime();

        criarDiscos(dt);
        updateGameObjects(dt); //Novo logic
        drawGameObjects();
        drawUI();
        
        // Aqui vai toda a lógica da fase 1 (dog, discos, UI…)

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                game.setScreen(new MenuScreen(game));
        }
    }

    //novo Logic - reponsável por mover as paradas
    public void updateGameObjects(float dt){
        dog.update(dt);
        inimigo.update(dt);
        for (int i = vetorDiscos.size - 1; i >= 0; i--){
            Disco disco = vetorDiscos.get(i);
            disco.update(dt);

            // ------ Disco saindo da tela
            if(disco.getHitbox().x < -disco.getHitbox().width){
                vetorDiscos.removeIndex(i);
                barraValor -=  0.2f;
                if (barraValor < 0f) barraValor = 0;
            }
            
            // ------ Disco sendo acertado
            float diferencaY = Math.abs(disco.getHitbox().y - dog.getHitbox().y);

            float discoCentroX = disco.getHitbox().x + disco.getHitbox().width / 2f;
            float dogCentroX = dog.getHitbox().x + dog.getHitbox().width / 2f;
            float diferencaX =  discoCentroX - dogCentroX;
            if(diferencaY < 0.5 && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){ //Verifica se esta na mesma esteira
                if (diferencaX >= -0.7f && diferencaX < 0.5f){
                    score += 10;
                    feedback = "PERFEITO!";
                    somAcerto.play();
                    vetorDiscos.removeIndex(i);
                    barraValor += 0.3f;
                    if (barraValor > 1f) barraValor = 1f;
                     
                    feedbackTimer = 0.5f;
                    feedbackLabel.setText(feedback);
                    scoreLabel.setText("Pontuação: " + score);
                    break;
                }else if(diferencaX >= 0.5f && diferencaX < 1.2f){
                    score += 10;
                    feedback = "BOM!";
                    somAcerto.play();
                    vetorDiscos.removeIndex(i);
                    barraValor += 0.1f;
                    
                    if (barraValor > 1f) barraValor = 1f;
                    feedbackTimer = 0.5f;

                    feedbackLabel.setText(feedback);
                    scoreLabel.setText("Pontuação: " + score);
                    break;
                }
            }
        }

        // Decaimento do feedback 
        if (feedbackTimer > 0) {
            feedbackTimer -= dt;
            if (feedbackTimer <= 0){
                feedback = "";
                feedbackLabel.setText(feedback);
            } 
}
    }


    private void criarDiscos(float dt){
        discoTimer += dt;
        
        float bpm = 138f; //Cada batida  é uma nota inteira Seminima
        float segundosporbatida = 60f / bpm;
        float clicksporbatida = segundosporbatida / 2f;      //Colcheia 2 clcks
        // float clicksporbatida = segundosporbatida / 4f;      //Semicolcheia 4 clicks
        // float clicksporbatida = segundosporbatida / 8f;      //Fusa 8 clicks
        // float clicksporbatida = segundosporbatida / 16f;      //SemiFusa 16 clis
        if (discoTimer > clicksporbatida){
            discoTimer -=  clicksporbatida;
            int pos = MathUtils.random(0, esteira.length - 1);
            float y = esteira[pos];
            Disco novoDisco =  new Disco( 9, y, 2, 2, viewport);
            vetorDiscos.add(novoDisco);
        }
        
        //TO DO 
        //CRIAR DISCO ESPECIAL
    }

    public void drawGameObjects(){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        
        spriteBatch.begin();
            spriteBatch.draw(fundo,0,0, viewport.getWorldWidth(), viewport.getWorldHeight());
            dog.draw(spriteBatch);
            inimigo.draw(spriteBatch);
            for (Disco disco : vetorDiscos){
                disco.draw(spriteBatch);
            }
        spriteBatch.end();

        // Desenhando a barra de progresso
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            // Fundo da barra (purple)
            shapeRenderer.setColor(Color.CORAL);
            shapeRenderer.rect(4f, 0.5f, 4f, 0.4f);

            // Barra preenchida (pink)
            shapeRenderer.setColor(Color.PINK);
            shapeRenderer.rect(4f, 0.5f, 4f * barraValor, 0.4f);

        shapeRenderer.end();
   
        }

    
    private void drawUI(){
        uiStage.act();
        uiStage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    @Override public void show() {

        musicaFase1.play();
    }
    @Override public void hide() {
            musicaFase1.stop();
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {
        musicaFase1.dispose();

    }
}