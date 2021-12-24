package com.example.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture background2;
    Texture[] bird;
    Texture topTube;
    Texture gameOver;
    Texture bottomTube;
    int flag = 0;
    float flyHeight;
    float fallingSpeed = 0;
    int isTouch = 0;
    int spaceBetweenTubs = 500;
    Random random;
    float tubeSpeed = 3;
    int tubesNumber = 5;
    float tubeX[] = new float[tubesNumber];
    float shiftTube[] = new float[tubesNumber];
    float distanceBetweenTubes;
    ShapeRenderer shapeRenderer;
    Circle birdCircle;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;
    int gameScore = 0;
    int passedTubeIndex = 0;
    BitmapFont scoreFont;


    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png");
        background2 = new Texture("background2.png");
        bird = new Texture[2];
        bird[0] = new Texture("bird_wings_up.png");
        bird[1] = new Texture("bird_wings_down.png");
        topTube = new Texture("top_tube.png");
        gameOver = new Texture("game_over.png");
        bottomTube = new Texture("bottom_tube.png");
        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        topTubeRectangles = new Rectangle[tubesNumber];
        bottomTubeRectangles = new Rectangle[tubesNumber];
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.RED);
        scoreFont.getData().setScale(10);

        random = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() / 2;

        initGame();
    }

    public void initGame() {
        flyHeight = Gdx.graphics.getHeight() / 2 - bird[0].getHeight() / 2;
        for (int i = 0; i < tubesNumber; i++) {
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes * 1.5f;
            shiftTube[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - (spaceBetweenTubs * 2));
            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void render() {
        batch.begin();
        if(gameScore < 10){
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }else {
            batch.draw(background2, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        if (isTouch == 1) {

            if (tubeX[passedTubeIndex] < Gdx.graphics.getWidth() / 2) {
                gameScore++;
                if (passedTubeIndex < tubesNumber - 1) {
                    passedTubeIndex++;
                } else {
                    passedTubeIndex = 0;
                }
                Gdx.app.log("gamescore", "Score: " + gameScore);
            }

            if (Gdx.input.justTouched()) {
                fallingSpeed = -20;
            }

            for (int i = 0; i < tubesNumber; i++) {

                if (tubeX[i] < -topTube.getWidth()) {
                    tubeX[i] = tubesNumber * distanceBetweenTubes;
                } else {
                    tubeX[i] -= tubeSpeed;
                }

                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubs / 2 + shiftTube[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubs / 2 - bottomTube.getHeight() + shiftTube[i]);
                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + spaceBetweenTubs / 2 + shiftTube[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - spaceBetweenTubs / 2 - bottomTube.getHeight() + shiftTube[i], bottomTube.getWidth(), bottomTube.getHeight());
            }

            if (flyHeight > bird[0].getHeight() / 2) {
                fallingSpeed++;
                flyHeight -= fallingSpeed;
            } else {
                isTouch = 2;
            }
        } else if (isTouch == 0) {
            if (Gdx.input.justTouched()) {
                Gdx.app.log("touch", "touch touch!!");
                isTouch = 1;
            }
        } else if (isTouch == 2) {
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            if (Gdx.input.justTouched()) {
                Gdx.app.log("touch", "touch touch!!");
                isTouch = 1;
                initGame();
                gameScore = 0;
                passedTubeIndex = 0;
                fallingSpeed = 0;
            }
        }


        if (flag == 0) {
            flag = 1;
        } else {
            flag = 0;
        }
        batch.draw(bird[flag], Gdx.graphics.getWidth() / 2 - bird[flag].getWidth() / 2, flyHeight);

        scoreFont.draw(batch, String.valueOf("Score: " + gameScore), Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);

        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, flyHeight + bird[flag].getHeight() / 2, bird[flag].getWidth() / 2);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.CORAL);
//        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

        for (int i = 0; i < tubesNumber; i++) {

//            shapeRenderer.rect(topTubeRectangles[i].x, topTubeRectangles[i].y, topTubeRectangles[i].width, topTubeRectangles[i].height);
//            shapeRenderer.rect(bottomTubeRectangles[i].x, bottomTubeRectangles[i].y, bottomTubeRectangles[i].width, bottomTubeRectangles[i].height);

            if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
                Gdx.app.log("itersected", "Bump!");
                isTouch = 2;
            }
        }
//        shapeRenderer.end();

    }

    @Override
    public void dispose() {

    }
}
