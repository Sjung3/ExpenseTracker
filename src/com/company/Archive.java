package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Archive {
    public static void saveDatesCsv(Days days) {
        try (
                FileWriter fw = new FileWriter("dates.csv", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter outFile = new PrintWriter(bw);
        ) {
            outFile.println(days.getStartDate() + ";" + days.getEndDate());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBudgetCsv(Budget budget) {
        try (
                FileWriter fw = new FileWriter("trpBudget.csv", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter outFile = new PrintWriter(bw);
        ) {
            outFile.println(budget.getTripBudget());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSpentCsv(Spent spent) {
        try (
                FileWriter fw = new FileWriter("spendings.csv", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter outFile = new PrintWriter(bw);
        ) {
            outFile.println(spent.getDateSpent() + ";" + spent.getAmntSpent() + ";" + spent.getCtgSpent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSaveCsv(Double db) {
        try (
                FileWriter fw = new FileWriter("savings.csv", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter outFile = new PrintWriter(bw);
        ) {
            outFile.println(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
