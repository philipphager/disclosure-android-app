package de.philipphager.disclosure.feature.overview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.philipphager.disclosure.R;
import de.philipphager.disclosure.database.app.model.App;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class InfoRecyclerAdapter extends RecyclerView.Adapter<InfoRecyclerAdapter.ViewHolder> {
  private final List<App> items;
  private final Context context;
  private OnItemClickListener listener;

  @Inject public InfoRecyclerAdapter(Context context) {
    super();
    this.context = context;
    this.items = new ArrayList<>();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.view_info_list_item, parent, false);
    return new ViewHolder(v, context);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    App item = items.get(position);
    holder.bind(item, listener);
  }

  @Override public int getItemCount() {
    return items.size();
  }

  public void setItems(List<App> items) {
    clear();
    this.items.addAll(items);
    notifyDataSetChanged();
  }

  public void clear() {
    this.items.clear();
    notifyDataSetChanged();
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.listener = listener;
  }

  public interface OnItemClickListener {
    void onItemClick(App info);
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    private final ImageView icon;
    private final TextView title;
    private final Context context;

    ViewHolder(View itemView, Context context) {
      super(itemView);
      this.icon = (ImageView) itemView.findViewById(R.id.icon);
      this.title = (TextView) itemView.findViewById(R.id.title);
      this.context = context;
    }

    public void bind(final App info, final OnItemClickListener listener) {
      // Bind app info to views.
    }
  }
}
