package qa.dcsdr.diplomaticclub.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import qa.dcsdr.diplomaticclub.Activities.ArticleReader;
import qa.dcsdr.diplomaticclub.Items.Alert;
import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;
import qa.dcsdr.diplomaticclub.Tools.ArticleContent;

/**
 * Created by Tamim on 6/11/2015.
 * This is the adapter for displaying the authors.
 */
public class RecentAlertsAdapter extends RecyclerView.Adapter<RecentAlertsAdapter.ArticleViewHolder> {

    private final LayoutInflater layoutInflater;
    private Context context;
    private ClickListener clickListener;
    private Activity activity;
    private ArrayList<Alert> alerts;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    public void setList(ArrayList<Alert> alerts) {
        this.alerts = alerts;
        notifyDataSetChanged();
    }

    public RecentAlertsAdapter(Context context) {
        this.alerts = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
        this.context = context;

    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.alert_card, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, int position) {
        final Alert currentAlert = alerts.get(position);
        holder.alertTitle.setText(currentAlert.getTitle());
        holder.alertTitle.setOnClickListener(getAlertOnClickListener(currentAlert.getArticleID()));
    }

    private View.OnClickListener getAlertOnClickListener(final int articleID) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (articleID != -1) {
                    Toast.makeText(context, context.getResources().
                            getString(R.string.LOADING), Toast.LENGTH_SHORT).show();
                    String url = context.getResources().getString(R.string.SINGLE_ARTICLE_ID) + articleID;
                    requestQueue.add(getStringRequest(context, url, articleID));
                }
            }
        };
    }

    private StringRequest getStringRequest(final Context context, final String url, final int ID) {
        return new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArticleContent articleContent = new ArticleContent(ID, url, context);
                Article c = articleContent.processXmlSingle(response, ID);
                ArrayList<Article> list = new ArrayList<>();
                list.add(c);

                // create intent to start your activity
                Intent intent = new Intent(context, ArticleReader.class);
                intent.putExtra("ARTICLE_LIST", list);
                intent.putExtra("POSITION", 0);
                intent.putExtra("CAT_TITLE", c.getTitle());
                intent.putExtra(context.getResources().getString(R.string.PARENT_CLASS_TAG),
                        context.getResources().getString(R.string.DISPLAY_FRAGMENT_TAG));
                intent.putExtra("URL", url);
                context.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }


    @Override
    public int getItemCount() {
        return alerts.size();
    }


    class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView alertTitle;
        Button remove;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            alertTitle = (TextView) itemView.findViewById(R.id.alertTitle);
            remove = (Button) itemView.findViewById(R.id.remove);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.itemClicked(v, getAdapterPosition());
        }
    }

}