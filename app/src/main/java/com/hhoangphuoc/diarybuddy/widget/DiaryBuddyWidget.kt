package com.hhoangphuoc.diarybuddy.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import com.hhoangphuoc.diarybuddy.R


/**
 * TO CREATE THIS WIDGET, BEFORE CREATE THIS WIDGET CLASS, REMEMBER TO ADD SOME CONFIGURATION IN
 * 1. THE MANIFEST FILE: Sign up the widget receiver and widget activity in the manifest file: <receiver> and <activity>
 * 2. XML FILE ( <appwidget-provider> )
 * 3. AND THE RECEIVER CLASS: DiaryBuddyWidgetReceiver
 */


/**
 * MAIN WIDGET CLASS
 * */
class DiaryBuddyWidget : GlanceAppWidget() {
//    override fun getGlanceAppWidgetView(context: Context, appWidgetId: Int): RemoteViews {
//        return RemoteViews(context.packageName, R.layout.locket_android_widget)
//    }

//    override fun getGlanceAppWidgetIntent(context: Context, appWidgetId: Int): PendingIntent {
//        val intent = Intent(context, MainActivity::class.java)
//        return PendingIntent.getActivity(context, appWidgetId, intent, 0)
//    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {

            //create a sample user post
            val userPost = UserPost(
                userName = "User Name",
                userAvatar = ImageBitmap.imageResource(R.drawable.ic_launcher_foreground),
                image = ImageBitmap.imageResource(R.drawable.ic_launcher_foreground),
                caption = "Caption for the first post"
            )
//            Text(text = "ADD THE CONTENT OF THE WIDGET")
            LocketAndroidWidgetContent(userPost)
        }
    }

    /**
     * UI COMPONENTS OF THE WIDGET
     * */
    @Composable
    private fun LocketAndroidWidgetContent(userPost: UserPost? = null) {
        // Create the UI for the AppWidget.
        // Use Compose functions to create the UI.

        // create a compose widget ui with the image cover full screen with small icon and text in the row under the image
        Box(
            modifier = GlanceModifier.fillMaxSize()
        ){
            //posted image
            Image(
                provider = ImageProvider(R.drawable.ic_launcher_background), //TODO: add the provider for the image
                contentDescription = null,
                modifier = GlanceModifier.fillMaxSize()
            )
            Row(
//                //row in the bottom of the widget
                modifier = GlanceModifier.padding(end = 16.dp, bottom = 16.dp)
            ){
                Image(
                    provider = ImageProvider(R.drawable.ic_launcher_background), //TODO: add the provider for the image
                    contentDescription = null,
                    modifier = GlanceModifier.fillMaxSize()
                )
                Spacer(modifier = GlanceModifier.width(8.dp))
                //text
                Text("SHOULD BE USER CAPTION")
            }
        }
    }
}


/**
 * DATA CLASS FOR THE USER POST
 * */

data class UserPost(
    val userName: String,
    val userAvatar: ImageBitmap, // or any other type that you use for images
    val image: ImageBitmap, // or any other type that you use for images
    val caption: String
)

/**
 * ACTIONS FOR THE WIDGET
 * */

class WidgetAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        TODO("SOME ACTIONS EXECUTE IN THE WIDGET")
//        LocketAndroidWidget().update(context, glanceId) {
//            //update the widget
//        }
    }
}