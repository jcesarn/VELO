package iftm.pdm.velo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import iftm.pdm.velo.R;
import iftm.pdm.velo.data.JourneyDAOSingleton;
import iftm.pdm.velo.model.Journey;
import iftm.pdm.velo.ui.dialogs.DialogConfirmDeletion;
import iftm.pdm.velo.ui.list_adapters.JourneyAdapter;
import iftm.pdm.velo.ui.list_builders.JourneyListBuilder;

public class JourneysActivity extends AppCompatActivity {

    public static final int EDIT_REQ_CODE = 101;
    private JourneyListBuilder journeyListBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journeys);
        RecyclerView rvJourneys = findViewById(R.id.rvJourneysList);
        String[] desc = new String[4];
        desc[0] = getString(R.string.time);
        desc[1] = getString(R.string.distance);
        desc[2] = getString(R.string.avg_speed);
        desc[3] = getString(R.string.max_speed);
        this.journeyListBuilder = new JourneyListBuilder(this, rvJourneys, JourneyDAOSingleton.getINSTANCE().getJourneys(), desc);
        this.journeyListBuilder.Load();
    }

    public void onDeleteSelectedJourneys() {
        new DialogConfirmDeletion(this) {
            @Override
            public void onClickYes() {
                JourneyDAOSingleton.getINSTANCE().removeJourneys(journeyListBuilder.refreshDeletedSelection());
                if(JourneyDAOSingleton.getINSTANCE().isEmpty())
                    finish();
            }

            @Override
            public void onClickNo() {

            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQ_CODE && resultCode == RESULT_OK && data != null) {
            long id = JourneyDAOSingleton.getINSTANCE().editJourney(data.getStringExtra(JourneyAdapter.JOURNEY_ADAPTER_KEY), data.getStringExtra(EditJourneyActivity.EDIT_JOURNEY_KEY));
            if(id != -1) {
                Journey journey = JourneyDAOSingleton.getINSTANCE().getJourney(id);
                this.journeyListBuilder.refreshEditedJourney(journey);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journeys_menu, menu);
        final MenuItem actionDelete = menu.findItem(R.id.bttDelete);
        this.journeyListBuilder.getJourneyAdapter().setSelectionNotifier(new JourneyAdapter.SelectionNotifier() {
            @Override
            public void notifySelection(int numSelected) {
                actionDelete.setVisible(true);
            }

            @Override
            public void notifyClearSelection() {
                actionDelete.setVisible(false);
            }
        });
        actionDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                onDeleteSelectedJourneys();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
