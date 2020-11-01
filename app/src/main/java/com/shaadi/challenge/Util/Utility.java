/**
 *
 */
package com.shaadi.challenge.Util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import com.shaadi.challenge.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class Utility {

    public static Typeface font = null;
    public static TypefaceSpan robotoRegularSpan;
    public static AlertDialog alertMsg = null;
    public static ProgressDialog pd = null;



    public static void removeErrorOnTextChange(final EditText et) {
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                et.setError(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    ////////////////////////////////////////////////////////////////////

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight()
                    * (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }


    public static void showProgress(Context context,
                                    String progressMsg,
                                    final Call<ResponseBody> caller) {
        try {
            if (!((Activity) context).isFinishing()) {
                if (pd != null) {
                    if (pd.isShowing()) {
                        pd.cancel();
                    }
                }
                pd = new ProgressDialog(context);
                pd.setMessage(Utility.getSpannableString(progressMsg));
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                // on cancel event cancel currently running AsyncTask
                pd.setButton(DialogInterface.BUTTON_NEGATIVE,
                        Utility.getSpannableString(context.getString(R.string.lbl_btn_cancel)),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {


                                if (pd != null) {
                                    pd.cancel();
                                }

                                if (caller != null) {
                                    caller.cancel();
                                }

                                return;
                            }
                        });
                pd.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (context instanceof Activity) {
            hideKeyboard((Activity) context);
        }

    }


    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void hideProgress() {

        try {
            if (pd != null) {
                if (pd.isShowing()) {
                    pd.cancel();
                    pd = null;


                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static String getRequest(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final okio.Buffer buffer = new okio.Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static void showPopup(Context context, String title, final String msg) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    context);
            if (!title.isEmpty()) {
                alertDialog.setTitle(getSpannableString(title));
            }
            alertDialog.setMessage(getSpannableString(msg));

            alertDialog.setPositiveButton("DISMISS",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });

            alertDialog.show();
        }

    }


    public static String getResponse(retrofit2.Response<ResponseBody> response) throws JSONException {
        //Try to get response body
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));

        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;

        if (!sb.toString().isEmpty()) {
            jsonObject = new JSONObject(sb.toString());
            return jsonObject.getString("d");
        } else {
            return "";
        }
    }


    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return true;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }





    public static boolean isJSONValid(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    public static boolean isJSONArrayValid(String json) {
        try {
            new JSONArray(json);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static void applyFontForToolbarTitle(Activity context, android.support.v7.widget.Toolbar app_bar) {

        for (int i = 0; i < app_bar.getChildCount(); i++) {
            View view = app_bar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(context.getTitle())) {
                    tv.setTypeface(font);
                    break;
                }
            }
        }
    }


    public static SpannableString getSpannableString(String txt) {
        SpannableString span_tlt = new SpannableString(txt.trim());
        span_tlt.setSpan(robotoRegularSpan, 0, span_tlt.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        return span_tlt;
    }




}