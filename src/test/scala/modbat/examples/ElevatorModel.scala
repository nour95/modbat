package modbat.examples

import modbat.dsl._

class ElevatorModel extends Model {
  var system: ElevatorSystem = _;

  // transitions
  "InitializeSystem" -> "DoorClosed" := {
    system = new ElevatorSystem()
  }

  "DoorClosed" -> "DoorOpened" := {
    require(!system.isDoorOpened)
    system.openDoor();            //todo maybe in unworking version have a toggleDoor method and delete the the require command
    assert(system.isDoorOpened) //todo may remove this
  }

  "DoorOpened" -> "DoorClosed" := {
    require(system.isDoorOpened)
    system.closeDoor();
    assert(!system.isDoorOpened)  //todo and this
  }

  "DoorClosed" -> "MoveToFloorZero" := {
    require(system.getCurrentFloor() == 1)
    val success: Boolean = system.moveElevator(0)  //todo or convert this to toggleFloor and remove the require or change the return type of the moveElevator
    assert(success)
    assert(system.getCurrentFloor == 0)
    assert(!system.isDoorOpened) //
  }

  "DoorClosed" -> "MoveToFloorOne" := {
    require(system.getCurrentFloor() == 0)
    val success: Boolean = system.moveElevator(1)
    assert(success)
    assert(system.getCurrentFloor == 1)
    assert(!system.isDoorOpened) //
  }

  "MoveToFloorZero" -> "DoorOpened" := {
    require(!system.isDoorOpened)
    require(system.getCurrentFloor == 0)
    system.openDoor();
    assert(system.isDoorOpened)
  }

  "MoveToFloorOne" -> "DoorOpened" := {
    require(!system.isDoorOpened)
    require(system.getCurrentFloor == 1)
    system.openDoor();
    assert(system.isDoorOpened)
  }


  "DoorClosed" -> "End" := skip

}
