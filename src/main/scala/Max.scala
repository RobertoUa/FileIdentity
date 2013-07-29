class Max {
  // N * logN
  val biggestSort = (numbers: List[Int], m: Int) => numbers.sortWith(_ > _).take(m)
}
