package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION;


public class StudyMatAdopter extends FirebaseRecyclerAdapter<Post, StudyMatAdopter.PostViewHolder> {
    private Context context;
    private String Id;
     StudyMatAdopter(@NonNull FirebaseRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context = context;
    }

    public StudyMatAdopter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final PostViewHolder holder, int position, @NonNull final Post post) {
        holder.tittle.setText(post.getTittle());
        holder.description.setText(post.getDescription());
        StudyMatAdopter.this.Id=post.getId();
        holder.link.setText(post.getLink());
        holder.link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(holder.link.getText().toString()));
                    v.getContext().startActivity(intent);
                }catch (android.content.ActivityNotFoundException anfe){
                    Toast.makeText(context,"Link not Valid",Toast.LENGTH_LONG).show();
                }

            }
        });


        holder.onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                try {
                    youTubePlayer.loadVideo(extractYoutubeId(post.getLink()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                youTubePlayer.setFullscreenControlFlags(FULLSCREEN_FLAG_CONTROL_ORIENTATION);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }

        };

        holder.thumbnailView.initialize(PlayerConfi.API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                try {
                    youTubeThumbnailLoader.setVideo(extractYoutubeId(post.getLink()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        holder.relativeLayout.setVisibility(View.GONE);
                        youTubeThumbnailLoader.release();
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        holder.thumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.thumbnailView.setVisibility(View.GONE);
                holder.youTubePlayerView.initialize(PlayerConfi.API_KEY,holder.onInitializedListener);

            }
        });



    }

    public void deleteItem(int adapterPosition) {
         String data=getSnapshots().getSnapshot(adapterPosition).getValue().toString();
         String[] s=data.split(",");
         for (int i=0;i<s.length;i++){
             String s1=s[i].replace("{","");
             s1.replace("}","");
            if (s1.replace(" ","").startsWith("Id"))
             {
                FirebaseDatabase.getInstance().getReference().child("abc").setValue(s1.substring(4,s1.length()));
                 if (s1.substring(4,s1.length()).equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                 getSnapshots().getSnapshot(adapterPosition).getRef().removeValue();
            }
         }



    }



    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_study_material, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull List<Object> payloads) {

        super.onBindViewHolder(holder, position, payloads);
    }


    protected static String extractYoutubeId(String url) throws MalformedURLException
    {
        String id = null;
        try
        {
            String query = new URL(url).getQuery();
            if (query != null)
            {
                String[] param = query.split("&");
                for (String row : param)
                {
                    String[] param1 = row.split("=");
                    if (param1[0].equals("v"))
                    {
                        id = param1[1];
                    }
                }
            }
            else
            {
                if (url.contains("embed"))
                {
                    id = url.substring(url.lastIndexOf("/") + 1);
                }
            }
            if (id==null)
            {
                id=url.substring(url.lastIndexOf("/") + 1);
            }
        }
        catch (Exception ex)
        {
            //Log.e("Exception", ex.toString());
        }

        return id;
    }




    class PostViewHolder extends RecyclerView.ViewHolder {

        TextView tittle, description,link;
        RelativeLayout relativeLayout;
        CardView cardView;
        LinearLayout expandableView;
        Button arrowBtn;
        YouTubePlayerView youTubePlayerView;
        YouTubePlayer.OnInitializedListener onInitializedListener;
        YouTubeThumbnailView thumbnailView;
        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubePlayerView=(YouTubePlayerView)itemView.findViewById(R.id.youtube);
            thumbnailView=itemView.findViewById(R.id.thumbnail);
            tittle=itemView.findViewById(R.id.title);
            description =itemView.findViewById(R.id.description);
            link=itemView.findViewById(R.id.link);
            expandableView = itemView.findViewById(R.id.expandabla);
            arrowBtn = itemView.findViewById(R.id.arrow);
            cardView = itemView.findViewById(R.id.card);
            expandableView.setVisibility(View.GONE);
             relativeLayout=itemView.findViewById(R.id.youtube_videos);

            arrowBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    if (expandableView.getVisibility()==View.GONE){
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                        arrowBtn.setBackgroundResource(R.drawable.ic_drop_up);

                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        arrowBtn.setBackgroundResource(R.drawable.ic_drop_down);
                    }
                }
            });




        }






    }
}
