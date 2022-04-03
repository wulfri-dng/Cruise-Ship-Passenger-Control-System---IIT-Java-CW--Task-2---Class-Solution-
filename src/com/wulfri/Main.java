package com.wulfri;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean systemOn = true;
        int cabinCount = 12;
        Cabin[] cabinArray = new Cabin[cabinCount];
        ArrayList<Passenger> waitingList = new ArrayList<>();
        initialise(cabinArray);
        mainMenu(cabinArray, systemOn);
    }

    private static void initialise(Cabin[] cabinArray) {
        for (int i = 0; i < cabinArray.length; i++) {
            cabinArray[i] = new Cabin(i);
            cabinArray[i].initialise();
        }
    }

    private static void mainMenu(Cabin[] cabinArray, boolean systemOn) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=======================================================");
        System.out.println("||| Welcome to Cruise Ship passenger control center |||");
        while (systemOn) {
            System.out.println("=======================================================");
            System.out.println("A: Add a customer to a cabin");
            System.out.println("V: View all cabins");
            System.out.println("E: Display Empty cabins");
            System.out.println("D: Delete customer from cabin");
            System.out.println("F: Find cabin from customer name");
            System.out.println("T: Display passengers expenses");
            System.out.println("S: Store program data into file");
            System.out.println("L: Load program data from file");
            System.out.println("O: View passengers Ordered alphabetically by name");
            System.out.println("Exit: Shut down the system and exit application");
            System.out.println("-----------------------------------------------------");
            System.out.print("Select an option above to continue: ");
            String userInput = scanner.next().toUpperCase(Locale.ROOT);

            switch (userInput) {
                case "A" -> addPassenger(cabinArray);
                case "V" -> viewAllCabins(cabinArray);
                case "E" -> displayEmptyCabins(cabinArray);
                case "D" -> deletePassenger(cabinArray);
                case "F" -> findCabinByPassengerName(cabinArray);
                case "T" -> displayPassengersExpenses(cabinArray);
                case "S" -> storeShipData(cabinArray);
                case "L" -> loadShipData(cabinArray);
                case "O" -> sortPassengerAlphabetically(cabinArray);
                case "EXIT" -> systemOn = shutDown();
                default -> System.out.println("Invalid input!!! Check the entered number and try again.");
            }
        }
    }

    public static ArrayList<Passenger> getBookedPassengersArray(Cabin[] cabinArray) {
        ArrayList<Passenger> passengerArray = new ArrayList<>();
        for(Cabin cabin : cabinArray) {
            for(Passenger passenger : cabin.getPassengerArray()) {
                if(!passenger.getFirstName().equals("e")) {
                    passengerArray.add(passenger);
                }
            }
        }
        return passengerArray;
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
                    String passengerFName;
                    while (true) {
                        System.out.println("Enter passenger's  first name : ");
                        passengerFName = scanner.next();
                        if(passengerFName.equals("e")){
                            System.out.println("You cannot enter 'e' as the passenger's first name. Enter a valid name again.");
                        } else {
                            break;
                        }
                    }
                    System.out.println("Enter passenger's surname : ");
                    String passengerSurname = scanner.next();
                    System.out.println("Enter passenger's expenses : ");
                    double passengerExpenses;
                    try {
                        passengerExpenses = scanner.nextDouble();
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

    public static void displayEmptyCabins(Cabin[] cabinArray) {
        ArrayList<Cabin> emptyCabins = new ArrayList<>();
        for (Cabin cabin : cabinArray) {
            if(!cabin.isFull()) {
                emptyCabins.add(cabin);
            }
        }
        if (!(emptyCabins.size() == 0)) {
            System.out.println("====================================");
            System.out.println(emptyCabins.size() + " cabins detected with empty slots.");
            System.out.println("------------------------------------");
            for (Cabin cabin : emptyCabins) {
                System.out.println("Cabin " + cabin.getCabinNo() + " : " + cabin.emptyPassengerSlots() + " passenger slot available");
            }
            System.out.println("------------ END OF DISPLAY EMPTY CABINS ------------");
        } else {
            System.out.println("Every cabin is full.");
        }
    }

    public static void deletePassenger(Cabin[] cabinArray) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=====================================================");
        System.out.println("Enter passenger's first name to remove the passenger");
        System.out.print("Enter: ");
        String userInput = scanner.next();
        boolean passengerDeleted = false;
        for(Cabin cabin : cabinArray) {
            if(cabin.deletePassenger(userInput)) {
                System.out.println("Passenger " + userInput + " deleted successfully");
                passengerDeleted = true;
                break;
            }
        }
        if (!passengerDeleted) {
            System.out.println("Invalid input!!! Cannot find passenger : " + userInput + ". Check the name and try again.");
            System.out.println("Exiting delete passenger...");
        }
        System.out.println("---------------- END OF DELETE PASSENGER ----------------\n");
    }

    public static void findCabinByPassengerName(Cabin[] cabinArray) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=====================================================");
        System.out.println("Enter the passenger's name");
        System.out.print("Enter: ");
        String userInput = scanner.next();
        boolean passengerFound = false;
        for(Cabin cabin : cabinArray) {
            if(cabin.isPassengerAvailable(userInput)) {
                System.out.println(userInput + "'s cabin number: " + cabin.getCabinNo());
                passengerFound = true;
                break;
            }
        }
        if(!passengerFound) {
            System.out.println("Invalid input!!! Cannot find passenger : " + userInput + ". Check the name and try again.");
            System.out.println("Exiting passenger search...");
        }
        System.out.println("-------------- END OF SEARCH PASSENGER --------------");
    }

    public static void displayPassengersExpenses(Cabin[] cabinArray) {
        System.out.println("=====================================================");
        ArrayList<Passenger> bookedPassengers = getBookedPassengersArray(cabinArray);
        System.out.println("   Passenger          Expense");
        System.out.println("-------------------------------");
        double totalExpense = 0;
        for(int i = 0; i < bookedPassengers.size(); i++) {
            int emptySpaceCount = 18 - bookedPassengers.get(i).getFirstName().length();
            String emptySpace = " ";
            for (int j = 0; j < emptySpaceCount; j++) {
                emptySpace = emptySpace + " ";
            }
            System.out.println(i + ") " + bookedPassengers.get(i).getFirstName() + emptySpace + bookedPassengers.get(i).getExpenses());
            totalExpense += bookedPassengers.get(i).getExpenses();
        }
        System.out.println("-------------------------------");
        System.out.println("*  Total       =      " + totalExpense);
        System.out.println("-------------------------------\n");
        if(bookedPassengers.size() == 0) {
            System.out.println("Cruise ship is empty. Add passengers before accessing data about expenses.\nExiting expenses...");
        }
    }

    public static void storeShipData(Cabin[] cabinArray) {
        try {
            FileWriter myWriter = new FileWriter("shipData.txt");
            for (Cabin cabin : cabinArray) {
                myWriter.write("Cabin : " + cabin.cabinNo);
                myWriter.write(System.lineSeparator());
                for (int i = 0; i < cabin.passengerArray.length; i++) {
                    myWriter.write("{Passenger " + i + " : ");
                    myWriter.write("[FName : " + cabin.passengerArray[i].getFirstName());
                    myWriter.write(", Surname : " + cabin.passengerArray[i].getSurname());
                    myWriter.write(", Expenses : " + cabin.passengerArray[i].getExpenses() + "]}");
                    myWriter.write(System.lineSeparator());
                }
            }
            myWriter.close();
            System.out.println("Ship data saved successfully!");
        } catch (IOException ex ) {
            System.out.println("Error!!! IOException " + ex );
        }
    }

    public static void loadShipData(Cabin[] cabinArray) {
        Scanner scanner = new Scanner(System.in);
        String filePath = "shipData.txt";
        boolean errorExit = false;
        if(previewStoredData(filePath)) {
            while (true) {
                try {
                    System.out.println("1: Enter loaded data into the cruise ship and update the database");
                    System.out.println("2: Exit to main menu");
                    int userInput = scanner.nextInt();
                    if(userInput == 1) {
                        try {
                            File inputFile = new File(filePath);
                            Scanner readFile = new Scanner(inputFile);
                            String fileLine;
                            for(int i = 0; i < cabinArray.length; i++) {
                                fileLine = readFile.nextLine();
                                String cabinNoString;
                                int cabinNoInt;
                                if(i < 10) {
                                    cabinNoString = fileLine.substring(8, 9);
                                } else {
                                    cabinNoString = fileLine.substring(8, 10);
                                }
                                try {
                                    cabinNoInt = Integer.parseInt(cabinNoString);
                                } catch (NumberFormatException ex) {
                                    System.out.println("Error!!! Data file format is invalid. Check the file and try again.\nExiting load ship data...");
                                    errorExit = true;
                                    break;
                                }
                                for (int j = 0; j < cabinArray[cabinNoInt].passengerArray.length; j++) {
                                    fileLine = readFile.nextLine();
                                    int fNameIndex = fileLine.indexOf("FName") + 8;
                                    int fNameCommaIndex = fileLine.indexOf(",", fNameIndex);
                                    int surnameIndex = fileLine.indexOf("Surname") + 10;
                                    int surnameCommaIndex = fileLine.indexOf(",", surnameIndex);
                                    int expensesIndex = fileLine.indexOf("Expenses") + 11;
                                    int expensesBracketIndex = fileLine.indexOf("]", expensesIndex);
                                    String fName = fileLine.substring(fNameIndex, fNameCommaIndex);
                                    String surname = fileLine.substring(surnameIndex, surnameCommaIndex);
                                    String expensesString = fileLine.substring(expensesIndex, expensesBracketIndex);
                                    double expensesDouble;
                                    try {
                                        expensesDouble = Double.parseDouble(expensesString);
                                    } catch (NumberFormatException ex) {
                                        System.out.println("Error!!! Passenger " + fName + " : Invalid expense data type detected");
                                        System.out.println("Making passenger slot empty...");
                                        fName = "e";
                                        surname = "e";
                                        expensesDouble = 0.0;
                                    }
                                    cabinArray[cabinNoInt].passengerArray[j] = new Passenger(fName, surname, expensesDouble);
                                    System.out.println("DATA LOADED --- fName: " + fName + ", surname: " + surname + ", expenses: " + expensesDouble);
                                }
                            }
                        } catch (IOException ex) {
                            System.out.println("Error!!! IOException " + ex );
                            errorExit = true;
                            break;
                        }
                        break;
                    } else if (userInput == 2) {
                        System.out.println("Exiting load data...");
                        break;
                    } else {
                        System.out.println("Invalid input!!!");
                    }
                } catch (InputMismatchException ex) {
                    System.out.println("Invalid input!!! ");
                    System.out.println("Exiting load data...");
                    break;
                }
            }
            if (!errorExit) {
                System.out.println("Ship data loaded successfully!");
            }
        } else {
            System.out.println("Empty file detected. Entered file path : " + filePath);
            System.out.println("Exiting load data...");
        }
    }

    public static boolean previewStoredData(String filePath) {
        boolean canRead = false;
        try {
            File inputFile = new File(filePath);
            Scanner readFile = new Scanner(inputFile);
            System.out.println("Reading " + filePath);
            while (readFile.hasNext()) {
                canRead = true;
                System.out.println(readFile.nextLine());
            }
            System.out.println("----------------------------------------------");
        } catch (IOException ex) {
            System.out.println("Error!!! IOException " + ex );
        }
        return canRead;
    }

    public static void sortPassengerAlphabetically(Cabin[] cabinArray) {
        int passengersInRoom = 3;
        String[] passengerList = new String[cabinArray.length * passengersInRoom];
        int listPosition = 0;
        for(Cabin cabin : cabinArray) {
            for(Passenger passenger : cabin.getPassengerArray()) {
                if(!passenger.getFirstName().equals("e")) {
                    passengerList[listPosition] = passenger.getFirstName();
                    listPosition++;
                }
            }
        }

        int currentWordIndex = 1;
        int currentCharIndex = 0;
        while (true) {
            try {
                if(passengerList[currentWordIndex] != null) {
                    try {
                        char prevPassengerChar = Character.toLowerCase(passengerList[currentWordIndex-1].charAt(currentCharIndex));
                        char currPassengerChar = Character.toLowerCase(passengerList[currentWordIndex].charAt(currentCharIndex));
                        if(prevPassengerChar > currPassengerChar) {
                            String temp = passengerList[currentWordIndex - 1];
                            passengerList[currentWordIndex - 1] = passengerList[currentWordIndex];
                            passengerList[currentWordIndex] = temp;
                            if(currentWordIndex > 1) {
                                currentWordIndex--;
                            } else {
                                currentWordIndex++;
                            }
                            currentCharIndex = 0;
                        } else if (prevPassengerChar == currPassengerChar) {
                            currentCharIndex++;
                        } else {
                            currentWordIndex ++;
                            currentCharIndex = 0;
                        }
                    } catch (StringIndexOutOfBoundsException ex) {
                        currentWordIndex ++;
                        currentCharIndex = 0;
                    }
                } else {
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                break;
            }
        }

        System.out.println("=====================================================");
        for (int i = 0; i < passengerList.length; i++) {
            if (passengerList[i] != null) {
                System.out.println(i + ") " + passengerList[i]);
            }
        }
        System.out.println("-------------- END OF PASSENGER SORT ----------------");
        System.out.println();
    }

    private static boolean shutDown() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to shut down the system?\n(This step cannot be reversible. Save your data before shutting down the system)");
        System.out.println("1: Confirm and shut down\n2: Abort. Exit to main menu");
        try {
            int userInput = scanner.nextInt();
            if(userInput == 1) {
                System.out.println("Shutting down the system...");
                return false;
            } else if (!(userInput == 2)) {
                System.out.println("Invalid input!!! Try again.");
            }
        } catch (InputMismatchException ex) {
            System.out.println("Invalid data type input!!! Try again.");
        }
        return true;
    }
}