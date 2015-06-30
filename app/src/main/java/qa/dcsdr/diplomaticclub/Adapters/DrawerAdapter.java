package qa.dcsdr.diplomaticclub.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.Items.DrawerEntry;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/7/2015.
 * The adapter for the navigation drawer.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<DrawerEntry> data = Collections.emptyList();
    private List<DrawerEntry> removed = new ArrayList<DrawerEntry>();

    private Context context;
    private ClickListener clickListener;

    @Override
    public int getItemViewType(int position) {
        String[] categories = context.getResources().getStringArray(R.array.categories);
        ArrayList<String> al = new ArrayList<String>(Arrays.asList(categories));
        if (al.contains(data.get(position).getTitle()) || data.get(position).getTitle().equals(context.getString(R.string.NO_CATEGORIES)))
            return 0;
        else return 1;
    }

    public DrawerAdapter(Context context, List<DrawerEntry> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = viewType == 0 ? R.layout.nav_drawer_entry : R.layout.nav_drawer_title;
        View view = inflater.inflate(layout, parent, false);
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DrawerEntry current = data.get(position);
        holder.getTitle().setText(current.getTitle());
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void delete(int position) {
        final List<String> categories = Arrays.asList((context.getResources().getStringArray(R.array.categories)));
        DrawerEntry de = new DrawerEntry();
        de.setTitle(categories.get(position));
        if (data.contains(de)) {
            removed.add(de);
            data.remove(de);
            notifyItemRemoved(position);
            if (data.size() == 7) {
                DrawerEntry nde = new DrawerEntry();
                    nde.setTitle(context.getString(R.string.NO_CATEGORIES));
                data.add(4, nde);
                notifyItemInserted(4);
            }

        } else {
            if (data.size() == 8) {
                if (data.get(4).getTitle().equals(context.getString(R.string.NO_CATEGORIES)))
                    data.remove(4);
            }
            int index = 4 + (categories.indexOf(categories.get(position)));
            int p;
            if (index > data.size() || index > (data.size() - 3)) {
                data.add(data.size() - 3, de);
                p = data.size() - 3;
            } else {
                data.add(index, de);
                p = index;
            }
            removed.remove(de);
            notifyItemChanged(p);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;

        public TextView getTitle() {
            return title;
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.itemClicked(v, getAdapterPosition());
        }
    }

}