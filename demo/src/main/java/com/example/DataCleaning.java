package com.example;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class is responsible for performing various data cleaning operations on a dataset.
 */
public class DataCleaning {
    private List<String> dataset; // List to store the dataset
    private Map<String, Integer> sumOfNAByColumn; // Map to store the sum of 'NA' values by column
    private boolean datasetLoaded; // Flag to check if the dataset is loaded

    // Constructor to initialize the fields
    public DataCleaning() {
        dataset = new ArrayList<>();
        sumOfNAByColumn = new HashMap<>();
        datasetLoaded = false;
    }

    /**
     * Method to import a dataset from a file.
     * @param filename The name of the file containing the dataset.
     */
    public void importDataset(String filename) {
        dataset.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                dataset.add(line);
            }
            System.out.println("Dataset imported successfully.");
            // Display dataset
            System.out.println("Dataset:");
            for (String row : dataset) {
                System.out.println(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to display the sum of 'NA' values by column.
     */
    public void displaySumOfNAValues() {
        calculateSumOfNAValues();
        System.out.println("Sum of NA values by column:");
        for (Map.Entry<String, Integer> entry : sumOfNAByColumn.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Method to remove rows containing 'NA' values from the dataset.
     */
    public void removeNARows() {
        List<String> cleanedDataset = new ArrayList<>();
        for (String row : dataset) {
            String[] values = row.split(",");
            boolean containsNA = false;
            for (String value : values) {
                if (value.trim().equalsIgnoreCase("NA")) {
                    containsNA = true;
                    break;
                }
            }
            if (!containsNA) {
                cleanedDataset.add(row);
            }
        }
        dataset = cleanedDataset;
        System.out.println("Rows containing 'NA' values removed.");
    }

    /**
     * Method to drop specified columns from the dataset.
     */
    public void dropColumns() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter column indices to drop (comma-separated): ");
        String input = scanner.nextLine();
        String[] indicesStr = input.split(",");
        List<Integer> columnIndicesToDrop = new ArrayList<>();
        for (String indexStr : indicesStr) {
            columnIndicesToDrop.add(Integer.parseInt(indexStr.trim()));
        }
        List<String> datasetAfterDroppingColumns = new ArrayList<>();
        for (String row : dataset) {
            String[] values = row.split(",");
            StringBuilder newRow = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                if (!columnIndicesToDrop.contains(i)) {
                    newRow.append(values[i]);
                    if (i < values.length - 1) {
                        newRow.append(",");
                    }
                }
            }
            datasetAfterDroppingColumns.add(newRow.toString());
        }
        dataset = datasetAfterDroppingColumns;
        System.out.println("Columns dropped successfully.");
    }

    /**
     * Method to count the number of rows based on the specified sex.
     * @param sex The sex character to count.
     * @return The count of rows with the specified sex.
     */
    public int getCountBySex(char sex) {
        int count = 0;
        for (String row : dataset) {
            String[] values = row.split(",");
            if (values.length > 1 && values[1].equalsIgnoreCase(String.valueOf(sex))) {
                count++;
            }
        }
        return count;
    }

    /**
     * Method to create a cleaned dataset by displaying it.
     */
    public void createCleanedDataset() {
        System.out.println("New cleaned dataset created.");
        for (String row : dataset) {
            System.out.println(row);
        }
    }

    /**
     * Method to write the cleaned dataset to a new file.
     * @param filename The name of the file to write the cleaned dataset to.
     */
    public void writeToNewFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String row : dataset) {
                writer.write(row);
                writer.newLine();
            }
            System.out.println("Cleaned dataset written to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to check if the dataset is loaded.
     * @return True if the dataset is loaded, false otherwise.
     */
    public boolean isDatasetLoaded() {
        return datasetLoaded;
    }

    /**
     * Method to get the dataset as a 2D array.
     * @return The dataset as a 2D array.
     */
    public String[][] getDataset() {
        String[][] data = new String[dataset.size()][];
        for (int i = 0; i < dataset.size(); i++) {
            data[i] = dataset.get(i).split(",");
        }
        return data;
    }

    /**
     * Method to calculate the sum of 'NA' values by column.
     */
    private void calculateSumOfNAValues() {
        sumOfNAByColumn.clear();
        if (!dataset.isEmpty()) {
            String[] columns = dataset.get(0).split(","); // Assuming CSV format
            for (String column : columns) {
                sumOfNAByColumn.put(column, 0);
            }
            for (int i = 1; i < dataset.size(); i++) {
                String[] values = dataset.get(i).split(",");
                for (int j = 0; j < values.length; j++) {
                    String value = values[j].trim();
                    if (value.isEmpty() || value.equalsIgnoreCase("NA")) {
                        String columnName = columns[j];
                        int currentCount = sumOfNAByColumn.get(columnName);
                        sumOfNAByColumn.put(columnName, currentCount + 1);
                    }
                }
            }
        }
    }
}
