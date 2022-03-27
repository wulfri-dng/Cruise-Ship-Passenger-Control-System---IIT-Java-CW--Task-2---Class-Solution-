package com.wulfri;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int cabinCount = 12;
        Cabin[] cabinArray = new Cabin[cabinCount];
        initialise(cabinArray);
        mainMenu(cabinArray);
    }

    private static void initialise(Cabin[] cabinArray) {
        for (int i = 0; i < cabinArray.length; i++) {
            cabinArray[i] = new Cabin(i);
            cabinArray[i].initialise();
        }
    }

    private static void mainMenu(Cabin[] cabinArray) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=======================================================");
        System.out.println("||| Welcome to Cruise Ship passenger control center |||");
        while (true) {
            System.out.println("=======================================================");
            System.out.println("A: Add a customer to a cabin");
            System.out.println("V: View all cabins");
            System.out.println("E: Display Empty cabins");
            System.out.println("D: Delete customer from cabin");
            System.out.println("F: Find cabin from customer name");
            System.out.println("S: Store program data into file");
            System.out.println("L: Load program data from file");
            System.out.println("O: View passengers Ordered alphabetically by name");
            System.out.println("-----------------------------------------------------");
            System.out.print("Select an option above to continue: ");
            String userInput = scanner.next().toUpperCase(Locale.ROOT);

            switch (userInput) {
                case "A" -> addPassenger(cabinArray);
                case "V" -> viewAllCabins(cabinArray);
//                case "E" -> displayEmptyCabins(cabinArray);
//                case "D" -> deletePassenger(cabinArray);
//                case "F" -> findCabinByPassengerName(cabinArray);
//                case "S" -> storeShipData(cabinArray);
//                case "L" -> loadShipData(cabinArray);
//                case "O" -> sortPassengerAlphabetically(cabinArray);
                default -> System.out.println("Invalid input!!! Check the entered number and try again.");
            }
        }
    }

    private static void addPassenger(Cabin[] cabinArray) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter cabin number (0 - " + (cabinArray.length - 1) + ") or " + (cabinArray.length) + " to stop: ");
            try {
                int cabinNo = scanner.nextInt();
                if(cabinNo == cabinArray.length) {
                    break;
                } else if(cabinNo >= 0 && cabinNo < cabinArray.length && !cabinArray[cabinNo].isFull()) {
                    System.out.println("Adding a passenger to the cabin " + cabinNo);
                    System.out.println("Enter passenger's  first name : ");
                    String passengerFName = scanner.next();
                    System.out.println("Enter passenger's surname : ");
                    String passengerSurname = scanner.next();
                    System.out.println("Enter passenger's expenses : ");
                    double passengerExpenses;
                    try {
                        passengerExpenses = scanner.nextDouble();
                        double checkValidityTemp = passengerExpenses + 10;
                    } catch (InputMismatchException ex) {
                        System.out.println("Error!!! You cannot add a String as an expense.");
                        break;
                    }
                    try {
                        Integer.parseInt(passengerFName);
                        Integer.parseInt(passengerSurname);
                        System.out.println("Error!!! You cannot enter a number as a passenger name.");
                    } catch (NumberFormatException ex) {
                        cabinArray[cabinNo].addPassenger(passengerFName, passengerSurname, passengerExpenses);
                    }
                } else if(cabinArray[cabinNo].isFull()) {
                    System.out.println("Cabin " + cabinNo + " is already booked. Try another cabin.");
                } else {
                    System.out.println("Invalid input!!! Enter a number between 0 - " + (cabinArray.length - 1));
                }
            } catch (InputMismatchException ex) {
                System.out.println("Invalid input!!! Enter a number between 0 - " + (cabinArray.length - 1));
                System.out.println("Exiting add passenger...");
                break;
            }
        }
    }

    public static void viewAllCabins(Cabin[] cabinArray) {
        System.out.println("=====================================================");
        for (Cabin cabin : cabinArray) {
            cabin.printPassengers();
        }
        System.out.println("----------------- END OF VIEW CABIN -----------------");
        System.out.println();
    }
}
