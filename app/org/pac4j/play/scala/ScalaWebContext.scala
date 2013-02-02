/*
  Copyright 2012 - 2013 Jerome Leleu

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.play.scala


import play.api.mvc.Session
import org.pac4j.play._
import org.pac4j.play.java._
import org.pac4j.core.profile._
import org.pac4j.core.context._
import org.apache.commons.lang3.StringUtils
import play.cache.Cache

/**
 * This class is the Scala web context for Play. "Session objects" are stored into cache.
 * 
 * @author Jerome Leleu
 * @since 1.0.0
 */
 sealed class ScalaWebContext(s: Session) extends JavaWebContext {
  var session: Session = s

  override def setSessionAttribute(key: String, value: Object): Unit = {
    val sessionId = session.get(Constants.SESSION_ID)
    if (sessionId.isDefined) {
      StorageHelper.save(sessionId.get, key, value);
    }
  }
}
