package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Paint;


import java.util.List;
import java.util.stream.Collectors;

public class Game extends Application {

    private Pane root = new Pane();

    private double timerEnemy= 0;
    private double timerPlayer= 0;
    private Sprite player = new Sprite(300,500,40,40,"player", Color.BLUE);

    private Parent createContent(){
        root.setPrefSize(600,600);
        root.getChildren().add(player);

        AnimationTimer timer= new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();
        nextLevel();
        return root;
    }

    //Genera los enemigos
    private void nextLevel() {

        for (int i=0;i<5;i++){
            Sprite s = new Sprite(90+i*100,150,30,30,"enemy",Color.RED);
            root.getChildren().add(s);
        }

    }


    //Devuelve todos los sprites del juego que aparecen en pantalla.
    private List<Sprite> sprites(){
        return root.getChildren().stream().map(n-> (Sprite)n).collect(Collectors.toList());
    }


    private void update(){
        timerEnemy+=0.016;
        timerPlayer+=0.016;
        sprites().forEach(sprite->{

            switch (sprite.type){
                case "enemybullet":

                    sprite.moveDown();
                    if(sprite.getBoundsInParent().intersects(player.getBoundsInParent())){
                        player.dead =true;
                        sprite.dead=true;
                    }
                    break;

                case "playerbullet":

                    sprite.moveUp();
                    sprites().stream().filter(e->e.type.equals("enemy")).forEach(enemy ->{
                        System.out.println("Se tocaron" +sprite.getBoundsInParent().intersects((enemy.getBoundsInParent())));
                        if(sprite.getBoundsInParent().intersects((enemy.getBoundsInParent()))){
                            System.out.println("Se tocaron" +
                                    "");
                            enemy.dead =true;
                            sprite.dead =true;
                        }
                    });
                    break;

                case "enemy":
                    if(timerEnemy>2){
                        if(Math.random()<0.3){
                           shoot(sprite);
                        }
                    }
                    break;
            }
        });

        root.getChildren().removeIf(n->{
            Sprite s = (Sprite)n;
            return s.dead;
        });

        if(timerEnemy>2){
            timerEnemy=0;
        }
    }

    private void shoot(Sprite who){

        if (timerPlayer>1){
            Sprite shoot = new Sprite((int) who.getTranslateX() +20, (int) who.getTranslateY(),5,20,who.type+"bullet",Color.BLACK);
            root.getChildren().add(shoot);
            timerPlayer=0;
        }

    }

    @Override
    public void start(Stage stage){
        Scene scene = new Scene( createContent() );
        scene.setOnKeyPressed(e->{
            switch (e.getCode()){
                case A:
                    player.moveLeft();
                    break;
                case D:
                    player.moveRight();
                    break;
                case LEFT:
                    player.moveLeft();
                    break;
                case RIGHT:
                    player.moveRight();
                    break;
                case SPACE:
                    shoot(player);
                    break;
            }
        });
        stage.setScene( scene );
        stage.show();
    }






    private static class Sprite extends Rectangle {
        boolean dead = false;
        final String type;

        Sprite(int x, int y, int w, int h, String type, Paint color ){
            super(w, h, color);

            this.type=type;
            setTranslateX(x);
            setTranslateY(y);
        }
        void moveLeft(){
            if(getTranslateX()>0){
                setTranslateX(getTranslateX()-10);
            }

        }
        void moveRight(){
            if(getTranslateX()<560){
                setTranslateX(getTranslateX()+10);
            }
        }
        void moveUp(){
            setTranslateY(getTranslateY()-10);
        }
        void moveDown(){
            setTranslateY(getTranslateY()+10);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
