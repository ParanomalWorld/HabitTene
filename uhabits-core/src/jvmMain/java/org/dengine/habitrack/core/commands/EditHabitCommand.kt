/*
 * Copyright (C) 2016-2021 Álinson Santos Xavier <git@axavier.org>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dengine.habitrack.core.commands

import org.dengine.habitrack.core.models.Habit
import org.dengine.habitrack.core.models.HabitList
import org.dengine.habitrack.core.models.HabitNotFoundException

data class EditHabitCommand(
    val habitList: HabitList,
    val habitId: Long,
    val modified: Habit
) : Command {
    override fun run() {
        val habit = habitList.getById(habitId) ?: throw HabitNotFoundException()
        habit.copyFrom(modified)
        habitList.update(habit)
        habit.observable.notifyListeners()
        habit.recompute()
        habitList.resort()
    }
}
