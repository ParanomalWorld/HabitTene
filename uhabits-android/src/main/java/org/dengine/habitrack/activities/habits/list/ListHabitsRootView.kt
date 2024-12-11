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

package org.dengine.habitrack.activities.habits.list

import android.content.Context
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.RelativeLayout
import nl.dionsegijn.konfetti.xml.KonfettiView
import org.dengine.habitrack.R
import org.dengine.habitrack.activities.common.views.ScrollableChart
import org.dengine.habitrack.activities.common.views.TaskProgressBar
import org.dengine.habitrack.activities.habits.list.views.EmptyListView
import org.dengine.habitrack.activities.habits.list.views.HabitCardListAdapter
import org.dengine.habitrack.activities.habits.list.views.HabitCardListView
import org.dengine.habitrack.activities.habits.list.views.HabitCardListViewFactory
import org.dengine.habitrack.activities.habits.list.views.HeaderView
import org.dengine.habitrack.activities.habits.list.views.HintView
import org.dengine.habitrack.core.models.ModelObservable
import org.dengine.habitrack.core.models.PaletteColor
import org.dengine.habitrack.core.preferences.Preferences
import org.dengine.habitrack.core.tasks.TaskRunner
import org.dengine.habitrack.core.ui.screens.habits.list.HintListFactory
import org.dengine.habitrack.core.utils.MidnightTimer
import org.dengine.habitrack.inject.ActivityContext
import org.dengine.habitrack.inject.ActivityScope
import org.dengine.habitrack.utils.addAtBottom
import org.dengine.habitrack.utils.addAtTop
import org.dengine.habitrack.utils.addBelow
import org.dengine.habitrack.utils.buildToolbar
import org.dengine.habitrack.utils.currentTheme
import org.dengine.habitrack.utils.dim
import org.dengine.habitrack.utils.dp
import org.dengine.habitrack.utils.setupToolbar
import org.dengine.habitrack.utils.sres
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

const val MAX_CHECKMARK_COUNT = 60

@ActivityScope
class ListHabitsRootView @Inject constructor(
    @ActivityContext context: Context,
    hintListFactory: HintListFactory,
    preferences: Preferences,
    midnightTimer: MidnightTimer,
    runner: TaskRunner,
    private val listAdapter: HabitCardListAdapter,
    habitCardListViewFactory: HabitCardListViewFactory
) : FrameLayout(context), ModelObservable.Listener {

    val listView: HabitCardListView = habitCardListViewFactory.create()
    val llEmpty = EmptyListView(context)
    val tbar = buildToolbar()
    val konfettiView = KonfettiView(context).apply {
        translationZ = 10f
    }
    val progressBar = TaskProgressBar(context, runner)
    val hintView: HintView
    val header = HeaderView(context, preferences, midnightTimer)

    init {
        val hints = resources.getStringArray(R.array.hints)
        val hintList = hintListFactory.create(hints)
        hintView = HintView(context, hintList)

        val rootView = RelativeLayout(context).apply {
            background = sres.getDrawable(R.attr.windowBackgroundColor)
            addAtTop(konfettiView)
            addAtTop(tbar)
            addBelow(header, tbar)
            addBelow(listView, header, height = MATCH_PARENT)
            addBelow(llEmpty, header, height = MATCH_PARENT)
            addBelow(progressBar, header) {
                it.topMargin = dp(-6.0f).toInt()
            }
            addAtBottom(hintView)
        }
        rootView.setupToolbar(
            toolbar = tbar,
            title = resources.getString(R.string.main_activity_title),
            color = PaletteColor(17),
            displayHomeAsUpEnabled = false,
            theme = currentTheme()
        )
        addView(rootView, MATCH_PARENT, MATCH_PARENT)
        listAdapter.setListView(listView)
    }

    override fun onModelChange() {
        updateEmptyView()
    }

    private fun setupControllers() {
        header.setScrollController(
            object : ScrollableChart.ScrollController {
                override fun onDataOffsetChanged(newDataOffset: Int) {
                    listView.dataOffset = newDataOffset
                }
            }
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupControllers()
        listAdapter.observable.addListener(this)
    }

    override fun onDetachedFromWindow() {
        listAdapter.observable.removeListener(this)
        super.onDetachedFromWindow()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val count = getCheckmarkCount()
        header.buttonCount = count
        header.setMaxDataOffset(max(MAX_CHECKMARK_COUNT - count, 0))
        listView.checkmarkCount = count
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun getCheckmarkCount(): Int {
        val nameWidth = dim(R.dimen.habitNameWidth)
        val buttonWidth = dim(R.dimen.checkmarkWidth)
        val labelWidth = max((measuredWidth / 3).toFloat(), nameWidth)
        val buttonCount = ((measuredWidth - labelWidth) / buttonWidth).toInt()
        return min(MAX_CHECKMARK_COUNT, max(0, buttonCount))
    }

    private fun updateEmptyView() {
        if (listAdapter.itemCount == 0) {
            if (listAdapter.hasNoHabit()) {
                llEmpty.showEmpty()
            } else {
                llEmpty.showDone()
            }
        } else {
            llEmpty.hide()
        }
    }
}
