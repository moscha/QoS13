package queue;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by MOSCHA on 01-05-2016.
 */
public class Generator extends AnimationTimer {
    private  double previousTime=0;
    private double  tempTotal = 0;
    final private double SECOND=1000;
    final private int heighSpeed  = 10000000;
    final private int mediumSpeed = 100000000;
    final private int slowSpeed   = 1000000000;
    private  Random rand;
    private  int posQueue;
    private  VBox centre;
    private  Group queue ;
    private Path path;
    final private int endQueue = 500;

    // lambda est le nombre moyen de paquet par seconde [Débit]
    private String name;
    private double lambda;
    private int weight;
    private BlockingQueue<Paquet> tampon;
    private List<String> lines;

    /**
     *
     * @param name est le nom de la file
     * @param weight le poids de la file d'attente
     * @param lambda le paramètre dans la loi exponentielle
     * @param posQueue la position de la file d'attente sur l'interface
     * @param centre le layout ou ajouter la file est les paquets générés
     *
     */
    public  Generator(String name,double lambda,int weight,int posQueue,VBox centre){
        this.name = name;
        this.lambda = lambda;
        this.weight = weight;
        this.tampon = new LinkedBlockingQueue<Paquet>();
        this.lines = new ArrayList<>();

        this.rand = new Random();
        this.posQueue = posQueue;
        this.centre = centre;
        this.queue = new Group();
        this.centre.getChildren().add(this.queue);
        path = getPath();
        queue.getChildren().add(path);
    }

    /**
     *
     * @return Chemin visuel pour les paquet pour simuler une file d'attente
     */
    private Path getPath() {
        Path path= new Path(new MoveTo(100,posQueue),new LineTo(endQueue,posQueue));
        path.setStrokeWidth(20);
        path.setStroke(Color.DARKGOLDENROD);
        return path;
    }

    public int getWeight() {
        return weight;
    }

    public double getLambda() {
        return lambda;
    }

    public String getName() {

        return name;
    }

    public List<String> getLines() {
        return lines;
    }

    public BlockingQueue<Paquet> getTampon() {
        return tampon;
    }

    @Override
    public void handle(long currentTime) {
        if(this.previousTime == 0){
            this.previousTime = currentTime;
            return;
        }
        double tempEcoule = (currentTime - this.previousTime) / mediumSpeed;
        double  t_attente = Exp(rand,lambda)*1000 ;
        tempTotal = tempTotal + t_attente;
        if(tempEcoule >= t_attente){
            final Paquet paquet = new  Paquet(tempTotal,lambda,this.posQueue);
            paquet.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    Node trame = paquet.getValue();

                    PathTransition transition = new PathTransition(Duration.seconds(2),path,trame);
                    transition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                    transition.setInterpolator(Interpolator.LINEAR);
                    queue.getChildren().addAll(transition.getNode());
                    transition.play();
                }
            });
            paquet.start();
            try {
                this.tampon.put(paquet);
                this.lines.add(this.name+";"+tempTotal+";"+lambda+";"+this.weight);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.previousTime = currentTime;

        }
    }

    /**
     *
     * @param rand pour chaque file d'attente un objet Random est créé
     * @param lambda la moyenne d'arrivée des paquets c'est le taux dans la loi exponentielle
     * @return le temps d'arrivée du paquet (inter-arrivée)
     */
    public static double Exp(Random rand,double lambda) {
        return -(1 / lambda) * Math.log(1 - rand.nextDouble());
    }


}
