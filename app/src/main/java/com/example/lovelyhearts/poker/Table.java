package com.example.lovelyhearts.poker;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by ryan on 4/21/15.
 */
@ParseClassName("Table")
public class Table extends ParseObject {
    //--------for testing use---------
    int tableId;
    int seatNumber;
    String userName;

    public void Table(){
        //Default Constructor
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
