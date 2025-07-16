package de.ok.conterra.weatherapp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptDemo {
    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // encode raw password
        String hash = encoder.encode("{placeholder}");

        System.out.println("Bcrypt-Hash for '{placeholder}':");
        System.out.println(hash);
    }
}
