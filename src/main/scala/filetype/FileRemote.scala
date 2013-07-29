package filetype

import java.net.URL
import extension.FileComparable

class FileRemote(val path: String) extends FileComparable {
  override def getInputStream = new URL(path).openStream
}
object FileRemote {
  def apply(path: String) = new FileRemote(path)
}
