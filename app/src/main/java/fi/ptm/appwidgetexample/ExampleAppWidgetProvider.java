package fi.ptm.appwidgetexample;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by PTM on 15/10/15.
 */
public class ExampleAppWidgetProvider extends AppWidgetProvider {
    private final String REFRESH = "fi.ptm.appwidgetexample.REFRESH";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // loop each App Widget that belongs to this provider
        for (int i=0; i<appWidgetIds.length; i++) {
            // get the layout for the App Widget
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);

            // create an intent to launch main activity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            // attach an on-click listener to the button
            views.setOnClickPendingIntent(R.id.activityButton, pendingIntent);

            // create intent to refresh a new random number to text view
            Intent refreshIntent = new Intent(REFRESH);
            PendingIntent refreshPendIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, 0);
            // attach an on-click listener to the button
            views.setOnClickPendingIntent(R.id.refreshButton, refreshPendIntent);

            // show random number
            showRandomNumber(views);

            // tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    private void showRandomNumber(RemoteViews views) {
        String number = Math.random()+"";
        number = number.substring(0,10);
        views.setTextViewText(R.id.numberTextView,"Number: " + number);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("WIDGET", ">>>ONRECEIVE");
        if (REFRESH.equals(intent.getAction())) {
            // get remote views
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);
            showRandomNumber(remoteViews);
            // update view
            ComponentName thisWidget = new ComponentName(context, ExampleAppWidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(thisWidget, remoteViews);
        }
        super.onReceive(context, intent);
    }
}
