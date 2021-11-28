
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

public class InOut {

    private Scanner in;
    private Scanner alumniFileIn;
    private Scanner eventFileIn;
    private Scanner donationsFileIn;
    private Scanner passwordFileIn;
    private Scanner trainingFileIn;
    private File donationsFile;
    private File alumniFile;
    private File eventFile;
    private File passwordsFile;
    private File trainingFile;
    private PrintWriter alumniSaved;
    private PrintWriter eventSaved;
    private PrintWriter donationsSaved;
    private PrintWriter passwordsSaved;
    private PrintWriter trainingSaved;
    private TreeMap<Integer, Alumni> alumniMap;
    private TreeMap<Integer, Event> eventMap;
    private TreeMap<Integer, Training> trainingMap;
    private HashMap<Integer, String> passwords;
    private ArrayList<Donation> donationList;

    /**
     * Initiate File, Scanner and PrintWriter
     * 
     * @throws FileNotFoundException
     */
    public InOut() throws FileNotFoundException {
        alumniFile = new File("alumni.txt");
        alumniFileIn = new Scanner(alumniFile);
        eventFile = new File("events.txt");
        eventFileIn = new Scanner(eventFile);
        donationsFile = new File("donations.txt");
        donationsFileIn = new Scanner(donationsFile);
        passwordsFile = new File("passwords.txt");
        passwordFileIn = new Scanner(passwordsFile);
        trainingFile = new File("training.txt");
        trainingFileIn = new Scanner(trainingFile);
        in = new Scanner(System.in);
        existingPasswords();
        existingAlumni();
        existingEvents();
        existingDonations();
        existingTrainingEvents();
    }

    /**
     * Close all Scanners and PrintWriters
     * 
     * @throws FileNotFoundException
     */
    public void closeEverythingAndSave() throws FileNotFoundException {
        alumniSaved = new PrintWriter("temp.txt");
        eventSaved = new PrintWriter("temp2.txt");
        donationsSaved = new PrintWriter("temp3.txt");
        passwordsSaved = new PrintWriter("temp4.txt");
        trainingSaved = new PrintWriter("temp5.txt");

        for (Alumni alumni : alumniMap.values()) {
            alumniSaved.println(alumni.save());
        }

        for (Event event : eventMap.values()) {
            eventSaved.println(event.save());
            eventSaved.println(event.saveDateTime());
            eventSaved.println(event.saveHost());
            eventSaved.println(event.saveAttendants());

        }

        for (Donation donation : donationList) {
            donationsSaved.println(donation.save());
        }

        for (String pw : passwords.values()) {
            passwordsSaved.println(pw);
        }

        for (Training training : trainingMap.values()) {
            trainingSaved.println(training.save());
            trainingSaved.println(training.saveDateTime());
            trainingSaved.println(training.saveHost());
            trainingSaved.println(training.saveAttendants());
        }

        in.close();
        alumniFileIn.close();
        eventFileIn.close();
        alumniSaved.close();
        eventSaved.close();
        trainingSaved.close();
        donationsSaved.close();
        passwordsSaved.close();

    }

    public void existingPasswords() {
        passwords = new HashMap<>();

        while (passwordFileIn.hasNextLine()) {
            String line = passwordFileIn.nextLine();
            String[] s = line.split(",");
            int id = Integer.parseInt(s[0]);
            String pw = s[1];
            passwords.put(id, pw);
        }

    }

    public String getPassword(int id) {
        return passwords.get(id);
    }

    public void setPassword(int id, String newPw) {
        passwords.put(id, newPw);
    }

    public boolean checkId(int id) {
        for (Alumni alumni : alumniMap.values()) {
            if (id == alumni.getID())
                return true;
        }
        return false;
    }

    public void existingDonations() {
        donationList = new ArrayList<>();

        while (donationsFileIn.hasNextLine()) {
            String line = donationsFileIn.nextLine();
            String[] s = line.split(",");
            int alumniID = Integer.parseInt(s[0]);
            int eventID = Integer.parseInt(s[1]);
            double amount = Double.parseDouble(s[2]);
            new Donation(alumniID, eventID, amount);
            donationList.add(new Donation(alumniID, eventID, amount));
        }

    }

    /**
     * Create and Fill a TreeMap of Events pulled from a Text File
     */
    public void existingEvents() {
        eventMap = new TreeMap<>();
        while (eventFileIn.hasNextLine()) {
            // event info
            String line = eventFileIn.nextLine();
            String[] s = line.split(",");
            int id = Integer.parseInt(s[0]);
            String name = s[1];
            int room = Integer.parseInt(s[2]);
            int totalSpots = Integer.parseInt(s[3]);
            // dateTime info
            String dateTimeString = eventFileIn.nextLine();
            LocalDateTime dateTime = extractDateTime(dateTimeString);
            // host info
            String h = eventFileIn.nextLine();
            Host host = extractHost(h);
            // attending alumni info
            String list = eventFileIn.nextLine();
            String[] listArr = list.split(",");
            ArrayList<String> att = new ArrayList<>();
            for (int i = 0; i < listArr.length; i++) {
                att.add(listArr[i]);
            }

            Event e = new Event(id, name, room, totalSpots, dateTime, att, host);
            eventMap.put(id, e);
        }
    }

   
    
    // Training
    public void existingTrainingEvents() {
        trainingMap = new TreeMap<>();
        while (trainingFileIn.hasNextLine()) {
            String line = trainingFileIn.nextLine();
            String[] s = line.split(",");
            int id = Integer.parseInt(s[0]);
            String name = s[1];
            int room = Integer.parseInt(s[2]);
            int totalSpots = Integer.parseInt(s[3]);
            String skill = s[4];
            // dateTime info
            String dateTimeString = trainingFileIn.nextLine();
            LocalDateTime dateTime = extractDateTime(dateTimeString);
            
            // host info
            String h = trainingFileIn.nextLine();
            Host host = extractHost(h);
            // attending alumni info
            String list = trainingFileIn.nextLine();
            String[] listArr = list.split(",");
            ArrayList<String> att = new ArrayList<>();
            for (int i = 0; i < listArr.length; i++) {
                att.add(listArr[i]);
            }

            Training t = new Training(id, name, room, totalSpots, dateTime, att, host, skill);
            trainingMap.put(id, t);
        }
    }

    private LocalDateTime extractDateTime(String dateTimeString) {
        String[] dt = dateTimeString.split(",");
        int year = Integer.parseInt(dt[0]);
        int month = Integer.parseInt(dt[1]);
        int dayOfMonth = Integer.parseInt(dt[2]);
        int hour = Integer.parseInt(dt[3]);
        int minute = Integer.parseInt(dt[4]);
        LocalDateTime dateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
        return dateTime;
    }
    
    private Host extractHost(String h) {
        String[] hArr = h.split(",");
        int hostId = Integer.parseInt(hArr[0]);
        String hostName = hArr[1];
        String hostAdd = hArr[2];
        String hostMaj = hArr[3];
        String hostGY = hArr[4];
        String hostJob = hArr[5];
        String hostOrg = hArr[6];
        String topic = hArr[7];
        int phone = Integer.parseInt(hArr[8]);
        String email = hArr[9];
        Host host = new Host(hostId, hostName, hostAdd, hostMaj, hostGY, hostJob, hostOrg, topic, phone, email);
        return host;
    }



    /**
     * Create and Fill a TreeMap of Alumni pulled from a Text File
     */
    public void existingAlumni() {
        alumniMap = new TreeMap<>();
        Alumni a = new Alumni();
        while (alumniFileIn.hasNextLine()) {
            String line = alumniFileIn.nextLine();
            String[] s = line.split(",");
            int id = Integer.parseInt(s[0]);
            String name = s[1];
            String address = s[2];
            String major = s[3];
            String gradYear = s[4];
            String job = s[5];
            String organization = s[6];
            String password = getPassword(id);
            a = new Alumni(id, name, address, major, gradYear, job, organization, password);
            alumniMap.put(id, a);
        }
    }

    /**
     * Display the Events Map
     */
    public void displayEvents() {
        for (Event events : eventMap.values()) {
            System.out.println(events.toString());
            System.out.println(events.getHost());
        }
    }

    /**
     * Display the Alumni Map
     */
    public void displayAlumni() {
        for (Alumni alumni : alumniMap.values()) {
            System.out.println(alumni.toString());
        }
    }

    public void displayTraining() {
        for (Training training : trainingMap.values()) {
            System.out.println(training.toString());
            System.out.println(training.getHost());
        }
    }

    /**
     * Display Host's for both Events and Training
     */
    public void displayHosts() {
        System.out.println("The Hosts for Events are:");
        for (Event events : eventMap.values()) {
            System.out.println("For Event " + events.getID() + " " + events.getHost());
        }
        System.out.println("The Hosts for Trainings are:");
        for (Training training : trainingMap.values()) {
            System.out.println("For Training " + training.getID() + " " + training.getHost());
        }

    }

    public void displayAttendantsEvent(int eventID) {
        System.out.println(eventMap.get(eventID).displayAttendants());
    }

    public void displayAttendantsTraining(int trainingID) {
        System.out.println(trainingMap.get(trainingID).displayAttendants());
    }

    public void displayByYear(int year) {
        int check = Integer.parseInt(Integer.toString(year).substring(2, 4));
        System.out.println("Events happening in the year " + year);
        for (Event event : eventMap.values()) {
            if (event.getYear() == check)
                System.out.println(event.toString());
        }
        System.out.println("Training happening in the year " + year);
        for (Training training : trainingMap.values()) {
            if (training.getYear() == check)
                System.out.println(training.toString());
        }
    }

    public void displayMyAttendance(int id) {
        System.out.println("My Events:");
        int counter = 0;
        for (Event event : eventMap.values()) {
            if (event.checkForAttendance(alumniMap.get(id).getName())) {
                System.out.println("You are attending " + event.getName() + " ID # " + event.getID());
                counter++;
            }
        }
        if (counter == 0) System.out.println("Not currently attending any Events");
        counter = 0;
        System.out.println("My Training:");
        for (Training training : trainingMap.values()) {
            if (training.checkForAttendance(alumniMap.get(id).getName())) {
                System.out.println("You are attending " + training.getName() + " ID # " + training.getID());
                counter++;
            }
        }
        if (counter == 0) System.out.println("Not currently attending any Training");
    }

    public boolean alreadyAttendingEvent(int id, int eventID) {
        if (eventMap.get(eventID).checkForAttendance(alumniMap.get(id).getName()))
            return true;
        else
            return false;
    }

    public boolean alreadyAttendingTraining(int id, int trainingID) {
        if (trainingMap.get(trainingID).checkForAttendance(alumniMap.get(id).getName()))
            return true;
        else
            return false;
    }
    // ---------- getters --------------

    /**
     * Get toString call for an Alumni Obj
     * 
     * @param id Alumni ID
     * @return Alumni toString
     */
    public String getAlumni(int id) {
        return alumniMap.get(id).toString();
    }

    /**
     * Get toString call for an Event Obj
     * 
     * @param id Event ID
     * @return Event toString
     */
    public String getEvent(int id) {
        return eventMap.get(id).toString();
    }

    public String getEventHost(int id) {
        return eventMap.get(id).getHost();
    }

    public String getTrainingHost(int id) {
        return trainingMap.get(id).getHost();
    }

    /**
     * Get toString call for a Training Obj
     * 
     * @param id Training ID
     * @return Training toString
     */
    public String getTraining(int id) {
        return trainingMap.get(id).toString();
    }

    public String getAlumniName(int id) {
        return alumniMap.get(id).getName();
    }

    public String getAlumniAddress(int id) {
        return alumniMap.get(id).getAddress();
    }

    public String getAlumniMajor(int id) {
        return alumniMap.get(id).getMajor();
    }

    public String getAlumniGradYear(int id) {
        return alumniMap.get(id).getGradYear();
    }

    public String getAlumniJob(int id) {
        return alumniMap.get(id).getJob();
    }

    public String getAlumniOrg(int id) {
        return alumniMap.get(id).getOrganization();
    }

    // ---------- setters--------------
    public void setAlumniName(int id, String name) {
        alumniMap.get(id).setName(name);
    }

    public void setAlumniAddress(int id, String address) {
        alumniMap.get(id).setAddress(address);
    }

    public void setAlumniMajor(int id, String major) {
        alumniMap.get(id).setMajor(major);
    }

    public void setAlumniGradYear(int id, String gradYear) {
        alumniMap.get(id).setGradYear(gradYear);
    }

    public void setAlumniJob(int id, String job) {
        alumniMap.get(id).setJob(job);
    }

    public void setAlumniOrg(int id, String org) {
        alumniMap.get(id).setOrganization(org);
    }

    public int createAlumni(String name, String address, String major, String gradYear, String job,
            String organization,
            String password) {
        int id = alumniMap.lastKey();
        id++;
        Alumni a = new Alumni(id, name, address, major, gradYear, job, organization, password);
        alumniMap.put(id, a);
        return id;
    }

    // --------- Event Methods here ---------------

    public void joinEvent(int id, String name) {
        eventMap.get(id).addAttendant(name);
    }

    // -----------Edit Event Methods----------------

    /**
     * sets event name
     * 
     * @param id   event id
     * @param name event name
     */
    public void setEventName(int id, String name) {
        eventMap.get(id).setName(name);
    }

    /**
     * sets a name for the event
     * 
     * @param id   event id
     * @param time event time
     */
    public void setEventDateTime(int id, int year, int month, int day, int hour, int minute) {
        eventMap.get(id).setTime(year, month, day, hour, minute);
    }

    /**
     * sets a room for event
     * 
     * @param id   event id
     * @param room event room
     */
    public void setEventRoom(int id, int room) {
        eventMap.get(id).setRoom(room);
    }

    public int getEventYear(int id) {
        return eventMap.get(id).getYear();
    }

    public int getEventMonth(int id) {
        return eventMap.get(id).getMonth();
    }

    public int getEventDay(int id) {
        return eventMap.get(id).getDay();
    }

    public int getEventHour(int id) {
        return eventMap.get(id).getHour();
    }

    public int getEventMin(int id) {
        return eventMap.get(id).getMinute();
    }

    public int getHostId(int id) {
        return eventMap.get(id).getHostId();
    }

    // ------ edit training methods -----
    public void setTrainingName(int id, String name) {
        trainingMap.get(id).setName(name);
    }

    public void setTrainingRoom(int id, int room) {
        trainingMap.get(id).setRoom(room);
    }

    public void setTrainingDate(int id, int year, int month, int dayOfMonth, int hour, int minute) {
        trainingMap.get(id).setStartDate(year, month, dayOfMonth, hour, minute);
    }

    public void setTrainingSkill(int id, String newSkill) {
        trainingMap.get(id).setSkill(newSkill);
    }

    public void setNumOfTotalSpotsTraining(int id, int spots) {
        trainingMap.get(id).setTotalSpots(spots);
    }

    public void setNumOfTotalSpotsEvents(int id, int spots) {
        eventMap.get(id).setTotalSpots(spots);
    }

    public void joinTraining(int id, String name) {
        trainingMap.get(id).addAttendant(name);
    }

    // ------- remove --------------
    public void deleteAlumni(int id) {
        alumniMap.remove(id);
    }

    public void deleteEvent(int id) {
        eventMap.remove(id);
    }

    public void deleteTraining(int id) {
        trainingMap.remove(id);
    }

    public void createEvent(String name, int room, int totalSpots, LocalDateTime startDate, Host host) {
        int id = eventMap.lastKey();
        id++;
        Event e = new Event(id, name, room, totalSpots, startDate, host);
        eventMap.put(id, e);
    }

    public void createTrainingEvent(String name, int room, int totalSpots, LocalDateTime startDate, Host host,
            String skill) {
        int id = trainingMap.lastKey();
        id++;
        Training t = new Training(id, name, room, totalSpots, startDate, host, skill);
        trainingMap.put(id, t);
    }
    // ------- donation list methods ------

    /**
     * Add a Donation and put it in the Donation List
     * 
     * @param alumniId       ID of the ALumni making the donation
     * @param eventId        ID of the Event that the donation is going towards
     * @param donationAmount Amount of the Donation being made
     */
    public void addDonationToList(int alumniId, int eventId, double donationAmount) {
        donationList.add(new Donation(alumniId, eventId, donationAmount));
    }

    /**
     * Displays the Donations made by a certain Alumni
     * 
     * @param id ID of the Alumni
     */
    public void displayDonationsAlumni(int id) {
        for (int i = 0; i < donationList.size(); i++) {
            if (id == donationList.get(i).getAlumniId()) {
                System.out.println("Donation amount" + donationList.get(i).getAmountDonated());
                System.out.println("Date and Time of Donation: " + donationList.get(i).formatDateTime());
            }
        }
    }

    /**
     * Displays the Donations for a certain Event
     * 
     * @param id ID of the Event
     */
    public void displayDonationsEvents(int id) {
        for (int i = 0; i < donationList.size(); i++) {
            if (id == donationList.get(i).getEventId()) {
                System.out.println("Donation amount" + donationList.get(i).getAmountDonated());
            }
        }

    }

    /**
     * Get User Text Input
     * 
     * @return User input : nextLine
     */
    public String stringInput() {
        return in.nextLine();

    }

    /**
     * Get User Integer Input
     * 
     * @return User input : int
     */
    public int intInput() {
        int n;
        do {
            while (!in.hasNextInt()) {
                String s = in.next();
                System.out.println(" ----------------------------------------------------- ");
                System.out.printf("\"%s\" is not a valid number%n", s);
                System.out.println(" ----------------------------------------------------- ");
            }
            n = in.nextInt();
            if (n < 0) {
                System.out.println(" ----------------------------------------------------- ");
                System.out.println(n + " is not a valid number.");
                System.out.println(" ----------------------------------------------------- ");
            }
            in.nextLine();

        } while (n < 0);
        return n;
    }

    /**
     * Get User Integer Input within a boundary
     * 
     * @return User input : int
     */
    public int intInput(int boundary) {
        int n = 0;
        if (!in.hasNextInt()) {
            String s = in.next();
            System.out.println(" ----------------------------------------------------- ");
            System.out.printf("\"%s\" is not a valid number%n", s);
            System.out.println(" ----------------------------------------------------- ");
            return n;
        }
        n = in.nextInt();
        if (n > boundary || n < 1) {
            System.out.println(" ----------------------------------------------------- ");
            System.out.println(n + " is not a valid number.");
            System.out.println(" ----------------------------------------------------- ");
        }
        in.nextLine();
        return n;
    }

    public long longInput() {
        long n;
        do {
            while (!in.hasNextLong()) {
                String s = in.next();
                System.out.println(" ----------------------------------------------------- ");
                System.out.printf("\"%s\" is not a valid number%n", s);
                System.out.println(" ----------------------------------------------------- ");
            }
            n = in.nextLong();
            if (n < 0) {
                System.out.println(" ----------------------------------------------------- ");
                System.out.println(n + " is not a valid number.");
                System.out.println(" ----------------------------------------------------- ");
            }
            in.nextLine();

        } while (n < 0);
        return n;
    }

    /**
     * Get User Double Input
     * 
     * @return User input : double
     */
    public double doubleInput() {
        double n;
        do {
            while (!in.hasNextDouble()) {
                String s = in.next();
                System.out.println(" ----------------------------------------------------- ");
                System.out.printf("\"%s\" is not a valid number%n", s);
                System.out.println(" ----------------------------------------------------- ");
            }
            n = in.nextDouble();
            if (n < 0) {
                System.out.println(" ----------------------------------------------------- ");
                System.out.println(n + " is not a valid number.");
                System.out.println(" ----------------------------------------------------- ");
            }
            in.nextLine();

        } while (n < 0);
        return n;
    }
}
