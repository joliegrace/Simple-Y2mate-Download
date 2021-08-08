package com.minhnhatlpx.simpley2mdl;
import android.support.v7.widget.*;
import android.app.*;
import java.util.*;
import android.view.*;
import android.widget.*;
import android.text.*;


public class QualityAdapter extends RecyclerView.Adapter<QualityAdapter.ViewHolder> {
    private final Activity activity;
    private List<Quality> mQualitys;
	private int cardColor;
    


    private QualityAdapterListener mQualityAdapterListener;

	public void setCardColor(int cardColor)
	{
		this.cardColor = cardColor;
	}

	public int getCardColor()
	{
		return cardColor;
	}

    public interface QualityAdapterListener {
        void onItemClicked(Quality quality);
    }

    public void setListener(QualityAdapterListener qualityAdapterListener) {
        mQualityAdapterListener = qualityAdapterListener;
    }

    public QualityAdapter(Activity hostActivity) {
        this.activity = hostActivity;
        this.mQualitys = new ArrayList<>();
    }

    public void setQualityList(List<Quality> qualityList) {
        final int positionStart = mQualitys.size() + 1;
        mQualitys.addAll(qualityList);
        notifyItemRangeInserted(positionStart, qualityList.size());
    }

    public void resetQualityList() {
        mQualitys.clear();
        notifyDataSetChanged();
    }

    @Override
    public QualityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QualityAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quality, parent, false));
    }

    @Override
    public void onBindViewHolder(QualityAdapter.ViewHolder holder, int position) {
        holder.bindView();
    }

    @Override
    public int getItemCount() {
        return mQualitys.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView mCard;
        private TextView mQualityName;
		private TextView mSize;

        private ViewHolder(View view) {
            super(view);
            mCard = view.findViewById(R.id.card);
            mQualityName = view.findViewById(R.id.item_quality);
			mSize = view.findViewById(R.id.item_size);
        }

        private void bindView() {
            final Quality quality = mQualitys.get(getAdapterPosition());
            mQualityName.setText(quality.getQualityName());
			String size = quality.getSize();
			if(!TextUtils.isEmpty(size))
			{
				mSize.setText(quality.getSize());
			}else
			{
				mSize.setVisibility(TextView.GONE);
			}
			
			mCard.setCardBackgroundColor(getCardColor());
            mCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == mCard) {
                if (mQualityAdapterListener != null) {
                    mQualityAdapterListener.onItemClicked(
						mQualitys.get(getAdapterPosition()));
                }
            }
        }
    }
}
