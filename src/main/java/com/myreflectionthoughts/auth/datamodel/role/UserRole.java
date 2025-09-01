package com.myreflectionthoughts.auth.datamodel.role;

public enum UserRole {

    USER("a user registering on the website"),
    ADMIN("a user promoted by Master"),
    MASTER("comes pre-configured");


    UserRole(String roleDescription){
        this.roleDescription = roleDescription;
    }

    private String roleDescription;


    @Override
    public String toString(){
        return this.name()+":- "+this.roleDescription;
    }
}
