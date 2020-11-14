package com.song.christopher.guesstheplayer;

import android.os.Parcel;
import android.os.Parcelable;

/* Same as the 'Player' class,
 * but also encapsulates a player's statistics */
public class PlayerStats implements Parcelable {
    // Fields associated with a 'PlayerStats' object
    private int id;
    private String firstName;
    private String lastName;
    private String position;
    private int heightFeet;
    private int heightInches;
    private int weight;
    private int teamID;
    private String teamName;
    private double points;
    private double assists;
    private double rebounds;

    // 'PlayerStats' object constructor
    public PlayerStats(int id,
                  String firstName,
                  String lastName,
                  String position,
                  int heightFeet,
                  int heightInches,
                  int weight,
                  int teamID,
                  String teamName,
                  double points,
                  double assists,
                  double rebounds) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.heightFeet = heightFeet;
        this.heightInches = heightInches;
        this.weight = weight;
        this.teamID = teamID;
        this.teamName = teamName;
        this.points = points;
        this.assists = assists;
        this.rebounds = rebounds;
    }

    // Getter methods for a 'PlayerStats' object
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

    public double getPoints() {
        return points;
    }

    public double getAssists() {
        return assists;
    }

    public double getRebounds() {
        return rebounds;
    }

    /* All of the methods below are used to implement the 'Parcelable' class;
     * Allows the data encapsulated in a 'PlayerStats' object to be transferred between activities */
    protected PlayerStats(Parcel in) {
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        position = in.readString();
        heightFeet = in.readInt();
        heightInches = in.readInt();
        weight = in.readInt();
        teamID = in.readInt();
        teamName = in.readString();
        points = in.readDouble();
        assists = in.readDouble();
        rebounds = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(position);
        dest.writeInt(heightFeet);
        dest.writeInt(heightInches);
        dest.writeInt(weight);
        dest.writeInt(teamID);
        dest.writeString(teamName);
        dest.writeDouble(points);
        dest.writeDouble(assists);
        dest.writeDouble(rebounds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlayerStats> CREATOR = new Creator<PlayerStats>() {
        @Override
        public PlayerStats createFromParcel(Parcel in) {
            return new PlayerStats(in);
        }

        @Override
        public PlayerStats[] newArray(int size) {
            return new PlayerStats[size];
        }
    };
}
