package org.example.utils;

import org.example.exceptions.EntityException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.PatternSyntaxException;

public class ReadingTypeUtils {
    public static Date readingDate(String meaning){
        try{
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date s = dateFormat.parse(meaning);
            return s;
        }catch(ParseException e){
            throw new EntityException("Invalid date format.Use yyyy-MM-dd HH:mm format.");
        }
    }

    public static String writingDate(Date date){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String s = dateFormat.format(date);
        return s;
    }

    public static int readingInt(String meaning){
        try{
            int value=Integer.parseInt(meaning);
            return value;
        }catch(NumberFormatException e){
            throw new EntityException("Invalid integer format.Use UINT32 format.");
        }
    }

    public static String readingString(String meaning){
        try{
            if (meaning.matches("^[^0-9]*$")) {
                return meaning;
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new EntityException("Invalid string format.Please enter a string");
        }
    }

    public static String[] readingStringArray(String meaning){
        try{
            String[] tokens = meaning.split(";");
            return tokens;
        }catch(PatternSyntaxException e){
            throw new EntityException("The parse couldn't be done.");
        }
    }

    public static Date readingDateDocument(String meaning){
        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(meaning, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDate = offsetDateTime.format(formatter);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date s = dateFormat.parse(formattedDate);
            return s;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
