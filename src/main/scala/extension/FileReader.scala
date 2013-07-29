package extension

import java.io.{InputStream, ByteArrayOutputStream}

trait FileReader {

  def getInputStream: InputStream

  def byteArray: Array[Byte] = {
    val is = getInputStream
    val buffer = new ByteArrayOutputStream
    val data = Array.ofDim[Byte](8192)
    try {
      Iterator.continually(is.read(data)).takeWhile(_ >= 0).foreach {
        buffer.write(data, 0, _)
      }
    } finally {
      is.close()
      buffer.flush()
    }
    buffer.toByteArray
  }
}

