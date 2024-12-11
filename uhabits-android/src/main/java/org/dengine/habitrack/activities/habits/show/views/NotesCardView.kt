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

package org.dengine.habitrack.activities.habits.show.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import org.dengine.habitrack.core.ui.screens.habits.show.views.NotesCardState
import org.dengine.habitrack.databinding.ShowHabitNotesBinding

class NotesCardView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val binding = ShowHabitNotesBinding.inflate(LayoutInflater.from(context), this)
    fun setState(state: NotesCardState) {
        if (state.description.isEmpty()) {
            visibility = GONE
        } else {
            visibility = VISIBLE
            binding.habitNotes.text = state.description
        }
        invalidate()
    }
}
