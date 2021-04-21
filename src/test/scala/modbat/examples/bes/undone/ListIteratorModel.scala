package modbat.examples.bes.undone

import java.util.ListIterator

import modbat.dsl._
import modbat.examples.bes.CollectionModel

class ListIteratorModel(val dataModelI: CollectionModel,
                        val itI: ListIterator[Integer]) extends Model {
  var posI = 0

  // needs to be mutable as we also model modifications through the iterator
  var versionI = dataModelI.version
  var lastCalledNextI = false

  def sizeI = dataModelI.n

  def validI = (versionI == dataModelI.version)

  def hasNextI: Unit = {
    if (validI) {
      assert ((posI < sizeI) == itI.hasNext)
    } else {
      itI.hasNext
    }
  }

  def nextI: Unit = {
    require (validI)
    require (posI < sizeI)
    itI.next
    posI += 1
    lastCalledNextI = true
  }

  def previousI: Unit = { // TODO: Fill in "previous"
    require (validI)
    // TODO: add correct precondition
    itI.previous // call function on system under test
    // TODO: update model variable(s)
    lastCalledNextI = false
  }

  // update version count in iterator and collection
  def markAsModifiedI: Unit = {
    versionI += 1
    dataModelI.version += 1
  }

  def addI: Unit = {
    require (validI)
    val element = Integer.valueOf(choose(0, dataModelI.N))
    itI.add(element)
    posI += 1
    dataModelI.n += 1
    markAsModifiedI
  }

  def removeI: Unit = {
    require (validI)
    itI.remove
    dataModelI.n -= 1
    if (lastCalledNextI)
      posI -= 1
    lastCalledNextI = false
    markAsModifiedI
  }

  def setI: Unit = {
    require (validI)
    val element = Integer.valueOf(choose(0, dataModelI.N))
    itI.set(element)
  }

  // this operation is possible in both states and does not change the state
  @States(Array("main", "modifiable")) // specify all states
  @Throws(Array("NoSuchElementException")) // specify the exception(s)
  def failingNextI: Unit = { // throws NoSuchElementException
    require (validI)
    require (posI >= sizeI)
    itI.next
  }

  // this operation is possible in both states and does not change the state
  @States(Array("main", "modifiable")) // specify all states
  @Throws(Array("ConcurrentModificationException")) // specify the exception(s)
  def concNextI: Unit = { // throws ConcurrentModificationException
    require(!validI)
    choose(
      { () => itI.next() } // TODO: Add a variant testing "it.previous()"
      // note: to pass multiple options to "choose", use a comma to separate
      // all options
    )
  }

  @States(Array("main", "modifiable")) // specify all states
  def checkIdxI: Unit = { // TODO: specify an assertion that checks if "pos"
    // from the model is equivalent to "nextIndex" from the iterator
  }

  "main" -> "main" := hasNextI
  // successfully moving the iterator allows modifying the current element
  "main" -> "modifiable" := nextI
  "main" -> "modifiable" := previousI

  // moving the iterator in state "modifiable" keeps it there
  "modifiable" -> "modifiable" := nextI
  "modifiable" -> "modifiable" := previousI

  // adding a new element at the iterator prevents modifications at that point
  "main" -> "main" := addI
  "modifiable" -> "main" := addI

   // we can only modify the current element when at a valid position
  "main" -> "main" := setI throws "IllegalStateException"
  // we can modify an element at a valid position several times
  "modifiable" -> "modifiable" := setI

  // we can only remove the current element when at a valid position
  "main" -> "main" := removeI throws "IllegalStateException"
  // removing an element prevents modifications at that point
  "modifiable" -> "main" := removeI
}
