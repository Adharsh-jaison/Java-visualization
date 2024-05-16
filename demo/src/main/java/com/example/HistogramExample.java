package com.example;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

// Class for displaying a histogram chart with age distribution by sex
public class HistogramExample extends JFrame {

    // Constructor for the HistogramExample class
    public HistogramExample(String title, List<Integer> maleAges, List<Integer> femaleAges) {
        super(title); // Call the constructor of the superclass (JFrame) with a title

        // Create a histogram chart
        JFreeChart chart = createHistogramChart(maleAges, femaleAges);
        
        // Create a panel to hold the chart and set its preferred size
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        
        // Set the content pane of the frame to the chart panel
        setContentPane(chartPanel);
    }

    // Method to create a histogram chart
    private JFreeChart createHistogramChart(List<Integer> maleAges, List<Integer> femaleAges) {
        // Create a dataset for the histogram
        HistogramDataset dataset = new HistogramDataset();
        
        // Add series for male and female ages to the dataset
        dataset.addSeries("Female", femaleAges.stream().mapToDouble(i -> i).toArray(), 20);
        dataset.addSeries("Male", maleAges.stream().mapToDouble(i -> i).toArray(), 20);

        // Create the histogram chart with the dataset
        return ChartFactory.createHistogram(
                "Age Distribution by Sex", // Chart title
                "Age", // X-axis label
                "Frequency", // Y-axis label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot orientation
                true, // Include legend
                true, // Include tooltips
                false // Don't include URLs
        );
    }

    // Main method for running the program
    public static void main(String[] args) {
        String csvFilePath = "cleaned_data.csv"; // Path to the CSV file
        List<Integer> maleAges = new ArrayList<>(); // List to store male ages
        List<Integer> femaleAges = new ArrayList<>(); // List to store female ages

        try (Reader reader = new FileReader(csvFilePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            // Iterate through each record in the CSV file
            for (CSVRecord record : csvParser) {
                String sex = record.get("Sex"); // Get the sex from the record
                int age = Integer.parseInt(record.get("Age")); // Get the age from the record and parse it to integer

                // Add age to the appropriate list based on sex
                if (sex.equalsIgnoreCase("m")) {
                    maleAges.add(age);
                } else if (sex.equalsIgnoreCase("f")) {
                    femaleAges.add(age);
                }
            }

            // Create and display the histogram example
            SwingUtilities.invokeLater(() -> {
                HistogramExample example = new HistogramExample("Histogram Example", maleAges, femaleAges);
                example.setSize(800, 600); // Set size of the frame
                example.setLocationRelativeTo(null); // Center the frame on the screen
                example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Exit program when window is closed
                example.setVisible(true); // Make the frame visible
            });

        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace if an IO exception occurs
        }
    }
}
