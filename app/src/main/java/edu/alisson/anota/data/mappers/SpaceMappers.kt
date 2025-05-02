package edu.alisson.anota.data.mappers

import edu.alisson.anota.data.dto.SpaceRequestResponse
import edu.alisson.anota.domain.model.Space
import edu.alisson.anota.presentation.utils.hexToColor

fun SpaceRequestResponse.toSpace(): Space {
    return Space(
        id = id,
        title = title,
        description = description,
        color = hexToColor(color),
        notes = notes,
        createdAt = createdAt,
        updatedAt = createdAt
    )
}