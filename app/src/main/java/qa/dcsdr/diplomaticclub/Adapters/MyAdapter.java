package qa.dcsdr.diplomaticclub.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.Items.DrawerEntry;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/7/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<DrawerEntry> data = Collections.emptyList();
    private Context context;
    private ClickListener clickListener;

    @Override
    public int getItemViewType(int position) {
        if (position >= 4 && position <= 8) {
            return 0;
        } else return 1;
    }

    public MyAdapter(Context context, List<DrawerEntry> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = viewType == 0 ? R.layout.nav_drawer_entry : R.layout.nav_drawer_title;
        View view = inflater.inflate(layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DrawerEntry current = data.get(position);
        holder.getTitle().setText(current.getTitle());
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
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

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.itemClicked(v, getPosition());
        }
    }

}