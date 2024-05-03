package com.hhoangphuoc.diarybuddy.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class DiaryBuddyWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DiaryBuddyWidget() //type: a Widget

    //the receiver will trigger the actions intent of the widget
    //this is the class that will be called when the widget is clicked

    //create the widget



}