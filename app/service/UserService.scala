package service

import org.mindrot.jbcrypt.BCrypt

class UserService {

  def hashPassword(password: String): String = {
    BCrypt.hashpw(password, BCrypt.gensalt())
  }

  def checkPassword(candidate: String, hashPassword: String): Boolean = {
    BCrypt.checkpw(candidate, hashPassword)
  }

}
