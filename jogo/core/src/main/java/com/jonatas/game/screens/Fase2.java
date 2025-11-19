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
import com.jonatas.game.objetos.Inimigo;

// ------ Fase 2 você pode perder até 7 disco bpm medio 80
// Trocar a música e  o fundo

//Deixar as musicas fora daqui
public class Fase2 implements Screen {
    private int discosPerdidosSeguidos = 0;

    private MyGame game;
    SpriteBatch spriteBatch;
    FitViewport viewport;

    //Criação dos disco
    Array<Disco> vetorDiscos;
    Texture discoTexture;
    float discoTimer;



    private Dog dog;
    private Inimigo inimigo;
   
    
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
    Texture discoOk, discoFail;

    private Label feedbackLabel;
    private final float[] esteira = {1 , 4};

    // Parallax
    Texture fundo;
    float fundoX = 0f;
    float velocidadeFundo = 1.5f;

    public Fase2(MyGame game){
        this.game = game;

        // ------ Iniciando SpriteBatch
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(16, 9);
        fundo = new Texture("zootopia_gemini_fase_2.png");

        //Ajuste pra imagem
        float bgAspect = (float) fundo.getWidth() / fundo.getHeight();
        float screenAspect = viewport.getWorldWidth() / viewport.getWorldHeight();
        float drawHeight = viewport.getWorldHeight();
        float drawWidth  = drawHeight * bgAspect;

        // ------ Elementos do jogo
        dog = new Dog(2, 1, 2, 2, viewport);
        inimigo = new Inimigo(13, 4, 3, 3, viewport);
        discoTexture = new Texture("disco.jpg");
        vetorDiscos = new Array<>();
        discoOk = new Texture("player-jump-2.png");   
        discoFail = new Texture("enemie-attack-1.png"); 
        
        // ----- Efeitos Sonoros
        somAcerto = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
        musicaFase1 = Gdx.audio.newMusic(Gdx.files.internal("LifeIsAHighway.mp3"));
        musicaFase1.setVolume(.5f);
        

        // ----- UI
        score = 0;
        uiStage = new Stage();
        font = new BitmapFont();
        font.getData().setScale(2.0f);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.GREEN);
        Label.LabelStyle style_feedback = new Label.LabelStyle(font, Color.PURPLE);

        scoreLabel = new Label("Pontuação: " + score, style);
        feedbackLabel = new Label(feedback, style_feedback);

        scoreLabel.setPosition(120, Gdx.graphics.getHeight() - 60 ); // distância do topo;
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
        fundoX -= velocidadeFundo * dt;

        if (fundoX <= -viewport.getWorldWidth()) {
                fundoX += viewport.getWorldWidth();
        }

        for (int i = vetorDiscos.size - 1; i >= 0; i--){
            Disco disco = vetorDiscos.get(i);
            disco.update(dt);

            // ------ Disco saindo da tela
            if(!disco.getAcerto() && disco.getHitbox().x < -disco.getHitbox().width){
                vetorDiscos.removeIndex(i);
            }
            //Se passar do personagem perde ponto
            if (!disco.getAcerto()  && !disco.getContado() && disco.getHitbox().x < dog.getHitbox().x - 2f) {
                    disco.setContado(true);
                    discosPerdidosSeguidos++;
                    barraValor -= 0.2f;
                    if (barraValor < 0f) barraValor = 0;
            }
              // verifica game over
            if (discosPerdidosSeguidos >= 7) {
                game.setScreen(new GameOverScreen(game, score));
            
                return;
            }
            
            // ------ Disco sendo acertado
            float diferencaY = Math.abs(disco.getHitbox().y - dog.getHitbox().y);

            float discoCentroX = disco.getHitbox().x + disco.getHitbox().width / 2f;
            float dogCentroX = dog.getHitbox().x + dog.getHitbox().width / 2f;
            float diferencaX =  discoCentroX - dogCentroX;
            if(diferencaY < 0.5 && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){ //Verifica se esta na mesma esteira
                if (diferencaX >= -0.7f && diferencaX < 0.5f){
                    disco.setAcerto(true);
                    discosPerdidosSeguidos = 0;
                    score += 20;
                    feedback = "PERFEITO!";
                    somAcerto.play();
                    vetorDiscos.removeIndex(i);
                    barraValor += 0.3f;
                    if (barraValor > 1f) barraValor = 1f;
                     
                    feedbackTimer = 0.5f;
                    feedbackLabel.setText(feedback);
                    scoreLabel.setText("Pontuação: " + score);
                    break;
                }else if(diferencaX >= 0.5f && diferencaX < 1.5f){
                    disco.setAcerto(true);
                    discosPerdidosSeguidos = 0;
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

        
        if (score >= 1000) {
            game.setScreen(new VictoryScreen(game, score));
            return;
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
        
        float bpm = 103f; //Cada batida  é uma nota inteira Seminima
        float segundosporbatida = 60f / bpm;
        float clicksporbatida = segundosporbatida / 2f;      //Colcheia 2 clcks
        // float clicksporbatida = segundosporbatida / 4f;      //Semicolcheia 4 clicks
        // float clicksporbatida = segundosporbatida / 8f;      //Fusa 8 clicks
        // float clicksporbatida = segundosporbatida / 16f;      //SemiFusa 16 clis
        if (discoTimer > clicksporbatida){
            discoTimer -=  clicksporbatida;
            int pos = MathUtils.random(0, esteira.length - 1);
            float y = esteira[pos];
            Disco novoDisco =  new Disco( 13, y, 2, 2, viewport);
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

            spriteBatch.draw(fundo, fundoX,0, viewport.getWorldWidth(), viewport.getWorldHeight());
            spriteBatch.draw(fundo, fundoX + viewport.getWorldWidth(),0, viewport.getWorldWidth(), viewport.getWorldHeight());

            dog.draw(spriteBatch);
            inimigo.draw(spriteBatch);
            for (Disco disco : vetorDiscos){
                disco.draw(spriteBatch);
            }
            //Desenhando os discos perdidos
            for (int i = 0; i < 7; i++) {
                Texture img = (i < discosPerdidosSeguidos) ? discoFail : discoOk;
                // Texture img = discoOk;
                spriteBatch.draw(img,  10f + i , viewport.getWorldHeight() - 1f - 0.5f, 1f, 1f);
            }
        spriteBatch.end();

        // Desenhando a barra de progresso
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            // Fundo da barra (purple)
            shapeRenderer.setColor(Color.CORAL);
            shapeRenderer.rect(6f, 0.5f, 4f, 0.4f);

            // Barra preenchida (pink)
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(6f, 0.5f, 4f * barraValor, 0.4f);

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
    @Override
        public void dispose() {

            // Sons
            somAcerto.dispose();
            musicaFase1.dispose();

            // Texturas
            fundo.dispose();
            discoTexture.dispose();
            discoOk.dispose();
            discoFail.dispose();

            // Renderizadores
            spriteBatch.dispose();
            shapeRenderer.dispose();

            // UI
            uiStage.dispose();
            font.dispose();

            // dog.dispose();
            // inimigo.dispose();
        }
}