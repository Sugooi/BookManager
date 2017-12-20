package sugoi.android.bookmanager.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sugoi.android.bookmanager.Adapter.BookAdapter;
import sugoi.android.bookmanager.Models.BookModel;
import sugoi.android.bookmanager.R;
import sugoi.android.bookmanager.others.API;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ArrayList<BookModel> heroList;

    BookAdapter adapter;
    ListView listView;

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);

        // use to enable search view popup text
//        if (TextUtils.isEmpty(newText)) {
//            friendListView.clearTextFilter();
//        }
//        else {
//            friendListView.setFilterText(newText.toString());
//        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heroList = new ArrayList<BookModel>();
        listView = (ListView)findViewById(R.id.booklist);


       // heroList = new List<BookModel>();
        performreq(API.URL_READBOOKS);
    }

    JSONArray object;

    public void performreq(final String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();

                        try{
                        object = new JSONArray(response);
                        refreshlist(object);
                        }
                        catch (Exception e){}

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();





                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    private void insert_req() {

        ins_title = title.getText().toString();
        ins_author = author.getText().toString();
        ins_type = title.getText().toString();
        ins_comment = comment.getText().toString();
        ins_lastseen = last_seen.getText().toString();
        ins_owner = owner.getText().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.URL_INSERTBOOK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("owner",ins_owner);
                params.put("type",ins_type);
                params.put("title",ins_title);
                params.put("last_seen",ins_lastseen);
                params.put("author",ins_author);
                params.put("comments",ins_comment);





                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }

    private void refreshlist(JSONArray array) {
        heroList.clear();

        try {
            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = array.getJSONObject(i);

                heroList.add(new BookModel(
                        obj.getInt("b_id"),
                        obj.getString("title"),
                        obj.getString("author"),
                        obj.getString("type"),
                        obj.getString("owner_name"),
                        obj.getString("last_seen")
                ));
            }

            adapter = new BookAdapter(this,heroList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();



        }catch(Exception e){}
    }

    private SearchView searchView;
    private MenuItem searchMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:
                final Dialog insert_book = new Dialog(MainActivity.this);
                insert_book.setContentView(R.layout.insert_book);



                insert_book.show();

                title = (EditText) insert_book.findViewById(R.id.ins_title);
                author = (EditText) insert_book.findViewById(R.id.ins_author);
                type =(EditText)insert_book.findViewById(R.id.ins_type);
                comment = (EditText) insert_book.findViewById(R.id.ins_comments);
                last_seen = (EditText) insert_book.findViewById(R.id.ins_lastseen);
                owner = (EditText)insert_book.findViewById(R.id.ins_owner);
                insert = (Button) insert_book.findViewById(R.id.insert_button);
                cancel_insert = (Button) insert_book.findViewById(R.id.insert_cancel);

                insert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        insert_req();
                        insert_book.dismiss();
                        performreq(API.URL_READBOOKS);
                    }
                });

                cancel_insert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        insert_book.dismiss();
                    }
                });


                break;

            default:
                break;
        }

        return true;
    }

    EditText title,author,type,comment,last_seen,owner;
    Button insert,cancel_insert;
    String ins_title,ins_author,ins_type,ins_comment,ins_lastseen,ins_owner;







}
