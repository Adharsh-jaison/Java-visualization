package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class MenuDrivenInterface {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Data Cleaning Menu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridLayout(2, 1));
            frame.add(panel, BorderLayout.CENTER);

            JTextArea textArea = new JTextArea(10, 50);
            JScrollPane scrollPane = new JScrollPane(textArea);
            panel.add(scrollPane);

            JPanel chartPanel = new JPanel();
            panel.add(chartPanel);

            DataCleaning dataCleaning = new DataCleaning();

            int choice;
            do {
                System.out.println("\nMenu:");
                System.out.println("1. Run the dataset");
                System.out.println("2. Sum of null values in the data");
                System.out.println("3. Drop rows containing null values");
                System.out.println("4. Drop columns");
                System.out.println("5. Create a new cleaned dataset");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                Scanner scanner = new Scanner(System.in);
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        dataCleaning.importDataset("HepatitisCdata.csv");
                        textArea.setText("Dataset imported successfully.\n");
                        break;
                    case 2:
                        dataCleaning.displaySumOfNAValues();
                        // textArea.append("Sum of NA values by column:\n" + dataCleaning.getSumOfNAValuesString() + "\n");
                        break;
                    case 3:
                        dataCleaning.removeNARows();
                        textArea.append("Rows containing 'NA' values removed.\n");
                        break;
                    case 4:
                        dataCleaning.dropColumns();
                        textArea.append("Columns dropped successfully.\n");
                        break;
                    case 5:
                        dataCleaning.createCleanedDataset();
                        dataCleaning.writeToNewFile("cleaned_data.csv");
                        textArea.append("New cleaned dataset created.\n");
                        textArea.append("Cleaned dataset written to cleaned_data.csv.\n");
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        break;
                    default:
                        textArea.append("Invalid choice. Please enter a valid option.\n");
                }

                // Update pie chart based on the current dataset
                if (dataCleaning.isDatasetLoaded()) {
                    JFreeChart pieChart = createPieChart(dataCleaning.getDataset());
                    chartPanel.removeAll();
                    ChartPanel chartPanelWrapper = new ChartPanel(pieChart);
                    chartPanel.add(chartPanelWrapper);
                    frame.revalidate();
                    frame.repaint();
                }
            } while (choice != 6);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static JFreeChart createPieChart(String[][] dataset) {
        DefaultPieDataset datasetForPieChart = new DefaultPieDataset();
        
        // Assuming "Sex" column is the second column (index 1) in the dataset
        Map<String, Integer> sexCounts = new HashMap<>();
        for (String[] row : dataset) {
            String sex = row[1].trim();
            sexCounts.put(sex, sexCounts.getOrDefault(sex, 0) + 1);
        }
        
        // Add data to the pie chart dataset
        for (Map.Entry<String, Integer> entry : sexCounts.entrySet()) {
            datasetForPieChart.setValue(entry.getKey(), entry.getValue());
        }

        return ChartFactory.createPieChart(
                "Sex Distribution",   // chart title
                datasetForPieChart,   // data
                true,                 // include legend
                true,
                false
        );
    }
}
