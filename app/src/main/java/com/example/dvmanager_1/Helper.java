package com.example.dvmanager_1;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.dvmanager_1.Constants.KEY_device_name;
import static com.example.dvmanager_1.Constants.KEY_institution_path;
import static com.example.dvmanager_1.Constants.backstack_TAG_Alert_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Analysis_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Home_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Login_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Manage_Devices_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_My_Ids_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_My_Profile_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Register_Device_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Scan_Add_Details_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Scan_Add_Details_Other_Id_Proof_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Scan_Add_Details_Profile_Picture_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Scan_Add_Details_Registration_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Scan_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Search_FRAGMENT;
import static com.example.dvmanager_1.Constants.backstack_TAG_Search_Result_FRAGMENT;

public class Helper {

    public static String DateToLongString(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Long dateInMills = calendar.getTimeInMillis();
        return String.valueOf(dateInMills);
    }

    public static String convert_Date_To(String convert_to, Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (convert_to) {
            case "DAY":
                return DateFormat.format("EEEE", date).toString();
            case "YEAR":
                return String.valueOf(calendar.get(Calendar.YEAR));
            case "MONTH":
                return String.valueOf(calendar.get(Calendar.MONTH));
            case "DATE":
                return String.valueOf(calendar.get(Calendar.DATE));
            case "HOUR_OF_DAY":
                return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));

        }
        return null;
    }

    public static String getMonthName(int month){

        switch(month){
            case 0: {return ("January");}
            case 1: {return ("February");}
            case 2: {return ("March");}
            case 3: {return ("April");}
            case 4: {return ("May");}
            case 5: {return ("June");}
            case 6: {return ("July");}
            case 7: {return ("August");}
            case 8: {return ("September");}
            case 9: {return ("October");}
            case 10:{return ("November");}
            case 11:{return ("December");}
        }
        return "Wrong Month Number";
    }

    public static String UTC_To_Local_Time(String dateStr)
    {
        return dateStr.substring(dateStr.indexOf(" ",10),dateStr.indexOf(" ", 13)).trim();//TODO: modify it values are assumed.
    }

    public static boolean check_If_SP_exists(Context context){

        String Institution_Path = getValueFromSharedPreferences(context,KEY_institution_path).toString();
        String Device_Name = getValueFromSharedPreferences(context,KEY_device_name).toString();
        if(Institution_Path.equals("false") && Device_Name.equals("false")){
            //TODO: SP doesn't exists.
            return false;
        }else
            return true;

    }

    public static Object getValueFromSharedPreferences(Context context, String Key)
    {
        SharedPreferences sharedPreferences;
        final String MY_PREFERENCES = "MyPrefs";
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key, "false");
    }

    public static void putValueInSharedPreferences(Context context, String Key, String Value)
    {
        SharedPreferences sharedPreferences;
        final String MY_PREFERENCES = "MyPrefs";
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key, Value);

        editor.apply();
    }

    public static String getIMEInumber(Context context)
    {
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission not Enabled.", Toast.LENGTH_SHORT).show();
            return "null";
        }
        else
            return telephonyManager.getDeviceId(); //TODO: Permission check is required.
    }

    public static boolean loadFragment_withBackstackEntry(FragmentManager fragmentManager, Fragment fragment, String TAG) {
        //switching fragment
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment,TAG).addToBackStack(TAG)
                    .commit();
            return true;
        }
        return false;
    }

    public static boolean showFragment(FragmentManager fragmentManager, Fragment fragment, String TAG) {
        //switching fragment
        if (fragment != null) {
            FragmentTransaction transaction= fragmentManager
                                                .beginTransaction()
                                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

            List<Fragment> fragments = fragmentManager.getFragments();
            for (Fragment frag : fragments) {
                if (frag != null && frag.isVisible())
                    transaction.hide(frag);
            }
            if(fragment.isAdded())
                transaction.show(fragment);
            else if(!fragment.isAdded())
                transaction.add(R.id.fragment_container, fragment)/*.addToBackStack(TAG)*/;
            transaction.commit();
            return true;
        }
        return false;
    }

    public static void popFragmentTill(FragmentManager fragmentManager, Fragment popTillThisFragment, String popTillThisFragmentsTAG){
        // Pop off everything up to and including the current tab
        fragmentManager.popBackStack(popTillThisFragmentsTAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Add the new tab fragment
        loadFragment_withBackstackEntry(fragmentManager, popTillThisFragment, popTillThisFragmentsTAG);

    }

}
