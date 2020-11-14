package com.song.christopher.guesstheplayer;

import android.os.Parcel;
import android.os.Parcelable;

/* Class to represent a 'Player' object
 * Does NOT include player stats */
public class Player implements Parcelable {
    // Fields related to a 'Player' object
    private int id;
    private String firstName;
    private String lastName;
    private String position;
    private int heightFeet;
    private int heightInches;
    private int weight;
    private int teamID;
    private String teamName;

    // 'Player' object constructor
    public Player(int id,
                  String firstName,
                  String lastName,
                  String position,
                  int heightFeet,
                  int heightInches,
                  int weight,
                  int teamID,
                  String teamName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.heightFeet = heightFeet;
        this.heightInches = heightInches;
        this.weight = weight;
        this.teamID = teamID;
        this.teamName = teamName;
    }

    // Getter methods for a 'Player' object
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPosition() {
        return position;
    }

    public int getHeightFeet() {
        return heightFeet;
    }

    public int getHeightInches() {
        return heightInches;
    }

    public int getWeight() {
        return weight;
    }

    public int getTeamID() {
        return teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    /* All of the methods below are used to implement the 'Parcelable' class;
     * Allows the data encapsulated in a 'Player' object to be transferred between activities */
    protected Player(Parcel in) {
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        position = in.readString();
        heightFeet = in.readInt();
        heightInches = in.readInt();
        weight = in.readInt();
        teamID = in.readInt();
        teamName = in.readString();
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(position);
        parcel.writeInt(heightFeet);
        parcel.writeInt(heightInches);
        parcel.writeInt(weight);
        parcel.writeInt(teamID);
        parcel.writeString(teamName);
    }
}
