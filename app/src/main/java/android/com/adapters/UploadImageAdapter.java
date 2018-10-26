package android.com.adapters;

import android.com.activity.UploadImageActivity;
import android.com.garytransportnew.R;
import android.com.interfaces.IpositionCallBack;
import android.com.models.DeleteProgress;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.ViewHolder> {


    List<DeleteProgress> deleteProgresses;
    IpositionCallBack ipositionCallBack;

    public UploadImageAdapter(List<DeleteProgress> deleteProgresses ,Context context){

        this.deleteProgresses  = deleteProgresses;
        if(context instanceof UploadImageActivity)
        {
            ipositionCallBack = (IpositionCallBack) context;


        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_row,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {



        holder.tvName.setText(deleteProgresses.get(position).getFilename());
        holder.tvSize.setText(deleteProgresses.get(position).getFileSize());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteProgresses.remove(position);
                notifyDataSetChanged();

            }
        });



        if(deleteProgresses.get(position).getIsCompleted())
        {

            holder.progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        }




    }

    @Override
    public int getItemCount() {
        return deleteProgresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSize;
        TextView tvName;
        ImageView imgDelete;
        ProgressBar progressBar;


        public ViewHolder(View itemView) {
            super(itemView);

            tvSize = itemView.findViewById(R.id.row_fileSizeTV);
            tvName= itemView.findViewById(R.id.row_fileNameTV);
            imgDelete= itemView.findViewById(R.id.row_deleteImage);
            progressBar= itemView.findViewById(R.id.row_progressbar);
        }
    }
}
