package schedule;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import queue.Generator;
import queue.Paquet;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by MOSCHA on 11-05-2016.
 */
public class Scheduler extends ScheduledService {


    List<Generator> queues;
    Scheduling algo;
    private BlockingQueue<Paquet> sortie;

    public  Scheduler(List queues,Scheduling algo){
        this.queues  = queues;
        this.algo = algo;
        this.sortie = new LinkedBlockingQueue<Paquet>();
    }

    @Override
    protected Task<Void> createTask() {
        /**
         * C'est ici que vous devez implémenter les algorithmes d'ordonnancement
         * lt nom de l'ordonnanceur est le paramètre passé au constructeur
         * c'est un élément de enum Scheduling
         */

        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                /**
                 * Parcourir la liste queues des Générateurs selon les information de
                 *  leurs tampons qui sont des listes
                 *  des BlockingQueue Contenant les paquets générés :
                 * 1- le poids [weight]
                 * 2- la numéro de priorité
                 * 3- à tour de rôle
                 *
                 * A chaque paquet traité on estampille le temps de sortie
                 * ainsi que le délai d'attente dans sa file initiale et on
                 * le met dans la sorite
                 */
                    for (Generator gen : queues) {
                        BlockingQueue<Paquet> tampon = gen.getTampon();
                        if (!tampon.isEmpty()) {
                            System.out.println(gen.getName() + " : " + tampon.take().getBeginTime());
                        }
                    }
                    return null;
            }
        };

    }
}
