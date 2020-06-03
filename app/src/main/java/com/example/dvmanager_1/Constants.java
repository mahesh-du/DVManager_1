package com.example.dvmanager_1;

public interface Constants {


    //Shared Preferences Keys
    String KEY_institution_path ="INSTITUTION_PATH";
    String KEY_device_name      ="DEVICE_NAME";
    String KEY_actual_position  ="ACTUAL POSITION";

    //Firestore Paths
    String  firestore_device                                    ="/Devices_Doc/Devices";
    String  firestore_alerts                                    ="/Devices_Doc/Alerts";
    String  firestore_alerts_battery                            ="/Devices_Doc/Alerts/Battery";
    String  firestore_email                                     ="/DV/Email";
    String firestore_institution                                ="/DV/Institutions";
    String firestore_institution_Details                        ="/Institution Details";
    String  firestore_ids                                       = "/ID_doc/IDS";
    String firestore_document_ANALYSIS_STATISTICS_YEAR          = "/Analysis/Statistics/Year";    //Firestore Analysis path.

    //Firestore Fields.
    String firestore_INSTITUTIONS_FIELD_INSTITUTION_Names             = "Institution Names";

    //Id Fields
    String firestore_IDS_FIELD_Name             = "Name";
    String firestore_IDS_FIELD_Phone_Number     = "Phone No";
    String firestore_IDS_FIELD_Address          = "Address";
    String firestore_IDS_FIELD_Admission_No     = "Admission No";
    String firestore_IDS_FIELD_Age              = "Age";
    String firestore_IDS_FIELD_Blocked          = "Blocked";
    String firestore_IDS_FIELD_Email            = "Email";
    String firestore_IDS_FIELD_Gender           = "Gender";
    String firestore_IDS_FIELD_Image            = "Profile Picture";

    //Device Fields
    String firestore_DEVICE_FIELD_Device_Name       ="Device Name";
    String firestore_DEVICE_FIELD_IMEI              ="IMEI";
    String firestore_DEVICE_FIELD_Blocked           ="Blocked";
    String firestore_DEVICE_FIELD_Scans_Today       = "Scans Today";
    String firestore_DEVICE_FIELD_Actual_Position   ="Actual Position";
    String firestore_DEVICE_FIELD_GPS_Location      ="GPS Location";
    String firestore_DEVICE_FIELD_Network_Status    ="Network Status";
    String firestore_DEVICE_FIELD_Battery_Remaining ="Battery Remaining";
    String firestore_DEVICE_FIELD_Battery_Status    ="Battery Status";
    String firestore_DEVICE_FIELD_Logged_In_Email   ="Logged In Email";
    String firestore_DEVICE_FIELD_Logged_In         ="Logged In";
    //String  ="";

    //Institution Details Fields
    String firestore_INSTITUTION_FIELD_Institution_Name                 ="Name";
    String firestore_INSTITUTION_FIELD_Institution_Email                ="Email";
    String firestore_INSTITUTION_FIELD_Institution_PhoneNo              ="Phone Number";
    String firestore_INSTITUTION_FIELD_Institution_Address              ="Address";
    String firestore_INSTITUTION_FIELD_Institution_Ids_Registered       ="Ids Registered";
    String firestore_INSTITUTION_FIELD_Institution_Blank_Ids            ="Blank Ids";
    String firestore_INSTITUTION_FIELD_Institution_Devices_Registered   ="Devices Registered";

    //Backstack Tags.
    String backstack_TAG_Register_Device_FRAGMENT                       = "REGISTER_DEVICE_FRAGMENT";
    String backstack_TAG_Login_FRAGMENT                                 = "LOGIN_FRAGMENT";
    String backstack_TAG_Alert_FRAGMENT                                 = "ALERT_FRAGMENT";
    String backstack_TAG_Analysis_FRAGMENT                              = "ANALYSIS_FRAGMENT";
    String backstack_TAG_Home_FRAGMENT                                  = "HOME_FRAGMENT";
    String backstack_TAG_Manage_Devices_FRAGMENT                        = "MANAGE_DEVICES_FRAGMENT";
    String backstack_TAG_Scan_FRAGMENT                                  = "SCAN_FRAGMENT";
    String backstack_TAG_Search_FRAGMENT                                = "SEARCH_FRAGMENT";
    String backstack_TAG_Scan_Add_Details_FRAGMENT                      = "SCAN_ADD_DETAILS_FRAGMENT";
    String backstack_TAG_Scan_Add_Details_Other_Id_Proof_FRAGMENT       = "SCAN_ADD_DETAILS_OTHER_ID_PROOF_FRAGMENT";
    String backstack_TAG_Scan_Add_Details_Profile_Picture_FRAGMENT      = "SCAN_ADD_DETAILS_PROFILE_PICTURE_FRAGMENT";
    String backstack_TAG_Scan_Add_Details_Registration_FRAGMENT         = "SCAN_ADD_DETAILS_REGISTRATION_FRAGMENT";
    String backstack_TAG_Search_Result_FRAGMENT                         = "SEARCH_RESULT_FRAGMENT";
    String backstack_TAG_My_Ids_FRAGMENT                                = "My_Ids_FRAGMENT";
    String backstack_TAG_My_Profile_FRAGMENT                            = "My_Profile_FRAGMENT";
    String backstack_TAG_About_FRAGMENT                                 = "About_FRAGMENT";

}
