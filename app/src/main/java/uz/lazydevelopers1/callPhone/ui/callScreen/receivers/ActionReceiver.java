package uz.lazydevelopers1.callPhone.ui.callScreen.receivers;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;

import uz.lazydevelopers1.callPhone.R;
import uz.lazydevelopers1.callPhone.ui.callScreen.CallActivity;
import uz.lazydevelopers1.callPhone.ui.callScreen.helpers.CallListHelper;
import uz.lazydevelopers1.callPhone.ui.callScreen.helpers.CallManager;
import uz.lazydevelopers1.callPhone.ui.callScreen.helpers.NotificationHelper;

public class ActionReceiver extends BroadcastReceiver {

    @SuppressLint({"UseCompatTextViewDrawableApis", "SetTextI18n"})
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("MADARA", "onReceive222: " + CallListHelper.callList.size()); // Debugging

        Bundle intentExtras = intent.getExtras();

        for (String k: intentExtras.keySet()) {
            Log.d("MADARA", "onReceive333 intent: " + k); // Debugging
        }

        if (intentExtras.containsKey("pickUpCall")){

            Log.d("MADARA", "onReceive333: "); // Debugging

            String action = intent.getStringExtra("pickUpCall");

            Log.d("MADARA", "onReceive444: " + action); // Debugging

            if(action.equalsIgnoreCase("YES")){
                CallManager.answerCall(CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
            }
            else if(action.equalsIgnoreCase("NO")){
                CallManager.hangUpCall(CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
            }
        }

        if (intentExtras.containsKey("cancelCall")){

            String action = intent.getStringExtra("cancelCall");

            if(action.equalsIgnoreCase("YES"))
                CallManager.hangUpCall(CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
        }

        if (intentExtras.containsKey("endCall")){

            String action = intent.getStringExtra("endCall");

            if(action.equalsIgnoreCase("YES"))
                CallManager.hangUpCall(CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
        }


        if (intentExtras.containsKey("speakerCall")){

            String action = intent.getStringExtra("speakerCall");

            String muteBtnName;

            if (CallActivity.isMuted){
                muteBtnName = "Unmute";
            }
            else {
                muteBtnName = "Mute";
            }

            if(action.equalsIgnoreCase("YES")){

                if (CallActivity.isSpeakerOn){
                    CallManager.speakerCall(false);
                    CallActivity.speakerBtn.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.my_theme)));
                    CallActivity.speakerBtn.setTextColor(context.getColor(R.color.my_theme));
                    CallActivity.isSpeakerOn = false;

                    NotificationHelper.createIngoingCallNotification(context, CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1), "01:12:00", "Speaker On", muteBtnName);
                }
                else{
                    CallManager.speakerCall(true);
                    CallActivity.speakerBtn.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.feature_on_color)));
                    CallActivity.speakerBtn.setTextColor(context.getColor(R.color.feature_on_color));
                    CallActivity.isSpeakerOn = true;

                    NotificationHelper.createIngoingCallNotification(context, CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1), "01:12:00", "Speaker Off", muteBtnName);
                }
            }
        }


        if (intentExtras.containsKey("muteCall")){

            String action = intent.getStringExtra("muteCall");

            String speakerBtnName;

            if (CallActivity.isSpeakerOn){
                speakerBtnName = "Speaker Off";
            }
            else {
                speakerBtnName = "Speaker On";
            }

            if(action.equalsIgnoreCase("YES")){

                if (CallActivity.isMuted){
                    CallManager.muteCall(false);
                    CallActivity.muteBtn.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.my_theme)));
                    CallActivity.muteBtn.setTextColor(context.getColor(R.color.my_theme));
                    CallActivity.muteBtn.setText("Mute");
                    CallActivity.isMuted = false;

                    NotificationHelper.createIngoingCallNotification(context, CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1), "01:12:00", speakerBtnName, "Mute");
                }
                else{
                    CallManager.muteCall(true);
                    CallActivity.muteBtn.setCompoundDrawableTintList(ColorStateList.valueOf(context.getColor(R.color.feature_on_color)));
                    CallActivity.muteBtn.setTextColor(context.getColor(R.color.feature_on_color));
                    CallActivity.muteBtn.setText("Unmute");
                    CallActivity.isMuted = true;

                    NotificationHelper.createIngoingCallNotification(context, CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1), "01:12:00", speakerBtnName, "Unmute");
                }
            }
        }
    }
}
