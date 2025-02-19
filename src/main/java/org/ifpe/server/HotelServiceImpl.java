package org.ifpe.server;

import org.ifpe.server.models.Booking;
import org.ifpe.server.models.Room;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

public class HotelServiceImpl extends UnicastRemoteObject implements HotelService {
    private static final long serialVersionUID = 1L;

    private final ConcurrentHashMap<Long, Booking> bookings = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Room> rooms = new ConcurrentHashMap<>();

    public HotelServiceImpl() throws RemoteException {
        super();
        for (long i = 1; i <= 10; i++) {
            rooms.put(i, new Room(i, "Standard", 100.0, true));
        }
        for (long i = 11; i <= 20; i++) {
            rooms.put(i, new Room(i, "Deluxe", 150.0, true));
        }
        for (long i = 21; i <= 30; i++) {
            rooms.put(i, new Room(i, "Suite", 200.0, true));
        }
        for (long i = 31; i <= 40; i++) {
            rooms.put(i, new Room(i, "Premium", 250.0, true));
        }
        for (long i = 41; i <= 50; i++) {
            rooms.put(i, new Room(i, "Luxury", 300.0, true));
        }
    }


    @Override
    public String sayHello() throws RemoteException {
        return "Olá, seja bem-vindo ao HotelService!";
    }

    @Override
    public String bookRoom(Long roomID, String guestName, LocalDate checkIn, LocalDate checkOut) throws RemoteException {
        if (rooms.containsKey(roomID) && rooms.get(roomID).getAvailable()) {
            Booking booking = new Booking(System.currentTimeMillis(), roomID, checkIn, checkOut, guestName);
            bookings.put(booking.getId(), booking);
            rooms.get(roomID).setAvailable(false);
            return "Quarto reservado com sucesso!";
        } else {
            return "Quarto não encontrado ou não disponível.";
        }
    }

    @Override
    public String cancelBooking(Long bookingID) throws RemoteException {
        Booking booking = bookings.remove(bookingID);
        if (booking != null) {
            rooms.get(booking.getRoomID()).setAvailable(true);
            return "Rserva cancelada com sucesso!";
        } else {
            return "Reserva não encontrada.";
        }
    }

    @Override
    public String getAvailableRooms(LocalDate checkIn, LocalDate checkOut) throws RemoteException {
        return rooms.values().stream()
                .filter(Room::getAvailable)
                .toList()
                .toString();
    }

    @Override
    public String getBookings() throws RemoteException {
        return bookings.values().toString();
    }

    @Override
    public String getBooking(Long bookingID) throws RemoteException {
        Booking booking = bookings.get(bookingID);
        return booking != null ? booking.toString() : "Reserva não encontrada.";
    }

    @Override
    public String getRoom(Long roomID) throws RemoteException {
        Room room = rooms.get(roomID);
        return room != null ? room.toString() : "Quarto não encontrado.";
    }

    @Override
    public String searchAvailableRoomsByType(String type) throws RemoteException {
        return rooms.values().stream()
                .filter(Room::getAvailable)
                .filter(r -> r.getType().equalsIgnoreCase(type))
                .toList()
                .toString();
    }

    @Override
    public String getRooms() throws RemoteException {
        return rooms.values().toString();
    }

    @Override
    public String updateRoom(Long roomID, String type, Double price, Boolean available) throws RemoteException {
        Room room = rooms.get(roomID);
        if (room != null) {
            room.setType(type);
            room.setPrice(price);
            room.setAvailable(available);
            return "Quarto atualizado com sucesso!";
        } else {
            return "Quarto não encontrado.";
        }
    }
}