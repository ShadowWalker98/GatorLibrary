package Library.Book;

import Library.Reservation.Reservation;

import java.util.Objects;

public class ReservationHeap {
    private Reservation[] reservations;
    private Integer size;

    private Reservation nextReservation;

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
            System.out.println("Failed to add reservation! Reservation list size is: " + this.size);
            return false;
        } else {
            // we first insert the element into the next child to be inserted into
            // the binary tree. Then we bubble it up the tree using heapify()
            reservations[this.size++] = reservation;
            this.bubbleUp(this.size - 1);
            this.nextReservation = reservations[0];
            return true;
        }
    }

    public Reservation deleteReservation() {
        // the next reservation is returned
        // this is stored in the nextReservation variable. We then
        // perform standard deletion in a minHeap plus checking of the timestamps
        if(this.size <= 0) {
            return null;
        } else if(this.size == 1) {
            Reservation next = nextReservation;
            nextReservation = null;
            this.size--;
            return next;
        } else {
            // retrieve the last inserted node in the minHeap
            // set it as the root and trickle it down
            Reservation lastReservation = reservations[this.size - 1];
            this.size--;
            Reservation toBeReturned = nextReservation;
            reservations[0] = lastReservation;
            if(this.size > 1) {
                trickleDown(0);
            }
            nextReservation = reservations[0];
            return toBeReturned;

        }
    }

    private void bubbleUp(Integer idx) {
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

    private void trickleDown(Integer idx) {
        int parentIdx = idx;
        int leftChild = parentIdx * 2 + 1;
        int rightChild = parentIdx * 2 + 2;

        while(parentIdx < this.size) {

            if(validChild(leftChild) && !validChild(rightChild)) {
                int imp = compareReservations(parentIdx, leftChild);
                if(imp == leftChild) {
                    // swap parent and left child
                    swapReservations(parentIdx, leftChild);
                    parentIdx = leftChild;
                    leftChild = parentIdx * 2 + 1;
                    rightChild = parentIdx * 2 + 2;
                }
                break;
            } else if(validChild(rightChild) && !validChild(leftChild)) {
                int imp = compareReservations(parentIdx, rightChild);
                if(imp == rightChild) {
                    // swap parent and right child
                    swapReservations(parentIdx, rightChild);
                    parentIdx = rightChild;
                    leftChild = parentIdx * 2 + 1;
                    rightChild = parentIdx * 2 + 2;
                }
                break;
            } else {
                int moreImpChild = compareReservations(leftChild, rightChild);
                int shouldSwap = compareReservations(parentIdx, moreImpChild);
                if(shouldSwap == moreImpChild) {
                    swapReservations(parentIdx, moreImpChild);
                    parentIdx = moreImpChild;
                    leftChild = parentIdx * 2 + 1;
                    rightChild = parentIdx * 2 + 2;
                } else {
                    break;
                }
            }
        }
    }

    private int compareReservations(Integer idx1, Integer idx2) {
        if((reservations[idx1].getPriority() < reservations[idx2].getPriority())
                || (Objects.equals(reservations[idx1].getPriority(), reservations[idx2].getPriority())
                && reservations[idx1].getTimeStamp().isBefore(reservations[idx2].getTimeStamp()))) {
            return idx1;
        } else {
            return idx2;
        }
    }

    private boolean validChild(int idx) {
        return idx < this.size;
    }

    private void swapReservations(Integer idx1, Integer idx2) {
        Reservation temp = reservations[idx1];
        reservations[idx1] = reservations[idx2];
        reservations[idx2] = temp;
    }

    public Reservation[] getReservations() {
        return this.reservations;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public Reservation returnBook() {
        return deleteReservation();
    }
    public Reservation getNextReservation() {
        return nextReservation;
    }

    public void printReservations() {
        for(int i = 0; i < this.size; i++) {
            System.out.println(reservations[i].toString());
        }
    }







}
