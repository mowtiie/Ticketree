package com.mowtiie.ticketree.data;

import java.util.ArrayList;
import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.color.MaterialColors;

import com.mowtiie.ticketree.R;
import com.mowtiie.ticketree.databinding.ItemRowBinding;

import java.util.Arrays;
import java.util.Locale;

public class Data {

    public static String USER_NAME = "";
    public static String USER_EMAIL = "";
    public static String USER_PASSWORD = "";
    public static boolean USER_IS_ORGANIZER = false;

    public static String[] EVENT_NAMES = {
            "Leadership Summit 2026",
            "AI Boot Camp",
            "Toastmasters Speech Night",
            "Startup Pitch Day",
            "Women in Tech Forum",
            "Cybersecurity Workshop",
            "Public Speaking Masterclass",
            "Youth Innovation Expo"
    };
    public static String[] EVENT_CATEGORIES = {
            "Leadership Training",
            "AI Boot Camp",
            "Toastmasters",
            "Business",
            "Conference",
            "Workshop",
            "Toastmasters",
            "Conference"
    };
    public static String[] EVENT_DATES = {
            "Nov 08, 2026 9:00 AM",
            "Nov 15, 2026 8:00 AM",
            "Nov 15, 2026 6:00 PM",
            "Nov 22, 2026 1:00 PM",
            "Dec 05, 2026 9:00 AM",
            "Dec 05, 2026 1:00 PM",
            "Dec 12, 2026 10:00 AM",
            "Dec 19, 2026 9:00 AM"
    };
    public static String[] EVENT_LOCATIONS = {
            "Manila",
            "Quezon City",
            "Makati",
            "Cebu",
            "Manila",
            "Davao",
            "Makati",
            "Quezon City"
    };
    public static String[] EVENT_VENUES = {
            "Hotel",
            "Onsite",
            "Hotel",
            "Onsite",
            "Hotel",
            "Training Camp",
            "Onsite",
            "Training Camp"
    };
    public static String[] EVENT_DESCRIPTIONS = {
            "A full-day summit on leading teams, with keynotes and breakout sessions.",
            "Hands-on bootcamp covering machine learning basics and prompt engineering.",
            "An evening of prepared speeches, table topics and evaluations.",
            "Ten startups pitch to a panel of investors and mentors.",
            "Talks and networking with women leaders across the tech industry.",
            "Learn practical defense against phishing, malware and data leaks.",
            "Build confidence and stage presence with live coaching and feedback.",
            "Student teams showcase prototypes, robotics and community projects."
    };
    public static String[] EVENT_ORGANIZERS = {
            "Lead PH Network",
            "TechForward Academy",
            "Manila Toastmasters Club",
            "Founders Circle",
            "TechHer Philippines",
            "SecureNet Training",
            "Voice Forward Coaching",
            "Youth Innovators Guild"
    };
    public static String[] EVENT_AGE_RESTRICTIONS = {
            "18+", "15+", "All ages", "18+", "16+", "18+", "15+", "All ages"
    };

    public static String[] EVENT_SPEAKERS = {
            "Maria Santos, Paolo Reyes, Dr. Liza Cruz",
            "Dr. Ken Tan, Aira Lim, Josh Bautista",
            "Club President, Table Topics Master, Chief Evaluator",
            "Panel of 5 investors, Founder mentors",
            "Angela Ramos, Dr. Nina Flores, Carla Mendoza",
            "Rico Dela Cruz, Sam Ortiz",
            "Coach Dina Villa, Mark Aquino",
            "Student innovators, Guest judges"
    };
    public static final String TERMS =
            "Tickets are non-refundable. Bring a valid ID and your QR ticket. "
                    + "Seats are held for 2 minutes during checkout. "
                    + "The organizer may change the schedule with prior notice.";
    public static final int[] TICKET_MIN_QTY = {1, 1, 1, 4};

    public static final String[] TICKET_TYPES = {"General Admission", "VIP", "Early Bird", "Group"};
    public static final int[] TICKET_PRICES = {1000, 3000, 750, 4500};
    public static final int[] TICKET_REMAINING = {120, 30, 50, 20};

    public static final String[] BOX_NAMES = {"Lower Box", "Middle Box", "Upper Box"};
    public static String[] EVENT_ORG_CONTACTS = {
            "0917 111 0001", "0917 111 0002", "0917 111 0003", "0917 111 0004",
            "0917 111 0005", "0917 111 0006", "0917 111 0007", "0917 111 0008"
    };
    public static String[] EVENT_ORG_ABOUT = {
            "Leadership community training professionals across the Philippines.",
            "A tech academy running hands-on AI and coding programs.",
            "Local Toastmasters chapter that meets every month in Manila.",
            "Network of founders, mentors and early-stage investors.",
            "Community that supports women building careers in tech.",
            "Security training team for students and small businesses.",
            "Coaches helping people speak with confidence.",
            "Student-led guild promoting innovation in schools."
    };
    public static String[] EVENT_TERMS = {
            TERMS, TERMS, TERMS, TERMS, TERMS, TERMS, TERMS, TERMS
    };
    public static int[] EVENT_IMAGES = {0, 3, 1, 5, 4, 3, 2, 5};
    public static final String[] IMAGE_NAMES = {"Stage", "Classroom", "Conference", "Workshop", "Networking", "Expo"};
    public static final int[] IMAGE_RES = {
            R.drawable.event_stage, R.drawable.event_classroom, R.drawable.event_conference,
            R.drawable.event_workshop, R.drawable.event_networking, R.drawable.event_expo
    };
    public static String USER_PHONE = "";
    public static String USER_ABOUT = "";

    public static boolean[] EVENT_PUBLISHED = {true, true, true, true, true, true, true, true};

    public static void addEvent() {
        int n = EVENT_NAMES.length + 1;
        EVENT_NAMES = Arrays.copyOf(EVENT_NAMES, n);
        EVENT_CATEGORIES = Arrays.copyOf(EVENT_CATEGORIES, n);
        EVENT_DATES = Arrays.copyOf(EVENT_DATES, n);
        EVENT_LOCATIONS = Arrays.copyOf(EVENT_LOCATIONS, n);
        EVENT_VENUES = Arrays.copyOf(EVENT_VENUES, n);
        EVENT_DESCRIPTIONS = Arrays.copyOf(EVENT_DESCRIPTIONS, n);
        EVENT_ORGANIZERS = Arrays.copyOf(EVENT_ORGANIZERS, n);
        EVENT_AGE_RESTRICTIONS = Arrays.copyOf(EVENT_AGE_RESTRICTIONS, n);
        EVENT_SPEAKERS = Arrays.copyOf(EVENT_SPEAKERS, n);
        EVENT_ORG_CONTACTS = Arrays.copyOf(EVENT_ORG_CONTACTS, n);
        EVENT_ORG_ABOUT = Arrays.copyOf(EVENT_ORG_ABOUT, n);
        EVENT_TERMS = Arrays.copyOf(EVENT_TERMS, n);
        EVENT_IMAGES = Arrays.copyOf(EVENT_IMAGES, n);
        EVENT_PUBLISHED = Arrays.copyOf(EVENT_PUBLISHED, n);
        SEAT_TAKEN = Arrays.copyOf(SEAT_TAKEN, n);
        SEAT_TAKEN[n - 1] = new boolean[3][20];
    }

    public static final ArrayList<String> VENUES = new ArrayList<>(Arrays.asList("Onsite", "Hotel", "Training Camp"));

    public static final ArrayList<Integer> ORDER_TYPES = new ArrayList<>();
    public static final ArrayList<Integer> ORDER_QTYS = new ArrayList<>();
    public static final ArrayList<Integer> ORDER_TOTALS = new ArrayList<>();
    public static final ArrayList<String> ORDER_TIMES = new ArrayList<>();

    public static final int[] DEMO_SOLD_BY_TYPE = {45, 12, 30, 5};
    public static final String[] DEMO_SALE_DATES = {"Oct 20, 2026", "Oct 27, 2026", "Nov 03, 2026"};
    public static final int[] DEMO_SALE_TICKETS = {20, 35, 37};
    public static final int DEMO_ORDERS = 24;

    public static final int HOLD_SECONDS = 120;
    public static boolean[][][] SEAT_TAKEN = new boolean[8][3][20];

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
    public static int UNREAD = 0;

    public static void addNotification(String message) {
        String time = new SimpleDateFormat("h:mm a", Locale.US).format(new Date());
        NOTIFICATIONS.add(0, time + " - " + message);
        UNREAD++;
    }

    public static void notify(Context app, String message) {
        addNotification(message);
        Toast.makeText(app, message, Toast.LENGTH_SHORT).show();
    }
    public static final ArrayList<String> RECEIPTS = new ArrayList<>();
    public static final ArrayList<String> PAYMENT_METHODS = new ArrayList<>(Arrays.asList(
            "GCash 0917 *** 4567", "Maya 0918 *** 8901", "Visa ****1234"));
    public static int DEFAULT_PAYMENT = 0;

    static final String[] CATEGORY_EMOJI = {
            "🎤", "🤖", "🗣", "💼",
            "🎪", "🛠", "🌟"
    };
    static final int[] CATEGORY_BG = {
            com.google.android.material.R.attr.colorPrimaryContainer,
            com.google.android.material.R.attr.colorSecondaryContainer,
            com.google.android.material.R.attr.colorTertiaryContainer,
            com.google.android.material.R.attr.colorPrimaryFixed,
            com.google.android.material.R.attr.colorSecondaryFixed,
            com.google.android.material.R.attr.colorTertiaryFixed
    };
    static final int[] CATEGORY_ON = {
            com.google.android.material.R.attr.colorOnPrimaryContainer,
            com.google.android.material.R.attr.colorOnSecondaryContainer,
            com.google.android.material.R.attr.colorOnTertiaryContainer,
            com.google.android.material.R.attr.colorOnPrimaryFixed,
            com.google.android.material.R.attr.colorOnSecondaryFixed,
            com.google.android.material.R.attr.colorOnTertiaryFixed
    };

    public static String greeting() {
        String first = USER_NAME.trim().split(" ")[0];
        return "Hello, " + (first.isEmpty() ? "there" : first) + "!";
    }

    public static int fromPrice() {
        int best = Integer.MAX_VALUE;
        for (int t = 0; t < TICKET_TYPES.length; t++) {
            if (TICKET_REMAINING[t] >= TICKET_MIN_QTY[t]) best = Math.min(best, TICKET_PRICES[t]);
        }
        if (best == Integer.MAX_VALUE) {
            for (int price : TICKET_PRICES) best = Math.min(best, price);
        }
        return best;
    }

    public static String money(int amount) {
        return "₱" + String.format(Locale.US, "%,d", amount);
    }

    public static String categoryEmoji(String category) {
        return CATEGORY_EMOJI[Math.abs(category.hashCode()) % CATEGORY_EMOJI.length];
    }

    public static int categoryColor(View view, String category) {
        return MaterialColors.getColor(view, CATEGORY_BG[Math.abs(category.hashCode()) % CATEGORY_BG.length]);
    }

    public static int categoryOnColor(View view, String category) {
        return MaterialColors.getColor(view, CATEGORY_ON[Math.abs(category.hashCode()) % CATEGORY_ON.length]);
    }

    public static int eventIndex(String name) {
        for (int i = 0; i < EVENT_NAMES.length; i++) {
            if (name.equals(EVENT_NAMES[i])) return i;
        }
        return 0;
    }

    public static void styleTimer(TextView tv, long msLeft) {
        boolean urgent = msLeft <= 15000;
        tv.setVisibility(View.VISIBLE);
        tv.setText("Seats held for " + (msLeft / 1000) + "s");
        tv.setBackgroundTintList(ColorStateList.valueOf(MaterialColors.getColor(tv, urgent
                ? com.google.android.material.R.attr.colorErrorContainer
                : com.google.android.material.R.attr.colorSecondaryContainer)));
        tv.setTextColor(MaterialColors.getColor(tv, urgent
                ? com.google.android.material.R.attr.colorOnErrorContainer
                : com.google.android.material.R.attr.colorOnSecondaryContainer));
    }

    public static void bindEvent(ItemRowBinding row, int e) {
        row.tvIcon.setVisibility(View.VISIBLE);
        row.tvIcon.setText(categoryEmoji(EVENT_CATEGORIES[e]));
        row.tvIcon.setBackgroundTintList(ColorStateList.valueOf(categoryColor(row.getRoot(), EVENT_CATEGORIES[e])));
        row.chipBadge.setTextColor(MaterialColors.getColor(row.getRoot(), androidx.appcompat.R.attr.colorPrimary));
        row.tvTitle.setText(EVENT_NAMES[e]);
        row.tvSubtitle.setText(EVENT_DATES[e] + "\n" + EVENT_LOCATIONS[e] + " | " + EVENT_VENUES[e]);
        row.chipBadge.setVisibility(View.VISIBLE);
        row.chipBadge.setText("From " + money(fromPrice()));
    }

    public static final String DEMO_CUSTOMER_EMAIL = "customer@demo.com";
    public static final String DEMO_ORGANIZER_EMAIL = "organizer@demo.com";
    public static final String DEMO_PASSWORD = "demo1234";
    static boolean demoSeeded = false;

    public static void seedDemoPurchase() {
        if (demoSeeded) return;
        demoSeeded = true;

        String orderId = "TKT-" + (1001 + ORDERS.size());
        String time = "Oct 10, 2026 2:15 PM";
        int event = 0;
        int type = 1;
        int qty = 2;
        int total = TICKET_PRICES[type] * qty;
        int[] seats = {3, 4};

        TICKET_REMAINING[type] -= qty;
        for (int s : seats) {
            SEAT_TAKEN[event][0][s] = true;
            TICKETS.add(orderId + "|" + EVENT_NAMES[event] + "|" + EVENT_DATES[event] + "|"
                    + TICKET_TYPES[type] + "|" + BOX_NAMES[0] + "|" + (s + 1));
        }
        ORDERS.add(orderId + " - " + EVENT_NAMES[event] + " - " + TICKET_TYPES[type] + " x" + qty + " - " + money(total));
        ORDER_TYPES.add(type);
        ORDER_QTYS.add(qty);
        ORDER_TOTALS.add(total);
        ORDER_TIMES.add(time);
        RECEIPTS.add("RECEIPT " + orderId
                + "\nDate: " + time
                + "\n\nEvent: " + EVENT_NAMES[event]
                + "\nWhen: " + EVENT_DATES[event]
                + "\nTicket: " + TICKET_TYPES[type] + " x" + qty
                + "\nSeats: " + BOX_NAMES[0] + " seat 4, 5"
                + "\n\nBilled to: Demo Customer"
                + "\nEmail: " + DEMO_CUSTOMER_EMAIL
                + "\nAddress: 123 Rizal Street, Manila"
                + "\nPaid with: GCash 0917 *** 4567"
                + "\n\nTOTAL: " + money(total));

        addNotification("Booking confirmed: " + EVENT_NAMES[event] + " (" + orderId + ")");
        addNotification("Payment received: " + money(total) + " via GCash 0917 *** 4567");
        addNotification("Tickets delivered to " + DEMO_CUSTOMER_EMAIL);
        addNotification("Reminder: " + EVENT_NAMES[event] + " starts soon. Bring your QR ticket!");
    }
}
