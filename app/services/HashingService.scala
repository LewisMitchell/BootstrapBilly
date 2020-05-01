package services

import java.security.MessageDigest

object HashingService {
  def getMD5(valueToHash: String): String = {
    MessageDigest.getInstance("MD5").digest(valueToHash.getBytes()).map(0xFF & _).map { "%02x".format(_) }.foldLeft(""){_ + _}
  }
}
