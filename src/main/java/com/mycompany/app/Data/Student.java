package com.mycompany.app.Data;

import java.io.Serializable;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/26/17.
 */
public class Student implements Serializable {
    String Frist;
    String Second;
    String Phone;
    String CityAddress;
    String CityStreet;

    public Student(String s1,String s2,String s3,String s4,String s5){
        Frist=s1;
        Second=s2;
        Phone=s3;
        CityAddress=s4;
        CityStreet=s5;

    }
}
