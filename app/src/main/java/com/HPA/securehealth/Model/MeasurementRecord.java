package com.HPA.securehealth.Model;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by Hamza on 11/29/16.
 */
public class MeasurementRecord implements Serializable {
    Date date;
    //I can assume the heart rate will fit in an integer otherwise something is probably wrong.
    int heartRate;

    //normal constructor, nothing special here
    public MeasurementRecord(Date date, int heartRate){
        this.heartRate=heartRate;
        this.date=date;
    }
    public Date getDate(){
        return this.date;
    }
    public int getHeartRate(){
        return this.heartRate;
    }


}
