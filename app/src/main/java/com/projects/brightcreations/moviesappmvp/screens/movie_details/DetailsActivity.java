package com.projects.brightcreations.moviesappmvp.screens.movie_details;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.projects.brightcreations.moviesappmvp.movie.Movie;
import com.projects.brightcreations.moviesappmvp.R;
import com.projects.brightcreations.moviesappmvp.interfaces.ActivityController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements ActivityController,MoviesDetailsListener {

    @BindView(R.id.iv_movie_background)
    ImageView Poster;
    @BindView(R.id.main_content)
    CoordinatorLayout coordinatorLayout;
    private String MovieDetails, PosterPath, MovieTitle, Overview, VoteAverage, Date, ID, backdropPath;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Movie mMovie;
    private ActionBar actionBar;
    private MoviesDetailsPresenter detailsPresenter;

    @Override
    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar = this.getSupportActionBar();
        ButterKnife.bind(this);
        detailsPresenter = new MoviesDetailsPresenter(this);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mMovie = extra.getParcelable("MovieDetails");
            viewAdapter(mMovie);
//            Log.e("CustomerWalletDetails", totalcustomerwallet.getRebate().get(0).getPointTotal());
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFrag(MovieDetailsFragment.newInstance(),"");

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> addToDB());
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_details;
    }

    private void addToDB(){
        detailsPresenter.addMovieToDB(mMovie,this);
    }

    public void viewAdapter(Movie movie) {
        ID = movie.getMovieID();
        MovieTitle = movie.getMovieTittle();
        Overview = movie.movieOverview;
        VoteAverage = movie.movieVoteAverage;
        Date = movie.movieDate;
        PosterPath = movie.getMoviePoster();
        backdropPath = movie.getMovieBackdrop();
        actionBar.setTitle(MovieTitle);
        Picasso.with(this).load(backdropPath).into(Poster);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addedToDataBaseSuccess(String successMsg) {
        Snackbar.make(coordinatorLayout, successMsg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void addedToDataBaseFailure(String errorMsg) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }
    }

}
