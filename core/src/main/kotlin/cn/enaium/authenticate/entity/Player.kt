/*
 * Copyright 2025 Enaium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.enaium.authenticate.entity

import cn.enaium.authenticate.entity.common.BaseEntity
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.Table
import java.time.LocalDateTime
import java.util.*

/**
 * @author Enaium
 */
@Entity
@Table(name = "player_v0")
interface Player : BaseEntity {
    @Key
    val uuid: UUID

    val password: String

    val banned: LocalDateTime?
}