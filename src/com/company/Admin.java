package com.company;

import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin {
    Scanner in = new Scanner(System.in);
    Savings savings = new Savings();

    public void startMenu() {
        System.out.println("[1] Set up new trip budget. ");
        System.out.println("[2] Add spend entry. ");
        System.out.println("[3] Add spend entry from savings.");
        System.out.println("[4] Total spent per category.");
        System.out.println("[5] Spent per category details.");
        System.out.println("[6] Total spent per day.");
        System.out.println("[7] Spent per days details.");
        System.out.println("[8] Quit.");
    }

    public Days daysObject() {
        try {
            System.out.println("Enter start date: (YYYY-MM-DD)");
            String str = in.nextLine();
            LocalDate startDate = LocalDate.parse(str);
            System.out.println("Enter end date: (YYYY-MM-DD)");
            String str1 = in.nextLine();
            LocalDate endDate = LocalDate.parse(str1);
            Days days = new Days(startDate, endDate);
            Archive.saveDatesCsv(days);
            return days;

        } catch (DateTimeException e) {
            System.out.println("Use date format YYYY-MM-DD");
        }
        return null;
    }

    public Budget budgetObject() {
        System.out.println("Enter budget: ");
        String str = in.nextLine();
        double bdgt = Double.parseDouble(str);
        Budget budget = new Budget(bdgt);
        Archive.saveBudgetCsv(budget);
        return budget;
    }

    public Spent spentObject(ArrayList<Days> daysList) {
        {
            try {
                System.out.println("Enter date: ");
                String str = in.nextLine();
                LocalDate dateSpent = LocalDate.parse(str);
                for (Days days : daysList) {
                    if ((days.getStartDate().isBefore(dateSpent) || days.getStartDate().isEqual(dateSpent)) &&
                            (days.getEndDate().isAfter(dateSpent) || days.getEndDate().isEqual(dateSpent))) {
                    } else {
                        System.out.println("You entered a date outside the budgeted period.");
                        return null;
                    }
                }
                System.out.println("Enter amount: ");
                String str1 = in.nextLine();
                double db = 0;
                double decimals = 0;
                if (str1.matches("^\\d+\\.?\\d*$")) {
                    db = Double.parseDouble(str1);
                }
                int intValue = (int) db;
                if (db != intValue) {
                    decimals = 1 - (db - intValue);
                }
                savings.addToListsavings(decimals);
                Archive.saveSaveCsv(decimals);
                Category ctgSpent = subMenu();
                if (ctgSpent != null) {
                    Spent spent = new Spent(dateSpent, intValue, ctgSpent);
                    Archive.saveSpentCsv(spent);
                    return spent;
                } else {
                    System.out.println("You entered a wrong entry.");
                    savings.getListSavings().remove(decimals);
                    savings.removeSavedCsv();
                }

            } catch (DateTimeException e) {
                System.out.println("Use date format YYYY-MM-DD");
            }
            return null;
        }
    }

    public Category subMenu() {
        try {
            System.out.println("Choose category: ");
            System.out.println("[1] Food.");
            System.out.println("[2] Entertainment.");
            System.out.println("[3] Accommodation.");
            System.out.println("[4] Transport.");
            System.out.println("[5] Misc.");
            String choice = in.nextLine();
            int x = Integer.parseInt(choice);
            if (x > 0 && x < 8) {
                return Category.values()[Integer.parseInt(choice) - 1];
            }
        } catch (NumberFormatException e) {

        }
        return null;
    }


    public double spendSavings() {
        System.out.println("Enter amount: ");
        String str = in.nextLine();
        double db = 0;
        if (str.matches("\\d+\\.\\d+")) {
            db = Double.parseDouble(str);
        }
        if (savings.totalSaved() > db) {
            double x = db * -1;
            savings.addToListsavings(x);
            Archive.saveSaveCsv(x);
            System.out.println("You successfully used funds from your savings account. ");
            return x;
        } else {
            System.out.println("You don't have enough saved for the entered amount. ");
        }

        return 0;
    }

    public void runReadSavings() {
        savings.readSavings();
    }

    public String savingsToString() {
        StringBuilder printSavings = new StringBuilder();
        DecimalFormat df2 = new DecimalFormat();
        printSavings.append("Total saved: €" + df2.format(savings.totalSaved()) + "\n");
        return printSavings.toString();
    }
}