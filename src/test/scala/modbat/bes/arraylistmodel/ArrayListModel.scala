package models.arraylistmodel

import modbat.dsl._

class ArrayListModel extends ListModel {
  override val testData = new java.util.ArrayList[Integer]()  
  //override val testData = new experiment.util.ArrayList // for the faulty version (TU library)
}
