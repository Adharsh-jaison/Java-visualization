package com.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Class for displaying a pie chart with data from a CSV file
public class PieChartExample extends JFrame {

    // Constructor for the PieChartExample class
    public PieChartExample(String title, Map<String, Map<String, Integer>> categoryData) {
        super(title); // Call the constructor of the superclass (JFrame) with a title

        JTabbedPane tabbedPane = new JTabbedPane(); // Create a tabbed pane for displaying multiple charts

        // Iterate through each category and its associated data
        for (String category : categoryData.keySet()) {
            DefaultPieDataset dataset = new DefaultPieDataset(); // Create a dataset for the current category
            Map<String, Integer> sexData = categoryData.get(category); // Get sex data for the current category

            // Add data points to the dataset
            for (String sex : sexData.keySet()) {
                dataset.setValue(sex, sexData.get(sex));
            }

            // Create a pie chart with the dataset
            JFreeChart chart = ChartFactory.createPieChart(
                    "Distribution of Males and Females in " + category, // Chart title
                    dataset, // Dataset
                    true, // Include legend
                    true, // Include tooltips
                    false // Don't include URLs
            );

            PiePlot plot = (PiePlot) chart.getPlot(); // Get the plot of the chart
            plot.setStartAngle(140); // Set the start angle for the pie plot

            ChartPanel chartPanel = new ChartPanel(chart); // Create a panel to hold the chart
            tabbedPane.add(category, chartPanel); // Add the chart panel to the tabbed pane
        }

        setContentPane(tabbedPane); // Set the content pane of the frame to the tabbed pane
    }

    // Main method for running the program
    public static void main(String[] args) {
        String csvFilePath = "cleaned_data.csv"; // Path to the CSV file
        Map<String, Map<String, Integer>> categoryData = readCsvData(csvFilePath); // Read data from CSV file

        // Create and display the pie chart example
        SwingUtilities.invokeLater(() -> {
            PieChartExample example = new PieChartExample("Category Distribution", categoryData);
            example.setSize(800, 600); // Set size of the frame
            example.setLocationRelativeTo(null); // Center the frame on the screen
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Exit program when window is closed
            example.setVisible(true); // Make the frame visible
        });
    }

    // Method to read data from a CSV file and store it in a map
    private static Map<String, Map<String, Integer>> readCsvData(String csvFilePath) {
        Map<String, Map<String, Integer>> categoryData = new HashMap<>(); // Map to store category data

        try (Reader reader = new FileReader(csvFilePath)) { // Open a reader for the CSV file
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader); // Parse CSV records

            // Iterate through each record in the CSV file
            for (CSVRecord record : records) {
                String category = record.get("Category"); // Get the category from the record
                String sex = record.get("Sex"); // Get the sex from the record

                // If category doesn't exist in the map, add it with an empty map
                categoryData.putIfAbsent(category, new HashMap<>());
                Map<String, Integer> sexData = categoryData.get(category); // Get sex data for the category
                sexData.put(sex, sexData.getOrDefault(sex, 0) + 1); // Increment count for the sex
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace if an IO exception occurs
        }

        return categoryData; // Return the map containing the category data
    }
}
