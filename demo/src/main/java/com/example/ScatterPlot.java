package com.example;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.awt.*;

public class ScatterPlot extends JFrame {

    // Constructor to create the frame and add the charts
    public ScatterPlot(String title) {
        super(title);

        // File path to the CSV file containing the data
        String csvFile = "cleaned_data.csv";

        // Load data from the CSV file for each pair of columns (Age vs biomarker)
        double[][] ALBData = loadData(csvFile, "ALB", "Age");
        double[][] ALPData = loadData(csvFile, "ALP", "Age");
        double[][] ALTData = loadData(csvFile, "ALT", "Age");
        double[][] ASTData = loadData(csvFile, "AST", "Age");
        double[][] BILData = loadData(csvFile, "BIL", "Age");
        double[][] CHEData = loadData(csvFile, "CHE", "Age");
        double[][] CHOLData = loadData(csvFile, "CHOL", "Age");
        double[][] CREAData = loadData(csvFile, "CREA", "Age");
        double[][] GGTData = loadData(csvFile, "GGT", "Age");
        double[][] PROTData = loadData(csvFile, "PROT", "Age");

        // Create scatter plot charts for each data set
        JFreeChart ALBChart = createChart(ALBData, "Age vs ALB", "Age", "ALB");
        JFreeChart ALPChart = createChart(ALPData, "ALP vs Age", "Age", "ALP");
        JFreeChart ALTChart = createChart(ALTData, "ALT vs Age", "Age", "ALT");
        JFreeChart ASTChart = createChart(ASTData, "AST vs Age", "Age", "AST");
        JFreeChart BILChart = createChart(BILData, "BIL vs Age", "Age", "BIL");
        JFreeChart CHEChart = createChart(CHEData, "CHE vs Age", "Age", "CHE");
        JFreeChart CHOLChart = createChart(CHOLData, "CHOL vs Age", "Age", "CHOL");
        JFreeChart CREAChart = createChart(CREAData, "CREA vs Age", "Age", "CREA");
        JFreeChart GGTChart = createChart(GGTData, "GGT vs Age", "Age", "GGT");
        JFreeChart PROTChart = createChart(PROTData, "PROT vs Age", "Age", "PROT");

        // Create a panel and add all the charts to it
        JPanel panel = new JPanel(new GridLayout(2, 3));
        panel.add(new ChartPanel(ALBChart));
        panel.add(new ChartPanel(ALPChart));
        panel.add(new ChartPanel(ALTChart));
        panel.add(new ChartPanel(ASTChart));
        panel.add(new ChartPanel(BILChart));
        panel.add(new ChartPanel(CHEChart));
        panel.add(new ChartPanel(CHOLChart));
        panel.add(new ChartPanel(CREAChart));
        panel.add(new ChartPanel(GGTChart));
        panel.add(new ChartPanel(PROTChart));

        // Add the panel to the frame
        add(panel, BorderLayout.CENTER);

        // Set default close operation, pack the components, and make the frame visible
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    // Method to load data from the CSV file
    private static double[][] loadData(String csvFile, String xField, String yField) {
        try (Reader reader = new FileReader(csvFile)) {
            // Parse the CSV file
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            // Convert the iterable to a list to get the size
            List<CSVRecord> recordList = new ArrayList<>();
            for (CSVRecord record : records) {
                recordList.add(record);
            }

            int recordCount = recordList.size();
            double[][] data = new double[2][recordCount];

            // Extract data for the specified fields
            for (int i = 0; i < recordCount; i++) {
                CSVRecord record = recordList.get(i);
                data[0][i] = Double.parseDouble(record.get(xField));
                data[1][i] = Double.parseDouble(record.get(yField));
            }

            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to create a scatter plot chart using the provided data
    private static JFreeChart createChart(double[][] data, String title, String xAxisLabel, String yAxisLabel) {
        // Create a data series
        XYSeries series = new XYSeries("Data");
        for (int i = 0; i < data[0].length; i++) {
            series.add(data[0][i], data[1][i]);
        }

        // Create a data set from the series
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        // Create the scatter plot
        JFreeChart chart = ChartFactory.createScatterPlot(title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false);

        // Customize the plot appearance
        XYPlot scatterPlot = (XYPlot) chart.getPlot();
        XYItemRenderer scatterRenderer = scatterPlot.getRenderer();
        scatterRenderer.setSeriesPaint(0, Color.CYAN);

        return chart;
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScatterPlot example = new ScatterPlot("Scatter Plot");
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setVisible(true);
        });
    }
}
