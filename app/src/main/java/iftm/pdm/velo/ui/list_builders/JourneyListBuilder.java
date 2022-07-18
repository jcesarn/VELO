package iftm.pdm.velo.ui.list_builders;

import android.app.Activity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import iftm.pdm.velo.model.Journey;
import iftm.pdm.velo.ui.list_adapters.JourneyAdapter;

public class JourneyListBuilder {

    private RecyclerView rvJourney;
    private JourneyAdapter journeyAdapter;
    private LinearLayoutManager layoutManager;

    public JourneyListBuilder(Activity activity, RecyclerView rvJourney, ArrayList<Journey> journeys, String[] desc) {
        this.rvJourney = rvJourney;
        this.journeyAdapter = new JourneyAdapter(journeys, activity, desc);
        this.layoutManager = new LinearLayoutManager(activity);
    }

    public void Load() {
        this.rvJourney.setLayoutManager(this.layoutManager);
        this.rvJourney.setAdapter(this.journeyAdapter);
    }

    public void refreshEditedJourney(Journey journey) {
        this.journeyAdapter.editJourney(journey);
    }

    public ArrayList<Long> refreshDeletedSelection() {
        return this.journeyAdapter.deleteSelectedJourneys();
    }

    public JourneyAdapter getJourneyAdapter() {
        return this.journeyAdapter;
    }

}
