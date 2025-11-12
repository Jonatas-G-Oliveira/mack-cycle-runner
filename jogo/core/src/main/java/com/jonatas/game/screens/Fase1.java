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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jonatas.game.MyGame;
import com.jonatas.game.objetos.Disco;
import com.jonatas.game.objetos.Dog;


//Deixar as musicas fora daqui
public class Fase1 implements Screen {
    
    private MyGame game;
    SpriteBatch spriteBatch;
    FitViewport viewport;

    //Criação dos disco
    Array<Disco> vetorDiscos;
    Texture discoTexture;
    float discoTimer;

    private Dog dog;
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
        viewport = new FitViewport(12, 8);
        // fundo = new Texture("fundo_ex.jpeg");

        // ------ Elementos do jogo
        dog = new Dog(1, 1, 2, 2, viewport);
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
        
        for (int i = vetorDiscos.size - 1; i >= 0; i--){
            Disco disco = vetorDiscos.get(i);
            disco.update(dt);

            //Disco saindo da tela
            if(disco.getHitbox().x < -disco.getHitbox().width){
                vetorDiscos.removeIndex(i);
                barraValor -=  0.2f;
                if (barraValor < 0f) barraValor = 0;
            }
            //Implementar a colisão certa
            // float tolerancia = 0.3f;
            // float personagemCentralX = .x + PersonagemRectangle.width / 2f;
            // float personagemCentralY = PersonagemRectangle.y + PersonagemRectangle.height / 2f; 

            // float discoCentroX = discoRectangle.x + discoRectangle.width / 2f ;
            // float discoCentroY = discoRectangle.y + discoRectangle.height / 2f;

            // float diferencaX = Math.abs(discoCentroX - personagemCentralX);
            // float diferencaY = Math.abs(discoCentroY - personagemCentralY);
            // if( < tolerancia && Math.abs(discoCentroY - personagemCentralY) < tolerancia ){
            //     vetorDiscos.removeIndex(i);
            //     discoAcertoSound.play();
            //    } 

            //Disco sendo acertado
                    
            float diferencaY = Math.abs(disco.getHitbox().y - dog.getHitbox().y);
            float diferencaX =  Math.abs(disco.getHitbox().x - dog.getHitbox().x);
            if(diferencaY < 0.5 && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){ //Verifica se esta na mesma esteira
                if (diferencaX < 0.5f){
                    score += 10;
                    feedback = "PERFEITO!";
                    somAcerto.play();
                    vetorDiscos.removeIndex(i);
                    barraValor += 0.3f;
                    if (barraValor > 1f) barraValor = 1f;
                     feedbackTimer = 1f;

                    feedbackLabel.setText(feedback);
                    scoreLabel.setText("Pontuação: " + score);
                }else if(diferencaX < 1.2f){
                    score += 10;
                    feedback = "BOM!";
                    somAcerto.play();
                    vetorDiscos.removeIndex(i);
                    barraValor += 0.1f;
                    
                    if (barraValor > 1f) barraValor = 1f;
                    feedbackTimer = 1f;

                    feedbackLabel.setText(feedback);
                    scoreLabel.setText("Pontuação: " + score);
                }
            }
           
            

            // if (disco.getHitbox().overlaps(dog.getHitbox())){
            //     somAcerto.play();
            //     vetorDiscos.removeIndex(i);
            //     score += 10;
            //     barraValor += 0.2f;
            //     if (barraValor > 1f) barraValor = 1f;// O máximo é 1
            //     feedback = "PERFEITO!";
            //     feedbackTimer = 0.5f;

            //     feedbackLabel.setText(feedback);
            //     scoreLabel.setText("Pontuação: " + score);
            // }

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
            Disco novoDisco =  new Disco(discoTexture, 12, y, 2, 2, viewport);
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
            // spriteBatch.draw(fundo,0,0, viewport.getWorldWidth(), viewport.getWorldHeight());
            dog.draw(spriteBatch);
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