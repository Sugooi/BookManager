package sugoi.android.bookmanager.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sugoi.android.bookmanager.Activities.MainActivity;
import sugoi.android.bookmanager.Models.BookModel;
import sugoi.android.bookmanager.R;
import sugoi.android.bookmanager.others.API;

/**
 * Created by Adil on 18-12-2017.
 */

public class BookAdapter extends BaseAdapter implements Filterable {

    private MainActivity activity;
    private FriendFilter friendFilter;
    private ArrayList<BookModel> friendList;
    private ArrayList<BookModel> filteredList;

    /**
     * Initialize context variables
     * @param activity friend list activity
     * @param friendList friend list
     */
    public BookAdapter(MainActivity activity, ArrayList<BookModel> friendList) {
        this.activity = activity;
        this.friendList = friendList;
        this.filteredList = friendList;


        getFilter();
    }

    /**
     * Get size of user list
     * @return userList size
     */
    @Override
    public int getCount() {
        return filteredList.size();
    }

    /**
     * Get specific item from user list
     * @param i item index
     * @return list item
     */
    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    /**
     * Get user list item id
     * @param i item index
     * @return current item id
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Create list row view
     * @param position index
     * @param view current list item view
     * @param parent parent
     * @return view
     */
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        final ViewHolder holder;
        final BookModel user = (BookModel) getItem(position);

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.book_title);
            holder.author = (TextView) view.findViewById(R.id.book_author);
            holder.type = (TextView) view.findViewById(R.id.book_type);
            holder.comment = (TextView) view.findViewById(R.id.book_comment);
            holder.last_seen = (TextView) view.findViewById(R.id.book_seen);
            holder.del = (ImageView) view.findViewById(R.id.delete_image);

            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog delte_dialog = new Dialog(activity);
                    delte_dialog.setContentView(R.layout.custom_dialog);

                    delte_dialog.show();

                    Button cancel = (Button) delte_dialog.findViewById(R.id.no);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            delte_dialog.dismiss();
                        }
                    });

                    Button proceed = (Button) delte_dialog.findViewById(R.id.yes);
                    proceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //  Toast.makeText(context,book.getB_id()+"",Toast.LENGTH_SHORT).show();
                            delete_book(position);
                            delte_dialog.dismiss();
                        }
                    });
                }
            });



            view.setTag(holder);
        } else {
            // get view holder back
            holder = (ViewHolder) view.getTag();
        }

        // bind text with view holder content view for efficient use
        holder.title.setText(user.getBook_name());
        holder.author.setText(user.getAuthor());
        holder.type.setText(user.getType());
        holder.comment.setText("woah");
        holder.last_seen.setText(user.getLastseen());

        return view;
    }

    /**
     * Get custom filter
     * @return filter
     */
    @Override
    public Filter getFilter() {
        if (friendFilter == null) {
            friendFilter = new FriendFilter();
        }

        return friendFilter;
    }

    /**
     * Keep reference to children view to avoid unnecessary calls
     */
    static class ViewHolder {

        TextView title, author, type, comment, last_seen;
        ImageView del;


    }

    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */
    private class FriendFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<BookModel> tempList = new ArrayList<BookModel>();

                // search content in friend list
                for (BookModel user : friendList) {
                    if (user.getBook_name().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(user);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = friendList.size();
                filterResults.values = friendList;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<BookModel>) results.values;
            notifyDataSetChanged();
        }
    }

    public void delete_book(int pos)
    {
       activity.performreq("http://shaikadil.esy.es/API/deletebookAPI.php?b_id="+pos);
       activity.performreq(API.URL_READBOOKS);
        //  adapter.notifyDataSetChanged();

    }


}