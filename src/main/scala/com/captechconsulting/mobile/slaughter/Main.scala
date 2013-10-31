package com.captechconsulting.mobile.slaughter

import org.json4s.JsonAST.JObject
import org.json4s.DefaultFormats
import org.json4s.Formats

import akka.actor.Actor
import spray.http.MediaTypes._
import spray.httpx.Json4sSupport
import spray.routing._

case class LocationCount(count: Integer)
case class Location(lat: String, long: String, date: String = "date")

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {
  implicit def json4sFormats: Formats = DefaultFormats

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService with Json4sSupport {
  val locations = scala.collection.concurrent.TrieMap[String, Location]()

  val myRoute =
    pathPrefix("api") {
      path("spotter") {
        get {
          parameters('count.as[Boolean]?) { count =>
            complete {
              // IF the count is passed in as a parameter
              // and it is set to TRUE return a count, 
              // otherwise return the list.
              if (count.isDefined && count.get) {
                LocationCount(locations.size)
              } else {
                locations.values
              }
            }
          }
        } ~
          put {
            entity(as[JObject]) { locationObj =>
              complete {
                // Adds a new element to the list.
                val location = locationObj.extract[Location]
                locations.put(java.util.UUID.randomUUID().toString(), location)
                location
              }
            }
          }
      }
    }
}
