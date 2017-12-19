package com.projects.brightcreations.moviesappmvp.Screens.MoviesPosters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.projects.brightcreations.moviesappmvp.Models.MovieObject;
import com.projects.brightcreations.moviesappmvp.Models.MoviesResponse;
import com.projects.brightcreations.moviesappmvp.Models.RealmMovie;
import com.projects.brightcreations.moviesappmvp.Models.Result;
import com.projects.brightcreations.moviesappmvp.SQLiteDataBase.MoviesDB;
import com.projects.brightcreations.moviesappmvp.Utilities.Constants;
import com.projects.brightcreations.moviesappmvp.Utilities.SharedPreferenceHelper;
import com.projects.brightcreations.moviesappmvp.webService.ServiceInterfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amr on 06/12/17.
 */

public class MoviesPresenter {

    private SharedPreferenceHelper sharedPreferenceHelper;
    private onGetMoviesListener moviesListener;
    private Realm realm;
    private List<RealmMovie> realmMovieList;
    private List<Result> resultList;
    private MoviesDB moviesDB;
    private Retrofit retrofit;
    private Disposable disposable;

    MoviesPresenter(Context context, onGetMoviesListener moviesListener) {
        this.moviesListener = moviesListener;
        sharedPreferenceHelper = new SharedPreferenceHelper(context);
        realm = Realm.getDefaultInstance();
        moviesDB = new MoviesDB(context);
        realmMovieList = new ArrayList<>();
        resultList = new ArrayList<>();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    void PerformMoviesCall(final String SortType, final int page,
                           final MoviesActivityViews moviesActivityViews){
        //progressDialog = ProgressDialog.show(getActivity(),"", "Loading. Please wait...", true);
        moviesActivityViews.isLoading(true);
        retrofit.create(ServiceInterfaces.Movies.class).getMovies(SortType,Constants.API_KEY,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    moviesActivityViews.isLoading(false);
                    if (SortType.equals(Constants.POPULAR)) {
                        sharedPreferenceHelper.putPref(SharedPreferenceHelper.POPULAR_CURRENT_PAGE, page);
                    } else sharedPreferenceHelper.putPref(SharedPreferenceHelper.TOP_CURRENT_PAGE, page);
                    if (response != null) {
                        resultList = response.getResults();
                        for (int i = 0; i < resultList.size(); i++) {
                            RealmMovie realmMovie = new RealmMovie(resultList.get(i).getId(),
                                    resultList.get(i).getVoteAverage(),
                                    resultList.get(i).getTitle(),
                                    resultList.get(i).getPosterPath(),
                                    resultList.get(i).getBackdropPath(),
                                    resultList.get(i).getTitle(),
                                    resultList.get(i).getOverview(),
                                    resultList.get(i).getReleaseDate(),
                                    SortType);
                            realmMovieList.add(realmMovie);
                        }
                        addMoviesToRealm(realmMovieList);
                        Log.e("Size:  ", realmMovieList.size() + "");
                    } else {
                        moviesActivityViews.isLoading(false);
                        moviesListener.onGetMoviesFailure("Error");
                    }
                }, throwable -> {
                    moviesActivityViews.isLoading(false);
                    moviesListener.onGetMoviesFailure(throwable.getMessage());
                });
    }

    private void addMoviesToRealm(List<RealmMovie> realmMovies){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmMovies);
        realm.commitTransaction();
        moviesListener.onGetMoviesSuccess();
    }

    void getFavoritesFromDB(List<MovieObject> movieObjectList, MoviesActivityViews moviesActivityViews) {
        movieObjectList.clear();
        moviesActivityViews.isLoading(true);
        disposable = Observable.create((ObservableOnSubscribe<ArrayList<ArrayList<Object>>>) e -> {
            e.onNext(moviesDB.getAllMovies());
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ArrayList<ArrayList<Object>>>() {
                    @Override
                    public void onNext(@NonNull ArrayList<ArrayList<Object>> arrayLists) {
                        for(int i=0 ; i<arrayLists.size() ; i++) {
                            movieObjectList.add(i,new MovieObject((String) arrayLists.get(i).get(0),
                            (String) arrayLists.get(i).get(1),
                            (String) arrayLists.get(i).get(2),
                            (String) arrayLists.get(i).get(3),
                            (String) arrayLists.get(i).get(5),
                            (String) arrayLists.get(i).get(6),
                            (String) arrayLists.get(i).get(4)));
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        moviesListener.onGetMoviesFromDBFailure(e.getMessage());
                        Log.e("Retrieve Error", e.toString());
                        e.printStackTrace();
                    }
                    @Override
                    public void onComplete() {
                        moviesActivityViews.isLoading(false);
                        moviesListener.onGetMoviesFromDBSuccess();
                    }
                });
    }

    void dispose(){
        if (disposable!=null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
