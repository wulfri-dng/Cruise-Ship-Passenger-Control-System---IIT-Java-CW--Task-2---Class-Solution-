package com.wulfri;

import java.util.Locale;

public class Cabin {
    int cabinNo;
    int passengerCount = 3;
    boolean isBooked;
    Passenger[] passengerArray = new Passenger[passengerCount];

    public Cabin(int cabinNo) {
        this.cabinNo = cabinNo;
    }

    public int getCabinNo() {
        return cabinNo;
    }

    public Passenger[] getPassengerArray() {
        return passengerArray;
    }

    public void initialise() {
        for (int i = 0; i < passengerArray.length; i++) {
            passengerArray[i] = new Passenger("e", "e", 0);
        }
    }

    public boolean isFull() {
        for (Passenger passenger : passengerArray) {
            if (passenger.firstName.equals("e")) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        for (Passenger passenger : passengerArray) {
            if(!passenger.firstName.equals("e")) {
                return false;
            }
        }
        return true;
    }

    public void addPassenger(String firstName, String surname, double expenses) {
        if(!isFull()) {
            for (Passenger passenger : passengerArray) {
                if (passenger.firstName.equals("e")) {
                    passenger.setFirstName(firstName);
                    passenger.setSurname(surname);
                    passenger.setExpenses(expenses);
                    System.out.println("* Passenger " + firstName + " added to the cabin " + cabinNo);
                    break;
                }
            }
        }
    }

    public void addPassenger(Passenger newPassenger, boolean loadData) {
        if(!isFull()) {
            for (Passenger passenger : passengerArray) {
                if (passenger.firstName.equals("e")) {
                    passenger.setFirstName(newPassenger.getFirstName());
                    passenger.setSurname(newPassenger.getSurname());
                    passenger.setExpenses(newPassenger.getExpenses());
                    if(!loadData) {
                        System.out.println("Passenger " + passenger.getFirstName() + " added to the cabin " + cabinNo);
                    }
                    break;
                }
            }
        }
    }

    public void printPassengers() {
        if(!isEmpty()) {
            System.out.println("\n======= Cabin " + cabinNo + " =======\n");
            int slotCount = 1;
            for (Passenger passenger : passengerArray) {
                if (!passenger.getFirstName().equals("e")) {
                    System.out.println("Passenger " + slotCount);
                    System.out.println("------------");
                    System.out.println("* First Name : " + passenger.getFirstName());
                    System.out.println("* Surname    : " + passenger.surname);
                    System.out.println("* Expenses   : " + passenger.getExpenses() + "\n");
                    slotCount++;
                }
            }
        } else {
            System.out.println("Cabin " + cabinNo + " is empty.");
        }
    }

    public int emptyPassengerSlots() {
        int emptySlots = 0;
        for (Passenger passenger : passengerArray) {
            if(passenger.firstName.equals("e")) {
                emptySlots++;
            }
        }
        return emptySlots;
    }

    public boolean isPassengerAvailable(String passengerName) {
        for (Passenger passenger : passengerArray) {
            if (passenger.getFirstName().toLowerCase(Locale.ROOT).equals(passengerName.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    public void deletePassenger(Passenger searchedPassenger) {
        for (Passenger passenger : passengerArray) {
            if (passenger == searchedPassenger) {
                if(!passenger.getFirstName().equals("e")) {
                    System.out.println("* Passenger " + passenger.getFirstName() + " deleted successfully.");
                }
                passenger.setFirstName("e");
                passenger.setSurname("e");
                passenger.setExpenses(0);
            }
        }
    }

    public void deleteCabin() {
        for (Passenger passenger : passengerArray) {
            deletePassenger(passenger);
        }
    }

    public Passenger searchInCabin(String passengerName) {
        for (Passenger passenger : passengerArray) {
            if(passenger.getFirstName().toLowerCase(Locale.ROOT).equals(passengerName.toLowerCase(Locale.ROOT))) {
                return passenger;
            }
        }
        return null;
    }
}