package edu.alisson.anota.data

import androidx.compose.ui.graphics.Color
import edu.alisson.anota.domain.model.Note
import edu.alisson.anota.domain.model.Space

object Mocks {
    val spaces = listOf(
        Space(
            id = "1",
            title = "Dias e dias",
            color = Color(0xFFFF7676),
            description = "Meu espaço pessoal",
            notes = listOf(
                Note(
                    id = "1",
                    title = "Meu dia de hoje",
                    content = "Hoje meu dia foi topzera demais",
                    spaceID = "1",
                    spaceTitle = "Space 1",
                    createdAt = "2025-04-30T10:00:00-03:00",
                    updatedAt = "2025-04-30T10:00:00-03:00"
                ),
                Note(
                    id = "2",
                    title = "Coisas para fazer",
                    content = "Estudar, limpar a casa, fazer compras",
                    spaceID = "1",
                    spaceTitle = "Space 1",
                    createdAt = "2025-04-29T14:30:00-03:00",
                    updatedAt = "2025-04-29T14:30:00-03:00"
                ),
                Note(
                    id = "3",
                    title = "Reflexões",
                    content = "Hoje pensei sobre a importância de descansar",
                    spaceID = "1",
                    spaceTitle = "Space 1",
                    createdAt = "2025-04-28T08:15:00-03:00",
                    updatedAt = "2025-04-28T08:15:00-03:00"
                )
            ),
            createdAt = "2025-04-27T11:45:00-03:00",
            updatedAt = "2025-04-30T10:00:00-03:00"
        ),
        Space(
            id = "2",
            title = "Coisas que quero fazer",
            color = Color(0xFF29B21C),
            description = "xD",
            notes = listOf(
                Note(
                    id = "4",
                    title = "Receitas",
                    content = "Torta de frango, bolo de cenoura, lasanha",
                    spaceID = "2",
                    spaceTitle = "Space 2",
                    createdAt = "2025-04-30T09:00:00-03:00",
                    updatedAt = "2025-04-30T09:00:00-03:00"
                ),
                Note(
                    id = "5",
                    title = "Planos de viagem",
                    content = "Visitar o sul do Brasil no inverno",
                    spaceID = "2",
                    spaceTitle = "Space 2",
                    createdAt = "2025-04-29T17:45:00-03:00",
                    updatedAt = "2025-04-29T17:45:00-03:00"
                ),
                Note(
                    id = "6",
                    title = "Livros para ler",
                    content = "1984, Sapiens, O Conto da Aia",
                    spaceID = "2",
                    spaceTitle = "Space 2",
                    createdAt = "2025-04-28T13:00:00-03:00",
                    updatedAt = "2025-04-28T13:00:00-03:00"
                )
            ),
            createdAt = "2025-04-27T09:20:00-03:00",
            updatedAt = "2025-04-30T09:00:00-03:00"
        )
    )
}