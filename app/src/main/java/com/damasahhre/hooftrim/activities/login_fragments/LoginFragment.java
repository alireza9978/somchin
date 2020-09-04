package com.damasahhre.hooftrim.activities.login_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.damasahhre.hooftrim.R;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

public class LoginFragment extends Fragment {

    private boolean keyboardState = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment_login, container, false);
    }

}
