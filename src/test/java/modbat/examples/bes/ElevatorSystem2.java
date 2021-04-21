package modbat.examples.bes;

import java.util.HashMap;
import java.util.Map;

public class ElevatorSystem2
{
    public enum Status { Running, Stopping }


    private boolean doorOpened = false;
    private int currentFloor = 0;
    private Map<Status, Double> motorStatus = new HashMap<>(); //todo velocity
    private double currentSpeed = 0.0;

    public ElevatorSystem2()
    {
        motorStatus.put(Status.Running, 2.5);
        motorStatus.put(Status.Stopping, 0.0);
    }

    public void moveElevator(int requestingFloor) //todo george
    {
//        closeDoor(); //todo maybe in the require
//        currentSpeed = motorStatus.get(Status.Running);
//
//        waitUntilReachingDistance();
//        currentFloor  = requestingFloor;
//        currentSpeed = motorStatus.get(Status.Stopping);
    }



    public void requestElevator(int requestingFloor)
    {
        if (requestingFloor != currentFloor)
            moveElevator(requestingFloor);

        //openDoor();
    }

    public void openDoor()
    {
        //todo require motor.Stopping
        doorOpened = true;
    }

    public void closeDoor()
    {
        doorOpened = false;
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

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(double currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    private void waitUntilReachingDistance()
    {
        for (int i = 0; i < 1000000; i++){}
    }





}
