package modbat.graphadaptor

import graph.{Edge, Node}
import modbat.dsl.{State, Transition}

sealed trait TransitionType

case class NormalTransition(isDeterministic: Boolean) extends TransitionType

case class ExpectedExceptionTransition(isDeterministic: Boolean) extends TransitionType

case class EdgeData(transitionLabel: String, transitionType: TransitionType, transition: Transition) {
  val originState: Node[State] = new Node(transition.origin)
  val destinationState: Node[State] = new Node(transition.dest)
}

