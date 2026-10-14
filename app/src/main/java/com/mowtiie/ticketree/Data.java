package com.mowtiie.ticketree;

import java.util.ArrayList;

public class Data {

    public static String USER_NAME = "";
    public static String USER_EMAIL = "";
    public static String USER_PASSWORD = "";
    public static boolean USER_IS_ORGANIZER = false;

    public static final String[] EVENT_NAMES = {
            "Leadership Summit 2026",
            "AI Boot Camp",
            "Toastmasters Speech Night",
            "Startup Pitch Day",
            "Women in Tech Forum",
            "Cybersecurity Workshop",
            "Public Speaking Masterclass",
            "Youth Innovation Expo"
    };
    public static final String[] EVENT_CATEGORIES = {
            "Leadership Training",
            "AI Boot Camp",
            "Toastmasters",
            "Business",
            "Conference",
            "Workshop",
            "Toastmasters",
            "Conference"
    };
    public static final String[] EVENT_DATES = {
            "Nov 08, 2026 9:00 AM",
            "Nov 15, 2026 8:00 AM",
            "Nov 15, 2026 6:00 PM",
            "Nov 22, 2026 1:00 PM",
            "Dec 05, 2026 9:00 AM",
            "Dec 05, 2026 1:00 PM",
            "Dec 12, 2026 10:00 AM",
            "Dec 19, 2026 9:00 AM"
    };
    public static final String[] EVENT_LOCATIONS = {
            "Manila",
            "Quezon City",
            "Makati",
            "Cebu",
            "Manila",
            "Davao",
            "Makati",
            "Quezon City"
    };
    public static final String[] EVENT_VENUES = {
            "Hotel",
            "Onsite",
            "Hotel",
            "Onsite",
            "Hotel",
            "Training Camp",
            "Onsite",
            "Training Camp"
    };
    public static final int[] EVENT_PRICES = {1500, 2500, 500, 800, 1200, 1800, 1000, 600};
    public static final String[] EVENT_DESCRIPTIONS = {
            "A full-day summit on leading teams, with keynotes and breakout sessions.",
            "Hands-on bootcamp covering machine learning basics and prompt engineering.",
            "An evening of prepared speeches, table topics and evaluations.",
            "Ten startups pitch to a panel of investors and mentors.",
            "Talks and networking with women leaders across the tech industry.",
            "Learn practical defense against phishing, malware and data leaks.",
            "Build confidence and stage presence with live coaching and feedback.",
            "Student teams showcase prototypes, robotics and community projects."
    };
    public static final String[] EVENT_ORGANIZERS = {
            "Lead PH Network",
            "TechForward Academy",
            "Manila Toastmasters Club",
            "Founders Circle",
            "TechHer Philippines",
            "SecureNet Training",
            "Voice Forward Coaching",
            "Youth Innovators Guild"
    };
    public static final String[] EVENT_AGE_RESTRICTIONS = {
            "18+", "15+", "All ages", "18+", "16+", "18+", "15+", "All ages"
    };

    public static final String[] TICKET_TYPES = {"General Admission", "VIP", "Early Bird", "Group"};
    public static final int[] TICKET_PRICES = {1000, 3000, 750, 4500};
    public static final int[] TICKET_REMAINING = {120, 30, 50, 20};

    public static final String[] BOX_NAMES = {"Lower Box", "Middle Box", "Upper Box"};
    public static final int HOLD_SECONDS = 120;
    public static final boolean[][][] SEAT_TAKEN = new boolean[8][3][20];

    static {
        for (int e = 0; e < 8; e++) {
            for (int b = 0; b < 3; b++) {
                for (int s = 0; s < 20; s++) {
                    SEAT_TAKEN[e][b][s] = (s * 7 + e * 3 + b * 5) % 6 == 0;
                }
            }
        }
    }

    public static int SEL_EVENT = 0;
    public static int SEL_TYPE = 0;
    public static int SEL_QTY = 1;
    public static int SEL_BOX = 0;
    public static final ArrayList<Integer> SEL_SEATS = new ArrayList<>();
    public static long HOLD_END = 0;

    public static void releaseHold() {
        for (int s : SEL_SEATS) SEAT_TAKEN[SEL_EVENT][SEL_BOX][s] = false;
        SEL_SEATS.clear();
        HOLD_END = 0;
    }

    public static final ArrayList<String> ORDERS = new ArrayList<>();
    public static final ArrayList<String> TICKETS = new ArrayList<>();
    public static final ArrayList<String> NOTIFICATIONS = new ArrayList<>();
    public static final ArrayList<String> RECEIPTS = new ArrayList<>();
    public static final ArrayList<String> PAYMENT_METHODS = new ArrayList<>();
}
