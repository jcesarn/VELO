package iftm.pdm.velo.ui.list_adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import iftm.pdm.velo.R;
import iftm.pdm.velo.model.Journey;

public class JourneyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    private TextView txtJourneyName;
    private TextView txtJourneyDate;
    private TextView txtJourneyDesc;
    private ImageView imgViewEdit;
    private ItemList<Journey> currentJourney;

    public JourneyViewHolder(@NonNull View itemView) {
        super(itemView);
        this.txtJourneyName = this.itemView.findViewById(R.id.txtJourneyName);
        this.txtJourneyDate = this.itemView.findViewById(R.id.txtJourneyDate);
        this.txtJourneyDesc = this.itemView.findViewById(R.id.txtJourneyDesc);
        this.imgViewEdit = this.itemView.findViewById(R.id.imgViewEdit);
        this.itemView.setOnLongClickListener(this);
        this.itemView.setOnClickListener(this);
    }

    public void bind(ItemList<Journey> j, String[] desc) {
        this.txtJourneyName.setText(j.item.getName());
        this.txtJourneyDate.setText(j.item.getDate());
        this.txtJourneyDesc.setText(j.item.getDesc(desc));
        this.currentJourney = j;
        this.txtJourneyDesc.setVisibility(this.currentJourney.isExpanded ? View.VISIBLE : View.GONE);
        this.itemView.setSelected(this.currentJourney.isSelected);
    }

    public ImageView getImgViewEdit() {
        return imgViewEdit;
    }

    public TextView getTxtJourneyName() {
        return txtJourneyName;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() != this.imgViewEdit.getId()) {
            this.txtJourneyDesc.setVisibility(this.currentJourney.isExpanded ? View.GONE : View.VISIBLE);
            this.currentJourney.isExpanded = !this.currentJourney.isExpanded;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(JourneyAdapter.notifier != null) {
            this.currentJourney.isSelected = !this.currentJourney.isSelected;
            view.setSelected(this.currentJourney.isSelected);
            if(this.currentJourney.isSelected) {
                JourneyAdapter.numSelected ++;
                JourneyAdapter.notifier.notifySelection(JourneyAdapter.numSelected);
            } else {
                JourneyAdapter.numSelected --;
                JourneyAdapter.notifier.notifySelection(JourneyAdapter.numSelected);
                if(JourneyAdapter.numSelected < 1)
                    JourneyAdapter.notifier.notifyClearSelection();
            }
            return true;
        }
        return false;
    }

}
