package queue;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import schedule.Scheduler;
import schedule.Scheduling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Main extends Application {
    final double lambda = 2;
    private Stage fenetre;
    private  BorderPane root;
    private VBox centre;
    private  TextField name,taux,numQueue,capacite;
    private TableView<Flux> table;
    private List<Generator> queues;

    @Override
    public void start(Stage primaryStage) throws Exception{
        fenetre = primaryStage;
        root = createContent();
        fenetre.setScene(new Scene(root));
        fenetre.show();
        //TO DO: sauvegarde après arrêt de la simulation et non la fenêtre
        fenetre.setOnCloseRequest(e -> seveTextConfig());
    }

    private void seveTextConfig() {
        Iterator<Generator> it = queues.listIterator();
        while (it.hasNext()){
            Generator gen = it.next();
            try {
                Files.write(Paths.get("config/"+gen.getName()+".txt"), gen.getLines());

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    private BorderPane createContent(){
        BorderPane root = new BorderPane();
        root.setTop(getMenubar());
        root.setLeft(getLeft());
        centre = new VBox(20);
        root.setCenter(centre);
        root.setBottom(getButton());
        root.setPrefSize(1024, 600);
        return root;
    }

    private Node getButton() {


        return null;
    }

    private Node getMenubar() {
        Menu fileMenu = new Menu("Fichier");

        MenuItem demarrer = new MenuItem("Démarrer");
        demarrer.setOnAction(e -> demarrer());
        fileMenu.getItems().add(demarrer);

        MenuItem ouvrir = new MenuItem("Ouvrir");
        ouvrir.setOnAction(e -> ouvrir());
        fileMenu.getItems().add(ouvrir);

        MenuItem refaire = new MenuItem("Refaire");
        refaire.setOnAction(e -> refaire());
        fileMenu.getItems().add(refaire);

        MenuItem quitter = new MenuItem("Quitter");
        quitter.setOnAction(e -> quitter());
        fileMenu.getItems().add(quitter);

        Menu editMenu = new Menu("Edition");
        //...

        Menu configMenu = new Menu("Configuration");
        //...

        MenuBar menubar = new MenuBar();
        menubar.getMenus().addAll(fileMenu,editMenu,configMenu);
        return menubar;
    }


    private VBox getLeft() {
        VBox vBox = new VBox();
        table = new TableView<>();
        TableColumn<Flux,String>  namec = new TableColumn<>("name");
        namec.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Flux,Integer>  numc = new TableColumn<>("numQueue");
        numc.setCellValueFactory(new PropertyValueFactory<>("numQueue"));

        TableColumn<Flux,Double>  tauxc = new TableColumn<>("Taux");
        tauxc.setCellValueFactory(new PropertyValueFactory<>("Taux"));

        TableColumn<Flux,Double>  capacitec = new TableColumn<>("Capacité");
        capacitec.setCellValueFactory(new PropertyValueFactory<>("Capacité"));

        table.setItems(getFlux());
        table.getColumns().addAll(namec, numc, tauxc, capacitec);
        vBox.getChildren().add(table);

        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        HBox hBox3 = new HBox();
        name = new TextField();
        name.setPromptText("Nom ");
        name.setMinWidth(50);

        numQueue = new TextField();
        numQueue.setPromptText("Numéro queue");
        numQueue.setMinWidth(50);

        taux = new TextField();
        taux.setPromptText("Taux ");

        capacite = new TextField();
        capacite.setPromptText("Capacité");

        Button add = new Button("Ajouter");
        add.setOnAction( e -> addConfig());
        Button del = new Button("Supprimer");
        del.setOnAction( e -> delConfig());

        hBox1.setPadding(new Insets(5,5,5,5));
        hBox1.setSpacing(10);
        hBox1.getChildren().addAll(name,numQueue);
        vBox.getChildren().add(hBox1);

        hBox2.setPadding(new Insets(5,5,5,5));
        hBox2.setSpacing(10);
        hBox2.getChildren().addAll(taux,capacite);
        vBox.getChildren().add(hBox2);

        hBox3.setPadding(new Insets(5,5,5,5));
        hBox3.setSpacing(10);
        hBox3.getChildren().addAll(add,del);
        vBox.getChildren().add(hBox3);
        vBox.setStyle("-fx-border-stylel:solid; -fx-border-width:1pt; -fx-border-color:black;");
        return vBox;
    }

    private void delConfig() {
        ObservableList<Flux> selectionne, tous;
        tous = table.getItems();
        selectionne = table.getSelectionModel().getSelectedItems();

        selectionne.forEach(tous::remove);
    }

    private void addConfig() {
        Flux flux = new Flux();
        if (name.getText().isEmpty() || taux.getText().isEmpty() || numQueue.getText().isEmpty() || capacite.getText().isEmpty()) return;
        flux.setName(name.getText());
        flux.setCapacité(Double.parseDouble(capacite.getText()));
        flux.setNumQueue(Integer.parseInt(numQueue.getText()));
        flux.setTaux(Double.parseDouble(taux.getText()));

        table.getItems().add(flux);
        name.clear();
        capacite.clear();
        taux.clear();
        numQueue.clear();
    }
    /**
     * Instanstantiation de générateurs selon le nonbre de files avec leur propre lambda
     */
    private void demarrer() {
        queues = new ArrayList<>();
        /**
         * Parcourir la table de configuration
         * Chaque ligne représente une file d'attente
         */
        queues.add(new Generator("A",lambda,1,50,centre));
        queues.add(new Generator("B",lambda,1,150,centre));

        ListIterator<Generator> it = queues.listIterator();
        while (it.hasNext()){
            it.next().start();
        }
        new Scheduler(queues, Scheduling.DRR).start();
    }

    /**
     * Refaire la simulation à partir du résultat de la simulation précédente
     * Les fichier de flux entrant sont enregistrer automatiquement
     * après la fermeture de l'application
     */
    private void refaire() {

    }

    private void quitter() {
    }


    private void ouvrir() {
    }



    private ObservableList<Flux> getFlux(){
        ObservableList<Flux> flux = FXCollections.observableArrayList();
        flux.add(new Flux("A",0,2,1024));
        flux.add(new Flux("B",1,2,1024));
        flux.add(new Flux("C",2,3,1024));
        flux.add(new Flux("D",3,2,1024));
        flux.add(new Flux("E",4,3,1024));
        return flux;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
