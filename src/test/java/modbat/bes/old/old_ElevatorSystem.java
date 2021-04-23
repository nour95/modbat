package modbat.bes.old;

public class old_ElevatorSystem
{

    private boolean doorOpened = false;
    private int currentFloor = 0;
    private int openWarningCounter = 0;
    private int closeWarningCounter = 0;
    private boolean warningHappen = false;


    public old_ElevatorSystem() { }

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
            System.out.println("The door is OPENED. You pressed the same button 5 times");
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
            System.out.println("The door is CLOSED. You pressed the same button 5 times");
        }

        if (openWarningCounter < 5 && closeWarningCounter < 5)
            warningHappen = false;
    }

    public boolean isDoorOpened() {
        return doorOpened;
    }

    public void setDoorOpened(boolean doorOpened) {
        this.doorOpened = doorOpened;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void requestElevator(int requestingFloor)
    {
        if (requestingFloor != currentFloor)
            moveElevator(requestingFloor);

        //openDoor();
    }






    private void waitUntilReachingDistance()
    {
        for (int i = 0; i < 1000000; i++){}
    }





}
