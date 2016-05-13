package queue;

/**
 * Created by MOSCHA on 08-05-2016.
 */
public class Flux {
    private String name;
    private int numQueue;
    private double taux;
    private double capacité;

    public Flux(){
        this.name = "";
        this.numQueue =0;
        this.taux = 0;
        this.capacité = 0;
    }
    public Flux(String name, int numQueue, double taux, double capacité){
        this.name = name;
        this.numQueue = numQueue;
        this.taux = taux;
        this.capacité = capacité;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumQueue() {
        return numQueue;
    }

    public void setNumQueue(int numQueue) {
        this.numQueue = numQueue;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public double getCapacité() {
        return capacité;
    }

    public void setCapacité(double capacité) {
        this.capacité = capacité;
    }
}
