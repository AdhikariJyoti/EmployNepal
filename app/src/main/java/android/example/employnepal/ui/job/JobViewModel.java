package android.example.employnepal.ui.job;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JobViewModel extends ViewModel {

    private static MutableLiveData<String> mText;

    public JobViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Job fragment");
    }

    public static LiveData<String> getText() {

        return mText;
    }
}