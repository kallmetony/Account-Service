package com.aaronr92.accountservice.util;

public enum Action {
    CREATE_USER,                        // user has been successfully registered
    CHANGE_PASSWORD,                    // user has changed the password successfully
    ACCESS_DENIED,                      // user is trying to access a resource without access rights
    LOGIN_FAILED,                       // failed authentication
    GRANT_ROLE,                         // role is granted to a user
    REMOVE_ROLE,                        // role has been revoked
    LOCK_USER,                          // administrator has locked a user
    UNLOCK_USER,                        // administrator has unlocked a user
    DELETE_USER,                        // administrator has deleted a user
    BRUTE_FORCE,                        // user has been blocked on suspicion of a brute force attack
    ADD_BREACHED_PASSWORD,              // breached password was added
    REMOVE_BREACHED_PASSWORD            // breached password was deleted
}
