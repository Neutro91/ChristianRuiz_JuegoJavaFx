package sample;
import javafx.application.Application;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

// Keyboard events
public class JuegoBueno extends Application {

    private int numero=0;
    private AudioClip shoot= new AudioClip(getClass().getClassLoader().getResource("Images/shoot.wav").toExternalForm());
    private AudioClip failed= new AudioClip(getClass().getClassLoader().getResource("Images/fallo.wav").toExternalForm());
    private AudioClip musica= new AudioClip(getClass().getClassLoader().getResource("Images/musicaprincipal.wav").toExternalForm());


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {

        theStage.setTitle( "Click the Target!" );
        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );
        Canvas canvas = new Canvas( 700, 345 );
        root.getChildren().add( canvas );

        //Rectangulo HitBox
        Rectangle targetData = new Rectangle(65,50);
        targetData.setX(100);
        targetData.setY(260);

        //Pajaro
        Bird masterbird = new Bird();
        masterbird.setImage("Images/bird.png");
        masterbird.setPosition(Math.random()*(700-masterbird.getImage().getWidth()/2)+30,260);
        masterbird.setVelocity(50,-50);

        Bird restabird = new Bird();
        restabird.setImage("Images/crown.png");
        restabird.setPosition(Math.random()*(700-restabird.getImage().getWidth()/2)+30,260);
        restabird.setVelocity(50,-50);

        //Graphic Context
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 24 );
        gc.setFont( theFont );
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(1);


        //Valores Aparte
        LongValue lastNanoTime = new LongValue( System.nanoTime() );
        Image background= new Image("Images/background.png");
        musica.play();

        theScene.setOnMouseClicked(
                new EventHandler<MouseEvent>() {

                    public void handle(MouseEvent e) {

                        if (masterbird.getBoundary().contains(e.getX(), e.getY())) {
                            double xbird = Math.random() * (700 - masterbird.getImage().getWidth()/2) + 30;

                            try {
                                shoot.setPriority(1);
                                shoot.play();
                                targetData.setX(xbird);
                                masterbird.setPosition(xbird, 250);
                                Thread.sleep(1);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }

                            //Movimiento del pajaro dependiendo de la zona donde aparezca, de 0 a 350 izquierda y de 350 a x derecha
                            if (numero <= 10) {
                                if (xbird < 350) {
                                    masterbird.setVelocity(50, -50);
                                } else {
                                    masterbird.setVelocity(-50, -50);
                                }
                            } else if (numero < 20) {
                                if (xbird < 350) {
                                    masterbird.setVelocity(70, -70);
                                } else {
                                    masterbird.setVelocity(-70, -70);
                                }
                            } else if (numero < 30) {
                                if (xbird < 350) {
                                    masterbird.setVelocity(90, -90);
                                } else {
                                    masterbird.setVelocity(-90, -90);
                                }
                            } else if (numero < 40) {
                                if (xbird < 350) {
                                    masterbird.setVelocity(110, -110);
                                } else {
                                    masterbird.setVelocity(-110, -110);
                                }
                            } else if (numero < 50) {
                                if (xbird < 350) {
                                    masterbird.setVelocity(150, -150);
                                } else {
                                    masterbird.setVelocity(-150, -150);
                                }
                            }
                            numero++;
                        } else if (restabird.getBoundary().contains(e.getX(), e.getY())) {
                            double xraven = Math.random() * (700 - restabird.getImage().getWidth()/2) + 30;
                            try {
                                shoot.setPriority(1);
                                failed.play();
                                targetData.setX(xraven);
                                restabird.setPosition(xraven, 250);
                                Thread.sleep(1);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }

                            if (xraven < 350) {
                                restabird.setVelocity(50, -50);
                            } else {
                                restabird.setVelocity(-50, -50);
                            }
                            numero--;
                        }else {
                            failed.setPriority(1);
                            failed.play();
                            numero = 0; }
                    }

                });

        new AnimationTimer() {
            public void handle(long currentNanoTime) {

                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;

                //FondoPantalla
                gc.drawImage(background,0,0);
                gc.setFill( Color.BLUE );


                //PajaroAnimació
                masterbird.render(gc);
                masterbird.update(elapsedTime);

                //Cuervo Animacion
                restabird.render(gc);
                restabird.update(elapsedTime);

                //Condicional evitar que salga de la pantalla
                if(masterbird.getPositionX()<0 || masterbird.getPositionX()>650){
                    double xbird = Math.random() * (700 - masterbird.getImage().getWidth()/2) + 30;
                    failed.setPriority(1);
                    failed.play();
                    masterbird.setPosition(Math.random()*(700-xbird),250);

                    if(xbird<350){
                        masterbird.setVelocity(50,-50);
                    }else {
                        masterbird.setVelocity(-50,-50);
                    }
                }else if(masterbird.getPositionY()<0 || masterbird.getPositionY()>345){
                    double xbird = Math.random() * (700 - masterbird.getImage().getWidth()/2) + 30;

                    failed.setPriority(1);
                    failed.play();
                    masterbird.setPosition(Math.random()*(700-xbird),250);

                    if(xbird<350){
                        masterbird.setVelocity(50,-50);
                    }else {
                        masterbird.setVelocity(-50,-50);
                    }
                }

                //Condicional evitar que salga de la pantalla
                if(restabird.getPositionX()<0 || restabird.getPositionX()>650){
                    double xraven = Math.random() * (700 - restabird.getImage().getWidth()/2) + 30;
                    failed.setPriority(1);
                    failed.play();
                    restabird.setPosition(Math.random()*(700-xraven),250);

                    if(xraven<350){
                        restabird.setVelocity(50,-50);
                    }else {
                        restabird.setVelocity(-50,-50);
                    }

                }else if(restabird.getPositionY()<0 || restabird.getPositionY()>345){
                    double xraven = Math.random() * (700 - restabird.getImage().getWidth()/2) + 30;
                    failed.setPriority(1);
                    failed.play();
                    restabird.setPosition(Math.random()*(700-xraven),250);

                    if(xraven<350){
                        restabird.setVelocity(50,-50);
                    }else {
                        restabird.setVelocity(-50,-50);
                    }
                }

                //Tabla de puntos
                String pointsText = "Points: " + numero;
                gc.fillText( pointsText, 290, 36 );
                gc.strokeText( pointsText, 290, 36 );


                if(numero==50){
                    stop();
                    Font theFont = Font.font( "Helvetica", FontWeight.BOLD, 30 );
                    gc.setFont( theFont );
                    gc.setFill(Color.BLACK);
                    gc.fillText( "Has ganado", 280, 170 );
                    gc.strokeText( "Has ganado", 280, 170 );
                    theScene.setOnMouseClicked(null);

                }

            }
        }.start();


        theStage.show();
    }
}