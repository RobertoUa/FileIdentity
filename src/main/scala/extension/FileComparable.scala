package extension

import filetype.File

trait FileComparable extends File with FileReader {

  //  scala.sys.process.Process(Seq("md5sum", path).toSeq).!!.split("""\s+""").head

  lazy val hash = computeHashSum(byteArray)

  def compareTo(that: FileComparable): Boolean = {
    this.hash == that.hash
  }

  protected def computeHashSum(array: Array[Byte]): String = {
    val hashDigest = java.security.MessageDigest.getInstance("MD5")
    hashDigest.digest(array).map("%02x".format(_)).mkString
  }
}
