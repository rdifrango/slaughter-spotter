package com.captechconsulting.mobile.slaughter

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.session.Database
import scala.slick.session.Database.threadLocalSession
import org.joda.time.Instant

object LocationsDAO {
  /**
   * Used to implicitly convert from Joda Time's Instant into a java.sql.Timestamp
   */
  implicit def date2dateTime = MappedTypeMapper.base[Instant, Timestamp](
    dateTime => new Timestamp(dateTime.getMillis),
    date => new Instant(date))
  /**
   * Maps from our Location object to the Locations table.
   */
  object Locations extends Table[Location]("LOCATIONS") {
    def id = column[Int]("LOCATION_ID", O.PrimaryKey, O.AutoInc)
    def latitude = column[String]("LATITUDE")
    def longitude = column[String]("LONGITUDE")
    def date = column[Instant]("DATE")
    def * = id.? ~ latitude ~ longitude ~ date <> (Location, Location.unapply _)
    def forInsert = latitude ~ longitude ~ date <> ({ t => Location(None, t._1, t._2, t._3) }, { (u: Location) => Some((u.latitude, u.longitude, u.date)) })
  }

  /**
   * Environment variables defined by AWS
   */
  val dbName = System.getProperty("RDS_DB_NAME")
  val userName = System.getProperty("RDS_USERNAME")
  val password = System.getProperty("RDS_PASSWORD")
  val hostname = System.getProperty("RDS_HOSTNAME")
  val port = System.getProperty("RDS_PORT")
  //  val dbName = "mobilebotcamp"
  //  val userName = "rdifrango"
  //  val password = "w052795w"
  //  val hostname = "mobilebootcamp.cp5k3lfdkxtn.us-east-1.rds.amazonaws.com"
  //  val port = "3306"
  /**
   * Build the URL based upon those variables.
   */
  val jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password
  /**
   * Set the driver.
   */
  val driver = "com.mysql.jdbc.Driver"

  /**
   * Creates a location
   */
  def locationCreate(loc: Location) =
    Database.forURL(jdbcUrl, driver = driver) withSession {
	  println("jdbcUrl = " + jdbcUrl)
      Locations.forInsert insert loc
    }

  /**
   * Counts the number of locations.
   */
  def locationCount: Int = {
    Database.forURL(jdbcUrl, driver = driver) withSession {
      println("jdbcUrl = " + jdbcUrl)
      val count = Query(Locations.length).first
      count
    }
  }

  /**
   * Gives a list of locations.
   */
  def locationList: List[Location] = {
    Database.forURL(jdbcUrl, driver = driver) withSession {
      println("jdbcUrl = " + jdbcUrl)
      val locations = Query(Locations).list
      locations
    }
  }
}