package hds.aplications.com.mycp.view.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.R;
import hds.aplications.com.mycp.models.Accommodation;
import hds.aplications.com.mycp.view.holders.HolderHeader;
import hds.aplications.com.mycp.view.holders.HolderItemAccommodation;



public class AdapterAccommodation extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //added view types
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;

    private List<Accommodation> mItemList;
    private Date checkIn;
    private Date checkOut;

    public AdapterAccommodation(List<Accommodation> itemList, Date checkIn, Date checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        mItemList = itemList;
    }

    //modified creating viewholder, so it creates appropriate holder for a given viewType
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(context).inflate(R.layout.item_accommodation, parent, false);
            return HolderItemAccommodation.newInstance(view, context, checkIn, checkOut);
        }
        else if (viewType == TYPE_HEADER) {
            final View view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
            return new HolderHeader(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types    correctly");
    }

    //modifed ViewHolder binding so it binds a correct View for the Adapter
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (!isPositionHeader(position)) {
            HolderItemAccommodation holder = (HolderItemAccommodation) viewHolder;
            Accommodation accommodation = mItemList.get(position - 1);
            holder.bindView(accommodation);
            setFadeAnimation(holder.itemView, position);
        }
    }

    //our old getItemCount()
    public int getBasicItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    //our new getItemCount() that includes header View
    @Override
    public int getItemCount() {
        return getBasicItemCount() + 1; // header
    }

    //added a method that returns viewType for a given position
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    //added a method to check if given position is a header
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private final static int FADE_DURATION = 500;
    private int lastPosition = -1;

    private void setFadeAnimation(View view, int position) {
        /*ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);*/

        if (position > lastPosition) {
            /*ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(FADE_DURATION);//to make duration random number between [0,501)
            view.startAnimation(anim);*/

            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);

            lastPosition = position;
        }
    }

    public List<Accommodation> getmItemList(){
        return mItemList;
    }
}