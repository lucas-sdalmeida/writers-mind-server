package com.lucassdalmeida.writing.infrastructure.story.repository.postgres

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StoryPostgresRepository : JpaRepository<StoryDataModel, UUID>
