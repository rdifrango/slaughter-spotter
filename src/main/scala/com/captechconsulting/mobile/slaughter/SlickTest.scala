package com.captechconsulting.mobile.slaughter

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession
import org.joda.time.Instant

object SlickTest extends App {
  implicit def date2dateTime = MappedTypeMapper.base[Instant, Timestamp](
    dateTime => new Timestamp(dateTime.getMillis),
    date => new Instant(date))

  object Locations extends Table[Location]("LOCATIONS") {
    def id = column[Int]("LOCATION_ID", O.PrimaryKey, O.AutoInc)
    def latitude = column[String]("LATITUDE")
    def longitude = column[String]("LONGITUDE")
    def date = column[Instant]("DATE")
    def * = id.? ~ latitude ~ longitude ~ date <> (Location, Location.unapply _)
    def forInsert = latitude ~ longitude ~ date <> ({ t => Location(None, t._1, t._2, t._3) }, { (u: Location) => Some((u.latitude, u.longitude, u.date)) })
  }

  val dbName = "mobilebotcamp"
  val userName = "rdifrango";
  val password = "w052795w";
  val hostname = "mobilebootcamp.cp5k3lfdkxtn.us-east-1.rds.amazonaws.com"
  val port = "3306";

  val jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
  
  Locations.ddl.createStatements.foreach(println)

//  val outerCount = Database.forURL(jdbcUrl, driver = "com.mysql.jdbc.Driver") withSession {
//    // The session is never named explicitly. It is bound to the current
//    // thread as the threadLocalSession that we imported
//
//    // Create the tables, including primary and foreign keys
//    val loc = Location(None, "01234", "56789", Instant.now())
//    val response = Locations.forInsert insert loc
//
//    val count = Query(Locations.length).first
//    count
//  }
  
//  val locations = Database.forURL(jdbcUrl, driver = "com.mysql.jdbc.Driver") withSession {
//    // The session is never named explicitly. It is bound to the current
//    // thread as the threadLocalSession that we imported
//
//    // Create the tables, including primary and foreign keys
//    val loc = Location(None, "01234", "56789", Instant.now())
//    val response = Locations.forInsert insert loc
//
//    val locations = Query(Locations).list
//    locations
//  }
//  
//  println(locations)
}