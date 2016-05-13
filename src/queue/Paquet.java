package queue;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



/**
 * Created by MOSCHA on 01-05-2016.
 */
public  class Paquet extends Service<Rectangle> {
    protected double beginTime ;
    protected int posQueue;
    protected double finichTime;
    protected double rate;
    protected double delai;

    public double getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(double beginTime) {
        this.beginTime = beginTime;
    }

    public double getFinichTime() {
        return finichTime;
    }

    public void setFinichTime(double finichTime) {
        this.finichTime = finichTime;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getDelai() {
        return delai;
    }

    public void setDelai(double delai) {
        this.delai = delai;
    }

    /**
     *
     * @param gTime le temps d'arrivé d'un paquet
     * @param rate le débit du flux
     * @param posQueue la postion de la file dans l'interface graphique
     */
    public  Paquet (double gTime,double rate,int posQueue){
        this.beginTime = gTime;
        this.posQueue = posQueue;
        this.finichTime = 0;
        this.rate = rate;
        this.delai=0;

    }


    @Override
    protected Task<Rectangle> createTask() {
        return new Task<Rectangle>() {
            @Override
            protected Rectangle call() throws Exception {
                if (isCancelled()) {
                    System.out.println("Annulation");
                }
                Rectangle trame = new Rectangle(100, posQueue,30,20);
                trame.setFill(Color.YELLOW);
                trame.setArcWidth(5);
                trame.setArcHeight(5);
                trame.setStroke(Color.BLACK);
                return trame;
            }
        };
    }

    /**
     *
     * @param number le nombre double à convertir en String
     * @return le double transformé en chaine de caractère
     */
    private String doubleToString(double number){
        return String.valueOf(Math.round(number));
    }
}
