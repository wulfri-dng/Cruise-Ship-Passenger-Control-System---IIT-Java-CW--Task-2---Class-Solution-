package com.wulfri;

import java.util.ArrayList;

public class CircularQueue {
    // Declaring the class variables.
    private int size, front, rear;

    // Declaring array list of integer type.
    private ArrayList<Passenger> queue = new ArrayList<Passenger>();

    CircularQueue(int size) {
        this.size = size;
        this.front = this.rear = -1;
    }

    public ArrayList<Passenger> getQueue() {
        return queue;
    }

    // Method to insert a new element in the queue.
    public void enQueue(Passenger passenger) {

        // Condition if queue is full.
        if((front == 0 && rear == size - 1) || (rear == (front - 1) % (size - 1))) {
            System.out.print("Waiting list is Full. Sorry! We cannot add anymore passengers to the waiting list.");
        }

        // condition for empty queue.
        else if(front == -1) {
            front = 0;
            rear = 0;
            queue.add(rear, passenger);
        }

        else if(rear == size - 1 && front != 0) {
            rear = 0;
            queue.set(rear, passenger);
        }

        else {
            rear = (rear + 1);

            // Adding a new element if
            if(front <= rear) {
                queue.add(rear, passenger);
            }

            // Else updating old value
            else {
                queue.set(rear, passenger);
            }
        }
    }

    // Function to dequeue an element
// form th queue.
    public Passenger deQueue() {
        Passenger deQueuedPassenger;

        // Condition for empty queue.
        if(front == -1) {
//            System.out.print("Queue is Empty");
            return null;
        }

        deQueuedPassenger = queue.get(front);

        // Condition for only one element
        if(front == rear) {
            front = -1;
            rear = -1;
        }

        else if(front == size - 1) {
            front = 0;
        }
        else {
            front += 1;
        }

        // Returns the dequeued element
        return deQueuedPassenger;
    }

    public void displayQueue() {

        // Condition for empty queue.
        if(front == -1) {
            System.out.print("Queue is Empty");
            return;
        }

        // If rear has not crossed the max size
        // or queue rear is still greater then
        // front.
        System.out.print("Elements in the " +
                "circular queue are: ");

        if(rear >= front) {

            // Loop to print elements from
            // front to rear.
            for(int i = front; i <= rear; i++) {
                System.out.print(queue.get(i));
                System.out.print(" ");
            }
            System.out.println();
        }

        // If rear crossed the max index and
        // indexing has started in loop
        else {

            // Loop for printing elements from
            // front to max size or last index
            for(int i = front; i < size; i++) {
                System.out.print(queue.get(i));
                System.out.print(" ");
            }

            // Loop for printing elements from
            // 0th index till rear position
            for(int i = 0; i <= rear; i++) {
                System.out.print(queue.get(i));
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
