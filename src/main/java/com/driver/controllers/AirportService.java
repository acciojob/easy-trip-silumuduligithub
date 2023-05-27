package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Date;
import java.util.Optional;

public class AirportService {
    AirportRepository airportRepository = new AirportRepository();

    public void addAirport(Airport airport) {
        airportRepository.addAirport(airport);
    }

    public void addFlight(Flight flight) {
        airportRepository.addFlight(flight);
    }

    public void addPassengers(Passenger passenger) {
        airportRepository.addPassengers(passenger);
    }

    public String largestAirport() {
        return airportRepository.largestAirport();
    }

    public double getSortedDuration(City fromCity, City toCity) {
        Optional<Double> durationopt = airportRepository.getSortedDuration(fromCity, toCity);
        return durationopt.get();
    }

    public String boolTicket(Integer flightId, Integer passengerId) throws RuntimeException{
       Optional<String> stringOpt =  airportRepository.bookTicket(flightId, passengerId);
       if(stringOpt.isEmpty()){
           throw new RuntimeException("flight may be full or passenger is already booked a ticket");
       }
       return "SUCCESS";
    }

    public String cancelTicket(Integer flightId, Integer passengerId) throws RuntimeException{
        Optional<String> stringOpt = airportRepository.cancelTicket(flightId, passengerId);
        if(stringOpt.isEmpty()){
            throw  new RuntimeException();
        }
        return "success";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        return airportRepository.countBookingsOfAPassenger(passengerId);
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        Optional<String> stringOpt = airportRepository.getAirportnameFromFlightId(flightId);
        if(stringOpt.isEmpty()){
            throw  new RuntimeException();
        }
        return stringOpt.get();
    }

    public int calculateFlightFare(Integer flightId) {
        return airportRepository.calculateFlightFare(flightId);
    }

    public int getNumberOfPeopleOnDate(Date date, String airportName) {
        return airportRepository.getNumberOfPeopleOnDate(date, airportName);
    }

    public int revenueOfAFlight(Integer flightId) {
        return airportRepository.revenuOfFlight(flightId);
    }
}
