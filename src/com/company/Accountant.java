package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class Accountant {
    private ArrayList<Days> listDays;
    private ArrayList<Budget> listBudget;
    private ArrayList<Spent> listSpent;
    private HashMap<Category, Double> listCtgSum;
    private HashMap<LocalDate, Double> listDatesSum;

    public Accountant() {
        this.listDays = new ArrayList<Days>();
        this.listBudget = new ArrayList<Budget>();
        this.listSpent = new ArrayList<Spent>();
        this.listCtgSum = new HashMap<Category, Double>();
        this.listDatesSum = new HashMap<LocalDate, Double>();
    }

    public ArrayList<Days> getListDays() {
        return listDays;
    }

    public ArrayList<Budget> getListBudget() {
        return listBudget;
    }

    public ArrayList<Spent> getListSpent() {
        return listSpent;
    }

    public void addToListDays(Days days) {
        listDays.add(days);
    }

    public void addToListBudget(Budget budget) {
        listBudget.add(budget);
    }

    public void addToListSpent(Spent spent) {
        listSpent.add(spent);
    }

    public void removeFromListDays(Days days) {
        listDays.remove(days);
    }

    public ArrayList<Days> readDatesCsv() {
        File file = new File("dates.csv");
        try {
            Scanner inFile = new Scanner(file);
            while (inFile.hasNext()) {
                String line = inFile.nextLine();
                String[] lineData = line.split(";");
                Days days = new Days(LocalDate.parse(lineData[0]), LocalDate.parse(lineData[1]));
                listDays.add(days);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return listDays;
    }

    public void removeDatesCsv() {
        try {
            RandomAccessFile raf = new RandomAccessFile("dates.csv", "rw");
            long length = raf.length();
            raf.setLength(length - 22);
            raf.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Budget> readBudgetCsv() {
        File file = new File("trpBudget.csv");
        try {
            Scanner inFile = new Scanner(file);
            while (inFile.hasNext()) {
                String line = inFile.nextLine();
                String[] lineData = line.split(";");
                Budget budget = new Budget(Double.parseDouble(lineData[0]));
                listBudget.add(budget);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return listBudget;
    }

    public ArrayList<Spent> readSpentCsv() {
        File file = new File("spendings.csv");
        try {
            Scanner inFile = new Scanner(file);
            while (inFile.hasNext()) {
                String line = inFile.nextLine();
                String[] lineData = line.split(";");
                Spent spent = new Spent(LocalDate.parse(lineData[0]), Double.parseDouble(lineData[1]), Category.valueOf(lineData[2]));
                listSpent.add(spent);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return listSpent;
    }

    public double sumSpent() {
        double dbSum = listSpent.stream()
                .mapToDouble(Spent::getAmntSpent)
                .sum();
        return dbSum;
    }

    public double nrDays() {
        double daysBudgeted = 0;
        for (Days list : listDays) {
            daysBudgeted = DAYS.between(list.getStartDate(), list.getEndDate());
        }
        return daysBudgeted;
    }

    public void sumCtgs() {
        for (Category ctg : Category.values()) {
            listCtgSum.put(ctg, 0.0);
        }
        for (Spent spent : listSpent) {
            double ctgSum = listCtgSum.get(spent.getCtgSpent());
            listCtgSum.put(spent.getCtgSpent(), ctgSum + spent.getAmntSpent());
        }
    }

    public void sumDates() {
        for (Spent dates : listSpent) {
            listDatesSum.put(dates.getDateSpent(), 0.0);
        }
        for (Spent spent : listSpent) {
            double dateSum = listDatesSum.get(spent.getDateSpent());
            listDatesSum.put(spent.getDateSpent(), dateSum + spent.getAmntSpent());
        }
    }

    public String sumCtgToString() {
        StringBuilder ctgSum = new StringBuilder();
        Map<Category, Double> newMap = new TreeMap<Category, Double>(listCtgSum);
        Set set = newMap.entrySet();
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            Map.Entry me = (Map.Entry) itr.next();
            ctgSum.append("\n" + me.getKey() + ": €" + me.getValue());
        }
        return ctgSum.toString();
    }

    public String ctgDtlsToString() {
        StringBuilder printCtgDtls = new StringBuilder();
        Collections.sort(listSpent);
        for (Spent list : listSpent) {
            printCtgDtls.append(list.getCtgSpent() + " €" + list.getAmntSpent() + " " + list.getDateSpent() + "\n");
        }
        return printCtgDtls.toString();
    }

    public String sumDateToString() {
        StringBuilder dateSum = new StringBuilder();
        Map<LocalDate, Double> newMap = new TreeMap<LocalDate, Double>(listDatesSum);
        Set set = newMap.entrySet();
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            Map.Entry me = (Map.Entry) itr.next();
            dateSum.append("\n" + me.getKey() + ": €" + me.getValue());
        }
        return dateSum.toString();
    }

    public String dateDtlsToString() {
        StringBuilder printDateDtls = new StringBuilder();
        Collections.sort(listSpent);
        for (Spent list : listSpent) {
            printDateDtls.append("\n" + list.getDateSpent() + " " + list.getCtgSpent() + " €" + list.getAmntSpent());
        }
        return printDateDtls.toString();
    }

    public String startTostring() {
        StringBuilder printStart = new StringBuilder();
        double avBudget = 0;
        if (listBudget.size() == 0) {
            printStart.append("No budget has been set for this trip. ");
        } else {
            for (Budget list : listBudget) {
                printStart.append("\nTrip budget: €" + list.getTripBudget() + "\n");
            }
            for (Days list : listDays) {
                printStart.append("Start date: " + list.getStartDate() + " End date: " + list.getEndDate() + "\n");
            }
            DecimalFormat df2 = new DecimalFormat("#.##");
            for (Budget list : listBudget) {
                avBudget = list.getTripBudget() / nrDays();
            }
            printStart.append("\n" + "Daily budget: €" + df2.format(avBudget) + "\n");
            double avSpent = sumSpent() / nrDays();
            printStart.append("Average daily spent: €" + df2.format(avSpent) + "\n");
            printStart.append("Total trip spent: €" + sumSpent() + "\n");
            double spentVsBudget = 0;
            for (Budget list : listBudget) {
                spentVsBudget = (sumSpent() / list.getTripBudget()) * 100;
            }
            printStart.append("Total spent of budget: " + df2.format(spentVsBudget) + " % ");
        }
        return printStart.toString();
    }
}