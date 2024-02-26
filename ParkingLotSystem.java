
    import java.util.*;

    class ParkingLot {
        private int capacity;
        private Map<Integer, Car> parkingSlots;
        private Map<String, List<Integer>> colorToSlotMap;
        private Map<String, Integer> registrationToSlotMap;

        public ParkingLot(int capacity) {
            this.capacity = capacity;
            this.parkingSlots = new HashMap<>();
            this.colorToSlotMap = new HashMap<>();
            this.registrationToSlotMap = new HashMap<>();
        }

        public void park(String registrationNumber, String color) {
            if (parkingSlots.size() == capacity) {
                System.out.println("Sorry, parking lot is full");
                return;
            }

            int slotNumber = findNextAvailableSlot();
            parkingSlots.put(slotNumber, new Car(registrationNumber, color));
            updateMaps(slotNumber, registrationNumber, color);

            System.out.println("Allocated slot number: " + slotNumber);
        }

        public void leave(int slotNumber) {
            if (parkingSlots.containsKey(slotNumber)) {
                Car car = parkingSlots.get(slotNumber);
                parkingSlots.remove(slotNumber);
                removeFromMaps(slotNumber, car.getRegistrationNumber(), car.getColor());

                System.out.println("Slot number " + slotNumber + " is free");
            } else {
                System.out.println("Slot number " + slotNumber + " is already empty");
            }
        }

        public void status() {
            System.out.println("Slot No. Registration No Colour");
            for (Map.Entry<Integer, Car> entry : parkingSlots.entrySet()) {
                int slotNumber = entry.getKey();
                Car car = entry.getValue();
                System.out.println(slotNumber + " " + car.getRegistrationNumber() + " " + car.getColor());
            }
        }

        public void registrationNumbersForCarsWithColor(String color) {
            if (colorToSlotMap.containsKey(color)) {
                List<Integer> slots = colorToSlotMap.get(color);
                List<String> registrationNumbers = new ArrayList<>();
                for (int slot : slots) {
                    registrationNumbers.add(parkingSlots.get(slot).getRegistrationNumber());
                }
                System.out.println(String.join(", ", registrationNumbers));
            } else {
                System.out.println("No cars found with the color " + color);
            }
        }

        public void slotNumberForRegistrationNumber(String registrationNumber) {
            if (registrationToSlotMap.containsKey(registrationNumber)) {
                int slotNumber = registrationToSlotMap.get(registrationNumber);
                System.out.println("Slot number for Registration No " + registrationNumber + " is " + slotNumber);
            } else {
                System.out.println("Car with Registration No " + registrationNumber + " not found");
            }
        }

        private int findNextAvailableSlot() {
            for (int i = 1; i <= capacity; i++) {
                if (!parkingSlots.containsKey(i)) {
                    return i;
                }
            }
            return -1; // Parking lot is full
        }

        private void updateMaps(int slotNumber, String registrationNumber, String color) {
            colorToSlotMap.putIfAbsent(color, new ArrayList<>());
            colorToSlotMap.get(color).add(slotNumber);

            registrationToSlotMap.put(registrationNumber, slotNumber);
        }

        private void removeFromMaps(int slotNumber, String registrationNumber, String color) {
            List<Integer> slots = colorToSlotMap.get(color);
            slots.remove(Integer.valueOf(slotNumber)); // Remove the slot from the list of slots with the color

            registrationToSlotMap.remove(registrationNumber);
        }
    }

    class Car {
        private String registrationNumber;
        private String color;

        public Car(String registrationNumber, String color) {
            this.registrationNumber = registrationNumber;
            this.color = color;
        }

        public String getRegistrationNumber() {
            return registrationNumber;
        }

        public String getColor() {
            return color;
        }
    }

    public class ParkingLotSystem {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            ParkingLot parkingLot = null;

            while (true) {
                String command = scanner.nextLine();
                String[] tokens = command.split(" ");

                switch (tokens[0]) {
                    case "create_parking_lot":
                        int capacity = Integer.parseInt(tokens[1]);
                        parkingLot = new ParkingLot(capacity);
                        System.out.println("Created a parking lot with " + capacity + " slots");
                        break;

                    case "park":
                        parkingLot.park(tokens[1], tokens[2]);
                        break;

                    case "leave":
                        parkingLot.leave(Integer.parseInt(tokens[1]));
                        break;

                    case "status":
                        parkingLot.status();
                        break;

                    case "registration_numbers_for_cars_with_colour":
                        parkingLot.registrationNumbersForCarsWithColor(tokens[1]);
                        break;

                    case "slot_number_for_registration_number":
                        parkingLot.slotNumberForRegistrationNumber(tokens[1]);
                        break;

                    case "exit":
                        System.exit(0);

                    default:
                        System.out.println("Invalid command");
                }
            }
        }
    }