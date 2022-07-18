package iftm.pdm.velo.ui.list_adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import iftm.pdm.velo.R;
import iftm.pdm.velo.model.Journey;
import iftm.pdm.velo.ui.activities.EditJourneyActivity;
import iftm.pdm.velo.ui.activities.JourneysActivity;

public class JourneyAdapter extends RecyclerView.Adapter<JourneyViewHolder> {

    public static final String JOURNEY_ADAPTER_KEY = "JourneyAdapter.JOURNEY";
    private ArrayList<ItemList<Journey>> journeys;
    private Activity activity;
    protected static int numSelected;
    protected static SelectionNotifier notifier;
    private String[] desc;

    public interface SelectionNotifier {
        void notifySelection(int numSelected);
        void notifyClearSelection();
    }

    public JourneyAdapter(ArrayList<Journey> journeysList, Activity activity, String[] desc) {
        notifier = null;
        numSelected = 0;
        this.desc = desc;
        this.activity = activity;
        this.journeys = new ArrayList<>();
        for (Journey j : journeysList) {
            this.journeys.add(new ItemList<>(j));
        }
        this.sort();
    }

    public void setSelectionNotifier(SelectionNotifier selectionNotifier) {
        notifier = selectionNotifier;
    }

    public void editJourney(Journey journey) {
        ItemList<Journey> itemList = new ItemList<>(journey);
        int pos = this.journeys.indexOf(itemList);
        this.journeys.get(pos).item.setName(journey.getName());
        this.sort();
        pos = this.journeys.indexOf(itemList);
        notifyItemChanged(pos);
    }

    public ArrayList<Long> deleteSelectedJourneys() {
        Iterator<ItemList<Journey>> iterator = this.journeys.iterator();
        int pos = 0;
        ArrayList<Long> idList = new ArrayList<>();
        while(iterator.hasNext()) {
            ItemList<Journey> journeyItemList = iterator.next();
            if(journeyItemList.isSelected) {
                long id = journeyItemList.item.getId();
                idList.add(id);
                iterator.remove();
                notifyItemRemoved(pos --);
            }
            pos ++;
        }
        numSelected = 0;
        notifier.notifyClearSelection();
        return idList;
    }

    private void sort() {
        Collections.sort(this.journeys, new Comparator<ItemList<Journey>>() {
            @Override
            public int compare(ItemList<Journey> i1, ItemList<Journey> i2) {
                return i1.item.compareTo(i2.item);
            }
        });
    }

    @NonNull
    @Override
    public JourneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.view_journeys, parent, false);
        return new JourneyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final JourneyViewHolder holder, final int position) {
        holder.bind(this.journeys.get(position), this.desc);
        holder.getImgViewEdit().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditJourneyActivity.class);
                intent.putExtra(JOURNEY_ADAPTER_KEY, holder.getTxtJourneyName().getText().toString());
                activity.startActivityForResult(intent, JourneysActivity.EDIT_REQ_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.journeys.size();
    }

}
