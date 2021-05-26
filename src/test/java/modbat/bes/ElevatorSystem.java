package modbat.bes;

public class ElevatorSystem
{

    private boolean doorOpened = false;
    private int currentFloor = 0;
    private int openWarningCounter = 0;
    private int closeWarningCounter = 0;
    private boolean warningHappen = false;


    public ElevatorSystem() { }

    public boolean moveElevator(int requestingFloor)       //todo have a similar method that call this method and that have a while(doorOpened){wait}??
    {
        warningHappen = false; //todo may delete this

        if (doorOpened == false) {             //todo this is always false
            currentFloor = requestingFloor;
            return true;
        }
        else
            return false;
    }

    public void openDoor()
    {
        closeWarningCounter = 0;
        doorOpened = true;
        openWarningCounter++;

        if (openWarningCounter >= 5){
            warningHappen = true;
//            System.out.println("The door is OPENED. You pressed the same button 5 times");
        }

        if (openWarningCounter < 5 && closeWarningCounter < 5)
            warningHappen = false;
    }

    public void closeDoor()
    {
        openWarningCounter = 0;
        doorOpened = false;
        closeWarningCounter++;

        if (closeWarningCounter >= 5){
            warningHappen = true;
//            System.out.println("The door is CLOSED. You pressed the same button 5 times");
        }

        if (openWarningCounter < 5 && closeWarningCounter < 5)
            warningHappen = false;
    }

    public boolean isDoorOpened() {
        return doorOpened;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public boolean isWarningHappen() {
        return warningHappen;
    }

    public int getOpenWarningCounter() {
        return openWarningCounter;
    }

    public int getCloseWarningCounter() {
        return closeWarningCounter;
    }

//    public void requestElevator(int requestingFloor)
//    {
//        if (requestingFloor != currentFloor)
//            moveElevator(requestingFloor);
//
//        //openDoor();
//    }


    public static void main(String[] args) {
        //
    }










}
