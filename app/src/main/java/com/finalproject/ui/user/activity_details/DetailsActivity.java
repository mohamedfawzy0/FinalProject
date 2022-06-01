package com.finalproject.ui.user.activity_details;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.finalproject.R;

import com.finalproject.adapter.CastAdapter;
import com.finalproject.databinding.ActivityDetailsBinding;
import com.finalproject.model.PostModel;
import com.finalproject.mvvm.ActivityDetailsMvvm;
import com.finalproject.ui.common_uis.activity_base.BaseActivity;
import com.finalproject.ui.user.activity_cinema_users.CinemasUserActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailsActivity extends BaseActivity {
    private CastAdapter castadapter;
    private ActivityDetailsBinding binding;
    private PostModel model;
    private String id;
    private ActivityDetailsMvvm mvvm;
    private int req;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        id = (String) intent.getSerializableExtra("post_id");
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.movie_details), R.color.color2, R.color.white);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
        mvvm = ViewModelProviders.of(this).get(ActivityDetailsMvvm.class);
        binding.setLang(getLang());
        model = new PostModel();
        binding.setModel(model);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        });

        mvvm.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.loader.setVisibility(View.VISIBLE);
                binding.loader.startShimmer();
                binding.constraint.setVisibility(View.GONE);
            } else {
                binding.loader.setVisibility(View.GONE);
                binding.loader.stopShimmer();
                binding.constraint.setVisibility(View.VISIBLE);
            }
        });
        mvvm.getOnDataSuccess().observe(this, postModel -> {
            model = postModel;
            binding.setModel(model);

            if (model != null) {
                if (model.getVideo() != null) {
                    setUpYoutube(this, model.getVideo());
                }
                if (castadapter != null) {
                    castadapter.updateList(postModel.getHeroes());
                }

            } else {
                binding.tvType.setVisibility(View.GONE);
            }
        });
        mvvm.getDetails(id);


        binding.btnChooseCinema.setOnClickListener(view -> {
            req = 1;
            Intent intent = new Intent(DetailsActivity.this, CinemasUserActivity.class);
            intent.putExtra("postModel", model);
            launcher.launch(intent);
        });
        castadapter = new CastAdapter(this);
        binding.recViewCast.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recViewCast.setAdapter(castadapter);


    }

    private void setUpYoutube(Context context, String url) {
        String videoId = extractYTId(url);
        if (videoId != null) {

            binding.youtubePlayerView.setEnableAutomaticInitialization(false);
            binding.youtubePlayerView.initialize(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    super.onReady(youTubePlayer);
                    youTubePlayer.cueVideo(videoId, 0);

                }
            }, true);


        } else {
        }


    }

    private String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile("^https?://.*(?:www\\.youtube\\.com/|v/|u/\\w/|embed/|watch\\?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }

}