package com.captechconsulting.mobile.slaughter

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import org.joda.time.Instant
import com.typesafe.config._
import scala.slick.lifted.TableQuery
import scala.slick.lifted.TableQuery
import scala.slick.jdbc.meta._
import com.mchange.v2.c3p0.ComboPooledDataSource

import org.slf4j.LoggerFactory
import org.slf4j.Logger

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
  
  val logger = LoggerFactory.getLogger(getClass())

  /**
   * Access the environment variable
   */
  logger.info("Loading Config")
  val config = ConfigFactory.load
  logger.info("Config Loaded")
  
  /*
   * Config a connection pool using C3P0
   */
  val datasource = {
    logger.info("Connecting to the Database")
    val ds = new ComboPooledDataSource
    ds.setDriverClass(config.getString("jdbc.driver"))
    logger.info("Driver Class: {}", ds.getDriverClass())
    ds.setJdbcUrl(config.getString("jdbc.url"))
    logger.info("JDBC URL: {}",  ds.getJdbcUrl())
    ds.setUser(config.getString("jdbc.userName"))
    logger.info("JDBC User: {}", ds.getUser())
    ds.setPassword(config.getString("jdbc.password"))
    Database.forDataSource(ds)
  }
  /*
   * Optionally create the table
   */
  datasource withSession { implicit session =>
  if (MTable.getTables("LOCATIONS").list.isEmpty) {
	    logger.info("Creating Locations Table")
    	TableQuery[Locations].ddl.create
    }
  }

  /**
   * Creates a location
   */
  def locationCreate(loc: Location) =
    datasource withSession { implicit session =>
      logger.info("Creating Location: {}", loc)
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