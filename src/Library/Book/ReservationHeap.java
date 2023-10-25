package Library.Book;

import Library.Reservation.Reservation;

import java.util.Objects;

public class ReservationHeap {
    Reservation[] reservations;
    Integer size;

    Reservation nextReservation;

    public ReservationHeap() {
        reservations = new Reservation[20];
        this.size = 0;
        nextReservation = null;
    }

    public boolean addReservation(Integer patronID, Integer priority) {
        Reservation reservation = new Reservation(patronID, priority);

        // if the size of the heap is zero,
        // we can directly add it as the root
        // and set the nextReservation field to be set to
        // the element inserted.
        if(this.size == 0) {
            reservations[0] = reservation;
            this.nextReservation = reservation;
            this.size++;
            return true;
        } else if(this.size >= 20) {
            // if the size of the heap is more than 1, we first check
            // if we are violating the size constraint, in which case we
            // throw an exception
            throw new RuntimeException("Size of reservation heap is going to exceed 20. Exiting...");
        } else {
            // we first insert the element into the next child to be inserted into
            // the binary tree. Then we bubble it up the tree using heapify()
            reservations[this.size++] = reservation;
            this.heapify(this.size - 1);
            return true;
        }
    }

    public void heapify(Integer idx) {
        // left child = idx * 2 + 1
        // right child = idx * 2 + 2
        // parent = floor((idx - 1)/2)
        if(idx == 0) {
            return;
        }

        int currParent = Math.floorDiv(idx - 1, 2);
        while(currParent >= 0) {
            if(reservations[currParent].getPriority() > reservations[idx].getPriority() ||
                    Objects.equals(reservations[currParent].getPriority(), reservations[idx].getPriority())
                            && reservations[currParent].getTimeStamp().isAfter(reservations[idx].getTimeStamp())) {
                Reservation temp = reservations[currParent];
                reservations[currParent] = reservations[idx];
                reservations[idx] = temp;
                idx = currParent;
                currParent = Math.floorDiv(idx - 1, 2);
            } else {
                break;
            }
        }
    }







}
