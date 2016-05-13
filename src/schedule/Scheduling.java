package schedule;

/**
 * Created by MOSCHA on 11-05-2016.
 */
public enum Scheduling {
    RR("RR"),
    DRR("DRR"),
    PQ("PQ"),
    SPQ("SPQ"),
    FQ("FQ"),
    WFQ("WFQ");

    private String algo;

    Scheduling(String algo) {
        this.algo = algo;
    }
}
