package org.ifpe.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

public interface HotelService extends Remote {
    String sayHello() throws RemoteException;
    String bookRoom(Long roomID, String guestName, LocalDate checkIn, LocalDate checkOut) throws RemoteException;
    String cancelBooking(Long bookingID) throws RemoteException;
    String getAvailableRooms(LocalDate checkIn, LocalDate checkOut) throws RemoteException;
    String getBookings() throws RemoteException;
    String getBooking(Long bookingID) throws RemoteException;
    String getBookingsByRoom(Long roomID) throws RemoteException;
    String getRoom(Long roomID) throws RemoteException;
    String searchAvailableRoomsByType(String type) throws RemoteException;
    String getRooms() throws RemoteException;
    String updateRoom(Long roomID, String type, Double price, Boolean available) throws RemoteException;
}
