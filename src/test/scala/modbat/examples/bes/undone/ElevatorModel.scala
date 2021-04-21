package modbat.examples.bes.undone

import modbat.dsl._
import modbat.examples.bes.ElevatorSystem

class ElevatorModel extends Model {
  var system: ElevatorSystem = _;

  // transitions
  "InitializeSystem" -> "DoorClosed" := {
    system = new ElevatorSystem()
  }

  "DoorClosed" -> "DoorOpened" := {
    require(!system.isDoorOpened)
    system.openDoor();
    assert(system.isDoorOpened) //todo may remove this
  } label "open"

  "DoorClosed" -> "DoorClosed" := {
    require(!system.isDoorOpened)
    system.closeDoor();
    assert(!system.isDoorOpened)     // todo assert no warning??
  } label "close"

  "DoorOpened" -> "DoorOpened" := {
    require(system.isDoorOpened)
    system.openDoor();
    assert(system.isDoorOpened)     // todo assert no warning??
  } label "open"

  "DoorOpened" -> "DoorClosed" := {
    require(system.isDoorOpened)
    system.closeDoor();
    assert(!system.isDoorOpened)  //todo and this
  } label "close"

  "DoorClosed" -> "MoveToFloorZero" := {
    require(system.getCurrentFloor == 1)   // todo assert or require no warning
    val success: Boolean = system.moveElevator(0)  //todo or convert this to toggleFloor and remove the require or change the return type of the moveElevator
    assert(success)
    assert(system.getCurrentFloor == 0)
    assert(!system.isDoorOpened) //
  } label "moveToZero"

  "DoorClosed" -> "MoveToFloorOne" := {
    require(system.getCurrentFloor == 0)
    val success: Boolean = system.moveElevator(1)
    assert(success)
    assert(system.getCurrentFloor == 1)
    assert(!system.isDoorOpened) //
  } label "moveToOne"

  "MoveToFloorZero" -> "DoorOpened" := {
    require(!system.isDoorOpened)
    require(system.getCurrentFloor == 0)
    system.openDoor();
    assert(system.isDoorOpened)
  } label "open"

  "MoveToFloorOne" -> "DoorOpened" := {
    require(!system.isDoorOpened)
    require(system.getCurrentFloor == 1)
    system.openDoor();
    assert(system.isDoorOpened)
  } label "open"

  "DoorClosed" -> "WarningCheck" := {
    assert(! system.isWarningHappen)
  } label "checkWarning"

 // "DoorClosed" -> "End" := skip   //todo add warning check here or

  "WarningCheck" -> "End" := skip


}
