package miaoyipu.broadcastreceiverpractice;

public class MUtili {
    public static final String SAVE_FILE = "MYP_Save";

    // Pending intent broadcast request code
    public static final int BR_CODE = 415411;
    public static final int NOT_ID = 3213;
    public static final String NOT_CHANNEL = "NOTI CHANNEL MYP";
    // Default filter start time
    public static final int TIME_SETTING_DEFAULT = 90;

    /* ACTIONS */
    // Snooze the screen filter for a short period of time
    public static final String ACTION_SNOOZE_FILTER = "filter snooze please";
    // Stop the screen filter
    public static final String ACTION_STOP_FILTER = "stop the filter please";
    // Start the filter
    public static final String ACTION_MYP_ALARM = "time is up please";
}
