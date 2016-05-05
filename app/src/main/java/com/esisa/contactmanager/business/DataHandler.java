package com.esisa.contactmanager.business;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.esisa.contactmanager.R;
import com.esisa.contactmanager.web.Service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * Created by Yassine on 05/01/2015.
 */
public class DataHandler {
    private Context context;

    public DataHandler(Context context) {
        this.context = context;
    }

    public List<HashMap<String, String>> loadFromWeb() {
        List<HashMap<String, String>> fillMaps = new ArrayList<>();
        Hashtable<String, String> keys = new Hashtable<>();
        keys.put("Action", "Import");
        try {
            JSONObject obj = new Service(context, keys).execute("").get();
            JSONArray array = obj.getJSONArray("Array");
            for (int i = 0; i < array.length(); i++) {
                HashMap map = new HashMap();
                JSONObject jsonObject = array.getJSONObject(i);
                String id = jsonObject.getString("id");
                String nom = jsonObject.getString("nom");
                JSONArray emailsArray = jsonObject.getJSONArray("emails");
                JSONArray numbersArray = jsonObject.getJSONArray("numbers");
                String picture = jsonObject.getString("picture");
                String emails = "";
                String phones = "";

                for (int j = 0 ; j < emailsArray.length(); j++){
                    JSONObject o = emailsArray.getJSONObject(i);
                    emails += o.getString("email") + ",";
                }
                for (int j = 0 ; j < emailsArray.length(); j++){
                    JSONObject o = numbersArray.getJSONObject(i);
                    phones += o.getString("number") + ",";
                }

                map.put("Nom", nom);
                map.put("Emails", emails);
                map.put("Numéros", phones);
                map.put("Image", picture);
                fillMaps.add(map);
            }

            return fillMaps;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void exportToWeb(List<HashMap<String, String>> fillmaps) {
        Hashtable<String, String> keys = new Hashtable<>();
        keys.put("Action", "Export");
        JSONArray array = new JSONArray(fillmaps);
        keys.put("jsonContacts", array.toString());
        new Service(context, keys).execute("");
    }


    public List<HashMap<String, String>> loadFromPhone() {
        Cursor contacts = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        while (contacts.moveToNext()) {
            if (contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).equals("1")) {
                HashMap<String, String> map = new HashMap<String, String>();

                String id = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID));
                String nom = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String picture = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

                if (picture == null) {
                    picture = Integer.toString(R.drawable.default_avatar);
                }
                Vector<String> tels = loadPhoneNumber(id);
                Vector<String> email = loadPhoneEmails(id);

                String nums = "";
                String emails = "";
                for (int i = 0; i < tels.size(); i++) {
                    nums += tels.get(i) + ", ";
                }

                for (int i = 0; i < email.size(); i++) {
                    emails += email.get(i) + ", ";
                }

                map.put("Nom", nom);
                map.put("Emails", emails);
                map.put("Numéros", nums);
                map.put("Image", picture);

                fillMaps.add(map);
            }
        }
        contacts.close();
        return fillMaps;
    }

    Vector<String> loadPhoneNumber(String id) {
        Vector<String> numbers = new Vector<>();
        String[] tels = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor phones = new CursorLoader(context, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                tels, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null).loadInBackground();

        while (phones.moveToNext()) {
            numbers.add(phones.getString(0));
        }
        phones.close();
        return numbers;
    }

    Vector<String> loadPhoneEmails(String id) {
        Vector<String> emails = new Vector<>();
        String[] email = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS};
        Cursor phones = new CursorLoader(context, ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                email, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + id, null, null).loadInBackground();

        while (phones.moveToNext()) {
            emails.add(phones.getString(0));
        }
        phones.close();
        return emails;
    }


}
