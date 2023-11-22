package com.gaffaryucel.newsgo.model

class UserProfile{
    var userId: String? = null
    var username: String? = null
    var email: String? = null
    var fullName: String? = null
    var profileImageUrl : String? = null
    constructor()
    constructor(
        userId: String? = null,
        username: String? = null,
        email: String? = null,
        fullName: String? = null,
        profileImageUrl : String? = null
    ){
        this.userId = userId
        this.username = username
        this.email = email
        this.fullName = fullName
        this.profileImageUrl  = profileImageUrl
    }
}