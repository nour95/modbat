package modbat.examples.bes.undone

import java.util.Iterator

import modbat.dsl._
import modbat.examples.bes.CollectionModel

class IteratorModel(val dataModelI: CollectionModel,
                    val itI: Iterator[Integer]) extends Model {
  var posI = 0
  val versionI = dataModelI.version

  def validI = (versionI == dataModelI.version)

  def actualSizeI = dataModelI.collection.size

  def hasNextI: Unit = {
    if (validI) {
      assert ((posI < actualSizeI) == itI.hasNext)
    } else {
      itI.hasNext
    }
  }

  def nextI: Unit = {
    require (validI)
    require (posI < actualSizeI)
    itI.next
    posI += 1
  }

  def failingNextI: Unit = { // throws NoSuchElementException
    require (validI)
    require (posI >= actualSizeI)
    itI.next
  }

  def concNextI: Unit = { // throws ConcurrentModificationException
    require(!validI)
    itI.next
  }

  "main" -> "main" := hasNextI
  "main" -> "main" := nextI
  "main" -> "main" := failingNextI throws "NoSuchElementException"
  "main" -> "main" := concNextI throws "ConcurrentModificationException"
}
