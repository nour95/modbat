package modbat.graphadaptor

import graph.Graph
import graph.Node
import modbat.dsl.State
import modbat.dsl.Transition
import modbat.mbt.{Configuration, ModelInstance}

class GraphAdaptor(val config: Configuration, val model: ModelInstance) {
//  private val log = model.mbt.log
  val graph: Graph[State, EdgeData] = new Graph(new Node(model.initialState), model.states.size)

  private def getTransitionLabel(transition: Transition): String = {
    if (!config.autoLabels && transition.action.label.isEmpty) {
      ""
    } else if (transition.action.transfunc != null) {
      transition.ppTrans(config.autoLabels) // String
    } else {
      ""
    }
  }

  /**
    * Add parentheses around label "(label)".
    */
  private def addParens(label: String): String = {
    if (label.equals("")) {
      ""
    } else {
      "(" + label + ")"
    }
  }

  private def createEdgeData(transition: Transition): EdgeData = {
    val transitionLabel: String = getTransitionLabel(transition)
    if (transition.expectedExceptions.isEmpty) {
      EdgeData(
        transitionLabel = transitionLabel,
        transitionType = NormalTransition(isDeterministic = true),
        transition = transition
      )
    } else {
      val transitionLabel = transition.expectedExceptions.mkString(", ")
      EdgeData(
        transitionLabel = transitionLabel,
        transitionType = ExpectedExceptionTransition(isDeterministic = true),
        transition = transition
      )
    }
  }

  def createGraph(): Unit = {
    for (transition <- model.transitions) {
      if (!transition.isSynthetic) {
        val transitionEdgeData = createEdgeData(transition)
        // add edge to graph
        graph.addEdge(transitionEdgeData.originState, transitionEdgeData.destinationState, transitionEdgeData)

        for (nextStatePredicate <- transition.nextStatePredicates) {
          // ask Cyrille about label to override: ( "original transition label" )
          val overridingTransitionEdgeData = EdgeData(
            transitionLabel = addParens(transitionEdgeData.transitionLabel),
            transitionType = NormalTransition(isDeterministic = !nextStatePredicate.nonDet),
            transition = nextStatePredicate.target // transition
          )

          // add edge to graph
          graph.addEdge(
            overridingTransitionEdgeData.originState,
            overridingTransitionEdgeData.destinationState,
            overridingTransitionEdgeData
          )
        }

        for (nonDeterministicException <- transition.nonDetExceptions) {
          val nonDeterministicExceptionTransition_EdgeData = EdgeData(
            transitionLabel = nonDeterministicException.exception.toString(),
            transitionType = ExpectedExceptionTransition(isDeterministic = false),
            transition = nonDeterministicException.target // transition
          )

          // add edge to graph
          graph.addEdge(
            nonDeterministicExceptionTransition_EdgeData.originState,
            nonDeterministicExceptionTransition_EdgeData.destinationState,
            nonDeterministicExceptionTransition_EdgeData
          )
        }
      }
    }
  }
}
