package com.projects.brightcreations.moviesappmvp.screens.movies_posters;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.projects.brightcreations.moviesappmvp.adapters.PostersRecyclerViewAdapter;
import com.projects.brightcreations.moviesappmvp.models.Movie;
import com.projects.brightcreations.moviesappmvp.models.MovieRealmObject;
import com.projects.brightcreations.moviesappmvp.R;
import com.projects.brightcreations.moviesappmvp.screens.movie_details.DetailsActivity;
import com.projects.brightcreations.moviesappmvp.utils.Constants;
import com.projects.brightcreations.moviesappmvp.utils.SharedPreferenceHelper;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements OnMenuItemClickListener, onGetMoviesListener,
        MoviesActivityViews{

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.posters_recycler_view)
    RecyclerView RecyclerView;
    @BindView(R.id.go_top_layout)
    Button mGoTopbtn;
    private PostersRecyclerViewAdapter recyclerViewAdapter;
    private android.support.v7.widget.RecyclerView.LayoutManager gridLayoutManager;
    private List<Movie> movieList;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;
    private List<MovieRealmObject> movyRealmObjects;
    private MoviesPresenter moviesPresenter;
    private boolean loading = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount, lastVisiblesItems;
    private Realm realm;
    private ActionBar actionBar;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RootView =  inflater.inflate(R.layout.fragment_movies, container, false);

        ButterKnife.bind(this,RootView);
        initMenuFragment();
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        moviesPresenter = new MoviesPresenter(getActivity(),this);
        SharedPreferenceHelper.init(getActivity());
        realm = Realm.getDefaultInstance();
        fragmentManager = getActivity().getSupportFragmentManager();
        movieList = new ArrayList<>();
        final String lastChoice = SharedPreferenceHelper.getInstance()
                .getPref(SharedPreferenceHelper.LAST_CHOICE,Constants.TOP_RATED);

        actionBar.setTitle(SharedPreferenceHelper.getInstance()
                .getPref(SharedPreferenceHelper.LAST_CHOICE,Constants.TOP_RATED).toUpperCase());

        RecyclerView.setHasFixedSize(true);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager = new GridLayoutManager(getContext(),2);
        } else gridLayoutManager = new GridLayoutManager(getContext(),4);
        RecyclerView.setLayoutManager(gridLayoutManager);
        recyclerViewAdapter = new PostersRecyclerViewAdapter(movieList,getActivity());
        RecyclerView.setAdapter(recyclerViewAdapter);
        RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = ((GridLayoutManager)RecyclerView.getLayoutManager());
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                lastVisiblesItems = layoutManager.findLastVisibleItemPosition();
                String lastChoice = SharedPreferenceHelper.getInstance()
                        .getPref(SharedPreferenceHelper.LAST_CHOICE,Constants.TOP_RATED);
                    if(dy > 0){
                        showGoTop(false);
                        if (!loading) {
                            if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                if (!lastChoice.equals(Constants.FAVORITE)){
                                    loading = true;
                                    getMovies(lastChoice,getCurrentPage(lastChoice)+1);
                                }
                            }
                        }
                    }else if (dy<0){
                        Log.e("last visible : ",lastVisiblesItems+" ");
                        if (lastVisiblesItems<=5) {
                            showGoTop(false);
                        }else showGoTop(true);
                    }
            }
        });

        swipeContainer.setRefreshing(true);
        if(lastChoice.equals(Constants.FAVORITE)){
            moviesPresenter.getFavoritesFromDB(movieList,this);
        }else getMovies(lastChoice,getCurrentPage(lastChoice));

        swipeContainer.setOnRefreshListener(() -> {
            swipeContainer.setEnabled( true );
            swipeContainer.setRefreshing( true );
            String lastChoice1 = SharedPreferenceHelper.getInstance()
                    .getPref(SharedPreferenceHelper.LAST_CHOICE,Constants.TOP_RATED);
            if(lastChoice1.equals(Constants.FAVORITE)){
                moviesPresenter.getFavoritesFromDB(movieList, isLoading -> {
                    loading = isLoading;
                    swipeContainer.setRefreshing(isLoading);
                });
            }else getMovies(lastChoice1,getCurrentPage(lastChoice1));
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerViewAdapter.setOnItemClickListener((position, v) -> {
            Intent intent = new Intent(getActivity(),DetailsActivity.class);
            intent.putExtra("MovieDetails", movieList.get(position));
            startActivity(intent);
        });

        mGoTopbtn.setOnClickListener(view -> {
            if (RecyclerView != null) {
                RecyclerView.scrollToPosition(7);
                RecyclerView.smoothScrollToPosition(0);
                showGoTop(false);
            }
        });

        return RootView;
    }

    public void getMovies(String lastChoice, int currentPage){
        moviesPresenter.PerformMoviesCall(lastChoice, currentPage,this);
    }

    public int getCurrentPage(String lastChoice){
        if (lastChoice.equals(Constants.POPULAR)){
            return SharedPreferenceHelper.getInstance()
                    .getPref(SharedPreferenceHelper.POPULAR_CURRENT_PAGE,1);
        } else if (lastChoice.equals(Constants.TOP_RATED)) {
            return SharedPreferenceHelper.getInstance()
                    .getPref(SharedPreferenceHelper.TOP_CURRENT_PAGE,1);
        }
        return 1;
    }

    public void showGoTop(boolean show) {
        if (show){
            mGoTopbtn.setVisibility(View.VISIBLE);
            // Start the animation
            mGoTopbtn.animate()
                    .translationY(40)
                    .alpha(1.0f)
                    .setListener(null);
        }else {
            mGoTopbtn.animate()
                    .translationY(0)
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mGoTopbtn.setVisibility(View.GONE);
                        }
                    });
        }
    }

    @Override
    public void onGetMoviesSuccess() {
        //avl_loading.hide();
        movieList.clear();
        realm.beginTransaction();
        movyRealmObjects = realm.where(MovieRealmObject.class).equalTo("sortType",
                SharedPreferenceHelper.getInstance()
                        .getPref(SharedPreferenceHelper.LAST_CHOICE,Constants.TOP_RATED)).findAll();

        for(int i = 0; i < movyRealmObjects.size(); i++) {
            //PostersPathsArray.add(i,Constants.BASIC_URL+AllMovies.get(i).poster_path);
            movieList.add(i,new Movie(String.valueOf(movyRealmObjects.get(i).getId()),
                    movyRealmObjects.get(i).getOriginalTitle(), movyRealmObjects.get(i).getOverview(),
                    String.valueOf(movyRealmObjects.get(i).getVoteAverage()),
                    Constants.BASIC_URL+ movyRealmObjects.get(i).getPosterPath(),
                    Constants.BASIC_URL+ movyRealmObjects.get(i).getBackdropPath(),
                    movyRealmObjects.get(i).getReleaseDate()));
        }
        recyclerViewAdapter.notifyDataSetChanged();
        realm.commitTransaction();
    }

    @Override
    public void onGetMoviesFailure(String errorMSg) {
        //avl_loading.hide();
        Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMoviesFromDBSuccess() {
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetMoviesFromDBFailure(String errorMSg) {
        //avl_loading.hide();
        Toast.makeText(getActivity(), errorMSg, Toast.LENGTH_SHORT).show();
    }

    private List<MenuObject> getMenuObjects() {

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject mostPopular = new MenuObject(getString(R.string.most_popular_label));
        mostPopular.setResource(R.drawable.ic_theaters_black_24dp);
        mostPopular.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject topRated = new MenuObject(getString(R.string.top_rated_label));
        topRated.setResource(R.drawable.ic_star_black_24dp);
        topRated.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject Favorites = new MenuObject(getString(R.string.favorites_label));
        Favorites.setResource(R.drawable.ic_favorite_black_24dp);
        Favorites.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        MenuObject Close = new MenuObject(getString(R.string.close_label));
        Close.setResource(R.drawable.ic_cancel_black_24dp);
        Close.setMenuTextAppearanceStyle(R.style.TextViewStyle);

        menuObjects.add(mostPopular);
        menuObjects.add(topRated);
        menuObjects.add(Favorites);
        menuObjects.add(Close);

        return menuObjects;
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {

        switch (position){
            case 0:
                SharedPreferenceHelper.getInstance()
                        .putPref(SharedPreferenceHelper.LAST_CHOICE,Constants.POPULAR);
                actionBar.setTitle(getString(R.string.most_popular_label));
                getMovies(Constants.POPULAR,getCurrentPage(Constants.POPULAR));
                break;
            case 1:
                SharedPreferenceHelper.getInstance()
                        .putPref(SharedPreferenceHelper.LAST_CHOICE,Constants.TOP_RATED);
                actionBar.setTitle(getString(R.string.top_rated_label));
                getMovies(Constants.TOP_RATED,getCurrentPage(Constants.TOP_RATED));
                break;
            case 2:
                SharedPreferenceHelper.getInstance()
                        .putPref(SharedPreferenceHelper.LAST_CHOICE,Constants.FAVORITE);
                actionBar.setTitle(getString(R.string.favorites_label));
                moviesPresenter.getFavoritesFromDB(movieList,this);
                break;
        }
    }

    @Override
    public void isLoading(boolean isLoading) {
        loading = isLoading;
        swipeContainer.setRefreshing(isLoading);
    }

    @Override
    public void onStop() {
        super.onStop();
        moviesPresenter.dispose();
    }

    public interface moviesFragmentCallbacks{
        public void posterClick(String movieDetails);
    }
}
