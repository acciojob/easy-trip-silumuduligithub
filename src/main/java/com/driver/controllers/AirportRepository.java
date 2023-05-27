package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;


import java.util.*;

public class AirportRepository {
    static class Pair implements Comparable<Pair>{
        int terminal;
        String str;
        Pair(int terminal, String str){
            this.terminal = terminal;
            this.str = str;
        }
        public int compareTo(Pair o){
            if(this.terminal == o.terminal)return this.str.compareTo(o.str);
            return o.terminal - this.terminal;
        }
    }
    class Helper{
        String airportName;
        int people;
        Helper(String airportName, int people){
            this.airportName = airportName;
            this.people = people;
        }
    }
    HashMap<String, Airport> airportData = new HashMap<>();
    HashMap<Integer, Flight> flightData = new HashMap<>();
    HashMap<Integer, Passenger> passengerData = new HashMap<>();
    HashMap<Integer, Set<Integer>> ticketCounter = new HashMap<>();
    HashMap<Integer, Integer> travelByPassenger = new HashMap<>();
    HashMap<Integer, Integer> revenuOfFlight = new HashMap<>();
    HashMap<Date, List<Helper>> PassengersOFTheDay = new HashMap<>();
    HashMap<Integer, List<Integer>> ticketPrice = new HashMap<>();
    public void addAirport(Airport airport) {
        airportData.put(airport.getAirportName(), airport);
    }

    public void addFlight(Flight flight) {
        flightData.put(flight.getFlightId(), flight);
    }

    public void addPassengers(Passenger passenger) {
        passengerData.put(passenger.getPassengerId(), passenger);
    }

    private String lexographicallySmallest(){
        PriorityQueue<Pair> pq = new PriorityQueue<>();
      for(String key : airportData.keySet()){
          pq.add(new Pair(airportData.get(key).getNoOfTerminals(), airportData.get(key).getAirportName()));
      }
      Pair cur = pq.peek();
      return cur.str;
    }

    public String largestAirport() {
        return lexographicallySmallest();
    }
    public Optional<Double> getSortedDuration(City fromCity, City toCity) {
        double duration = Double.MAX_VALUE;
        for(Integer id : flightData.keySet()) {
            if(flightData.get(id).getFromCity().equals(fromCity) && flightData.get(id).getToCity().equals(toCity)){
                duration = Math.min(flightData.get(id).getDuration(), duration);
            }
        }
        if(duration == Double.MAX_VALUE)return Optional.of(-1.0);
        return Optional.of(duration);
    }

    public Optional<String> bookTicket(Integer flightId, Integer passengerId) {
        if(ticketCounter.get(flightId).size() >= flightData.get(flightId).getMaxCapacity()){
            return Optional.empty();
        }
        if(ticketCounter.containsKey(passengerId) && ticketCounter.get(passengerId).contains(passengerId)){
            return Optional.empty();
        }
        Set<Integer> passengers = ticketCounter.getOrDefault(flightId, new HashSet<>());
        passengers.add(passengerId);
        ticketCounter.put(flightId, passengers);
        Date date = flightData.get(flightId).getFlightDate();
        String fromCity = flightData.get(flightId).getFromCity().toString();
        String toCity = flightData.get(flightId).getToCity().toString();
        List<Helper> al = PassengersOFTheDay.getOrDefault(date, new ArrayList<>());
        Helper cur = al.get(flightId);
        if(cur == null){
            al.add(new Helper(fromCity, 1));
            al.add(new Helper(toCity, 1));
        }else{
            al.add(new Helper(fromCity, cur.people + 1));
            al.add(new Helper(toCity, cur.people + 1));
        }
        PassengersOFTheDay.put(date, al);
        travelByPassenger.put(passengerId, travelByPassenger.getOrDefault(passengerId, 0) + 1);
        return Optional.of("SUCCESS");
    }
    public Optional<String> cancelTicket(Integer flightId, Integer passengerId) {
        if(ticketCounter.containsKey(flightId) == false){
            return Optional.empty();
        }
        if(ticketCounter.get(flightId).contains(passengerId) == false){
            return Optional.empty();
        }
        Set<Integer> tickets = ticketCounter.get(flightId);
        tickets.remove(passengerId);
        ticketCounter.put(flightId, tickets);
        List<Integer> al = ticketPrice.get(flightId);
        int price = al.get(1);
        revenuOfFlight.put(flightId, revenuOfFlight.get(flightId) - price);
        travelByPassenger.put(passengerId, travelByPassenger.getOrDefault(passengerId, 0) - 1);
        return Optional.of("SUCCESS");
    }
    public int countBookingsOfAPassenger(Integer passengerId) {
        return travelByPassenger.get(passengerId);
    }
    public Optional<String> getAirportnameFromFlightId(Integer flightId) {
        if(flightData.containsKey(flightId) == false)return Optional.empty();
        return Optional.of(flightData.get(flightId).getFromCity().toString());
    }
    public int calculateFlightFare(Integer flightId) {
        int Price = 3000;
       Set<Integer>passengers = ticketCounter.getOrDefault(flightId, new HashSet<>());
       if(passengers.size() == 0)return Price;
       int flightFare = (passengers.size() * 50) + Price;
       List<Integer> al = new ArrayList<>();
       al.add(passengers.size() + 1);
       al.add(flightFare);
        ticketPrice.put(flightId, al);
       revenuOfFlight.put(flightId, revenuOfFlight.getOrDefault(flightId, 0) + flightFare);
       return flightFare;
    }
    public int revenuOfFlight(Integer flightId) {
        return revenuOfFlight.get(flightId);
    }
    public int getNumberOfPeopleOnDate(Date requireddate, String airportName) {
       for(Date date: PassengersOFTheDay.keySet()){
           if(date.equals(requireddate)){
               List<Helper> list =  PassengersOFTheDay.get(date);
               for(Helper helper : list){
                   if(helper.airportName.equals(airportName))return helper.people;
               }
           }
       }
       return 0;
    }
}
