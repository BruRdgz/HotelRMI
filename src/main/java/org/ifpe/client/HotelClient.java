package org.ifpe.client;

import org.ifpe.server.HotelService;
import java.rmi.Naming;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HotelClient {
    private final HotelService hotelService;
    private final Scanner scanner;
    private final Map<String, Command> commands;
    private boolean running;

    public HotelClient(HotelService hotelService) {
        this.hotelService = hotelService;
        this.scanner = new Scanner(System.in);
        this.commands = new HashMap<>();
        initializeCommands();
    }

    private void initializeCommands() {
        commands.put("ajuda", this::showHelp);
        commands.put("ola", () -> println(hotelService.sayHello()));
        commands.put("reservar", this::bookRoom);
        commands.put("cancelar", this::cancelBooking);
        commands.put("disponiveis", this::getAvailableRooms);
        commands.put("disponiveis_tipo", this::searchRoomsByType);
        commands.put("reservas", () -> println(hotelService.getBookings()));
        commands.put("reserva", this::getBooking);
        commands.put("quarto", this::getRoom);
        commands.put("quartos", () -> println(hotelService.getRooms()));
        commands.put("atualizar", this::updateRoom);
        commands.put("sair", () -> running = false);
    }

    public void start() {
        running = true;
        println("Bem-vindo ao Sistema do Hotel. Digite 'ajuda' para ver a lista de comandos.");

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();
            Command command = commands.get(input);

            if (command != null) {
                try {
                    command.execute();
                } catch (Exception e) {
                    println("Erro: " + e.getMessage());
                }
            } else {
                println("Comando desconhecido. Digite 'ajuda' para ver a lista de comandos.");
            }
        }
        scanner.close();
    }

    private void showHelp() {
        println("Comandos disponíveis:");
        println("ola            - Cumprimentar o servidor");
        println("reservar       - Fazer uma reserva de quarto");
        println("cancelar       - Cancelar uma reserva");
        println("disponiveis    - Ver quartos disponíveis");
        println("reservas       - Ver todas as reservas");
        println("reserva        - Ver uma reserva específica");
        println("quarto         - Ver um quarto específico");
        println("disponiveis_tipo - Ver quartos disponíveis por tipo");
        println("quartos        - Ver todos os quartos");
        println("atualizar      - Atualizar um quarto");
        println("sair           - Sair do sistema");
    }

    private void searchRoomsByType() throws Exception {
        String type = readString("Digite o tipo do quarto");
        println(hotelService.searchAvailableRoomsByType(type));
    }

    private void bookRoom() throws Exception {
        Long roomID = readLong("Digite o ID do quarto");
        String guestName = readString("Digite o nome do hóspede");
        LocalDate checkIn = readDate("Digite a data de entrada (AAAA-MM-DD)");
        LocalDate checkOut = readDate("Digite a data de saída (AAAA-MM-DD)");
        println(hotelService.bookRoom(roomID, guestName, checkIn, checkOut));
    }

    private void cancelBooking() throws Exception {
        Long bookingID = readLong("Digite o ID da reserva");
        println(hotelService.cancelBooking(bookingID));
    }

    private void getAvailableRooms() throws Exception {
        LocalDate checkIn = readDate("Digite a data de entrada (AAAA-MM-DD)");
        LocalDate checkOut = readDate("Digite a data de saída (AAAA-MM-DD)");
        println(hotelService.getAvailableRooms(checkIn, checkOut));
    }

    private void getBooking() throws Exception {
        Long bookingID = readLong("Digite o ID da reserva");
        println(hotelService.getBooking(bookingID));
    }

    private void getRoom() throws Exception {
        Long roomID = readLong("Digite o ID do quarto");
        println(hotelService.getRoom(roomID));
    }

    private void updateRoom() throws Exception {
        Long roomID = readLong("Digite o ID do quarto");
        String type = readString("Digite o tipo do quarto");
        Double price = readDouble("Digite o preço do quarto");
        Boolean available = readBoolean("O quarto está disponível? (true/false)");
        println(hotelService.updateRoom(roomID, type, price, available));
    }

    private String readString(String prompt) {
        println(prompt);
        return scanner.nextLine().trim();
    }

    private Long readLong(String prompt) {
        return Long.parseLong(readString(prompt));
    }

    private Double readDouble(String prompt) {
        return Double.parseDouble(readString(prompt));
    }

    private Boolean readBoolean(String prompt) {
        return Boolean.parseBoolean(readString(prompt));
    }

    private LocalDate readDate(String prompt) {
        return LocalDate.parse(readString(prompt), DateTimeFormatter.ISO_DATE);
    }

    private void println(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        try {
            HotelService hotelService = (HotelService) Naming.lookup("rmi://localhost:1099/HotelService");
            new HotelClient(hotelService).start();
        } catch (Exception e) {
            System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    private interface Command {
        void execute() throws Exception;
    }
}