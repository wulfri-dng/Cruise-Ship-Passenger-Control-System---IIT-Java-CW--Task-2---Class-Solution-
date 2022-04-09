package com.wulfri;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        boolean systemOn = true;
        int cabinCount = 12;
        Cabin[] cabinArray = new Cabin[cabinCount];
        CircularQueue queue = new CircularQueue(12);
        initialise(cabinArray);
        mainMenu(cabinArray, systemOn, queue);
    }

    private static void initialise(Cabin[] cabinArray) {
        for (int i = 0; i < cabinArray.length; i++) {
            cabinArray[i] = new Cabin(i);
            cabinArray[i].initialise();
        }
    }

    private static void mainMenu(Cabin[] cabinArray, boolean systemOn, CircularQueue queue) {
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
                case "A" -> addPassenger(cabinArray, queue);
                case "V" -> viewAllCabins(cabinArray);
                case "E" -> displayEmptyCabins(cabinArray);
                case "D" -> deletePassenger(cabinArray, queue);
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

    private static boolean isShipFull(Cabin[] cabinArray) {
        for(Cabin cabin : cabinArray) {
            if (cabin.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static Passenger[] getBookedPassengersArray(Cabin[] cabinArray) {
        Passenger[] passengerArray = new Passenger[36];
        int index = 0;
        for(Cabin cabin : cabinArray) {
            for(Passenger passenger : cabin.getPassengerArray()) {
                if(!passenger.getFirstName().equals("e")) {
                    passengerArray[index] = passenger;
                    index++;
                }
            }
        }
        return passengerArray;
    }

    private static void addingSinglePassenger(Cabin[] cabinArray, CircularQueue queue, int cabinNo) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter passenger's  first name : ");
            String passengerFName = scanner.next();
            if(passengerFName.equals("e")){
                System.out.println("You cannot enter 'e' as the passenger's first name. Enter a valid name again.");
                continue;
            }
            try {
                Integer.parseInt(passengerFName);
                System.out.println("Error!!! You cannot enter a number as a passenger name.");
                continue;
            } catch (NumberFormatException ex) {
                System.out.println("Enter passenger's surname : ");
            }
            String passengerSurname = scanner.next();
            try {
                Integer.parseInt(passengerSurname);
                System.out.println("Error!!! You cannot enter a number as a passenger name.");
                continue;
            } catch (NumberFormatException ex) {
                System.out.println("Enter passenger's expenses : ");
            }
            try {
                double passengerExpenses = scanner.nextDouble();
                if(cabinNo != -1) {
                    cabinArray[cabinNo].addPassenger(new Passenger(passengerFName, passengerSurname, passengerExpenses));
                } else {
                    queue.enQueue(new Passenger(passengerFName, passengerSurname, passengerExpenses));
                    System.out.println("Passenger " + passengerFName + " added to the waiting list.");
                }
                break;
            } catch (InputMismatchException ex) {
                System.out.println("Error!!! You cannot add a String as an expense.");
                scanner.next();
            }
        }
    }

    private static void addPassengersOfCabin(Cabin[] cabinArray, int cabinNo, CircularQueue queue) {
        Scanner scanner = new Scanner(System.in);
        int passengerIndex = 1;
        System.out.println("You can add upto 3 passengers into the cabin " + cabinNo);
        boolean addAnotherPassenger = true;
        while (passengerIndex < 4) {
            addingSinglePassenger(cabinArray, queue, cabinNo);
            passengerIndex++;
            if(passengerIndex != 4) {
                while (true) {
                    System.out.println("Do you want to add another passenger into the cabin " + cabinNo + "?");
                    System.out.println("Y: Yes. Add passenger " + passengerIndex + "\nN: No. Back to the previous menu.");
                    try {
                        String userInput = scanner.nextLine();
                        if(userInput.toLowerCase(Locale.ROOT).equals("y")) {
                            break;
                        } else if (userInput.toLowerCase(Locale.ROOT).equals("n")) {
                            addAnotherPassenger = false;
                            break;
                        } else {
                            System.out.println("Invalid input!!! Try again.");
                        }
                    } catch (InputMismatchException ex) {
                        System.out.println("Invalid input!!! Try again.");
                    }
                }
            }
            if (!addAnotherPassenger) {
                System.out.println("Moving to previous menu...");
                return;
            }
        }
        if (passengerIndex == 4) {
            System.out.println("Cabin " + cabinNo + " is full. \nMoving to previous menu...");
        }
    }

    private static void addPassenger(Cabin[] cabinArray, CircularQueue queue) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
                if (!isShipFull(cabinArray)) {
                    int cabinNo;
                    try {
                        System.out.println("Enter cabin number (0 - " + (cabinArray.length - 1) + ") or " + (cabinArray.length) + " to stop: ");
                        cabinNo = scanner.nextInt();
                    } catch (InputMismatchException ex) {
                        System.out.println("Invalid input!!!");
                        scanner.next();
                        continue;
                    }
                    if (cabinNo == cabinArray.length) {
                        break;
                    }
                    try {
                        if (cabinNo < 0 || cabinNo > cabinArray.length) {
                            System.out.println("Invalid input!!! Enter a number between 0 - " + (cabinArray.length - 1));
                        } else if (!cabinArray[cabinNo].isEmpty()) {
                            System.out.println("Cabin " + cabinNo + " is already booked. You can add the passenger into a another cabin.");
                        } else {
                            addPassengersOfCabin(cabinArray, cabinNo, queue);
                        }
                    } catch (Exception ex) {
                        System.out.println("Invalid input!!! Enter a number between 0 - " + (cabinArray.length - 1) + " or " + (cabinArray.length) + " to stop: ");
                        System.out.println("Exiting add passenger...");
                        break;
                    }
                } else {
                    while (true) {
                        try {
                            System.out.println("Cruise ship is full. Do you want to add the passenger into the waiting list?");
                            System.out.println("1: Add the new passenger into waiting list\n2: Return to main menu");
                            String userInput = scanner.next();
                            if (userInput.equals("1")) {
                                addingSinglePassenger(cabinArray, queue, -1);
                            } else if (userInput.equals("2")) {
                                break;
                            } else {
                                System.out.println("Invalid input!!! Try again.");
                            }
                            scanner.nextLine();
                        } catch (InputMismatchException ex) {
                            System.out.println("Invalid input!!! Try again.");
                        }
                    }
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
        Cabin[] emptyCabins = new Cabin[cabinArray.length];
        int index = 0;
        for (Cabin cabin : cabinArray) {
            if(!cabin.isFull()) {
                emptyCabins[index] = cabin;
                index++;
            }
        }
        if (!(emptyCabins.length == 0)) {
            System.out.println("====================================");
            System.out.println(emptyCabins.length + " cabins detected with empty slots.");
            System.out.println("------------------------------------");
            for (Cabin cabin : emptyCabins) {
                if(cabin.isEmpty()) {
                    System.out.println("Cabin " + cabin.getCabinNo() + " : EMPTY CABIN");
                } else {
                    System.out.println("Cabin " + cabin.getCabinNo() + " : " + cabin.emptyPassengerSlots() + " passenger slot available");
                }
            }
            System.out.println("\n------------ END OF DISPLAY EMPTY CABINS ------------");
        } else {
            System.out.println("Every cabin is full.");
        }
    }

    public static void deletePassenger(Cabin[] cabinArray, CircularQueue queue) {
        Scanner scanner = new Scanner(System.in);
        boolean passengerDeleted = false;
        boolean isDequeue = isShipFull(cabinArray);
        System.out.println("=====================================================");
        System.out.println("Enter passenger's first name to remove the passenger");
        System.out.print("Enter: ");
        String userInput = scanner.next();
        for(Cabin cabin : cabinArray) {
            Passenger searchedPassenger = cabin.searchInCabin(userInput);
            if(searchedPassenger != null) {
                while (true) {
                    boolean canDeQueue = false;
                    System.out.println("Searched passenger " + searchedPassenger.getFirstName() + " found in cabin " + cabin.getCabinNo());
                    System.out.println("1: Only delete the selected passenger (" + searchedPassenger.getFirstName() + ")\n2: Delete the whole cabin (Cabin - " + cabin.getCabinNo() + ")");
                    scanner.nextLine();
                    String innerUserInput = scanner.nextLine();
                    if(innerUserInput.equals("1")) {
                        cabin.deletePassenger(searchedPassenger);
                        passengerDeleted = true;
                        if(cabin.isEmpty()) {
                            canDeQueue = true;
                        } else {
                            break;
                        }
                    } else if(innerUserInput.equals("2")) {
                        cabin.deleteCabin();
                        canDeQueue = true;
                        passengerDeleted = true;
                        System.out.println("* Cabin " + cabin.getCabinNo() + " deleted successfully.");
                    } else {
                        System.out.println("Invalid input!!! Try again.");
                    }
                    if(canDeQueue) {
                        Passenger deQueuedPassenger = queue.deQueue(); //DeQueue passengers from waiting list into the ship
                        if(isDequeue && deQueuedPassenger != null) {
                            System.out.println("* Empty cabin detected. Adding a passenger from the waiting list...");
                            cabin.addPassenger(deQueuedPassenger.getFirstName(), deQueuedPassenger.getSurname(), deQueuedPassenger.getExpenses());
                        }
                        break;
                    }
                }
            }
        }
        if (!passengerDeleted) {
            System.out.println("Invalid input!!! Cannot find passenger : " + userInput + ". Check the name and try again.");
            System.out.println("Exiting delete passenger...");
        }
        System.out.println("\n---------------- END OF DELETE PASSENGER ----------------\n");
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
        Passenger[] bookedPassengers = getBookedPassengersArray(cabinArray);
        System.out.println("   Passenger          Expense");
        System.out.println("-------------------------------");
        double totalExpense = 0;
        for(int i = 0; i < bookedPassengers.length; i++) {
            if(bookedPassengers[i] != null) {
                int emptySpaceCount = 18 - bookedPassengers[i].getFirstName().length();
                String emptySpace = " ";
                for (int j = 0; j < emptySpaceCount; j++) {
                    emptySpace = emptySpace + " ";
                }
                System.out.println(i + ") " + bookedPassengers[i].getFirstName() + emptySpace + bookedPassengers[i].getExpenses());
                totalExpense += bookedPassengers[i].getExpenses();
            }
        }
        System.out.println("-------------------------------");
        System.out.println("*  Total       =      " + totalExpense);
        System.out.println("-------------------------------\n");
        if(bookedPassengers.length == 0) {
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