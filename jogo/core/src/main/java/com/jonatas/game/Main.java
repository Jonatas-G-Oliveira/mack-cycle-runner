package com.jonatas.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {    
    SpriteBatch spriteBatch;
    FitViewport viewport;

    Array<Disco> vetorDiscos;
    Texture discoTexture;
    float discoTimer;

    private Dog dog;
    

    @Override
    public void create() {
        // ------ Iniciando SpriteBatch
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(12, 8);

        // ------ Elementos do jogo
        dog = new Dog(1, 1, 2, 2, viewport);
        discoTexture = new Texture("disco.jpg");
        vetorDiscos = new Array<>();
        
        
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height,true);
    }


    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        updateGameObjects(dt); //Novo logic
        handleCollisions();
        criarDiscos(dt);
        drawGameObjects();

        //TO DO
        //CRIAR UI
    }

    
    //novo Logic -reponsável por mover as paradas
    public void updateGameObjects(float dt){
        dog.update(dt);
        
        //colisão
        for (int i = vetorDiscos.size - 1; i >= 0; i--){
            Disco disco = vetorDiscos.get(i);
            disco.update(dt);
            if(disco.getHitbox().x < -disco.getHitbox().width){
                vetorDiscos.removeIndex(i);
            }
        }
    }

    private void handleCollisions() {
        for (int i = vetorDiscos.size - 1; i >= 0; i--) {
            Disco disco = vetorDiscos.get(i);
             if (disco.getHitbox().overlaps(dog.getHitbox())){
                vetorDiscos.removeIndex(i);
                System.out.println("Acertou miseravi!");
            }

            //TO DO 
            //INSERIR PONTUAÇÃO E SOM
        }
    }

    private void criarDiscos(float dt){
        discoTimer += dt;
        if (discoTimer > 1f){
            discoTimer = 0;
            Disco novoDisco =  new Disco(discoTexture, 12, 4, 2, 2, viewport);
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
            dog.draw(spriteBatch);
            for (Disco disco : vetorDiscos){
                disco.draw(spriteBatch);
            }
        spriteBatch.end();
    }

      @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    // TO DO 
    // IMPLEMENTAR UI E DISPOSE
    @Override
    public void dispose() {

    }
}