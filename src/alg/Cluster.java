package alg;

import Core.Controller;

import java.util.ArrayList;
import java.util.Arrays;

public class Cluster {
    ArrayList<ArrayList<Double>> elements = new ArrayList<>();
    ArrayList<Double> centroid = new ArrayList<>();
    double WCV = 0;
    private static boolean CHANGED = true;
    private static StringBuilder stringBuilder = new StringBuilder();


    public ArrayList<ArrayList<Double>> getElements() {
        return elements;
    }

    public void setCentroid() {
        ArrayList<Double> prev = (ArrayList<Double>) centroid.clone();
        if (elements.size() > 1) {
            for (int i = 0; i < centroid.size(); i++) {
                centroid.set(i, 0.);
            }
            for (ArrayList<Double> a : elements) {
                for (int i = 0; i < a.size(); i++) {
                    centroid.set(i, centroid.get(i) + a.get(i));
                }
            }
            for (int i = 0; i < centroid.size(); i++) {
                centroid.set(i, centroid.get(i) / elements.size());
            }
        }
        for (int i = 0; i < centroid.size(); i++) {
            if (prev.get(i) - centroid.get(i) != 0) {
                CHANGED = true;
                break;
            }
        }
    }

    public void clearElements() {
        this.elements.clear();
    }

    public void add(ArrayList<Double> element) {
        elements.add(element);
        if (elements.size() == 1 && centroid.size() == 0) {
            for (int i = 0; i < elements.get(0).size(); i++) {
                centroid.add(0.);
            }
        }
    }

    public ArrayList<Double> getCentroid() {
        return centroid;
    }

    public static String train(ArrayList<Cluster> clusters) {
        int it = 0;
        do {
            CHANGED = false;
            for (Cluster c :
                    clusters) {
                c.setWCV();
                c.setCentroid();
                c.clearElements();
            }
            for (ArrayList<Double> e :
                    Controller.DATA) {
                Cluster.defineCluster(clusters, e);
            }
            stringBuilder.append("it").append(it++).append(" WCF: ").append(completeWCV(clusters)).append("\n");
        } while (CHANGED);
        stringBuilder.append("RESULTS-----").append("\n");
        int id = 0;
        for (Cluster cluster:
             clusters) {
            for (ArrayList<Double> d :
                    cluster.getElements()) {
                stringBuilder.append("Cluster ").append(id).append(Arrays.toString(d.toArray())).append("\n");
            }
            stringBuilder.append("Cluster ").append(id).append(" WCV: ").append(cluster.WCV).append("\n");
            id++;
        }
        stringBuilder.append("OUT WCV: ").append(completeWCV(clusters)).append("\n");
        String out = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());
        return out;
    }

    public static double completeWCV(ArrayList<Cluster> clusters) {
        double sum = 0;
        for (Cluster c :
                clusters) {
            sum += c.WCV;
        }
        return sum;
    }

    public void setWCV() {
        WCV = 0;
        for (int i = 0; i < elements.size() - 1; i++) {
            for (int j = i + 1; j < elements.size(); j++) {
                for (int x = 0; x < elements.get(i).size(); x++) {
                    WCV += Math.pow(elements.get(i).get(x) - elements.get(j).get(x), 2);
                }
            }
        }
        WCV /= elements.size();
    }

    public static void defineCluster(ArrayList<Cluster> clusters, ArrayList<Double> element) {
        double distance = Cluster.calcDistance(clusters.get(0), element);
        int id = 0;
        for (int i = 1; i < clusters.size(); i++) {
            double tmp = Cluster.calcDistance(clusters.get(i), element);
            if (distance > tmp) {
                distance = tmp;
                id = i;
            }
        }
        clusters.get(id).add(element);

    }

    private static Double calcDistance(Cluster cluster, ArrayList<Double> element) {
        ArrayList<Double> centroid = cluster.getCentroid();
        double distance = 0;
        for (int i = 0; i < centroid.size(); i++) {
            distance += Math.pow(centroid.get(i) - element.get(i), 2);
        }
        return Math.sqrt(distance);
    }
}

