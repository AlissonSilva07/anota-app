package edu.alisson.anota.data.mappers

import edu.alisson.anota.data.local.user.UserEntity
import edu.alisson.anota.domain.model.User

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        uid = uid.toString(),
        name = name.toString(),
        email = email.toString(),
        profileImageBase64 = profileImage
    )
}

fun UserEntity.toUser(): User {
    return User(
        uid = uid,
        name = name,
        email = email,
        profileImage = profileImageBase64
    )
}