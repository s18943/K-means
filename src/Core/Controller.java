package Core;

import alg.Cluster;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Controller {

    private int k = 3;
    private ArrayList<Cluster> clusters = new ArrayList<>();
    public static ArrayList<ArrayList<Double>> DATA = new ArrayList<>();
    ArrayList<XYChart.Series<Number, Number>> series = new ArrayList<>();
    String dataFilePath = "iris.data";

    @FXML
    ScatterChart<Number, Number> chart;
    @FXML
    TextField kField;
    @FXML
    TextArea log;

    public void initialize() {
        chart.setLegendVisible(false);
    }

    public void newKMeans() throws FileNotFoundException {
        clusters.clear();
        Scanner scanner = new Scanner(new FileInputStream(dataFilePath));
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster());
        }
        int index = 0;
        while (scanner.hasNext()) {
            StringTokenizer st = new StringTokenizer(scanner.nextLine(), ", ");
            ArrayList<Double> data = new ArrayList<>();
            while (st.hasMoreTokens()) {
                data.add(Double.parseDouble(st.nextToken()));
            }
            if (index < k) {
                clusters.get(index++).add(data);
            } else {
                clusters.get((int) (Math.random() * k)).add(data);
            }
        }
        for (Cluster c :
                clusters) {
            DATA.addAll(c.getElements());
        }
    }

    public void createChart() {
        chart.getData().clear();
        series.clear();
        for (int i = 0; i < k; i++) {
            series.add(new XYChart.Series<>());
        }
        int id = 0;
        for (Cluster c :
                clusters) {
            ArrayList<ArrayList<Double>> list = c.getElements();
            for (ArrayList<Double> doubles : list) {
                double X = 0;
                double Y = 0;
                for (int x = 0; x < doubles.size(); x++) {
                    if (x < doubles.size() / 2) {
                        X += doubles.get(x);
                    } else {
                        Y += doubles.get(x);
                    }
                }
                X /= doubles.size() / 2;
                Y /= doubles.size() - doubles.size() / 2;
                series.get(id).getData().add(new XYChart.Data<>(X, Y));
            }
            id++;
        }
        for (XYChart.Series<Number, Number> s :
                series) {
            chart.getData().add(s);
        }


    }

    @FXML
    void setK() {
        try {
            k = Integer.parseInt(kField.getText());
            log.setText("DONE");
        } catch (Exception e) {
            log.setText("ERROR 1");
        }
    }

    @FXML
    void start() {
        try {
            newKMeans();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        log.setText(Cluster.train(clusters));
        createChart();
    }

    @FXML
    void choose(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(((Node) actionEvent.getSource()).getScene().getWindow());
        if (file == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
                log.setText("ERROR 2");
            }
        } else
            dataFilePath = file.getAbsolutePath();
    }
}
