package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    public static void run() {
        Admin admin = new Admin();
        Accountant accountant = new Accountant();

        Scanner in = new Scanner(System.in);
        String input = " ";

        ArrayList<Days> dates = accountant.readDatesCsv();
        ArrayList<Budget> trpBudget = accountant.readBudgetCsv();
        ArrayList<Spent> spendings = accountant.readSpentCsv();
        admin.runReadSavings();

        while (!input.equals("8")) {
            System.out.println(accountant.startTostring());
            System.out.println(admin.savingsToString());
            admin.startMenu();
            input = in.nextLine();

            switch (input) {
                case "1":
                    if (accountant.getListBudget().size() < 1) {
                        Days days = admin.daysObject();
                        if (days != null) {
                            if (days.getStartDate().isBefore(days.getEndDate())) {
                                accountant.addToListDays(days);
                            } else {
                                System.out.println("End date can´t be set to an earlier date than start date. \n");
                                accountant.removeFromListDays(days);
                                accountant.removeDatesCsv();
                                break;
                            }
                            if (!accountant.getListDays().equals(null)) {
                                Budget budget = admin.budgetObject();
                                accountant.addToListBudget(budget);
                            }
                        }
                    } else {
                        System.out.println("Budget has already been set. \n");
                    }

                    break;
                case "2":
                    Spent spent = admin.spentObject(accountant.getListDays());
                    if (spent != null) {
                        accountant.addToListSpent(spent);
                    }
                    break;
                case "3":
                    admin.spendSavings();
                    break;
                case "4":
                    accountant.sumCtgs();
                    System.out.println(accountant.sumCtgToString());
                    break;
                case "5":
                    System.out.println(accountant.ctgDtlsToString());
                    break;
                case "6":
                    accountant.sumDates();
                    System.out.println(accountant.sumDateToString());
                    break;
                case "7":
                    System.out.println(accountant.dateDtlsToString());
                    break;
                case "8":
                    System.out.println("Enjoy your day.");
                    break;
                default:
                    System.out.println("Something went wrong. Please try again.");
                    break;
            }
        }
    }
}
