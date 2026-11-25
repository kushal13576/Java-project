
import java.util.*;

class Cab {
    String type;
    int baseFare;
    int perKm;

    Cab(String type, int baseFare, int perKm) {
        this.type = type;
        this.baseFare = baseFare;
        this.perKm = perKm;
    }

    int calculateFare(int distance) {
        return baseFare + (distance * perKm);
    }
}

class Edge {
    String destination;
    int distance;

    Edge(String destination, int distance) {
        this.destination = destination;
        this.distance = distance;
    }
}

class Graph {
    HashMap<String, ArrayList<Edge>> map = new HashMap<>();

    void addLocation(String location) {
        map.putIfAbsent(location, new ArrayList<>());
    }

    void addRoute(String source, String destination, int distance) {
        map.get(source).add(new Edge(destination, distance));
        map.get(destination).add(new Edge(source, distance));
    }

    int dijkstra(String source, String destination) {

        HashMap<String, Integer> dist = new HashMap<>();
        HashMap<String, String> parent = new HashMap<>();

        for (String location : map.keySet()) {
            dist.put(location, Integer.MAX_VALUE);
        }

        PriorityQueue<String> pq = new PriorityQueue<>(
            Comparator.comparingInt(dist::get)
        );

        dist.put(source, 0);
        pq.add(source);

        while (!pq.isEmpty()) {
            String current = pq.poll();

            for (Edge edge : map.get(current)) {
                int newDist = dist.get(current) + edge.distance;

                if (newDist < dist.get(edge.destination)) {
                    dist.put(edge.destination, newDist);
                    parent.put(edge.destination, current);

                    pq.remove(edge.destination);
                    pq.add(edge.destination);
                }
            }
        }

        if (dist.get(destination) == Integer.MAX_VALUE) {
            System.out.println("Route not found!");
            return -1;
        }

        ArrayList<String> path = new ArrayList<>();

        String current = destination;

        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }

        Collections.reverse(path);

        System.out.println("\nShortest Route: " + path);

        return dist.get(destination);
    }
}

class Booking {
    String bookingId;
    String pickup;
    String destination;
    String cabType;
    int distance;
    int fare;

    Booking(String id, String p, String d,
            String type, int distance, int fare) {

        bookingId = id;
        pickup = p;
        destination = d;
        cabType = type;
        this.distance = distance;
        this.fare = fare;
    }

    void display() {
        System.out.println("\n----- BOOKING CONFIRMED -----");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Pickup: " + pickup);
        System.out.println("Destination: " + destination);
        System.out.println("Cab: " + cabType);
        System.out.println("Distance: " + distance + " KM");
        System.out.println("Fare: Rs. " + fare);
    }
}

public class project {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Graph graph = new Graph();

        // Adding locations
        graph.addLocation("A");
        graph.addLocation("B");
        graph.addLocation("C");
        graph.addLocation("D");

        // Adding routes
        graph.addRoute("A", "B", 5);
        graph.addRoute("A", "C", 3);
        graph.addRoute("C", "D", 4);
        graph.addRoute("B", "D", 6);

        // Available cabs
        ArrayList<Cab> cabs = new ArrayList<>();

        cabs.add(new Cab("Mini", 40, 12));
        cabs.add(new Cab("Sedan", 60, 15));
        cabs.add(new Cab("SUV", 100, 20));

        // Booking records
        HashMap<String, Booking> bookings = new HashMap<>();

        System.out.println("================================");
        System.out.println("      CAB BOOKING SIMULATOR");
        System.out.println("================================");

        System.out.println("\nAvailable Locations: A, B, C, D");

        System.out.print("\nEnter Pickup Location: ");
        String pickup = sc.next().toUpperCase();

        System.out.print("Enter Destination: ");
        String destination = sc.next().toUpperCase();

        if (!graph.map.containsKey(pickup) ||
            !graph.map.containsKey(destination) ||
            pickup.equals(destination)) {

            System.out.println("Invalid locations!");
            sc.close();
            return;
        }

        // Finding shortest distance
        int distance = graph.dijkstra(pickup, destination);

        if (distance == -1) {
            sc.close();
            return;
        }

        System.out.println("Total Distance: " + distance + " KM");

        // Displaying cabs
        System.out.println("\nAvailable Cabs:");

        for (int i = 0; i < cabs.size(); i++) {
            Cab cab = cabs.get(i);

            System.out.println((i + 1) + ". " + cab.type +
                " - Rs. " + cab.calculateFare(distance));
        }

        System.out.print("\nSelect Cab (1-3): ");
        int choice = sc.nextInt();

        if (choice < 1 || choice > cabs.size()) {
            System.out.println("Invalid Cab Selection!");
            sc.close();
            return;
        }

        Cab selectedCab = cabs.get(choice - 1);

        int fare = selectedCab.calculateFare(distance);

        System.out.println("\nEstimated Fare: Rs. " + fare);

        System.out.print("Confirm Booking? (yes/no): ");
        String confirm = sc.next();

        if (confirm.equalsIgnoreCase("yes")) {

            String id = "CAB1001";

            Booking booking = new Booking(
                id, pickup, destination,
                selectedCab.type, distance, fare
            );

            bookings.put(id, booking);

            booking.display();

        } else {
            System.out.println("Booking Cancelled!");
        }

        sc.close();
    }
}
