package id.nadji.cctvjakarta;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.nadji.cctvjakarta.model.Feature;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Feature> featureList;
    private List<Feature> featureListFiltered;
    private AdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onFeatureSelected(featureListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public Adapter(Context context, List<Feature> featureList, AdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.featureList = featureList;
        this.featureListFiltered = featureList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Feature feature = featureListFiltered.get(position);

        String latitude = feature.getProperties().getLocation().getLatitude();
        String longitude = feature.getProperties().getLocation().getLongitude();

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(Double.valueOf(latitude),Double.valueOf(longitude),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String alamat = addresses.get(0).getAddressLine(0);


        holder.name.setText(feature.getProperties().getId());
       // holder.phone.setText((CharSequence) feature.getProperties());
       // holder.phone.setText(feature.getProperties().getUrl());
        holder.phone.setText(alamat);

       // Glide.with(context)
                //.load(feature.getImage())
               // .apply(RequestOptions.circleCropTransform())
               // .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return featureListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    featureListFiltered = featureList;
                } else {
                    List<Feature> filteredList = new ArrayList<>();
                    for (Feature row : featureList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getProperties().getUrl().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    featureListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = featureListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                featureListFiltered = (ArrayList<Feature>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AdapterListener {
        void onFeatureSelected(Feature feature);
    }
}
