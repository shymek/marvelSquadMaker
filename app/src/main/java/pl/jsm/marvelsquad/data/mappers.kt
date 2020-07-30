package pl.jsm.marvelsquad.data

import pl.jsm.marvelsquad.net.CharacterDtoModel
import pl.jsm.marvelsquad.net.ComicDtoModel

fun CharacterDtoModel.toEntity() = Character(id, name, description, thumbnail)

fun ComicDtoModel.toEntity() = Comic(id, title, thumbnail, onSaleDate)
