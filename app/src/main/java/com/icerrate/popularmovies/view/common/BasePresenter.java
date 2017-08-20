package com.icerrate.popularmovies.view.common;

/**
 * Created by Ivan Cerrate.
 */

public abstract class BasePresenter<T extends BaseView> {

    protected T view;

    protected BasePresenter(T view) {
        this.view = view;
    }

    public String getStringRes(int resId) {
        return view.getContext().getString(resId);
    }

    public int getColorRes(int resId) {
        return view.getContext().getResources().getColor(resId);
    }

    private void showError(String messageError) {
        view.showError(messageError);
    }

    public void cancelRequests(){

    }

    protected abstract class PresenterCallback<T> implements BaseCallback<T> {

        @Override
        public void onFailure(String messageError) {
            view.dismissProgressDialog();
            view.showError(messageError);
        }
    }
}
