package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserService {

    public static final String API_USER_BASE_URL = "http://localhost:8080/user/";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken;

    public void setAuthToken(String authToken) {this.authToken = authToken;}

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    public User getUser (int id) {
        User user = null;
        try {
            ResponseEntity<User> response =
                    restTemplate.exchange(API_USER_BASE_URL + id, HttpMethod.GET, makeAuthEntity(),
                            User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }

    public String getUsernameForList (int id) {
        User user = new User();
        try {
            ResponseEntity<User> response =
                    restTemplate.exchange(API_USER_BASE_URL + id, HttpMethod.GET, makeAuthEntity(),
                            User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user.getUsername();
    }

    public User[] getUsersList() {
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_USER_BASE_URL, HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

    public void printUsers(int currentUserId) {
        User[] users = getUsersList();
        System.out.println("------------------------------------------");
        System.out.println("Users");
        System.out.printf("%-8s  %-4s ", "ID", "Name"); System.out.println();
        System.out.println("------------------------------------------");
        for (User user : users) {
            if (user.getId() != currentUserId) {
                System.out.printf("%-8d  %-10s ", user.getId(), StringUtils.capitalize(user.getUsername())); System.out.println();
            }
//            System.out.printf("%-8d  %-10s ", user.getId(), StringUtils.capitalize(user.getUsername())); System.out.println();
        }
        System.out.println("------------------------------------------");
    }

}
