package com.projects.brightcreations.moviesappmvp.screens.movies_posters;

import android.content.Context;
import android.util.Log;

import com.projects.brightcreations.moviesappmvp.movie.Movie;
import com.projects.brightcreations.moviesappmvp.movie.MovieRealmObject;
import com.projects.brightcreations.moviesappmvp.movie.Result;
import com.projects.brightcreations.moviesappmvp.local_db.MoviesDB;
import com.projects.brightcreations.moviesappmvp.utils.Constants;
import com.projects.brightcreations.moviesappmvp.utils.SharedPreferenceHelper;
import com.projects.brightcreations.moviesappmvp.web_service.ApiInterfaces;
import com.projects.brightcreations.moviesappmvp.web_service.PaginatedResponse;
import com.projects.brightcreations.moviesappmvp.web_service.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by ahmed on 06/12/17.
 */

public class MoviesPresenter {

    private static final String TAG = MoviesPresenter.class.getSimpleName();
    private onGetMoviesListener moviesListener;
    private Realm realm;
    private MoviesDB moviesDB;
    private RestClient restClient;
    private Disposable disposable;
    private List<MovieRealmObject> movieRealmObjectList;
    private Result[] resultList;

    MoviesPresenter(Context context, onGetMoviesListener moviesListener) {
        SharedPreferenceHelper.init(context);
        this.moviesListener = moviesListener;
        realm = Realm.getDefaultInstance();
        restClient = new RestClient();
        moviesDB = new MoviesDB(context);
        movieRealmObjectList = new ArrayList<>();
    }

    void PerformMoviesCall(final String SortType, final int page,
                           final MoviesActivityViews moviesActivityViews){
        moviesActivityViews.isLoading(true);
        ApiInterfaces apiInterfaces = restClient.createService(ApiInterfaces.class);
        Observable<PaginatedResponse<Result>> call =
                apiInterfaces.getMovies(SortType,Constants.API_KEY,page);
        call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    moviesActivityViews.isLoading(false);
                    if (SortType.equals(Constants.POPULAR)) {
                        SharedPreferenceHelper.getInstance().
                                putPref(SharedPreferenceHelper.POPULAR_CURRENT_PAGE, page);
                    } else SharedPreferenceHelper.getInstance().
                            putPref(SharedPreferenceHelper.TOP_CURRENT_PAGE, page);
                    if (response != null) {
                        resultList = new Result[response.getResults().length];
                        resultList = response.getResults();
                        for (Result result : resultList) {
                            MovieRealmObject movieRealmObject = new MovieRealmObject(result.getId(),
                                    result.getVoteAverage(),
                                    result.getTitle(),
                                    result.getPosterPath(),
                                    result.getBackdropPath(),
                                    result.getTitle(),
                                    result.getOverview(),
                                    result.getReleaseDate(),
                                    SortType);
                            movieRealmObjectList.add(movieRealmObject);
                        }
                        addMoviesToRealm(movieRealmObjectList);
                        Log.e(TAG, "Current Thread "+Thread.currentThread().getName());
                    } else {
                        moviesActivityViews.isLoading(false);
                        moviesListener.onGetMoviesFailure("Error");
                    }
                }, throwable -> {
                    moviesActivityViews.isLoading(false);
                    moviesListener.onGetMoviesFailure(throwable.getMessage());
                });
    }

    private void addMoviesToRealm(List<MovieRealmObject> movyRealmObjects){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(movyRealmObjects);
        realm.commitTransaction();
        moviesListener.onGetMoviesSuccess();
    }

    void getFavoritesFromDB(List<Movie> movieList, MoviesActivityViews moviesActivityViews) {
        movieList.clear();
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
                            movieList.add(i,new Movie((String) arrayLists.get(i).get(0),
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
