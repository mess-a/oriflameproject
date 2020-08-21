package com.example.android_oriflame;

import com.google.firebase.auth.FirebaseAuth;

public class Helper {
    private static FirebaseAuth fAuth = FirebaseAuth.getInstance();

    public static boolean isAdmin() {
        String[] admins = { "alalfakawma@gmail.com", "malsawmatlau4@gmail.com" };

        String email = fAuth.getCurrentUser().getEmail();

        for (String admin : admins) {
            if (admin.equals(email)) {
                return true;
            }
        }

        return false;
    }
}
