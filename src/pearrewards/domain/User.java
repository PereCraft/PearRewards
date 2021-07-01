/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pearrewards.domain;

//import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author antonio
 */
public class User {
    String username;
    //LocalDate date;
    Date date;
    int numRewards;
    int reedemRewards;
    
    public User(String username, Date date, int numRewards, int reedemRewards) {
        this.username = username;
        this.date = date;
        this.numRewards = numRewards;
        this.reedemRewards = reedemRewards;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNumRewards() {
        return numRewards;
    }

    public void setNumRewards(int numRewards) {
        this.numRewards = numRewards;
    }

    public int getReedemRewards() {
        return reedemRewards;
    }

    public void setReedemRewards(int reedemRewards) {
        this.reedemRewards = reedemRewards;
    }
}
