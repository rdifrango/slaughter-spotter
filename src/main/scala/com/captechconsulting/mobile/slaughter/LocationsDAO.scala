package com.captechconsulting.mobile.slaughter

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import org.joda.time.Instant
import com.typesafe.config._
import scala.slick.lifted.TableQuery
import scala.slick.lifted.TableQuery
import scala.slick.jdbc.meta._
import com.mchange.v2.c3p0.ComboPooledDataSource

object LocationsDAO {
  /**
   * Used to implicitly convert from Joda Time's Instant into a java.sql.Timestamp
   */
  implicit def date2dateTime = MappedColumnType.base[Instant, Timestamp](
    dateTime => new Timestamp(dateTime.getMillis),
    date => new Instant(date))
  /**
   * Maps from our Location object to the Locations table.
   */
  class Locations(tag: Tag) extends Table[Location](tag, "LOCATIONS") {
    def id = column[Int]("LOCATION_ID", O.PrimaryKey, O.AutoInc)
    def latitude = column[String]("LATITUDE")
    def longitude = column[String]("LONGITUDE")
    def date = column[Instant]("DATE")
    def * = (id.?, latitude, longitude, date) <> (Location.tupled, Location.unapply)
  }

  /**
   * Access the environment variable
   */
  val config = ConfigFactory.load();
  /**
   * Retrieve the URL
   */
  val url = config.getString("jdbc.url")
  /**
   * Retrieve the driver.
   */
  val driver = config.getString("jdbc.driver")
  /*
   * Config a connection pool using C3P0
   */
  val datasource = {
    val ds = new ComboPooledDataSource
    ds.setDriverClass(driver)
    ds.setJdbcUrl(url)
    Database.forDataSource(ds)
  }
  /*
   * Optionally create the table
   */
  datasource withSession { implicit session =>
  if (MTable.getTables("LOCATIONS").list.isEmpty) {
	    println("Creating Locations Table")
    	TableQuery[Locations].ddl.create
    }
  }

  /**
   * Creates a location
   */
  def locationCreate(loc: Location) =
    datasource withSession { implicit session =>
      TableQuery[Locations].insert(loc)
    }

  /**
   * Counts the number of locations.
   */
  def locationCount: Int = {
    datasource withSession { implicit session =>
      TableQuery[Locations].length.run
    }
  }

  /**
   * Gives a list of locations.
   */
  def locationList: List[Location] = {
    datasource withSession { implicit session =>
      TableQuery[Locations].list
    }
  }
}