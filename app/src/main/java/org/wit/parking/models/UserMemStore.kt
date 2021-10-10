package org.wit.parking.models

import timber.log.Timber.i

var lastUserId = 0L

internal fun getUserId(): Long {
    return lastUserId++
}

class UserMemStore : UserStore {

    val users = ArrayList<UserModel>()

    override fun findById(id: Long):UserModel? {
        var foundUser: UserModel? = users.find { p -> p.id == id }
        if (foundUser != null) {
            return foundUser
        }
        return null
    }

    override fun create(user: UserModel) {
        user.id = getUserId()
        users.add(user)
        logAll()
    }
    override fun update(user: UserModel) {
        var foundUser: UserModel? = users.find { p -> p.id == user.id }
        if (foundUser != null) {
            foundUser.username = user.username
            foundUser.password = user.password
            logAll()
        }
    }
    override fun delete(user: UserModel) {
        var foundUser: UserModel? = users.find { p -> p.id == user.id }
        if (foundUser != null) {
            users.removeAt(users.indexOf(foundUser))
            logAll()
        }
    }
    override fun authenticate(user: UserModel) :Boolean {
        var foundUser: UserModel? = users.find { p -> p.id == user.id }
        if (foundUser != null) {
            if(foundUser.password == user.password){
                i("User: ${user.username} Authenticated")
                return true;
            }
        }
        i("User: ${user.username} Rejected")
        return false;
    }
    fun logAll() {
        users.forEach{ i("${it}") }
    }
}