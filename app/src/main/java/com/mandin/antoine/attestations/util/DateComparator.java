package com.mandin.antoine.attestations.util;

import java.util.Calendar;
import java.util.Date;

public class DateComparator {
    private DateValues firstDate, lastDate;
    private boolean positive;

    public DateComparator() {
    }

    /**
     * Set the dates to compare
     *
     *  true if the referenceDate if before the comparingDate, false otherwise,
     */
    public void setDates(Date referenceDate, Date comparingDate) {
        if (referenceDate.before(comparingDate)) {
            firstDate = new DateValues(referenceDate);
            lastDate = new DateValues(comparingDate);
            positive = true;
        } else {
            firstDate = new DateValues(comparingDate);
            lastDate = new DateValues(referenceDate);
            positive = false;
        }
    }

    public boolean isSameYear() {
        return firstDate.year == lastDate.year;
    }

    public boolean isSameMonth() {
        return isSameYear() && firstDate.month == lastDate.month;
    }

    public boolean isSameDay() {
        return isSameYear() && firstDate.dayOfYear == lastDate.dayOfYear;
    }

    public int dayDifference() {
        if (isSameDay())
            return 0;
        if (isSameYear())
            return lastDate.dayOfYear - firstDate.dayOfYear;

        int result = (int) (lastDate.timeInMinutes / (60L * 24L)) - (int) (firstDate.timeInMinutes / (60L * 24L));
        return Math.max(result, 1);
    }

    public int hourDifference() {
        if (isSameDay())
            return lastDate.hourOfDay - firstDate.hourOfDay;

        return (int) (((lastDate.timeInMinutes / 60L) - (firstDate.timeInMinutes / 60L)) % 24L);
    }

    public int minuteDifference() {
        return (int) ((lastDate.timeInMinutes - firstDate.timeInMinutes) % 60L);
    }

    public static String towDigit(int value){
        return (value < 10 ? "0"+value : ""+value);
    }

    public String shortDifferenceFR() {
        int dayDifference = dayDifference();
        int hourDifference = hourDifference();
        int minuteDifference = minuteDifference();
        String result = null;
        if (positive) { // in the future
            if (dayDifference > 1) {
                // several days of difference
                result = "Dans " + dayDifference + " jours.";
            } else {
                if (dayDifference == 0) { //Today
                    boolean dansAdded = false;
                    if (hourDifference > 0) { // In more that one hour
                        result = "Dans " + hourDifference + "h";
                        dansAdded = true;
                    }
                    if (minuteDifference > 0) {
                        if (dansAdded) // In between [1min ; 59min]
                            result += towDigit(minuteDifference) + "min.";
                        else
                            result = "Dans "+minuteDifference + "min.";
                        dansAdded = true;
                    }
                    if(!dansAdded)//No difference : now
                        result = "Maintenant.";

                } else if (dayDifference == 1) { //Tomorrow
                    result = "Demain à "+towDigit(lastDate.hourOfDay)+"h"+towDigit(lastDate.minute);
                }
            }
        } else { // In the past
            if (dayDifference > 1) { // Several days
                result = "Il y a " + dayDifference + " jours.";
            } else {
                if (dayDifference == 0) { // Today
                    boolean ilyaAdded = false;
                    if(hourDifference > 0){ // More than one hour
                        result = "Il y a "+hourDifference+"h";
                        ilyaAdded = true;
                    }
                    if(minuteDifference > 0){ // more than one minute
                        if(ilyaAdded)
                            result += towDigit(minuteDifference)+"min.";
                        else
                            result = "Il y a "+minuteDifference+"min.";
                        ilyaAdded = true;
                    }
                    if(!ilyaAdded) // No difference
                        result = "Maintenant.";
                } else if (dayDifference == 1) { // Yesterday
                    result =  "Hier à "+towDigit(firstDate.hourOfDay)+"h"+towDigit(firstDate.minute);
                }
            }
        }
        return result;
    }

    private static class DateValues {
        public final int year,
                month,
                dayOfMonth,
                dayOfYear,
                hourOfDay,
                minute,
                second;
        public final long timeInMinutes;

        public DateValues(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            second = calendar.get(Calendar.SECOND);
            timeInMinutes = date.getTime() / (60L * 1000L);
        }
    }

}
