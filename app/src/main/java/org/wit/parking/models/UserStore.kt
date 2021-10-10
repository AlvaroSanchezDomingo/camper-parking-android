package org.wit.parking.models

interface UserStore {
    fun findById(id: Long) : UserModel?
    fun create(user: UserModel)
    fun update(user: UserModel)
    fun delete(user: UserModel)
    fun authenticate(user: UserModel) : Boolean
}