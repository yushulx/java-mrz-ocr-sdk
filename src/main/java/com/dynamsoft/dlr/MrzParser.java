package com.dynamsoft.dlr;

import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MrzParser {
    public static JsonObject parse(String[] lines)
    {
        JsonObject mrzInfo = new JsonObject();

        if (lines.length == 0)
        {
            return null;
        }

        if (lines.length == 2)
        {
            String line1 = lines[0];
            String line2 = lines[1];

            String  type = line1.substring(0, 1);
            Pattern pattern = Pattern.compile("[I|P|V]");
            Matcher matcher = pattern.matcher(type);
            if (!matcher.matches()) return null;

            if (type == "P")
            {
                mrzInfo.addProperty("type", "PASSPORT (TD-3)");
            }
            else if (type == "V")
            {
                if (line1.length() == 44)
                {
                    mrzInfo.addProperty("type", "VISA (MRV-A)");
                }
                else if (line1.length() == 36)
                {
                    mrzInfo.addProperty("type", "VISA (MRV-B)");
                }
            }
            else if (type == "I")
            {
                mrzInfo.addProperty("type", "ID CARD (TD-2)");
            }

            // Get issuing State infomation
            String  nation = line1.substring(2, 7);
            pattern = Pattern.compile("[0-9]");
            matcher = pattern.matcher(nation);
            if (matcher.matches()) return null;
            if (nation.charAt(nation.length() - 1) == '<') {
                nation = nation.substring(0, 2);
            }
            mrzInfo.addProperty("nationality", nation);
            // Get surname information
            line1 = line1.substring(5);
            int pos = line1.indexOf("<<");
            String  surName = line1.substring(0, pos);
            pattern = Pattern.compile("[0-9]");
            matcher = pattern.matcher(surName);
            if (matcher.matches()) return null;
            surName = surName.replace("<", " ");
            mrzInfo.addProperty("surname", surName);
            // Get givenname information
            String  givenName = line1.substring(surName.length() + 2);
            pattern = Pattern.compile("[0-9]");
            matcher = pattern.matcher(givenName);
            if (matcher.matches()) return null;
            givenName = givenName.replace("<", " ");
            givenName = givenName.trim();
            mrzInfo.addProperty("givenname", givenName);
            // Get passport number information
            String  passportNumber = "";
            passportNumber = line2.substring(0, 9);
            passportNumber = passportNumber.replace("<", " ");
            mrzInfo.addProperty("passportnumber", passportNumber);
            // Get Nationality information
            String  issueCountry = line2.substring(10, 13);
            pattern = Pattern.compile("[0-9]");
            matcher = pattern.matcher(issueCountry);
            if (matcher.matches()) return null;
            if (issueCountry.charAt(issueCountry.length() - 1) == '<') {
                issueCountry = issueCountry.substring(0, 2);
            }
            mrzInfo.addProperty("issuecountry", issueCountry);
            // Get date of birth information
            String  birth = line2.substring(13, 19);
            Date  date = new Date();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            if (Integer.parseInt(birth.substring(0, 2)) > (year % 100)) {
                birth = "19" + birth;
            } else {
                birth = "20" + birth;
            }
            birth = birth.substring(0, 4) + "-" + birth.substring(4, 6) + "-" + birth.substring(6, 8);
            pattern = Pattern.compile("[A-Za-z]");
            matcher = pattern.matcher(birth);
            if (matcher.matches()) return null;
            mrzInfo.addProperty("birth", birth);
            // Get gender information
            String  gender = line2.charAt(20) + "";
            pattern = Pattern.compile("[M|F|x|<]");
            matcher = pattern.matcher(gender);
            if (!matcher.matches()) return null;
            mrzInfo.addProperty("gender", gender);
            // Get date of expiry information
            String  expiry = line2.substring(21, 27);
            pattern = Pattern.compile("[A-Za-z]");
            matcher = pattern.matcher(expiry);
            if (matcher.matches()) return null;
            if (Integer.parseInt(expiry.substring(0, 2)) >= 60) {
                expiry = "19" + expiry;
            } else {
                expiry = "20" + expiry;
            }
            expiry = expiry.substring(0, 4) + "-" + expiry.substring(4, 6) + "-" + expiry.substring(6);
            mrzInfo.addProperty("expiry", expiry);
        }
        else if (lines.length == 3)
        {
            String line1 = lines[0];
            String line2 = lines[1];
            String line3 = lines[2];
            String  type = line1.substring(0, 1);
            Pattern pattern = Pattern.compile("[I|P|V]");
            Matcher matcher = pattern.matcher(type);
            if (!matcher.matches()) return null;
            mrzInfo.addProperty("type", "ID CARD (TD-1)");
            // Get nationality infomation
            String  nation = line2.substring(15, 18);
            pattern = Pattern.compile("[0-9]");
            matcher = pattern.matcher(nation);
            if (matcher.matches()) return null;
            nation = nation.replace("<", "");
            mrzInfo.addProperty("nationality", nation);
            // Get surname information
            int pos = line3.indexOf("<<");
            String  surName = line3.substring(0, pos);
            pattern = Pattern.compile("[0-9]");
            matcher = pattern.matcher(surName);
            if (matcher.matches()) return null;
            surName = surName.replace("<", " ");
            surName.trim();
            mrzInfo.addProperty("surname", surName);
            // Get givenname information
            String  givenName = line3.substring(surName.length() + 2);
            pattern = Pattern.compile("[0-9]");
            matcher = pattern.matcher(givenName);
            if (matcher.matches()) return null;
            givenName = givenName.replace("<", " ");
            givenName = givenName.trim();
            mrzInfo.addProperty("givenname", givenName);
            // Get passport number information
            String  passportNumber = "";
            passportNumber = line1.substring(5, 14);
            passportNumber = passportNumber.replace("<", " ");
            mrzInfo.addProperty("passportnumber", passportNumber);
            // Get issuing country or organization information
            String  issueCountry = line1.substring(2, 5);
            pattern = Pattern.compile("[0-9]");
            matcher = pattern.matcher(issueCountry);
            if (matcher.matches()) return null;
            issueCountry = issueCountry.replace("<", "");
            mrzInfo.addProperty("issuecountry", issueCountry);
            // Get date of birth information
            String  birth = line2.substring(0, 6);
            pattern = Pattern.compile("[A-Za-z]");
            matcher = pattern.matcher(birth);
            if (matcher.matches()) return null;

            Date  date = new Date();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);

            if (Integer.parseInt(birth.substring(0, 2)) > (year % 100)) {
                birth = "19" + birth;
            } else {
                birth = "20" + birth;
            }
            birth = birth.substring(0, 4) + "-" + birth.substring(4, 6) + "-" + birth.substring(6);
            mrzInfo.addProperty("birth", birth);
            // Get gender information
            String  gender = line2.charAt(7) + "";
            pattern = Pattern.compile("[M|F|x|<]");
            matcher = pattern.matcher(gender);
            if (!matcher.matches()) return null;
            gender = gender.replace("<", "X");
            mrzInfo.addProperty("gender", gender);
            // Get date of expiry information
            String  expiry = "20" + line2.substring(8, 14);
            pattern = Pattern.compile("[A-Za-z]");
            matcher = pattern.matcher(expiry);
            if (matcher.matches()) return null;
            expiry = expiry.substring(0, 4) + "-" + expiry.substring(4, 6) + "-" + expiry.substring(6);
            mrzInfo.addProperty("expiry", expiry);
        }

        return mrzInfo;
    }
}
