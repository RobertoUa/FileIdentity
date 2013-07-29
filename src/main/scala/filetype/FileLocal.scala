package filetype

import java.io.FileInputStream
import extension.FileComparable

class FileLocal(val path: String) extends FileComparable {
  override def getInputStream = new FileInputStream(path)
}

object FileLocal {
  def apply(path: String) = new FileLocal(path)
}