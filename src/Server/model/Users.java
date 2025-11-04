/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author anh97
 */
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String status;
    private int totalPoints;
    private int totalWins;
    private int totalDraws;
    private int totalLosses;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor mặc định
    public Users() {
    }

    // Constructor đầy đủ
    public Users(int userId, String username, String password, String fullName, String status, 
                 int totalPoints, int totalWins, int totalDraws, int totalLosses, 
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.status = status;
        this.totalPoints = totalPoints;
        this.totalWins = totalWins;
        this.totalDraws = totalDraws;
        this.totalLosses = totalLosses;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor không có ID (dùng khi insert mới)
    public Users(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.status = "offline";
        this.totalPoints = 0;
        this.totalWins = 0;
        this.totalDraws = 0;
        this.totalLosses = 0;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalDraws() {
        return totalDraws;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public void setTotalDraws(int totalDraws) {
        this.totalDraws = totalDraws;
    }

    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", status='" + status + '\'' +
                ", totalPoints=" + totalPoints +
                ", totalWins=" + totalWins +
                ", totalDraws=" + totalDraws +
                ", totalLosses=" + totalLosses +
                '}';
    }
}
