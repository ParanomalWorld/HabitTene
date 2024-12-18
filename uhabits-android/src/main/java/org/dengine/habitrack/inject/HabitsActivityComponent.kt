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

package org.dengine.habitrack.inject

import dagger.Component
import org.dengine.habitrack.activities.common.dialogs.ColorPickerDialogFactory
import org.dengine.habitrack.activities.habits.list.ListHabitsMenu
import org.dengine.habitrack.activities.habits.list.ListHabitsModule
import org.dengine.habitrack.activities.habits.list.ListHabitsRootView
import org.dengine.habitrack.activities.habits.list.ListHabitsScreen
import org.dengine.habitrack.activities.habits.list.ListHabitsSelectionMenu
import org.dengine.habitrack.activities.habits.list.views.HabitCardListAdapter
import org.dengine.habitrack.core.ui.ThemeSwitcher
import org.dengine.habitrack.core.ui.screens.habits.list.ListHabitsBehavior

@ActivityScope
@Component(
    modules = [ActivityContextModule::class, HabitsActivityModule::class, ListHabitsModule::class, HabitModule::class],
    dependencies = [HabitsApplicationComponent::class]
)
interface HabitsActivityComponent {
    val colorPickerDialogFactory: ColorPickerDialogFactory
    val habitCardListAdapter: HabitCardListAdapter
    val listHabitsBehavior: ListHabitsBehavior
    val listHabitsMenu: ListHabitsMenu
    val listHabitsRootView: ListHabitsRootView
    val listHabitsScreen: ListHabitsScreen
    val listHabitsSelectionMenu: ListHabitsSelectionMenu
    val themeSwitcher: ThemeSwitcher
}
