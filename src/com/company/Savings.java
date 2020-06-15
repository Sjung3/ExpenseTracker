package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Savings extends Accountant{
    ArrayList<Double> listSavings;

    public Savings() {
        this.listSavings = new ArrayList<Double>();
    }

    public ArrayList<Double> getListSavings() {
        return listSavings;
    }

    public void addToListsavings(Double db) {
        listSavings.add(db);
    }

    public void removeListSavings(Double db) {
        listSavings.remove(db);
    }

    public double totalSaved() {
        double sum = 0;
        double dif = 0;
        for (int i = 0; i < listSavings.size(); i++) {
            sum += listSavings.get(i);
        }
        dif = sum - sumSpent();
        return dif;
    }

    public ArrayList<Double> readSavings() {
        File file = new File("savings.csv");
        try {
            Scanner inFile = new Scanner(file);
            while (inFile.hasNext()) {
                String str = inFile.nextLine();
                double db = Double.parseDouble(str);
                listSavings.add(db);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return getListSavings();
    }

    public void removeSavedCsv() {
        try {
            RandomAccessFile raf = new RandomAccessFile("savings.csv", "rw");
            long length = raf.length();
            raf.setLength(length - 2);
            raf.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
