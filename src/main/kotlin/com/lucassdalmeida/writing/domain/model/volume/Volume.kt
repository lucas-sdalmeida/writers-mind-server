package com.lucassdalmeida.writing.domain.model.volume

import com.lucassdalmeida.writing.shared.Entity

class Volume(id: VolumeId, val title: String, val summary: String?) : Entity<VolumeId>(id)
